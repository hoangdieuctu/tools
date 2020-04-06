package com.karrostech.tool.messaging;

import com.karrostech.tool.model.ConsumerKey;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface ConsumerMessageListener {

    void onMessage(ConsumerKey consumerKey, ConsumerRecord<String, String> consumerRecord);

}
