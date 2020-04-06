package com.karrostech.tool.config;

import com.karrostech.tool.service.NotificationService;
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
public class NotificationConfig {

    @Autowired
    private NotificationService notificationService;

    @Scheduled(fixedRate = 1000 * 60 * 5)
    public void execute() {
        log.info("Execute notification rules checking");
        notificationService.executeActiveRules();
    }
}

