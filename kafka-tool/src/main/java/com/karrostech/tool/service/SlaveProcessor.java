package com.karrostech.tool.service;

import com.google.gson.Gson;
import com.karrostech.tool.manager.KafkaConnectorManager;
import com.karrostech.tool.model.BulkProduce;
import com.karrostech.tool.model.Environment;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Slf4j
@Component
@ConditionalOnProperty(
        value = "distribute.role",
        havingValue = "slave")
public class SlaveProcessor extends Thread {

    @Autowired
    private KafkaConnectorManager kafkaConnectorManager;

    @Autowired
    private BulkRequestQueueHolder queueHolder;

    @Value("${distribute.topic}")
    private String topic;

    private boolean isRunning = true;

    private KafkaConsumer<String, String> consumer;

    @PostConstruct
    public void init() {
        this.start();
    }

    @Override
    public void run() {
        log.info("Starting consume to topic: {}", topic);

        consumer = kafkaConnectorManager.getConsumer(Environment.NON_DEV, UUID.randomUUID().toString());
        consumer.subscribe(Collections.singleton(topic), new ConsumerRebalanceListener() {
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                log.info("On partitions revoked: {}", partitions.size());
            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                log.info("Assigned to partitions: {}", partitions.size());
                consumer.seekToEnd(partitions);
            }
        });
        while (isRunning) {
            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofSeconds(1));
            consumerRecords.forEach(record -> {
                String value = record.value();
                BulkProduce bulkProduce = new Gson().fromJson(value, BulkProduce.class);
                execute(bulkProduce);
            });
            if (!consumerRecords.isEmpty()) {
                consumer.commitAsync();
            }
        }

        log.info("Unsubscribed and closed a consumer to topic: {}", topic);
    }

    @PreDestroy
    public void unconsume() {
        this.isRunning = false;
        consumer.unsubscribe();
        consumer.close();
    }

    public void execute(BulkProduce bulkProduce) {
        log.info("Slave execute job: {}", bulkProduce.getName());
        queueHolder.add(bulkProduce);
    }
}
