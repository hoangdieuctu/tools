package com.hoangdieuctu.tools.kafkas.model;

public class KafkaRecordData {
    private long timestamp;
    private int partition;
    private long offset;

    public KafkaRecordData() {
    }

    public KafkaRecordData(long timestamp, int partition, long offset) {
        this.timestamp = timestamp;
        this.partition = partition;
        this.offset = offset;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getPartition() {
        return partition;
    }

    public void setPartition(int partition) {
        this.partition = partition;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }
}
