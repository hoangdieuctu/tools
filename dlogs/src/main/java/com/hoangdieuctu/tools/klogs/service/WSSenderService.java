package com.hoangdieuctu.tools.klogs.service;

import com.hoangdieuctu.tools.klogs.constant.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
public class WSSenderService {

    private static Logger logger = LoggerFactory.getLogger(WSSenderService.class);

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    public void send(String topic, Object content) {
        logger.debug("Send message to topic: {}", topic);
        messagingTemplate.convertAndSend(Constants.TOPIC_PREFIX + topic, content);
    }


}
