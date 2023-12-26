package org.spoken_tutorial.health.elasticsearch.controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.spoken_tutorial.health.elasticsearch.models.QueueManagement;
import org.spoken_tutorial.health.elasticsearch.repositories.QueueManagementRepository;
import org.spoken_tutorial.health.elasticsearch.repositories.TutorialSearchRepository;
import org.spoken_tutorial.health.elasticsearch.services.QueueManagementService;
import org.spoken_tutorial.health.elasticsearch.services.TutorialSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @Autowired
    TutorialSearchRepository tutorialRepo;

    @Autowired
    QueueManagementRepository queRepo;

    @Autowired
    QueueManagementService queuemntService;

    @Autowired
    TutorialSearchService tutSearchService;

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

    /*
     * @PostMapping("/addDocument/{path}/{documentType}/{documentTypeId}") public
     * int addDocument(@PathVariable String path, @PathVariable String documentType,
     * 
     * @PathVariable String documentTypeId) {
     * 
     * TutorialSearch tut = new TutorialSearch();
     * 
     * Parser parser = new AutoDetectParser(); String docContent = "";
     * 
     * try { docContent = ContentsfromFile.extractContentForElastic(parser, path);
     * tut.setDocumentContent(docContent); } catch (Exception e) {
     * 
     * }
     * 
     * tut.setDocumentType(documentType); tut.setDocumentTypeId(documentTypeId);
     * tutorialRepo.save(tut); return 1;
     * 
     * }
     * 
     * 
     * @PutMapping("/updateDocument/{path}/{documentType}/{documentId}") public int
     * updateDocument(@PathVariable String path, @PathVariable String documentType,
     * 
     * @PathVariable String documentId) {
     * 
     * TutorialSearch tut =
     * tutorialRepo.findByDocumentTypeAndDocumentTypeId(documentType, documentId);
     * Parser parser = new AutoDetectParser(); String docContent = "";
     * 
     * try { docContent = ContentsfromFile.extractContentForElastic(parser, path);
     * tut.setDocumentContent(docContent);
     * 
     * tutorialRepo.save(tut); } catch (Exception e) {
     * 
     * }
     * 
     * return 1;
     * 
     * }
     * 
     */

    @PostMapping("/addDocument/{documentId}/{documentType}/{documentPath}/{documenttUrl}/{rank}/{view_url}/{language}")
    public Map<Long, String> addDocument(@PathVariable String documentId, @PathVariable String documentType,
            @PathVariable String documentPath, @PathVariable String documenttUrl, @PathVariable int rank,
            @PathVariable String view_url, @PathVariable String language, @RequestParam String category,
            @RequestParam String topic, @RequestParam String outlinePath) {

        Map<Long, String> resultMap = new HashMap<>();
        long queueId = queuemntService.getNewId();

        if (doesFileExist(documentPath)) {

            QueueManagement queuemnt = new QueueManagement();
            queuemnt.setQueueId(queueId);
            queuemnt.setRequestId(documentType + documentId);
            queuemnt.setRequestTime(getCurrentTime());
            queuemnt.setRequestType("addDocument");
            queuemnt.setDocumentId(documentId);
            queuemnt.setDocumentType(documentType);
            queuemnt.setDocumentId(documentPath);
            queuemnt.setDocumentUrl(documenttUrl);
            queuemnt.setRank(rank);
            queuemnt.setView_url(view_url);
            queuemnt.setLanguage(language);
            queuemnt.setMessage("success");

            Optional category1 = Optional.ofNullable(category);
            if (category1.isPresent()) {
                queuemnt.setCategory((String) category1.get());
            }

            Optional topic1 = Optional.ofNullable(topic);
            if (topic1.isPresent()) {
                queuemnt.setTopic((String) topic1.get());
            }

            Optional outlinePath1 = Optional.ofNullable(outlinePath);
            if (outlinePath1.isPresent()) {
                if (doesFileExist((String) outlinePath1.get())) {
                    queuemnt.setOutlinePth((String) outlinePath1.get());
                }

            }

            queRepo.save(queuemnt);
            resultMap.put(queueId, "success");
        }

        else {
            resultMap.put(queueId, "failed");
        }

        return resultMap;

    }

    @PutMapping("/updateDocument/{documentId}/{documentType}/{documentPath}")
    public Map<Long, String> updateDocument(@PathVariable String documentId, @PathVariable String documentType,
            @PathVariable String documentPath, @RequestParam String category, @RequestParam String topic,
            @RequestParam String outlinePath) {

        QueueManagement queuemnt = queRepo.findByDocumentTypeAndDocumentId(documentType, documentId);
        Map<Long, String> resultMap = new HashMap<>();

        if (queuemnt != null) {
            if (doesFileExist(documentPath)) {
                queuemnt.setDocumentPath(documentPath);
                queuemnt.setMessage("success");
                Optional category1 = Optional.ofNullable(category);
                if (category1.isPresent()) {
                    queuemnt.setCategory((String) category1.get());
                }

                Optional topic1 = Optional.ofNullable(topic);
                if (topic1.isPresent()) {
                    queuemnt.setTopic((String) topic1.get());
                }

                Optional outlinePath1 = Optional.ofNullable(outlinePath);
                if (outlinePath1.isPresent()) {
                    if (doesFileExist((String) outlinePath1.get())) {
                        queuemnt.setOutlinePth((String) outlinePath1.get());
                    }

                }

                queRepo.save(queuemnt);
                resultMap.put(queuemnt.getQueueId(), "success");
            }

            else {
                resultMap.put(queuemnt.getQueueId(), "failed");
            }
        } else {
            resultMap.put(null, "failed");
        }

        return resultMap;

    }

    @PutMapping("/updateDocumentRank/{documentId}/{documentType}/{rank}")
    public Map<Long, String> updateDocumentRank(@PathVariable String documentId, @PathVariable String documentType,
            @PathVariable int rank) {

        QueueManagement queuemnt = queRepo.findByDocumentTypeAndDocumentId(documentType, documentId);
        Map<Long, String> resultMap = new HashMap<>();

        if (queuemnt != null) {
            queuemnt.setRank(rank);
            queuemnt.setMessage("success");
            resultMap.put(queuemnt.getQueueId(), "success");

        } else {
            resultMap.put(null, "failed");
        }

        return resultMap;

    }

    @DeleteMapping("/deleteDocument/{documentType}/{documentTypeId}")
    public Map<Long, String> deleteDocument(@PathVariable String documentId, @PathVariable String documentType) {

        QueueManagement queuemnt = queRepo.findByDocumentTypeAndDocumentId(documentType, documentId);
        Map<Long, String> resultMap = new HashMap<>();

        if (queuemnt != null) {

            queRepo.delete(queuemnt);
            resultMap.put(queuemnt.getQueueId(), "success");
        } else {
            resultMap.put(null, "failed");
        }

        return resultMap;

    }

    /************
     * Testing
     ************************************************************/

    String path = "D:\\Users\\aloks\\Documents\\TimeScript.pdf";

    @PostMapping("/saveTest")
    Map<Long, String> saveTest() {
        Map<Long, String> resultMap = addDocument("1", "tutorial", path, "url", 1, "view_url", "English", "category",
                "topic", null);
        return resultMap;
    }

//    @PostMapping("/addDocumentTest/{content}/{documentType}/{documentTypeId}")
//    public int addDocumentTest(@PathVariable String content, @PathVariable String documentType,
//            @PathVariable String documentTypeId) {
//        TutorialSearch tut = new TutorialSearch();
//        // tut.setId(repo.findMaxId().getId());
//        tut.setId("1");
//        tut.setDocumentContent(content);
//        tut.setDocumentType(documentType);
//        tut.setDocumentId(documentTypeId);
//        tutorialRepo.save(tut);
//        return 1;
//
//    }
//
//    @PutMapping("/updateDocumentTest/{content}/{documentType}/{documentTypeId}")
//    public int updateDocumentTest(@PathVariable String content, @PathVariable String documentType,
//            @PathVariable String documentTypeId) {
//
//        TutorialSearch tut = tutorialRepo.findByDocumentTypeAndDocumentTypeId(documentType, documentTypeId);
//
//        tut.setDocumentContent(content);
//
//        tutorialRepo.save(tut);
//        return 1;
//
//    }
//
//    @PostMapping("/saveQueueTest/{requestId}/{requestType}")
//    QueueManagement saveQueueTest(@PathVariable String requestId, @PathVariable String requestType) {
//        QueueManagement q1 = new QueueManagement();
//        q1.setQueueId(queuemntService.getNewId());
//        q1.setRequestId(requestId);
//        q1.setRequestType(requestType);
//        queRepo.save(q1);
//        return q1;
//
//    }
//
//    @GetMapping("/findAllQueueTest")
//    Iterable<QueueManagement> findAllQueueTest() {
//
//        return queRepo.findAll();
//
//    }

}
