package com.hoangdieuctu.tools.kafkas.service;

import com.hoangdieuctu.tools.kafkas.model.ConsumerGroupDetail;
import com.hoangdieuctu.tools.kafkas.model.Environment;
import com.hoangdieuctu.tools.kafkas.model.PartitionOffsets;
import com.omarsmak.kafka.consumer.lag.monitoring.client.data.Lag;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class MonitorService {

    @Autowired
    private KafkaService kafkaService;

    public List<String> getConsumerGroups(Environment env) {
        List<String> groups = kafkaService.getConsumerGroups(env);
        groups.removeIf(i -> i.isEmpty());

        return groups;
    }

    public ConsumerGroupDetail describeConsumerGroup(Environment env, String groupId) {
        return kafkaService.describeConsumerGroup(env, groupId);
    }

    public Map<String, ConsumerGroupDetail> describeConsumerGroups(Environment env, List<String> groupIds) {
        return kafkaService.describeConsumerGroups(env, groupIds);
    }

    public Map<TopicPartition, PartitionOffsets> getConsumerGroupOffsets(Environment env, String topic, String groupId) {
        return kafkaService.getConsumerGroupOffsets(env, topic, groupId);
    }

    public List<Lag> getConsumerLags(Environment env, String groupId) {
        return kafkaService.getConsumerLags(env, groupId);
    }
}
