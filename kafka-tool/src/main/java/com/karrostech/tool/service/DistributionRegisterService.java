package com.karrostech.tool.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.InetAddress;

@Slf4j
@Component
@ConditionalOnProperty(
        value = "distribute.role",
        havingValue = "slave")
public class DistributionRegisterService {

    private static final String REGISTER_PATH = "/distribute/register";
    private static final String UNREGISTER_PATH = "/distribute/unregister";

    @Autowired
    private RestTemplate restTemplate;

    @Value("${master.url}")
    private String masterUrl;

    private String slaveHost;

    public DistributionRegisterService() throws IOException {
        this.slaveHost = InetAddress.getLocalHost().getHostName();
    }

    private void callRequest(String url) {
        try {
            restTemplate.getForObject(url, Void.class);
        } catch (Exception ex) {
            log.warn("Error while getting to url={}.", url);
            log.warn("Error: {}", ex.getMessage());
        }
    }

    @Scheduled(fixedRate = 1000 * 60)
    public void register() {
        log.info("Register to master, host={}", slaveHost);
        String url = masterUrl + REGISTER_PATH + "?host=" + slaveHost;
        callRequest(url);
    }

    @PreDestroy
    public void unregister() {
        log.info("Unregister to master");
        String url = masterUrl + UNREGISTER_PATH + "?host=" + slaveHost;
        callRequest(url);
    }
}
