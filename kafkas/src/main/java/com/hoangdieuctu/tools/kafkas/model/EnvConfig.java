package com.hoangdieuctu.tools.kafkas.model;

import java.util.Objects;

public class EnvConfig {
    private String name;
    private String bootstrapServer;

    public EnvConfig() {
    }

    public EnvConfig(String name, String bootstrapServer) {
        this.name = name;
        this.bootstrapServer = bootstrapServer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBootstrapServer() {
        return bootstrapServer;
    }

    public void setBootstrapServer(String bootstrapServer) {
        this.bootstrapServer = bootstrapServer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnvConfig envConfig = (EnvConfig) o;
        return name.equals(envConfig.name) &&
                bootstrapServer.equals(envConfig.bootstrapServer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, bootstrapServer);
    }
}
