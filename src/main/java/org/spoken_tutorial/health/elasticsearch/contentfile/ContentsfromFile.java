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

    private String hnadleZipFile(final Parser parser, InputStream inputStream) {

        ZipEntry zipEntry;
        String content = "";
        int count = 0;
        BodyContentHandler handler = new BodyContentHandler(10000000);
        Metadata metadata = new Metadata();
        List<String> allowedExtensions = Arrays.asList(config.ALLOWED_EXTENSIONS.split(","));

        try (ZipInputStream zis = new ZipInputStream(inputStream)) {
            while ((zipEntry = zis.getNextEntry()) != null) {
                if (!zipEntry.isDirectory() && !zipEntry.getName().toLowerCase().endsWith(".zip") && allowedExtensions
                        .contains(zipEntry.getName().substring(zipEntry.getName().lastIndexOf(".") + 1))) {

                    if (zipEntry.getName().toLowerCase().endsWith(".txt")) {

                        content += new String(readFromStream(zis));
                        count++;

                    } else {
                        parser.parse(zis, handler, metadata, new ParseContext());
                        content += handler.toString();
                        count++;

                    }

                }
                if (count == config.SCANNING_LIMIT) {
                    logger.info("COUNT VALUE:{}", count);
                    zis.close();
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

        BodyContentHandler handler = new BodyContentHandler(10000000);
        Metadata metadata = new Metadata();
        Path path = Paths.get(config.BASE_PATH, filePath);

        try (InputStream inputStream = Files.newInputStream(path);) {
            if (allowedExtensions.contains(filePath.substring(filePath.lastIndexOf(".") + 1))) {

                if (filePath.toLowerCase().endsWith(".zip")) {

                    content += hnadleZipFile(parser, inputStream);
                }

                else if (filePath.toLowerCase().endsWith(".txt")) {

                    content = new String(Files.readAllBytes(path));
                } else {

                    parser.parse(inputStream, handler, metadata, new ParseContext());
                    content += handler.toString();
                    inputStream.close();
                }

            }

        } catch (Exception e) {
            logger.error("Input Stream Error", e);
        }

        return content;

    }

    private byte[] readFromStream(ZipInputStream zipInputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = zipInputStream.read(buffer)) > 0) {
            byteArrayOutputStream.write(buffer, 0, len);
        }
        return byteArrayOutputStream.toByteArray();
    }

}
