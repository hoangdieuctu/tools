package com.hoangdieuctu.tools.dlogs.service;

import io.kubernetes.client.openapi.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WsConnectionHolder {

    private static Logger logger = LoggerFactory.getLogger(WsConnectionHolder.class);

    private Map<String, String> sessionPod = new HashMap<>();
    private Map<String, List<String>> podSessions = new HashMap<>();

    @Autowired
    private LogService logService;

    public void onSubscribed(String session, String pod) {
        sessionPod.put(session, pod);

        podSessions.computeIfAbsent(pod, k -> new ArrayList<>());
        podSessions.get(pod).add(session);

        try {
            logger.info("Subscribed session={}, pod={}", session, pod);
            logService.connect(pod);
        } catch (ApiException e) {
            logger.info("Error while open connection. ", e);
        }
    }

    public void onUnsubscribed(String session) {
        String pod = sessionPod.remove(session);
        if (pod != null) {
            logger.info("Unsubscribe session={}, pod={}", session, pod);

            List<String> sessions = podSessions.get(pod);
            if (!CollectionUtils.isEmpty(sessions)) {
                sessions.remove(session);
                if (sessions.isEmpty()) {
                    logger.info("Last session closed, close connection to pod: {}", pod);
                    logService.close(pod);
                }
            }
        }
    }
}
