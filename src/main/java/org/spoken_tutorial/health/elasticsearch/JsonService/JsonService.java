package org.spoken_tutorial.health.elasticsearch.JsonService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.tomcat.util.json.ParseException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spoken_tutorial.health.elasticsearch.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class JsonService {

    private static final Logger logger = LoggerFactory.getLogger(JsonService.class);

    private final RestTemplate restTemplate;

    @Value("${spring.applicationexternalPath.name}")
    private String mediaRoot;

    @Value("${spring.libreoffice}")
    private String libreoffice;

    @Autowired
    public JsonService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String saveNarrationToFile(String url, String documentId) throws ParseException, IOException {

        String jsonString = restTemplate.getForObject(url, String.class);

        String document = "";
        try {
            if (jsonString != null) {

                JSONObject mainJsonObject = new JSONObject(jsonString);
                JSONArray jsonArrayNarrations = (JSONArray) mainJsonObject.get("slides");

                StringBuffer sb = new StringBuffer();
                sb.append("<html>\n<head>\n<title>\n");
                sb.append(mainJsonObject.get("tutorial"));
                sb.append("-");
                sb.append(mainJsonObject.get("language"));
                sb.append("\n</title>\n</head>\n<body>\n<h3>\n");
                sb.append(mainJsonObject.get("tutorial"));
                sb.append(" - ");
                sb.append(mainJsonObject.get("language"));
                sb.append("\n</h3>\n");

                for (int i = 0; i < jsonArrayNarrations.length(); i++) {
                    JSONObject jsonNarration = (JSONObject) jsonArrayNarrations.get(i);
                    sb.append("<p>\n");
                    sb.append((String) jsonNarration.get("narration"));
                    sb.append("\n</p>\n");

                }
                sb.append("\n</body>\n</html>");
                String narration = sb.toString();
                Path path = Paths.get(mediaRoot, Config.uploadDirectoryScriptHtmlFile);

                Files.createDirectories(path);

                Path filePath = Paths.get(mediaRoot, Config.uploadDirectoryScriptHtmlFile, documentId + ".html");

                Files.writeString(filePath, narration);

                String temp = filePath.toString();

                int indexToStart = temp.indexOf("Media");

                document = temp.substring(indexToStart, temp.length());

            }

        } catch (Exception e) {
            logger.error("Exception Error", e);
        }
        return document;
    }

    public String saveNarrationAndCuefScriptoHtmlFile(String url, int tutorialId, int lanId)
            throws ParseException, IOException {

        String jsonString = "";
        boolean flag = true;

        String document = "";

        Path jsonDir = Paths.get(mediaRoot, Config.uploadDirectoryScriptJsonFile);
        Files.createDirectories(jsonDir);
        Path jsonfilePath = Paths.get(mediaRoot, Config.uploadDirectoryScriptJsonFile, tutorialId + ".json");

        if (Files.exists(jsonfilePath)) {

            jsonString = new String(Files.readAllBytes(jsonfilePath));

        } else {
            jsonString = restTemplate.getForObject(url, String.class);
            Files.writeString(jsonfilePath, jsonString);
        }

        Path htmlDir = Paths.get(mediaRoot, Config.uploadDirectoryScriptHtmlFileforDownload);
        Files.createDirectories(htmlDir);

        Path htmlFilePath = Paths.get(mediaRoot, Config.uploadDirectoryScriptHtmlFileforDownload, tutorialId + ".html");

        if (Files.exists(htmlFilePath)) {
            if (Files.getLastModifiedTime(jsonfilePath).toMillis() < Files.getLastModifiedTime(htmlFilePath)
                    .toMillis()) {
                flag = false;
            }
        }

        try {
            if (jsonString != null && flag) {

                JSONObject mainJsonObject = new JSONObject(jsonString);
                JSONArray jsonArrayNarrations = (JSONArray) mainJsonObject.get("slides");

                StringBuffer sb = new StringBuffer();
                sb.append("<html>\n<head>\n");
                if (lanId == 22) {
                    sb.append("  <style type=\"text/css\">\r\n"
                            + "    @page { size: 240.59cm 424.94cm; margin: 2.54cm }\r\n"
                            + "    p { line-height: 115%; margin-bottom: 0.25cm; background: transparent }\r\n"
                            + "th, td {\r\n" + "  border-style:solid;\r\n" + "  border-color: #96D4D4;\r\n" + "}\r\n"
                            + "\r\n" + "  </style>");
                } else {
                    sb.append("  <style type=\"text/css\">\r\n"
                            + "    @page { size: 38.59cm 626.94cm; margin: 2.54cm }\r\n"
                            + "    p { line-height: 115%; margin-bottom: 0.25cm; background: transparent }\r\n"
                            + "th, td {\r\n" + "  border-style:solid;\r\n" + "  border-color: #96D4D4;\r\n" + "}\r\n"
                            + "\r\n" + "  </style>");
                }

                sb.append("\n</head>\n<body>\n");

                sb.append("\n<table>\n");
                if (lanId == 22) {
                    sb.append("<tr>\n<th>\n");
                    sb.append("Visual Cue");
                    sb.append("\n</th>\n<th>\n");
                    sb.append("Narration");
                    sb.append("\n</th>\n</tr>");

                } else {
                    sb.append("<tr>\n<th>\n");
                    sb.append("Time");
                    sb.append("\n</th>\n<th>\n");
                    sb.append("Narration");
                    sb.append("\n</th>\n</tr>");
                }

                for (int i = 0; i < jsonArrayNarrations.length(); i++) {
                    JSONObject jsonNarration = (JSONObject) jsonArrayNarrations.get(i);
                    sb.append("\n<tr>");

                    sb.append((String) jsonNarration.get("cue"));

                    sb.append((String) jsonNarration.get("narration"));
                    sb.append("\n</tr>");

                }
                sb.append("\n</table>\n");
                sb.append("<br>\n");
                sb.append("</body>\n</html>");
                String narration = sb.toString();

                Files.writeString(htmlFilePath, narration);

            }

        } catch (Exception e) {
            logger.error("Exception Error", e);
        }
        String temp = htmlFilePath.toString();

        int indexToStart = temp.indexOf("Media");

        document = temp.substring(indexToStart, temp.length());

        return document;
    }

    public String convertHtmltoOdt(String htmlFie, int tutorialId) {

        String document = "";
        boolean flag = true;

        try {

            Path odtDir = Paths.get(mediaRoot, Config.uploadDirectoryScriptOdtFileforDownload);
            Files.createDirectories(odtDir);
            Path odtfilePath = Paths.get(mediaRoot, Config.uploadDirectoryScriptOdtFileforDownload,
                    tutorialId + ".odt");

            Path htmlDir = Paths.get(mediaRoot, Config.uploadDirectoryScriptHtmlFileforDownload);
            Files.createDirectories(htmlDir);
            Path htmlFilePath = Paths.get(mediaRoot, htmlFie);

            if (Files.exists(htmlFilePath) && Files.exists(odtfilePath)) {
                if (Files.getLastModifiedTime(odtfilePath).toMillis() > Files.getLastModifiedTime(htmlFilePath)
                        .toMillis()) {
                    flag = false;
                }
            }

            if (flag) {
                Path htmlfilePath = Paths.get(mediaRoot, htmlFie);
                String htmlInputPath = htmlfilePath.toString();

                String odtdirstr = odtDir.toString();

                ProcessBuilder processBuilder = new ProcessBuilder(libreoffice, "--headless", "--convert-to", "odt",
                        "--outdir", odtdirstr, htmlInputPath);

                Process process = processBuilder.start();

                int exitCode = process.waitFor();
                if (exitCode == 0) {

                    String temp = odtfilePath.toString();

                    int indexToStart = temp.indexOf("Media");
                    document = temp.substring(indexToStart, temp.length());
                    logger.info(document);

                    logger.info("Conversion successful");
                } else {
                    logger.info("Conversion failed:{}", tutorialId);
                }
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Exception Error", e);
        }

        return document;
    }

}
