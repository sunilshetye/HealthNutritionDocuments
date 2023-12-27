package org.spoken_tutorial.health.elasticsearch.contentfile;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

@Component
public class ContentsfromFile {

    public static String extractContent(final Parser parser, final String path)
            throws IOException, SAXException, TikaException {

        long start = System.currentTimeMillis();

        BodyContentHandler handler = new BodyContentHandler(10000000);
        Metadata metadata = new Metadata();

        FileInputStream content = new FileInputStream(path);
        parser.parse(content, handler, metadata, new ParseContext());

        return handler.toString();

    }
}
