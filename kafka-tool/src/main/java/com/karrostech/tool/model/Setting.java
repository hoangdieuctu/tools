package com.karrostech.tool.model;

public abstract class Setting {
    private SettingType type;

    public Setting(SettingType type) {
        this.type = type;
    }

    public SettingType getType() {
        return type;
    }

    public void setType(SettingType type) {
        this.type = type;
    }
}
