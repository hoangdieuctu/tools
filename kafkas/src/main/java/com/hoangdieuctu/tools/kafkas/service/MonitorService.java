package com.hoangdieuctu.tools.kafkas.service;

import com.hoangdieuctu.tools.kafkas.model.ConsumerGroupDetail;
import com.hoangdieuctu.tools.kafkas.model.EnvConfig;
import com.hoangdieuctu.tools.kafkas.model.PartitionOffsets;
import com.hoangdieuctu.tools.kafkas.util.EnvironmentHolder;
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

    @Autowired
    private EnvironmentHolder envsHolder;

    public List<String> getConsumerGroups(String env) {
        List<String> groups = kafkaService.getConsumerGroups(envsHolder.getEnv(env));
        groups.removeIf(i -> i.isEmpty());

        return groups;
    }

    public ConsumerGroupDetail describeConsumerGroup(String env, String groupId) {
        return kafkaService.describeConsumerGroup(envsHolder.getEnv(env), groupId);
    }

    public Map<String, ConsumerGroupDetail> describeConsumerGroups(String env, List<String> groupIds) {
        return kafkaService.describeConsumerGroups(envsHolder.getEnv(env), groupIds);
    }

    public Map<TopicPartition, PartitionOffsets> getConsumerGroupOffsets(EnvConfig env, String topic, String groupId) {
        return kafkaService.getConsumerGroupOffsets(env, topic, groupId);
    }

    public List<Lag> getConsumerLags(String env, String groupId) {
        return kafkaService.getConsumerLags(envsHolder.getEnv(env), groupId);
    }
}
