package com.karrostech.tool.messaging;

import com.karrostech.tool.model.ConsumerDataMessage;
import com.karrostech.tool.model.ConsumerKey;
import com.karrostech.tool.model.ConsumerOffset;
import com.karrostech.tool.model.CustomMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WebsocketMessageSender {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    private static final String PREFIX_TOPIC = "/topic/";

    private String getTopic(ConsumerKey consumerKey) {
        StringBuilder builder = new StringBuilder();
        builder.append(PREFIX_TOPIC).append(consumerKey.getEnv().name()).append("_").append(consumerKey.getTopic());

        if (ConsumerOffset.oldest.equals(consumerKey.getConsumerOffset())) {
            builder.append("_").append(consumerKey.getCustomId());
        }

        return builder.toString();
    }

    public void send(String topic, Object message) {
        if (message == null) {
            log.warn("Cannot send null value");
            return;
        }
        messagingTemplate.convertAndSend(topic, message);
    }

    public void send(ConsumerKey key, CustomMessage customMessage) {
        String topic = getTopic(key);
        send(topic, customMessage);
    }

    public void send(ConsumerKey key, ConsumerRecord<String, String> record) {
        ConsumerDataMessage message = new ConsumerDataMessage();
        message.setKey(record.key());
        message.setPartition(record.partition());
        message.setTimestamp(record.timestamp());
        message.setOffset(record.offset());
        message.setValue(record.value());

        String topic = getTopic(key);
        send(topic, message);
    }
}
