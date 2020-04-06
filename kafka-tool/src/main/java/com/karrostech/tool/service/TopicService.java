package com.karrostech.tool.service;

import com.karrostech.tool.model.Environment;
import com.karrostech.tool.model.PartitionData;
import com.karrostech.tool.model.TopicConfigValue;
import com.karrostech.tool.model.TopicInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class TopicService {

    @Autowired
    private KafkaService kafkaService;

    public List<TopicInfo> getAll(Environment env) {
        return kafkaService.getAllTopics(env);
    }

    public List<TopicConfigValue> describeTopic(Environment env, String topic) {
        return kafkaService.describeTopic(env, topic);
    }

    public List<PartitionData> getTopicDetails(Environment env, String topic) {
        return kafkaService.getTopicDetails(env, topic);
    }
}
