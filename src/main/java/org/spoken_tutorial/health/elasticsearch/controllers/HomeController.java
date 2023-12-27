package org.spoken_tutorial.health.elasticsearch.controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spoken_tutorial.health.elasticsearch.models.QueueManagement;
import org.spoken_tutorial.health.elasticsearch.repositories.DocumentSearchRepository;
import org.spoken_tutorial.health.elasticsearch.repositories.QueueManagementRepository;
import org.spoken_tutorial.health.elasticsearch.services.DocumentSearchService;
import org.spoken_tutorial.health.elasticsearch.services.QueueManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    DocumentSearchRepository docuSearchRepo;

    @Autowired
    QueueManagementRepository queRepo;

    @Autowired
    QueueManagementService queuemntService;

    @Autowired
    DocumentSearchService docuSearchService;

    private Timestamp getCurrentTime() { // Current Date

        Date date = new Date();
        long t = date.getTime();
        Timestamp st = new Timestamp(t);

        return st;
    }

    private boolean doesFileExist(String filePath) {

        Path path = Paths.get(filePath);
        return Files.exists(path);
    }

    @GetMapping("/findAll")
    public List<QueueManagement> findAll() {
        return queRepo.findAll();
    }

    public Map<String, String> addDocument(String documentId, String documentType, String documentPath,
            String documentUrl, int rank, String view_url, String language, Optional<String> category,
            Optional<String> topic, Optional<String> outlinePath, String requestType) {

        Map<String, String> resultMap = new HashMap<>();
        long queueId = queuemntService.getNewId();

        logger.info(
                "RequestType:{} Language:{} View_URL: {} documentId:{} documentPath:{} documentType:{} outlinePath:{}",
                requestType, language, view_url, documentId, documentPath, documentType, outlinePath);

        if (documentPath != null && !doesFileExist(documentPath)) {
            resultMap.put("status", "failed");
            resultMap.put("reason", "document file does not exist");
            return resultMap;
        }

        if (outlinePath != null && outlinePath.isPresent() && !doesFileExist(outlinePath.get())) {

            resultMap.put("status", "failed");
            resultMap.put("reason", "outline file does not exist");
            return resultMap;

        }

        QueueManagement queuemnt = new QueueManagement();
        queuemnt.setQueueId(queueId);
        if (outlinePath.isPresent())
            queuemnt.setOutlinePth(outlinePath.get());
        queuemnt.setRequestTime(getCurrentTime());
        queuemnt.setRequestType(requestType);
        queuemnt.setDocumentId(documentId);
        queuemnt.setDocumentType(documentType);
        queuemnt.setDocumentPath(documentPath);
        queuemnt.setDocumentUrl(documentUrl);
        queuemnt.setRank(rank);
        queuemnt.setViewUrl(view_url);
        queuemnt.setLanguage(language);
        queuemnt.setStatus("pending");
        if (category.isPresent())
            queuemnt.setCategory(category.get());
        if (topic.isPresent())
            queuemnt.setTopic(topic.get());

        resultMap.put("Id", Long.toString(queueId));
        resultMap.put("status", "success");

        queRepo.save(queuemnt);

        return resultMap;

    }

    @PostMapping("/addDocument/{documentId}/{documentType}/{language}/{rank}")
    public Map<String, String> addDocument(@PathVariable String documentId, @PathVariable String documentType,
            @PathVariable String language, @PathVariable int rank, @RequestParam String documentPath,
            @RequestParam String documentUrl, @RequestParam String view_url, @RequestParam Optional<String> category,
            @RequestParam Optional<String> topic, @RequestParam Optional<String> outlinePath) {

        return addDocument(documentId, documentType, documentPath, documentUrl, rank, view_url, language, category,
                topic, outlinePath, "addDocument");
    }

    @PostMapping("/updateDocument/{documentId}/{documentType}/{language}/{rank}")
    public Map<String, String> updateDocument(@PathVariable String documentId, @PathVariable String documentType,
            @PathVariable String language, @PathVariable int rank, @RequestParam String documentPath,
            @RequestParam String documentUrl, @RequestParam String view_url, @RequestParam Optional<String> category,
            @RequestParam Optional<String> topic, @RequestParam Optional<String> outlinePath) {

        return addDocument(documentId, documentType, documentPath, documentUrl, rank, view_url, language, category,
                topic, outlinePath, "updateDocument");
    }

    @PostMapping("/updateDocumentRank/{documentId}/{documentType}/{language}/{rank}")
    public Map<String, String> updateDocumentRank(@PathVariable String documentId, @PathVariable String documentType,
            @PathVariable String language, @PathVariable int rank) {
        return addDocument(documentId, documentType, null, null, rank, null, language, null, null, null,
                "updateDocumentRank");
    }

    @GetMapping("/deleteDocument/{documentId}/{documentType}/{language}")
    public Map<String, String> deleteDocument(@PathVariable String documentId, @PathVariable String documentType,
            @PathVariable String language) {

        return addDocument(documentId, documentType, null, null, 0, null, language, null, null, null, "deleteDocument");
    }

    /************
     * Testing
     ************************************************************/

    String path = "D:\\Users\\aloks\\Documents\\TimeScript.pdf";

}
