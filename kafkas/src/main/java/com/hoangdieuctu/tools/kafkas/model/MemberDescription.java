package com.hoangdieuctu.tools.kafkas.model;


public class MemberDescription {
    private String host;
    private MemberAssignment assignment;

    public MemberDescription(org.apache.kafka.clients.admin.MemberDescription description) {
        if (description != null) {
            this.host = description.host();
            this.assignment = new MemberAssignment(description.assignment());
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public MemberAssignment getAssignment() {
        return assignment;
    }

    public void setAssignment(MemberAssignment assignment) {
        this.assignment = assignment;
    }
}
