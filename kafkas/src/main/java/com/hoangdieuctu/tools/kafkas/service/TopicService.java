package com.hoangdieuctu.tools.kafkas.service;

import com.hoangdieuctu.tools.kafkas.model.PartitionData;
import com.hoangdieuctu.tools.kafkas.model.TopicConfigValue;
import com.hoangdieuctu.tools.kafkas.model.TopicInfo;
import com.hoangdieuctu.tools.kafkas.util.EnvironmentHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class TopicService {

    @Autowired
    private KafkaService kafkaService;

    @Autowired
    private EnvironmentHolder envsHolder;

    public List<TopicInfo> getAll(String env) {
        return kafkaService.getAllTopics(envsHolder.getEnv(env));
    }

    public List<TopicConfigValue> describeTopic(String env, String topic) {
        return kafkaService.describeTopic(envsHolder.getEnv(env), topic);
    }

    public List<PartitionData> getTopicDetails(String env, String topic) {
        return kafkaService.getTopicDetails(envsHolder.getEnv(env), topic);
    }
}
