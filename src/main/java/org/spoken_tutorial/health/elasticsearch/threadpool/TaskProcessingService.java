package org.spoken_tutorial.health.elasticsearch.threadpool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spoken_tutorial.health.elasticsearch.config.Config;
import org.spoken_tutorial.health.elasticsearch.models.QueueManagement;
import org.spoken_tutorial.health.elasticsearch.repositories.QueueManagementRepository;
import org.spoken_tutorial.health.elasticsearch.services.QueueManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class TaskProcessingService {

    private static final Logger logger = LoggerFactory.getLogger(TaskProcessingService.class);
    @Autowired
    private QueueManagementService queuemntService;

    @Autowired
    private QueueManagementRepository repo;

    private Map<String, Long> runningDocuments = new HashMap<>();

    private ExecutorService executorService;

    @Async
    public void queueProcessor() {

        logger.info("QueueProcessor method has started");

        while (true) {

            Map<String, Long> skippedDocuments = new HashMap<>();
            skippedDocuments.putAll(runningDocuments);
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
                if (skippedDocuments.containsKey(qmnt.getDocumentId())) {

                    continue;
                }

                skippedDocuments.put(qmnt.getDocumentId(), System.currentTimeMillis());
                runningDocuments.put(qmnt.getDocumentId(), System.currentTimeMillis());
                qmnt.setStatus(Config.STATUS_QUEUED);
                qmnt.setQueueTime(System.currentTimeMillis());
                repo.save(qmnt);
                executorService.submit(qmnt);
                count = count + 1;

            }
            if (count > 0) {
                try {
                    Thread.sleep(Config.TASK_SLEEP_TIME);
                } catch (InterruptedException e) {

                }

            } else {
                try {
                    Thread.sleep(Config.NO_TASK_SLEEP_TIME);
                } catch (InterruptedException e) {

                }

            }
        }

    }
}