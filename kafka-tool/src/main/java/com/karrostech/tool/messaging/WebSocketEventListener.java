package com.karrostech.tool.messaging;

import com.karrostech.tool.config.WebsocketConnectionHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Component
public class WebSocketEventListener {

    @Autowired
    private WebsocketConnectionHolder wsConnectionHolder;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        log.info("A new client connected");

        wsConnectionHolder.onConnected(event);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        log.info("One client disconnected");

        wsConnectionHolder.onDisconnected(event);
    }

}
