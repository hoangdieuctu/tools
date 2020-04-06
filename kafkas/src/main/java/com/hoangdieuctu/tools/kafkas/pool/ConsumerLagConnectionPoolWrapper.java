package com.hoangdieuctu.tools.kafkas.pool;

import com.omarsmak.kafka.consumer.lag.monitoring.client.KafkaConsumerLagClient;
import com.omarsmak.kafka.consumer.lag.monitoring.client.data.Lag;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class ConsumerLagConnectionPoolWrapper {

    private BlockingQueue<KafkaConsumerLagClient> pool;

    public ConsumerLagConnectionPoolWrapper(int maxSize) {
        this.pool = new LinkedBlockingQueue<>(maxSize);
    }

    private void closeQuietly(KafkaConsumerLagClient client) {
        try {
            client.close();
        } catch (Exception ex) {
        }
    }

    public void close() {
        pool.forEach(connection -> closeQuietly(connection));
        pool.clear();
    }

    public void offer(KafkaConsumerLagClient client) {
        pool.offer(client);
    }

    public List<Lag> getConsumerLag(String groupId) {
        try {
            KafkaConsumerLagClient client = pool.take();

            try {
                List<Lag> consumerLags = client.getConsumerLag(groupId);
                return consumerLags;
            } finally {
                offer(client);
            }
        } catch (InterruptedException e) {
            log.error("Error while getting connection from pool. {}", e.getMessage());
        }

        return Collections.emptyList();
    }
}
