package com.hoangdieuctu.tools.kafkas.thread;

import com.hoangdieuctu.tools.kafkas.model.ConsumerKey;
import com.hoangdieuctu.tools.kafkas.model.ConsumerOffset;
import com.hoangdieuctu.tools.kafkas.messaging.ConsumerMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ConsumerThread extends Thread {

    private boolean isRunning = true;

    private ConsumerKey consumerKey;
    private KafkaConsumer<String, String> consumer;
    private ConsumerMessageListener messageListener;

    public void setConsumerKey(ConsumerKey consumerKey) {
        this.consumerKey = consumerKey;
    }

    public void setConsumer(KafkaConsumer<String, String> consumer) {
        this.consumer = consumer;
    }

    public void setMessageListener(ConsumerMessageListener messageListener) {
        this.messageListener = messageListener;
    }

    public void unconsume() {
        this.isRunning = false;
    }

    @Override
    public void run() {
        log.info("Starting consumer thread: {}", consumerKey);

        consumer.subscribe(Collections.singleton(consumerKey.getTopic()), new ConsumerRebalanceListener() {
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                List<Integer> indexes = partitions.stream().map(p -> p.partition()).collect(Collectors.toList());
                Collections.sort(indexes);
                log.info("Revoked from partitions: {}", indexes);
            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                List<Integer> indexes = partitions.stream().map(p -> p.partition()).collect(Collectors.toList());
                Collections.sort(indexes);
                log.info("Assigned to partitions: {}", indexes);

                if (ConsumerOffset.latest.equals(consumerKey.getConsumerOffset())) {
                    log.info("Reset offset of all partitions to latest");
                    consumer.seekToEnd(partitions);
                } else {
                    Long partitionOffset = consumerKey.getPartitionOffset();

                    if (partitionOffset == null) {
                        log.info("Reset offset of all partitions to oldest");
                        consumer.seekToBeginning(partitions);
                    } else {
                        log.info("Reset offset of all partitions to: {}", partitionOffset);
                        partitions.forEach(p -> consumer.seek(p, partitionOffset));
                    }
                }
            }
        });
        while (isRunning) {
            String filter = consumerKey.getFilter();
            int duration = (filter != null && !filter.isEmpty() ? 10 : 1); // apply high duration when filtering

            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofSeconds(duration));
            consumerRecords.forEach(record -> {
                if (consumerKey.getPartitionNumber() == null
                        || consumerKey.getPartitionNumber().equals(record.partition())) {

                    if (filter == null || filter.isEmpty() || record.value().contains(filter)) { // for filtering
                        messageListener.onMessage(consumerKey, record);

                        int sleep = ConsumerOffset.latest.equals(consumerKey.getConsumerOffset()) ? 1 : 10;
                        try {
                            Thread.sleep(sleep); // prevent web socket disconnect
                        } catch (InterruptedException e) {
                            log.warn("Error while sleeping. {}", e.getMessage());
                        }
                    }
                }
            });
            if (!consumerRecords.isEmpty()) {
                consumer.commitAsync();
            }
        }
        consumer.unsubscribe();
        consumer.close();

        log.info("Unsubscribed and closed a consumer: {}", consumerKey);
    }

}
