package com.hoangdieuctu.tools.kafkas.model;

import java.util.List;

public class TopicInfo {
    private String name;
    private List<PartitionData> partitions;

    public TopicInfo(String name, List<PartitionData> partitions) {
        this.name = name;
        this.partitions = partitions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PartitionData> getPartitions() {
        return partitions;
    }

    public void setPartitions(List<PartitionData> partitions) {
        this.partitions = partitions;
    }
}
