package com.hoangdieuctu.tools.kafkas.util;

import com.hoangdieuctu.tools.kafkas.model.EnvConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EnvironmentHolder {

    @Value("${envs}")
    private String envs;

    private List<EnvConfig> configs = new ArrayList<>();
    private Map<String, EnvConfig> configsMapper = new HashMap<>();

    @PostConstruct
    public void init() {
        String[] items = envs.split(",");
        for (String item : items) {
            String[] parts = item.split(":");
            EnvConfig env = new EnvConfig(parts[0], parts[1] + ":" + parts[2]);
            configs.add(env);
            configsMapper.put(parts[0], env);
        }
    }

    public List<EnvConfig> getConfigs() {
        return configs;
    }

    public EnvConfig getEnv(String name) {
        return configsMapper.get(name);
    }
}
