package com.health.elastic.search.contentfile;

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

	public static  void extractContent(final Parser parser, final String path) throws IOException, SAXException,TikaException {
		
		long start=System.currentTimeMillis();
		
		BodyContentHandler handler= new BodyContentHandler(10000000);
		Metadata metadata=new Metadata();
		
		FileInputStream content=new FileInputStream(path);
		parser.parse(content, handler, metadata, new ParseContext());
		
		for(String name: metadata.names()) {
			System.out.println(name + ":\t" + metadata.get(name) );
		}
		
		System.out.println("\n\n File Content:" + handler.toString());
		
		System.out.println(String.format("Processing took %s millis \n\n", System.currentTimeMillis()));
		;
	}
	
public static  String extractContentForElastic(final Parser parser, final String path) throws IOException, SAXException,TikaException {
		
		long start=System.currentTimeMillis();
		
		BodyContentHandler handler= new BodyContentHandler(10000000);
		Metadata metadata=new Metadata();
		
		FileInputStream content=new FileInputStream(path);
		parser.parse(content, handler, metadata, new ParseContext());
		
		return handler.toString();
		
		
	}
}
