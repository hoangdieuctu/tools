package com.karrostech.tool.util;

public class FileNameUtil {
    private static final String ROOT_FOLDER = "kafka-tool";
    private static final String PRODUCE_FOLDER = "produce";
    private static final String SETTING_FOLDER = "setting";
    private static final String INDEX_FOLDER = "index";
    private static final String BULK_PRODUCE_FOLDER = "bulk-produce";

    public static final String getRootFolder() {
        return ROOT_FOLDER;
    }

    public static final String getProduceFolder() {
        StringBuilder builder = new StringBuilder();
        builder.append(ROOT_FOLDER).append("/").append(PRODUCE_FOLDER).append("/");
        return builder.toString();
    }

    public static final String getProduceFolder(String folder) {
        StringBuilder builder = new StringBuilder();
        builder.append(ROOT_FOLDER).append("/").append(PRODUCE_FOLDER).append("/");
        builder.append(folder).append("/");
        return builder.toString();
    }

    public static final String getBulkProduceFolder() {
        StringBuilder builder = new StringBuilder();
        builder.append(ROOT_FOLDER).append("/").append(BULK_PRODUCE_FOLDER).append("/");
        return builder.toString();
    }

    public static final String getSettingFolder() {
        StringBuilder builder = new StringBuilder();
        builder.append(ROOT_FOLDER).append("/").append(SETTING_FOLDER).append("/");
        return builder.toString();
    }

    public static final String getIndexFolder() {
        StringBuilder builder = new StringBuilder();
        builder.append(ROOT_FOLDER).append("/").append(INDEX_FOLDER).append("/");
        return builder.toString();
    }
}
