package org.spoken_tutorial.health.elasticsearch.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spoken_tutorial.health.elasticsearch.models.QueueManagement;
import org.spoken_tutorial.health.elasticsearch.repositories.QueueManagementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;

@Service
public class QueueManagementService {
    private static final Logger logger = LoggerFactory.getLogger(QueueManagementService.class);

    @Autowired
    private QueueManagementRepository repo;

    @Value("${spring.queryResult}")
    private int limitQuery;

    public long getNewId() {

        try {
            return repo.getNewId() + 1;
        } catch (Exception e) {

            logger.error("Error in getNewId of QueueManagement", e);
            return 1;
        }
    }

    public List<QueueManagement> findByStatusOrderByRequestTimeAsc(String status) {

        return repo.findByStatusOrderByRequestTimeAsc(status);

    }

    public List<QueueManagement> findByStatusOrderByRequestTimeAscWithNqueries(String status) {
        List<QueueManagement> queueList = repo.findByStatusOrderByRequestTimeAsc(status, Limit.of(limitQuery));
        return queueList;
    }

}
