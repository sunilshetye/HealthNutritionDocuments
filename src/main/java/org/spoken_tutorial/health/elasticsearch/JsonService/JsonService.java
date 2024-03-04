package org.spoken_tutorial.health.elasticsearch.JsonService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.tomcat.util.json.ParseException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.spoken_tutorial.health.elasticsearch.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class JsonService {

    private final RestTemplate restTemplate;

    @Value("${scriptmanager_url_for_json}")
    private String scriptmanager_url_for_json;

    @Value("${spring.applicationexternalPath.name}")
    private String mediaRoot;

    @Autowired
    public JsonService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private String CreateJsonSMurl(int catId, int tutorialId, int lanId, int version) {

        String sm_url_json = "";

        StringBuilder sb = new StringBuilder();

        sb.append(scriptmanager_url_for_json);
        sb.append(String.valueOf(catId));
        sb.append("/");
        sb.append("tutorial");
        sb.append("/");
        sb.append(String.valueOf(tutorialId));
        sb.append("/");
        sb.append("language");
        sb.append("/");
        sb.append(String.valueOf(lanId));
        sb.append("/");
        sb.append("scripts");
        sb.append("/");
        sb.append(String.valueOf(version));
        sb.append("/");
        sb.append("healthnutrition");
        sb.append("/");
        sm_url_json = sb.toString();
        return sm_url_json;

    }

    public String saveNarrationToFile(int catId, int tutorialId, int lanId, int version)
            throws ParseException, IOException {

        String url = CreateJsonSMurl(catId, tutorialId, lanId, version);
        String jsonString = restTemplate.getForObject(url, String.class);
        System.out.println(jsonString);
        String document = "";
        try {
            if (jsonString != null) {

                JSONObject mainJsonObject = new JSONObject(jsonString);

                JSONArray jsonArrayNarrartions = (JSONArray) mainJsonObject.get("slides");
                System.out.println("Narration : ");
                String narration = "";
                for (int i = 0; i < jsonArrayNarrartions.length(); i++) {
                    JSONObject jsonNarration = (JSONObject) jsonArrayNarrartions.get(i);

                    narration = narration + (String) jsonNarration.get("narration") + "\n";
                    System.out.println(narration);

                }
                Path path = Paths.get(mediaRoot, Config.uploadDirectoryScriptHtmlFile);

                Files.createDirectories(path);

                Path filePath = Paths.get(mediaRoot, Config.uploadDirectoryScriptHtmlFile, tutorialId + ".html");

                Files.writeString(filePath, narration);

                String temp = filePath.toString();

                int indexToStart = temp.indexOf("Media");

                document = temp.substring(indexToStart, temp.length());

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return document;
    }
}
