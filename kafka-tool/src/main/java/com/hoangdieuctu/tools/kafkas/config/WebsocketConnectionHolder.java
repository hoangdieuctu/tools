package com.hoangdieuctu.tools.kafkas.config;

import com.hoangdieuctu.tools.kafkas.model.ConsumerKey;
import com.hoangdieuctu.tools.kafkas.model.ConsumerOffset;
import com.hoangdieuctu.tools.kafkas.service.ConsumerRunnerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.messaging.support.NativeMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class WebsocketConnectionHolder {

    private final Map<ConsumerKey, List<String>> consumerSessions = new HashMap<>();
    private final Map<String, ConsumerKey> sessionKey = new HashMap<>();

    @Autowired
    private ConsumerRunnerService consumerRunnerService;

    private String getSessionId(AbstractSubProtocolEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        return sha.getSessionId();
    }

    private ConsumerKey getConsumerKey(AbstractSubProtocolEvent event) {
        MessageHeaderAccessor accessor = NativeMessageHeaderAccessor.getAccessor(event.getMessage(), SimpMessageHeaderAccessor.class);
        GenericMessage<?> generic = (GenericMessage<?>) accessor.getHeader("simpConnectMessage");

        Map<Object, Object> headers = (Map<Object, Object>) generic.getHeaders().get("nativeHeaders");
        List<String> envs = (List<String>) headers.get("env");
        List<String> topics = (List<String>) headers.get("topic");
        List<String> offsets = (List<String>) headers.get("offset");
        List<String> customIds = (List<String>) headers.get("customId");
        List<String> partitionOffsets = (List<String>) headers.get("partitionOffset");
        List<String> partitionNumbers = (List<String>) headers.get("partitionNumber");

        if (CollectionUtils.isEmpty(offsets) || ConsumerOffset.latest.name().equals(offsets.get(0))) {
            return new ConsumerKey(envs.get(0), topics.get(0));
        }

        Long partitionOffset = CollectionUtils.isEmpty(partitionOffsets) ? null : Long.parseLong(partitionOffsets.get(0));
        Integer partitionNumber = CollectionUtils.isEmpty(partitionNumbers) ? null : Integer.parseInt(partitionNumbers.get(0));

        return new ConsumerKey(envs.get(0), topics.get(0), customIds.get(0), partitionOffset, partitionNumber);
    }

    public synchronized final void onConnected(AbstractSubProtocolEvent event) {
        String sessionId = getSessionId(event);
        ConsumerKey consumerKey = getConsumerKey(event);

        log.info("On connected, sessionId={}, consumerKey={}", sessionId, consumerKey);

        List<String> sessions = consumerSessions.get(consumerKey);
        if (CollectionUtils.isEmpty(sessions)) {
            sessions = new ArrayList<>();
            sessions.add(sessionId);
            consumerSessions.put(consumerKey, sessions);

            log.info("A new session was added");
        } else {
            sessions.add(sessionId);
        }

        sessionKey.put(sessionId, consumerKey);

        consumerRunnerService.consume(consumerKey);
    }


    public synchronized final void onDisconnected(AbstractSubProtocolEvent event) {
        String sessionId = getSessionId(event);
        ConsumerKey consumerKey = sessionKey.remove(sessionId);

        log.info("On disconnected, sessionId={}, consumerKey={}", sessionId, consumerKey);
        if (consumerKey != null) {
            List<String> sessions = consumerSessions.get(consumerKey);
            if (!CollectionUtils.isEmpty(sessions)) {
                sessions.remove(sessionId);

                if (sessions.isEmpty()) {
                    consumerRunnerService.unconsume(consumerKey);
                }
            }
        }
    }

    public Map<String, ConsumerKey> getSessions() {
        return sessionKey;
    }

    public ConsumerKey getConsumerKey(String sessionId) {
        return sessionKey.get(sessionId);
    }
}
