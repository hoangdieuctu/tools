package com.karrostech.tool.model;

import java.util.UUID;

public class ProduceHistory {
    private String id = UUID.randomUUID().toString();
    private long timestamp = System.currentTimeMillis();
    private ProduceContent content;
    private ProduceResponse response;
    private String host = "127.0.0.1";

    public ProduceContent getContent() {
        return content;
    }

    public void setContent(ProduceContent content) {
        this.content = content;
    }

    public ProduceResponse getResponse() {
        return response;
    }

    public void setResponse(ProduceResponse response) {
        this.response = response;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
