package org.spoken_tutorial.health.elasticsearch;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spoken_tutorial.health.elasticsearch.threadpool.TaskProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HealthNutritionElasticSearchApplicationTests {
    private static final Logger logger = LoggerFactory.getLogger(HealthNutritionElasticSearchApplicationTests.class);

    @Autowired
    private TaskProcessingService taskProcessingService;

    @Test
    void contextLoads() {
        logger.info("Stopping");
        taskProcessingService.stop();
    }

}
