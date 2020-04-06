package com.hoangdieuctu.tools.kafkas.messaging;

import com.hoangdieuctu.tools.kafkas.model.ConsumerKey;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface ConsumerMessageListener {

    void onMessage(ConsumerKey consumerKey, ConsumerRecord<String, String> consumerRecord);

}
