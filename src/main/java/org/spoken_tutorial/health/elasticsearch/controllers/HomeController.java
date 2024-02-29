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
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spoken_tutorial.health.elasticsearch.config.Config;
import org.spoken_tutorial.health.elasticsearch.models.DocumentSearch;
import org.spoken_tutorial.health.elasticsearch.models.QueueManagement;
import org.spoken_tutorial.health.elasticsearch.repositories.DocumentSearchRepository;
import org.spoken_tutorial.health.elasticsearch.repositories.QueueManagementRepository;
import org.spoken_tutorial.health.elasticsearch.services.DocumentSearchService;
import org.spoken_tutorial.health.elasticsearch.services.QueueManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private DocumentSearchRepository docuSearchRepo;

    @Autowired
    private QueueManagementRepository queRepo;

    @Autowired
    private QueueManagementService queuemntService;

    @Autowired
    private Config config;

    @Autowired
    private ElasticsearchOperations operations;

    @Autowired
    DocumentSearchService docuSearchService;

    private Timestamp getCurrentTime() {

        Date date = new Date();
        long t = date.getTime();
        Timestamp st = new Timestamp(t);

        return st;
    }

    private boolean doesFileExist(String filePath) {
        if (filePath.contains("..") || !filePath.startsWith(config.BASE_NAME)) {
            return false;
        }
        Path path = Paths.get(config.BASE_PATH, filePath);

        return Files.exists(path);
    }

    @GetMapping("/queueStatus/{queueId}")
    public Map<String, String> queueStatus(@PathVariable Long queueId) {

        Map<String, String> resultMap = new HashMap<>();
        QueueManagement queuemnt = queRepo.findByQueueId(queueId);

        if (queuemnt == null) {
            resultMap.put(Config.STATUS, Config.STATUS_NOTFOUND);

        } else {

            String status = queuemnt.getStatus();

            if (status.equals(Config.STATUS_PENDING)) {
                resultMap.put(Config.STATUS, status);
            }

            else if (status.equals(Config.STATUS_PROCESSING)) {
                resultMap.put(Config.STATUS, status);
                resultMap.put(Config.START_TIME, Long.toString(queuemnt.getStartTime()));
                resultMap.put(Config.CURRENT_TIME, Long.toString(System.currentTimeMillis()));
            } else if (status.equals(Config.STATUS_QUEUED)) {
                resultMap.put(Config.STATUS, status);
                resultMap.put(Config.QUEUE_TIME, Long.toString(queuemnt.getQueueTime()));
                resultMap.put(Config.CURRENT_TIME, Long.toString(System.currentTimeMillis()));
            } else if (status.equals(Config.STATUS_DONE)) {
                resultMap.put(Config.STATUS, status);
                resultMap.put(Config.START_TIME, Long.toString(queuemnt.getStartTime()));
                resultMap.put(Config.END_TIME, Long.toString(queuemnt.getEndTime()));
                resultMap.put(Config.PROCESSING_TIME, Long.toString(queuemnt.getProcesingTime()));

            }

            else if (status.equals(Config.STATUS_FAILED)) {
                resultMap.put(Config.STATUS, status);
                resultMap.put(Config.REASON, queuemnt.getReason());
                resultMap.put(Config.START_TIME, Long.toString(queuemnt.getStartTime()));
                resultMap.put(Config.END_TIME, Long.toString(queuemnt.getEndTime()));
                resultMap.put(Config.PROCESSING_TIME, Long.toString(queuemnt.getProcesingTime()));
            }

        }

        return resultMap;

    }

    @GetMapping("/documentStatus/{documentId}")
    public Map<String, String> documentStatus(@PathVariable String documentId) {

        Map<String, String> resultMap = new HashMap<>();
        DocumentSearch documentSearch = docuSearchRepo.findByDocumentId(documentId);

        if (documentSearch == null) {
            resultMap.put(Config.STATUS, Config.STATUS_NOTFOUND);
        } else {
            resultMap.put(Config.DOCUMENT_ID, documentSearch.getDocumentId());
            resultMap.put(Config.DOCUMENT_TYPE, documentSearch.getDocumentType());
            resultMap.put(Config.LANGUAGE, documentSearch.getLanguage());
            resultMap.put(Config.RANK, Integer.toString(documentSearch.getRank()));
            resultMap.put(Config.CREATION_TIME, Long.toString(documentSearch.getCreationTime()));
            resultMap.put(Config.MODIFICATION_TIME, Long.toString(documentSearch.getModificationTime()));
            resultMap.put(Config.CHANGE_TIME, Long.toString(documentSearch.getChangeTime()));
        }
        return resultMap;

    }

    @GetMapping("/")
    public String Wlecome() {
        return "Welcome to Health and Nutrition Elastic Search Application";
    }

    @GetMapping("/findAll")
    public List<QueueManagement> findAll() {
        return queRepo.findAll();
    }

    public Map<String, String> addDocument(String documentId, String documentType, String documentPath,
            String documentUrl, int rank, String view_url, int languageId, String language,
            Optional<Integer> categoryId, Optional<String> category, Optional<Integer> topicId, Optional<String> topic,
            Optional<String> outlinePath, String requestType) {

        Map<String, String> resultMap = new HashMap<>();

        logger.info(
                "RequestType:{} Language:{} View_URL: {} documentId:{} documentPath:{} documentType:{} outlinePath:{}",
                requestType, language, view_url, documentId, documentPath, documentType, outlinePath);

        if (documentPath != null && !doesFileExist(documentPath)) {
            resultMap.put(Config.STATUS, Config.STATUS_FAILED);
            resultMap.put(Config.REASON, "document file does not exist");
            return resultMap;
        }

        if (outlinePath != null && outlinePath.isPresent() && !doesFileExist(outlinePath.get())) {

            resultMap.put(Config.STATUS, Config.STATUS_FAILED);
            resultMap.put(Config.REASON, "outline file does not exist");
            return resultMap;

        }

        QueueManagement queuemnt = new QueueManagement();

        if (outlinePath != null && outlinePath.isPresent())
            queuemnt.setOutlinePath(outlinePath.get());
        queuemnt.setRequestTime(getCurrentTime());
        queuemnt.setRequestType(requestType);
        queuemnt.setDocumentId(documentId);
        queuemnt.setDocumentType(documentType);
        queuemnt.setDocumentPath(documentPath);
        queuemnt.setDocumentUrl(documentUrl);
        queuemnt.setRank(rank);
        queuemnt.setViewUrl(view_url);
        queuemnt.setLanguageId(languageId);
        queuemnt.setStatus("pending");
        if (language != null)
            queuemnt.setLanguage(language);
        if (category != null && category.isPresent())
            queuemnt.setCategory(category.get());
        if (categoryId != null && categoryId.isPresent())
            queuemnt.setCategoryId(categoryId.get());
        if (topic != null && topic.isPresent())
            queuemnt.setTopic(topic.get());
        if (topicId != null && topicId.isPresent())
            queuemnt.setTopicId(topicId.get());

        queRepo.save(queuemnt);

        resultMap.put(Config.QUEUE_ID, Long.toString(queuemnt.getQueueId()));
        resultMap.put(Config.STATUS, Config.SUCCESS);

        return resultMap;

    }

    @PostMapping("/addDocument/{documentId}/{documentType}/{languageId}/{language}/{rank}")
    public Map<String, String> addDocument(@PathVariable String documentId, @PathVariable String documentType,
            @PathVariable int languageId, @PathVariable String language, @PathVariable int rank,
            @RequestParam String documentPath, @RequestParam String documentUrl, @RequestParam String view_url,
            @RequestParam Optional<Integer> categoryId, @RequestParam Optional<String> category,
            @RequestParam Optional<Integer> topicId, @RequestParam Optional<String> topic,
            @RequestParam Optional<String> outlinePath) {

        return addDocument(documentId, documentType, documentPath, documentUrl, rank, view_url, languageId, language,
                categoryId, category, topicId, topic, outlinePath, Config.ADD_DOCUMENT);
    }

    @PostMapping("/updateDocument/{documentId}/{documentType}/{languageId}/{language}/{rank}")
    public Map<String, String> updateDocument(@PathVariable String documentId, @PathVariable String documentType,
            @PathVariable int languageId, @PathVariable String language, @PathVariable int rank,
            @RequestParam String documentPath, @RequestParam String documentUrl, @RequestParam String view_url,
            @RequestParam Optional<String> category, @RequestParam Optional<Integer> categoryId,
            @RequestParam Optional<String> topic, @RequestParam Optional<Integer> topicId,
            @RequestParam Optional<String> outlinePath) {

        return addDocument(documentId, documentType, documentPath, documentUrl, rank, view_url, languageId, language,
                categoryId, category, topicId, topic, outlinePath, Config.UPDATE_DOCUMENT);
    }

    @GetMapping("/updateDocumentRank/{documentId}/{documentType}/{languageId}/{rank}")
    public Map<String, String> updateDocumentRank(@PathVariable String documentId, @PathVariable String documentType,
            @PathVariable int languageId, @PathVariable int rank) {
        return addDocument(documentId, documentType, null, null, rank, null, languageId, null, null, null, null, null,
                null, Config.UPDATE_DOCUMENT_RANK);
    }

    @GetMapping("/deleteDocument/{documentId}/{documentType}/{languageId}")
    public Map<String, String> deleteDocument(@PathVariable String documentId, @PathVariable String documentType,
            @PathVariable int languageId) {

        return addDocument(documentId, documentType, null, null, 0, null, languageId, null, null, null, null, null,
                null, Config.DELETE_DOCUMENT);
    }

    @GetMapping("/search")
    List<DocumentSearch> findByDocumentContentTest(@RequestParam Optional<Integer> categoryId,
            @RequestParam Optional<Integer> topicId, @RequestParam Optional<Integer> languageId,
            @RequestParam Optional<String> documentContent) {
        Criteria criteria = new Criteria();

        if (categoryId.isPresent()) {
            criteria = criteria.and("categoryId").is(categoryId.get());
        }

        if (topicId.isPresent()) {
            criteria = criteria.and("topicId").is(topicId.get());
        }

        if (languageId.isPresent()) {
            criteria = criteria.and("languageId").is(languageId.get());
        }

        if (documentContent.isPresent()) {
            criteria = criteria.and("documentContent").is(documentContent.get());
        }

        SearchHits<DocumentSearch> searchHits = operations.search(new CriteriaQuery(criteria), DocumentSearch.class);

        // to only get the objects without hit information
        return searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());

    }
}
