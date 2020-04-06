package com.karrostech.tool.model;

import com.karrostech.tool.constant.Constants;

public class JsonFile {
    private String name;
    private String fullName;
    private String folder = Constants.ROOT_STORAGE;

    private ProduceContent content;

    private Long createdTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProduceContent getContent() {
        return content;
    }

    public void setContent(ProduceContent content) {
        this.content = content;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }
}
