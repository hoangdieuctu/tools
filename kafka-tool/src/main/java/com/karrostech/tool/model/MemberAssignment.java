package com.karrostech.tool.model;

import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MemberAssignment {
    private Set<TopicPartition> partitions = new HashSet<>();

    public MemberAssignment(org.apache.kafka.clients.admin.MemberAssignment assignment) {
        if (assignment != null) {
            Set<org.apache.kafka.common.TopicPartition> partitions = assignment.topicPartitions();
            if (!CollectionUtils.isEmpty(partitions)) {
                Map<String, List<org.apache.kafka.common.TopicPartition>> groups = partitions.stream()
                        .collect(Collectors.groupingBy(org.apache.kafka.common.TopicPartition::topic));

                Set<String> keys = groups.keySet();
                keys.forEach(topic -> {
                    List<Integer> topicPartitions = groups.get(topic)
                            .stream().map(org.apache.kafka.common.TopicPartition::partition)
                            .collect(Collectors.toList());

                    this.partitions.add(new TopicPartition(topic, topicPartitions));
                });
            }
        }
    }

    public Set<TopicPartition> getPartitions() {
        return partitions;
    }

    public void setPartitions(Set<TopicPartition> partitions) {
        this.partitions = partitions;
    }
}
