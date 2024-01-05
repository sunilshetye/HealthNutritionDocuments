package org.spoken_tutorial.health.elasticsearch.threadpool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private final Map<String, Long> runningDocuments = new HashMap<>();

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

    @Async
    public void queueProcessor() {

        logger.info("QueueProcessor method has started");

        while (true) {

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

    public Map<String, Long> getRunningDocuments() {
        return runningDocuments;
    }
}