package com.hoangdieuctu.tools.dlogs.config;

import com.hoangdieuctu.tools.dlogs.constant.Constants;
import com.hoangdieuctu.tools.dlogs.service.WsConnectionHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
public class WebSocketEventListener {

    private static Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private WsConnectionHolder wsConnectionHolder;

    @EventListener
    public void onDisconnected(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();

        logger.info("Unsubscribed: {}", sessionId);
        wsConnectionHolder.onUnsubscribed(sessionId);
    }

    @EventListener
    public void onSubscribed(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = accessor.getDestination();
        String sessionId = accessor.getSessionId();
        String pod = destination.substring(destination.lastIndexOf(Constants.SPLASH) + 1);

        logger.info("On subscribe session={}, pod={}", sessionId, pod);
        wsConnectionHolder.onSubscribed(sessionId, pod);
    }

}
