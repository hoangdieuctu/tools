package com.karrostech.tool.service;

import com.karrostech.tool.model.PushChannel;
import com.karrostech.tool.model.PushMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class PushNotificationService {

    @Autowired
    private GoogleChatService googleChatService;

    public void sendNotification(List<PushChannel> channels, PushMessage message) {
        log.info("Send push to channel({}): {}", channels, message);

        googleChatService.sendMsg2ChatChanel(message);
    }

}
