package org.spoken_tutorial.health.elasticsearch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spoken_tutorial.health.elasticsearch.threadpool.TaskProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@ServletComponentScan
@EnableCaching
@SpringBootApplication

@EnableAsync

public class HealthNutritionElasticSearchApplication extends SpringBootServletInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(HealthNutritionElasticSearchApplication.class);

    @Autowired
    private TaskProcessingService taskProcessingService;

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

        return application.sources(HealthNutritionElasticSearchApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(HealthNutritionElasticSearchApplication.class, args).close();

    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Starting");

        try {
            taskProcessingService.intializeQueue();
            taskProcessingService.queueProcessor();

        } catch (Exception e) {
            logger.error("Error in queueProcessor method", e);
        }
    }

}
