package com.karrostech.tool.config;

import com.karrostech.tool.service.AvlEventService;
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
public class AvlEventSendingConfig {

    @Autowired
    private AvlEventService avlEventService;

    @Scheduled(cron = "0 0 8 * * ?")
    public void noon() {
        sendEvents();
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void morning() {
        sendEvents();
    }

    private void sendEvents() {
        log.info("Sending avl events");
        avlEventService.sendAvlEvents();
    }
}

