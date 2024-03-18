package org.spoken_tutorial.health.elasticsearch.threadpool;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spoken_tutorial.health.elasticsearch.config.Config;
import org.spoken_tutorial.health.elasticsearch.models.QueueManagement;
import org.spoken_tutorial.health.elasticsearch.repositories.QueueManagementRepository;
import org.spoken_tutorial.health.elasticsearch.services.QueueManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Service
public class TaskProcessingService {

    private static final Logger logger = LoggerFactory.getLogger(TaskProcessingService.class);
    @Autowired
    private QueueManagementService queuemntService;

    @Autowired
    private QueueManagementRepository repo;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private ApplicationContext applicationContext;

    private final Map<String, Long> runningDocuments = new ConcurrentHashMap<>();

    public void intializeQueue() {
        List<QueueManagement> qmnts = queuemntService.findByStatusOrderByRequestTimeAsc(Config.STATUS_QUEUED);
        for (QueueManagement qmnt : qmnts) {
            logger.info("Pending:{}", qmnt);
            qmnt.setStatus(Config.STATUS_PENDING);
            qmnt.setQueueTime(0);
            repo.save(qmnt);
        }

        qmnts = queuemntService.findByStatusOrderByRequestTimeAsc(Config.STATUS_PROCESSING);
        for (QueueManagement qmnt : qmnts) {
            logger.info("Pending:{}", qmnt);
            qmnt.setStatus(Config.STATUS_PENDING);
            qmnt.setQueueTime(0);
            repo.save(qmnt);
        }

    }

    public boolean isURLWorking(String url) {
        boolean flag = false;

        try {

            URL url1 = new URL(url);

            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();

            // connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            // Check if the response code is OK (200)
            if (responseCode == HttpURLConnection.HTTP_OK) {
                flag = true;
                logger.info("Connection Request successful");
            } else {

                logger.info("Request failed with status code:{} ", responseCode);
            }

            connection.disconnect();

        } catch (IOException e) {

            logger.error("Request failed due to I/O error: " + e);
        }
        return flag;

    }

    @Async
    public void queueProcessor() {

        logger.info("starting QueueProcessor thread");

        while (true) {
            if (Thread.interrupted()) {
                logger.info("Interrupted");
                break;
            }

            Map<String, Long> skippedDocuments = new HashMap<>();
            skippedDocuments.putAll(getRunningDocuments());
            int count = 0;

            List<QueueManagement> qmnts = queuemntService.findByStatusOrderByRequestTimeAsc(Config.STATUS_PENDING);
            if (qmnts == null) {
                try {
                    Thread.sleep(Config.NO_TASK_SLEEP_TIME);
                    continue;

                } catch (InterruptedException e) {

                }
            }

            for (QueueManagement qmnt : qmnts) {
                logger.info("Queueing:{}", qmnt);
                if (skippedDocuments.containsKey(qmnt.getDocumentId())) {

                    continue;
                }

                skippedDocuments.put(qmnt.getDocumentId(), System.currentTimeMillis());
                getRunningDocuments().put(qmnt.getDocumentId(), System.currentTimeMillis());
                qmnt.setStatus(Config.STATUS_QUEUED);
                qmnt.setQueueTime(System.currentTimeMillis());
                repo.save(qmnt);

                applicationContext.getAutowireCapableBeanFactory().autowireBean(qmnt);
                taskExecutor.submit(qmnt);
                count = count + 1;

            }
            long sleepTime = count > 0 ? Config.TASK_SLEEP_TIME : Config.NO_TASK_SLEEP_TIME;
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                logger.info("Interrupted");
                break;
            }
        }

    }

    public Map<String, Long> getRunningDocuments() {
        return runningDocuments;
    }

    public void stop() {
        logger.info("stopping QueueProcessor thread");
        taskExecutor.shutdown();
    }
}