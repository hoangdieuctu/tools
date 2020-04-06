package com.karrostech.tool.model;

public enum Environment {
    DEV("10.20.20.121:9092"), NON_DEV("10.40.26.222:9092");

    private String bootstrapServers;

    Environment(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public static Environment forName(String name) {
        for (Environment value : Environment.values()) {
            if (value.name().equals(name)) {
                return value;
            }
        }

        return null;
    }
}
