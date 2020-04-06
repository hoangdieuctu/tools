package com.karrostech.tool.model;

import java.util.ArrayList;
import java.util.List;

public class ProduceFolderSetting extends Setting {

    private List<String> folders = new ArrayList<>();

    public ProduceFolderSetting() {
        super(SettingType.PRODUCE_FOLDER);
    }

    public List<String> getFolders() {
        return folders;
    }

    public void setFolders(List<String> folders) {
        this.folders = folders;
    }
}
