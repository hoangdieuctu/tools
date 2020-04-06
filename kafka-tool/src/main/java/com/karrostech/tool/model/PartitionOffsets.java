package com.karrostech.tool.model;

public class PartitionOffsets {
    private long endOffset;
    private long currentOffset;
    private int partition;
    private String topic;

    public PartitionOffsets(long endOffset, long currentOffset, int partition, String topic) {
        this.endOffset = endOffset;
        this.currentOffset = currentOffset;
        this.partition = partition;
        this.topic = topic;
    }

    public long getEndOffset() {
        return endOffset;
    }

    public void setEndOffset(long endOffset) {
        this.endOffset = endOffset;
    }

    public long getCurrentOffset() {
        return currentOffset;
    }

    public void setCurrentOffset(long currentOffset) {
        this.currentOffset = currentOffset;
    }

    public int getPartition() {
        return partition;
    }

    public void setPartition(int partition) {
        this.partition = partition;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
