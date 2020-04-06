package com.hoangdieuctu.tools.kafkas.model;

import org.apache.kafka.clients.admin.ConfigEntry;

public class TopicConfigValue {
    private String name;
    private String value;
    private String source;
    private boolean isSensitive;
    private boolean isReadOnly;

    public TopicConfigValue(ConfigEntry entry) {
        this.name = entry.name();
        this.value = entry.value();
        this.source = entry.source().name();
        this.isSensitive = entry.isSensitive();
        this.isReadOnly = entry.isReadOnly();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isSensitive() {
        return isSensitive;
    }

    public void setSensitive(boolean sensitive) {
        isSensitive = sensitive;
    }

    public boolean isReadOnly() {
        return isReadOnly;
    }

    public void setReadOnly(boolean readOnly) {
        isReadOnly = readOnly;
    }
}
