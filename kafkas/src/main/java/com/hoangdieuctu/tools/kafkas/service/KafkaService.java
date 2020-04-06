package com.hoangdieuctu.tools.kafkas.service;

import com.hoangdieuctu.tools.kafkas.constant.Constants;
import com.hoangdieuctu.tools.kafkas.model.*;
import com.hoangdieuctu.tools.kafkas.repository.KafkaRepository;
import com.hoangdieuctu.tools.kafkas.util.PatternMatcher;
import com.omarsmak.kafka.consumer.lag.monitoring.client.data.Lag;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class KafkaService {

    private static final String KAFKA_TOOL_CONSUMER_PREFIX = Constants.CONSUMER_GROUP_NAME + "-";
    private static final String KAFKA_EMPTY_STATE = "EMPTY";

    @Autowired
    private KafkaRepository kafkaRepository;

    @Autowired
    private AdminService adminService;

    @Autowired
    private MessagePreSendingProcessor preSendingProcessor;

    public List<TopicInfo> getAllTopics(EnvConfig env) {
        Map<String, List<PartitionInfo>> topics = kafkaRepository.getKafkaTopics(env);
        List<String> exclusions = adminService.getTopicsExclusion();

        List<TopicInfo> results = new ArrayList<>();
        topics.forEach((topic, partitions) -> {
            if (excludeTopic(exclusions, topic)) {
                List<PartitionData> objs = partitions.stream().map(partition -> {
                    PartitionData obj = new PartitionData();
                    obj.copy(partition);
                    return obj;
                }).collect(Collectors.toList());

                results.add(new TopicInfo(topic, objs));
            }
        });

        return results;
    }

    public Set<String> getTopics(EnvConfig env) {
        Map<String, List<PartitionInfo>> kafkaTopics = kafkaRepository.getKafkaTopics(env);
        return kafkaTopics.keySet();
    }

    public List<String> getFilteredTopics(EnvConfig env) {
        Set<String> topics = getTopics(env);
        List<String> exclusions = adminService.getTopicsExclusion();

        return topics.stream()
                .filter(t -> excludeTopic(exclusions, t))
                .sorted()
                .collect(Collectors.toList());
    }

    public List<String> getFilteredFavTopics(EnvConfig env) {
        List<String> topics = getFilteredTopics(env);

        List<String> favTopics = adminService.getFavTopicsSetting();
        return topics.stream()
                .filter(topic -> favTopics.contains(topic))
                .sorted()
                .collect(Collectors.toList());
    }

    public List<String> getConsumerGroups(EnvConfig env) {
        return kafkaRepository.getConsumerGroups(env);
    }

    public ConsumerGroupDetail describeConsumerGroup(EnvConfig env, String groupId) {
        return kafkaRepository.describeConsumerGroup(env, groupId);
    }

    public Map<String, ConsumerGroupDetail> describeConsumerGroups(EnvConfig env, List<String> groupIds) {
        return kafkaRepository.describeConsumerGroups(env, groupIds);
    }

    public Map<TopicPartition, PartitionOffsets> getConsumerGroupOffsets(EnvConfig env, String topic, String groupId) {
        return kafkaRepository.getConsumerGroupOffsets(env, topic, groupId);
    }

    public List<Lag> getConsumerLags(EnvConfig env, String groupId) {
        return kafkaRepository.getConsumerLags(env, groupId);
    }

    public KafkaRecordData send(EnvConfig env, String topic, String message) {
        return this.send(env, topic, null, message);
    }

    public KafkaRecordData send(EnvConfig env, String topic, Integer partition, String message) {
        return this.send(env, topic, partition, null, message);
    }

    public KafkaRecordData send(EnvConfig env, String topic, Integer partition, String key, String message) {
        String processedMsg = preSendingProcessor.process(message);
        return kafkaRepository.send(env, topic, partition, key, processedMsg);
    }

    private boolean excludeTopic(List<String> exclusions, String topic) {
        for (String exclusion : exclusions) {
            if (PatternMatcher.match(exclusion, topic)) {
                return false;
            }
        }
        return true;
    }

    public void evictKafkaToolGroups(EnvConfig env) {
        List<String> allGroups = getConsumerGroups(env);
        List<String> kafkaToolGroups = allGroups.stream().filter(g -> g.startsWith(KAFKA_TOOL_CONSUMER_PREFIX)).collect(Collectors.toList());
        log.info("[{}] Validate {} groups", env, kafkaToolGroups.size());

        Map<String, ConsumerGroupDetail> statuses = describeConsumerGroups(env, kafkaToolGroups);

        List<String> groupsWillBeDeleted = new ArrayList<>();

        Set<String> groupIds = statuses.keySet();
        groupIds.forEach(group -> {
            ConsumerGroupDetail detail = statuses.get(group);
            String state = detail.getState();
            if (KAFKA_EMPTY_STATE.equals(state)) {
                groupsWillBeDeleted.add(group);
            }
        });

        if (groupsWillBeDeleted.isEmpty()) {
            log.info("[{}] No group will be deleted", env);
            return;
        }

        log.info("[{}] Delete {} groups", env, groupsWillBeDeleted.size());
        kafkaRepository.deleteConsumerGroups(env, groupsWillBeDeleted);
    }

    public List<PartitionData> getTopicDetails(EnvConfig env, String topic) {
        return kafkaRepository.getTopicDetails(env, topic);
    }

    public List<TopicConfigValue> describeTopic(EnvConfig env, String topic) {
        return kafkaRepository.describeTopic(env, topic);
    }
}
