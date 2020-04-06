package com.hoangdieuctu.tools.kafkas.model;

import org.apache.kafka.common.Node;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartitionInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PartitionData {
    private int partition;
    private PartitionNode leader;
    private List<PartitionNode> replicas;
    private List<PartitionNode> isr;

    public int getPartition() {
        return partition;
    }

    public void setPartition(int partition) {
        this.partition = partition;
    }

    public PartitionNode getLeader() {
        return leader;
    }

    public void setLeader(PartitionNode leader) {
        this.leader = leader;
    }

    public List<PartitionNode> getReplicas() {
        return replicas;
    }

    public void setReplicas(List<PartitionNode> replicas) {
        this.replicas = replicas;
    }

    public List<PartitionNode> getIsr() {
        return isr;
    }

    public void setIsr(List<PartitionNode> isr) {
        this.isr = isr;
    }

    public void copy(PartitionInfo p) {
        this.setPartition(p.partition());
        this.copyLeader(p.leader());

        Node[] replicas = p.replicas();
        this.setReplicas(convert(Arrays.asList(replicas)));

        Node[] isr = p.inSyncReplicas();
        this.setIsr(convert(Arrays.asList(isr)));
    }

    public void copy(TopicPartitionInfo p) {
        this.setPartition(p.partition());
        this.copyLeader(p.leader());

        List<Node> replicas = p.replicas();
        this.setReplicas(convert(replicas));

        List<Node> isr = p.isr();
        this.setIsr(convert(isr));
    }

    private void copyLeader(Node leader) {
        PartitionNode leaderNode = new PartitionNode();
        copy(leader, leaderNode);
        this.setLeader(leaderNode);
    }

    private void copy(Node src, PartitionNode dest) {
        dest.setId(src.id());
        dest.setHost(src.host());
        dest.setPort(src.port());
    }

    private List<PartitionNode> convert(List<Node> sources) {
        List<PartitionNode> results = new ArrayList<>();
        sources.forEach(src -> {
            PartitionNode dest = new PartitionNode();
            copy(src, dest);
            results.add(dest);
        });

        return results;
    }
}
