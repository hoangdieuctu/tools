package com.hoangdieuctu.tools.kafkas.model;

import java.util.Objects;

public class ConsumerKey {

    private Environment env;
    private String topic;
    private long date = System.currentTimeMillis(); // don't add to hashcode and equals

    private String customId;

    private Long partitionOffset;
    private Integer partitionNumber;

    private String filter; // don't add to hashcode and equals

    public ConsumerKey(String env, String topic) {
        this.env = Environment.valueOf(env);
        this.topic = topic;
    }

    public ConsumerKey(String env, String topic, String customId, Long partitionOffset, Integer partitionNumber) {
        this.env = Environment.valueOf(env);
        this.topic = topic;
        this.customId = customId;
        this.partitionOffset = partitionOffset;
        this.partitionNumber = partitionNumber;
    }

    public Environment getEnv() {
        return env;
    }

    public void setEnv(Environment env) {
        this.env = env;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    public ConsumerOffset getConsumerOffset() {
        return customId == null ? ConsumerOffset.latest : ConsumerOffset.oldest;
    }

    public Long getPartitionOffset() {
        return partitionOffset;
    }

    public void setPartitionOffset(Long partitionOffset) {
        this.partitionOffset = partitionOffset;
    }

    public Integer getPartitionNumber() {
        return partitionNumber;
    }

    public void setPartitionNumber(Integer partitionNumber) {
        this.partitionNumber = partitionNumber;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConsumerKey that = (ConsumerKey) o;
        return env == that.env &&
                Objects.equals(topic, that.topic) &&
                Objects.equals(customId, that.customId) &&
                Objects.equals(partitionOffset, that.partitionOffset) &&
                Objects.equals(partitionNumber, that.partitionNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(env, topic, customId, partitionOffset, partitionNumber);
    }

    @Override
    public String toString() {
        return "ConsumerKey{" +
                "env=" + env +
                ", topic='" + topic + '\'' +
                ", customId='" + customId + '\'' +
                ", partitionOffset=" + partitionOffset +
                ", partitionNumber=" + partitionNumber +
                '}';
    }
}
