package org.spoken_tutorial.health.elasticsearch.config;

import java.io.IOException;
import java.io.InputStream;
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
        logger.info("Entered into extractTextFromFile method timeScriptPath:{}", timeScriptPath.toString());

        String content = "";
        try (InputStream inputStream = Files.newInputStream(timeScriptPath)) {
            BodyContentHandler handler = new BodyContentHandler(10000000);
            Metadata metadata = new Metadata();
            ParseContext pcontext = new ParseContext();

            AutoDetectParser parser = new AutoDetectParser();
            parser.parse(inputStream, handler, metadata, pcontext);
            content = handler.toString();
        } catch (Exception e) {
            logger.error("InputStream Exception Error in extractTextFromFile method timeScriptPath :{}, content:{}",
                    timeScriptPath.toString(), content, e);
        }

        return content;
    }

    public static void writeTextToVtt(String text, Path vttFilePath) throws IOException {
        logger.info("Entered into writeTextToVtt method vttFilePath:{}", vttFilePath.toString());
        try {
            StringBuffer sb = new StringBuffer();
            String[] lines = text.split("\n");
            int index = 1;

            String textValue = "";
            String startTimeString = "00:";
            boolean textFlag = true;

            for (String line : lines) {
                line = line.replace("\u00A0", "").trim();
                if (line.isEmpty()) {
                    continue;
                }
                if (line.equalsIgnoreCase("Time"))
                    continue;
                if (line.equalsIgnoreCase("Narration")) {
                    sb.append("WEBVTT" + "\n\n");
                    continue;
                }
                if (line.matches("^([01]?\\d|2[0-3]):([0-5]?\\d)$")
                        || line.matches("^([01]?\\d|2[0-3])\\.([0-5]?\\d)$")) {
                    textFlag = false;

                    String newtimeValue = "00:" + formatToTimeScript(line);

                    if (!startTimeString.equals("00:")) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                        LocalTime endTime = LocalTime.parse(newtimeValue, formatter);

                        endTime = endTime.minusNanos(500_000_000);

                        String endTimeString = endTime.toString();
                        sb.append(startTimeString + " --> " + endTimeString + "\n");
                        sb.append(textValue + "\n\n");
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
            sb.append(startTimeString + " --> " + endTimeString + "\n");
            sb.append(textValue + "\n\n");
            Files.writeString(vttFilePath, sb.toString());
            logger.info("VttFile is created filePath:{}", vttFilePath.toString());
        } catch (Exception e) {
            logger.error("Exception in writeTextToVtt method filePath:{}, text:{}", vttFilePath.toString(), text, e);
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
