package com.hoangdieuctu.tools.kafkas.service;

import com.hoangdieuctu.tools.kafkas.manager.KafkaConnectorManager;
import com.hoangdieuctu.tools.kafkas.model.ConsumerKey;
import com.hoangdieuctu.tools.kafkas.model.ConsumerOffset;
import com.hoangdieuctu.tools.kafkas.model.Environment;
import com.hoangdieuctu.tools.kafkas.thread.ConsumerThread;
import com.hoangdieuctu.tools.kafkas.messaging.WebsocketMessageSender;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class ConsumerRunnerService {

    private final Map<ConsumerKey, ConsumerThread> consumers = new HashMap<>();

    @Autowired
    private KafkaConnectorManager kafkaConnectorManager;

    @Autowired
    private WebsocketMessageSender wsMessageSender;

    private KafkaConsumer<String, String> getConsumerGroup(ConsumerKey consumerKey) {
        Environment env = consumerKey.getEnv();

        return ConsumerOffset.latest.equals(consumerKey.getConsumerOffset()) ?
                kafkaConnectorManager.getConsumer(env) :
                kafkaConnectorManager.getConsumer(env, consumerKey.getCustomId());
    }

    public void consume(ConsumerKey consumerKey) {
        ConsumerThread currentConsumerThread = consumers.get(consumerKey);
        if (currentConsumerThread != null) {
            log.info("Found existing consumer for key: {}", consumerKey);
            return;
        }

        KafkaConsumer<String, String> consumer = getConsumerGroup(consumerKey);

        ConsumerThread consumerThread = new ConsumerThread();
        consumerThread.setConsumerKey(consumerKey);
        consumerThread.setConsumer(consumer);
        consumerThread.setMessageListener((key, record) -> wsMessageSender.send(key, record));

        consumerThread.start();
        log.info("Started a new thread to consume data: {}", consumerKey);

        consumers.put(consumerKey, consumerThread);
        log.info("Current consumer threads: {}", consumers.size());
    }

    public void unconsume(ConsumerKey consumerKey) {
        log.info("Unconsume: {}", consumerKey);

        ConsumerThread consumerThread = consumers.remove(consumerKey);
        consumerThread.unconsume();

        log.info("Current consumer threads: {}", consumers.size());
    }
}
