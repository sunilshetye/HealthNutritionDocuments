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
}
