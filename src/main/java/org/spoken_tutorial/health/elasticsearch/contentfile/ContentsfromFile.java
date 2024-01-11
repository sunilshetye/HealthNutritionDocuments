package org.spoken_tutorial.health.elasticsearch.contentfile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spoken_tutorial.health.elasticsearch.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

@Component
public class ContentsfromFile {

    private static final Logger logger = LoggerFactory.getLogger(ContentsfromFile.class);

    @Autowired
    private Config config;

    private String handleZipFile(final Parser parser, InputStream inputStream) {

        ZipEntry zipEntry;
        String content = "";
        int count = 0;

        List<String> allowedExtensions = Arrays.asList(config.ALLOWED_EXTENSIONS.split(","));

        try (ZipInputStream zis = new ZipInputStream(inputStream)) {
            while ((zipEntry = zis.getNextEntry()) != null) {

                if (zipEntry.isDirectory()) {
                    continue;
                }

                String name = zipEntry.getName().toLowerCase();

                if (name.endsWith(".zip")) {
                    logger.info("skipping {} in zip", name);
                    continue;
                }
                int index = name.lastIndexOf(".");
                if (index == -1) {
                    continue;
                }
                if (!allowedExtensions.contains(name.substring(index + 1))) {
                    continue;
                }

                if (name.endsWith(".txt")) {

                    content += " " + new String(readFromStream(zis));
                    count++;

                } else {
                    BodyContentHandler handler = new BodyContentHandler(config.HANDLER_DATA);
                    Metadata metadata = new Metadata();
                    parser.parse(zis, handler, metadata, new ParseContext());
                    content += " " + handler.toString();
                    count++;

                }

                if (count == config.SCANNING_LIMIT) {

                    break;
                }
            }
        } catch (Exception e) {
            logger.error("ZipInputStream Error", e);

        }

        return content;

    }

    public String extractContent(final Parser parser, final String filePath)
            throws IOException, SAXException, TikaException {

        String content = "";
        List<String> allowedExtensions = Arrays.asList(config.ALLOWED_EXTENSIONS.split(","));

        Path path = Paths.get(config.BASE_PATH, filePath);

        String name = filePath.toLowerCase();

        int index = name.lastIndexOf(".");
        if (index == -1) {
            return "";
        }
        if (!allowedExtensions.contains(name.substring(index + 1))) {
            return "";
        }

        try (InputStream inputStream = Files.newInputStream(path)) {

            if (name.endsWith(".zip")) {

                content = handleZipFile(parser, inputStream);
            }

            else if (name.endsWith(".txt")) {

                content = new String(Files.readAllBytes(path));
            } else {
                BodyContentHandler handler = new BodyContentHandler(config.HANDLER_DATA);
                Metadata metadata = new Metadata();
                parser.parse(inputStream, handler, metadata, new ParseContext());
                content = handler.toString();

            }

        } catch (Exception e) {
            logger.error("Input Stream Error", e);
        }

        return content;

    }

    private byte[] readFromStream(ZipInputStream zipInputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[16384];
        int len;
        while ((len = zipInputStream.read(buffer)) > 0) {
            byteArrayOutputStream.write(buffer, 0, len);
        }
        return byteArrayOutputStream.toByteArray();
    }

}
