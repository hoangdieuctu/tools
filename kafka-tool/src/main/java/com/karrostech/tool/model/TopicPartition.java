package com.karrostech.tool.model;

import java.util.List;
import java.util.Objects;

public class TopicPartition {
    private String topic;
    private List<Integer> partitions;

    public TopicPartition(String topic, List<Integer> partitions) {
        this.topic = topic;
        this.partitions = partitions;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public List<Integer> getPartitions() {
        return partitions;
    }

    public void setPartitions(List<Integer> partitions) {
        this.partitions = partitions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TopicPartition that = (TopicPartition) o;
        return Objects.equals(topic, that.topic) && Objects.equals(partitions, that.partitions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topic, partitions);
    }
}
