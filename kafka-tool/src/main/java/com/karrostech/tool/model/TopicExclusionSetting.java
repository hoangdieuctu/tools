package com.karrostech.tool.model;

import java.util.ArrayList;
import java.util.List;

public class TopicExclusionSetting extends Setting {

    private List<String> topics = new ArrayList<>();

    public TopicExclusionSetting() {
        super(SettingType.TOPIC_EXCLUSION);
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }
}
