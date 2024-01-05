package org.spoken_tutorial.health.elasticsearch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spoken_tutorial.health.elasticsearch.threadpool.TaskProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HealthNutritionElasticSearchApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(HealthNutritionElasticSearchApplication.class);

    @Autowired
    private TaskProcessingService taskProcessingService;

    public static void main(String[] args) {
        SpringApplication.run(HealthNutritionElasticSearchApplication.class, args);

    }

    @Override
    public void run(String... args) throws Exception {

        try {
            taskProcessingService.intializeQueue();
            taskProcessingService.queueProcessor();

        } catch (Exception e) {
            logger.error("Error in queueProcessor method", e);
        }
    }

}
