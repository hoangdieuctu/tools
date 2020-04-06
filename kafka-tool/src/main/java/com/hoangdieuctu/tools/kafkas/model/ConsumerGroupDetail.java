package com.hoangdieuctu.tools.kafkas.model;


import org.apache.kafka.clients.admin.ConsumerGroupDescription;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConsumerGroupDetail {
    private String state;
    private String groupId;
    private String partitionAssignor;
    private List<MemberDescription> members = new ArrayList<>();

    public ConsumerGroupDetail() {
    }

    public ConsumerGroupDetail(ConsumerGroupDescription description) {
        if (description != null) {
            this.state = description.state().name();
            this.groupId = description.groupId();
            this.partitionAssignor = description.partitionAssignor();

            Collection<org.apache.kafka.clients.admin.MemberDescription> members = description.members();
            if (!CollectionUtils.isEmpty(members)) {
                members.stream().forEach(m -> this.members.add(new MemberDescription(m)));
            }
        }
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPartitionAssignor() {
        return partitionAssignor;
    }

    public void setPartitionAssignor(String partitionAssignor) {
        this.partitionAssignor = partitionAssignor;
    }

    public List<MemberDescription> getMembers() {
        return members;
    }

    public void setMembers(List<MemberDescription> members) {
        this.members = members;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
