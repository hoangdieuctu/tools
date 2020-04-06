package com.karrostech.tool.model;

import java.util.List;

public class BulkConfigSetting extends Setting {

    private List<String> elements;
    private List<String> threads;

    public BulkConfigSetting() {
        super(SettingType.BULK_CONFIG);
    }

    public List<String> getElements() {
        return elements;
    }

    public void setElements(List<String> elements) {
        this.elements = elements;
    }

    public List<String> getThreads() {
        return threads;
    }

    public void setThreads(List<String> threads) {
        this.threads = threads;
    }

    public String getElementString() {
        return String.join(",", elements);
    }

    public String getThreadString() {
        return String.join(",", threads);
    }
}
