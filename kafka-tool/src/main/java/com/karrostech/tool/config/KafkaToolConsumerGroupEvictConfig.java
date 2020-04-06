package com.karrostech.tool.config;

import com.karrostech.tool.model.Environment;
import com.karrostech.tool.service.KafkaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(
        value = "distribute.role",
        havingValue = "master")
public class KafkaToolConsumerGroupEvictConfig {

    @Autowired
    private KafkaService kafkaService;

    @Scheduled(fixedRate = 3 * 1000 * 60 * 60)
    public void evictConsumerGroups() {
        log.info("Evicting all unused kafka tool consumer groups");

        Environment[] envs = Environment.values();
        for (Environment env : envs) {
            kafkaService.evictKafkaToolGroups(env);
        }
    }
}

