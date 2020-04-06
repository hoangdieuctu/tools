package com.hoangdieuctu.tools.kafkas.model;

import java.util.ArrayList;
import java.util.List;

public class FavoriteTopicSetting extends Setting {

    private List<String> topics = new ArrayList<>();

    public FavoriteTopicSetting() {
        super(SettingType.FAVORITE_TOPIC);
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }
}
