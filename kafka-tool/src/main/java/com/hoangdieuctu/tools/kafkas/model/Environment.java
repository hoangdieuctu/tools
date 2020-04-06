package com.hoangdieuctu.tools.kafkas.model;

public enum Environment {
    LOCAL("127.0.0.1:9092");

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
