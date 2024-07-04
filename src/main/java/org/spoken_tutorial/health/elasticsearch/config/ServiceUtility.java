package org.spoken_tutorial.health.elasticsearch.config;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

@Component
public class ServiceUtility {
    private static final Logger logger = LoggerFactory.getLogger(ServiceUtility.class);

    @Autowired
    private Config config;

    /********** srt file conversion start **********/

    public static String extractTextFromFile(Path timeScriptPath) throws IOException, TikaException, SAXException {

        String content = "";
        try (InputStream inputStream = Files.newInputStream(timeScriptPath)) {
            BodyContentHandler handler = new BodyContentHandler(10000000);
            Metadata metadata = new Metadata();
            ParseContext pcontext = new ParseContext();

            AutoDetectParser parser = new AutoDetectParser();
            parser.parse(inputStream, handler, metadata, pcontext);
            content = handler.toString();
        } catch (Exception e) {
            logger.error("InputStream Exception Error", e);
        }

        return content;
    }

    public static void writeTextToVtt(String text, Path vttFilePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(Files.newOutputStream(vttFilePath), StandardCharsets.UTF_8))) {
            String[] lines = text.split("\n");
            int index = 1;

            String textValue = "";
            String startTimeString = "00:";
            boolean textFlag = true;

            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                if (line.trim().equalsIgnoreCase("Time"))
                    continue;
                if (line.trim().equalsIgnoreCase("Narration")) {
                    writer.write("WEBVTT" + "\n\n");
                    continue;
                }
                if (line.replace("\u00A0", "").trim().matches("^([01]?\\d|2[0-3]):([0-5]?\\d)$")
                        || line.replace("\u00A0", "").trim().matches("^([01]?\\d|2[0-3])\\.([0-5]?\\d)$")) {
                    textFlag = false;

                    String newtimeValue = "00:" + formatToTimeScript(line.replace("\u00A0", "").trim());

                    if (!startTimeString.equals("00:")) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                        LocalTime endTime = LocalTime.parse(newtimeValue, formatter);

                        endTime = endTime.minusNanos(500_000_000);

                        String endTimeString = endTime.toString();
                        writer.write(startTimeString + " --> " + endTimeString + "\n");
                        writer.write(textValue + "\n\n");
                        index++;
                    }
                    startTimeString = newtimeValue + ".000";

                } else {
                    if (textFlag) {
                        textValue = textValue + line;
                    } else {
                        textValue = line.trim();
                        textFlag = true;
                    }
                }
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
            LocalTime startTime = LocalTime.parse(startTimeString, formatter);
            LocalTime newTime = startTime.plusSeconds(7);

            String endTimeString = formatter.format(newTime);
            writer.write(startTimeString + " --> " + endTimeString + "\n");
            writer.write(textValue + "\n\n");
        }
    }

    public static String formatToTimeScript(String line) {
        line = line.replace(".", ":");

        String[] parts = line.split(":");

        String minutes = parts.length > 0 ? String.format("%02d", Integer.parseInt(parts[0])) : "00";
        String seconds = parts.length > 1 ? String.format("%02d", Integer.parseInt(parts[1])) : "00";

        return minutes + ":" + seconds;
    }

    /************ srt file conversion ends **********/

}
