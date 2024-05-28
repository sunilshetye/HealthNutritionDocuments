package org.spoken_tutorial.health.elasticsearch.JsonService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    @Bean
    public RestTemplate restTemplate() {
        logger.info("RestTemplate is called");
        return new RestTemplate();
    }

//    @Bean
//    ServletListenerRegistrationBean<ServletContextListener> servletListener() {
//        ServletListenerRegistrationBean<ServletContextListener> srb = new ServletListenerRegistrationBean<>();
//        srb.setListener(new MySpecialListener());
//        logger.info("ServletListenerRegistrationBean is called");
//        return srb;
//    }
}