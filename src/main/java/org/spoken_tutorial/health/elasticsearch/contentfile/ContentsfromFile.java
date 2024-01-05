package org.spoken_tutorial.health.elasticsearch.contentfile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.spoken_tutorial.health.elasticsearch.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

@Component
public class ContentsfromFile {

    @Autowired
    private Config config;

    public String extractContent(final Parser parser, final String filePath)
            throws IOException, SAXException, TikaException {

        long start = System.currentTimeMillis();

        BodyContentHandler handler = new BodyContentHandler(10000000);
        Metadata metadata = new Metadata();
        Path path = Paths.get(config.BASE_PATH, filePath);
        InputStream content = Files.newInputStream(path);
        parser.parse(content, handler, metadata, new ParseContext());

        return handler.toString();

    }
}
