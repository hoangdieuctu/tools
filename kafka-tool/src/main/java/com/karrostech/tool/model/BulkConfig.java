package com.karrostech.tool.model;

import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

public class BulkConfig {

    private String elements;
    private String threads;

    public String getElements() {
        return elements;
    }

    public void setElements(String elements) {
        this.elements = elements;
    }

    public List<String> getElementList() {
        return Arrays.asList(elements.split(","));
    }

    public String getThreads() {
        return threads;
    }

    public void setThreads(String threads) {
        this.threads = threads;
    }

    public List<String> getThreadList() {
        return Arrays.asList(threads.split(","));
    }

    public boolean validate() {
        if (StringUtils.isEmpty(elements) || StringUtils.isEmpty(threads)) {
            return false;
        }

        return validate(elements) && validate(threads);
    }

    private boolean validate(String item) {
        String[] elementValues = item.split(",");
        for (String value : elementValues) {
            try {
                int v = Integer.parseInt(value);
                if (v < 0) {
                    return false;
                }
            } catch (Exception ex) {
                return false;
            }
        }
        return true;
    }
}
