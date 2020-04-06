package com.karrostech.tool.model;

public class EnvConfig {
    private String name;
    private String bootstrapServer;

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
}
