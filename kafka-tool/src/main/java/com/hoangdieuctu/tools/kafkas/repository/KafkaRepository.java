package com.hoangdieuctu.tools.kafkas.repository;

import com.hoangdieuctu.tools.kafkas.manager.KafkaConnectorManager;
import com.hoangdieuctu.tools.kafkas.model.*;
import com.hoangdieuctu.tools.kafkas.pool.AdminClientConnectionPoolWrapper;
import com.hoangdieuctu.tools.kafkas.pool.ConsumerLagConnectionPoolWrapper;
import com.omarsmak.kafka.consumer.lag.monitoring.client.data.Lag;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.ConsumerGroupDescription;
import org.apache.kafka.clients.admin.ConsumerGroupListing;
import org.apache.kafka.clients.admin.DescribeConsumerGroupsResult;
import org.apache.kafka.clients.admin.ListConsumerGroupsResult;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Component
public class KafkaRepository {

    @Autowired
    private KafkaConnectorManager kafkaConnectorManager;

    public Map<String, List<PartitionInfo>> getKafkaTopics(EnvConfig env) {
        KafkaConsumer<String, String> consumer = kafkaConnectorManager.getConsumer(env);
        try {
            return consumer.listTopics();
        } finally {
            consumer.close();
        }
    }

    public KafkaRecordData send(EnvConfig env, String topic, Integer partition, String key, String message) {
        KafkaProducer<String, String> producer = kafkaConnectorManager.getProducer(env);

        ProducerRecord<String, String> record = new ProducerRecord<>(topic, partition, key, message);
        try {
            RecordMetadata metadata = producer.send(record).get();
            log.info("Message sent to {}. partition={}, offset={}", topic, metadata.partition(), metadata.offset());

            return new KafkaRecordData(metadata.timestamp(), metadata.partition(), metadata.offset());
        } catch (Exception e) {
            log.error("Error while sending kafka message. {}", e.getMessage());

            return new KafkaRecordData();
        }
    }

    public List<String> getConsumerGroups(EnvConfig env) {
        AdminClientConnectionPoolWrapper connection = kafkaConnectorManager.getAdminClient(env);
        ListConsumerGroupsResult consumerGroups = connection.listConsumerGroups();
        if (consumerGroups == null) {
            return Collections.emptyList();
        }

        KafkaFuture<Collection<ConsumerGroupListing>> allGroups = consumerGroups.all();
        try {
            Collection<ConsumerGroupListing> listings = allGroups.get();
            return listings.stream().map(ConsumerGroupListing::groupId).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Cannot load consumer groups. ", e);

            return Collections.emptyList();
        }
    }

    public ConsumerGroupDetail describeConsumerGroup(EnvConfig env, String groupId) {
        AdminClientConnectionPoolWrapper connection = kafkaConnectorManager.getAdminClient(env);

        DescribeConsumerGroupsResult describe = connection.describeConsumerGroups(Collections.singleton(groupId));
        if (describe == null) {
            return new ConsumerGroupDetail();
        }

        try {
            Map<String, ConsumerGroupDescription> groupDescription = describe.all().get();
            ConsumerGroupDescription description = groupDescription.get(groupId);
            if (description == null) {
                return new ConsumerGroupDetail();
            }

            return new ConsumerGroupDetail(description);
        } catch (Exception e) {
            log.error("Cannot load group description. ", e);
            return new ConsumerGroupDetail();
        }
    }

    public Map<String, ConsumerGroupDetail> describeConsumerGroups(EnvConfig env, List<String> groupIds) {
        Map<String, ConsumerGroupDetail> results = new HashMap<>();

        AdminClientConnectionPoolWrapper connection = kafkaConnectorManager.getAdminClient(env);
        DescribeConsumerGroupsResult describe = connection.describeConsumerGroups(groupIds);
        if (describe == null) {
            return results;
        }

        try {
            Map<String, ConsumerGroupDescription> groupsDescription = describe.all().get();
            Set<String> keys = groupsDescription.keySet();
            for (String key : keys) {
                results.put(key, new ConsumerGroupDetail(groupsDescription.get(key)));
            }
        } catch (Exception e) {
            log.error("Cannot load group description. ", e);
        }

        return results;
    }

    public Map<TopicPartition, PartitionOffsets> getConsumerGroupOffsets(EnvConfig env, String topic, String groupId) {
        Map<TopicPartition, Long> endOffsets = getEndOffset(env, topic);

        Map<TopicPartition, PartitionOffsets> result = new HashMap<>();

        AdminClientConnectionPoolWrapper connection = kafkaConnectorManager.getAdminClient(env);
        try {
            Map<TopicPartition, OffsetAndMetadata> partitionOffsets = connection
                    .listConsumerGroupOffsets(groupId)
                    .partitionsToOffsetAndMetadata()
                    .get();

            partitionOffsets.keySet().stream().forEach(key -> {
                Long endOffset = endOffsets.get(key);
                if (endOffset != null) {
                    OffsetAndMetadata partitionOffset = partitionOffsets.get(key);

                    PartitionOffsets obj = new PartitionOffsets(endOffset, partitionOffset.offset(), key.partition(), key.topic());
                    result.put(key, obj);
                }
            });
        } catch (Exception e) {
            log.error("Cannot get topic description. ", e);
        }

        return result;
    }

    private Map<TopicPartition, Long> getEndOffset(EnvConfig env, String topic) {
        Map<TopicPartition, Long> endOffsets = new ConcurrentHashMap<>();

        KafkaConsumer<String, String> consumer = kafkaConnectorManager.getConsumer(env);
        try {
            List<PartitionInfo> partitionInfos = consumer.partitionsFor(topic);
            List<TopicPartition> topicPartitions = partitionInfos.stream().map(pi -> new TopicPartition(topic, pi.partition())).collect(Collectors.toList());
            consumer.assign(topicPartitions);
            consumer.seekToEnd(topicPartitions);
            topicPartitions.forEach(topicPartition -> endOffsets.put(topicPartition, consumer.position(topicPartition)));

            return endOffsets;
        } finally {
            consumer.close();
        }
    }

    public List<Lag> getConsumerLags(EnvConfig env, String groupId) {
        ConsumerLagConnectionPoolWrapper connection = kafkaConnectorManager.getConsumerLagClient(env);

        List<Lag> lags = connection.getConsumerLag(groupId);
        return lags;
    }

    public void deleteConsumerGroups(EnvConfig env, Collection<String> groupIds) {
        AdminClientConnectionPoolWrapper connection = kafkaConnectorManager.getAdminClient(env);
        connection.deleteConsumerGroups(groupIds);
    }

    public List<PartitionData> getTopicDetails(EnvConfig env, String topic) {
        AdminClientConnectionPoolWrapper connection = kafkaConnectorManager.getAdminClient(env);
        return connection.getTopicDetails(topic);
    }

    public List<TopicConfigValue> describeTopic(EnvConfig env, String topic) {
        AdminClientConnectionPoolWrapper connection = kafkaConnectorManager.getAdminClient(env);
        return connection.describeTopic(topic);
    }
}
