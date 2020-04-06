package com.hoangdieuctu.tools.kafkas.controller;

import com.hoangdieuctu.tools.kafkas.model.EnvConfig;
import com.hoangdieuctu.tools.kafkas.service.KafkaService;
import com.hoangdieuctu.tools.kafkas.util.EnvironmentHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import java.util.List;

public class KafkaController {

    @Autowired
    private KafkaService kafkaService;

    @Autowired
    private EnvironmentHolder envsHolder;

    protected void setEnvsAndTopics(Model model) {
        List<EnvConfig> envConfigs = envsHolder.getConfigs();
        model.addAttribute("environments", envConfigs);
    }

    protected List<String> getTopics(EnvConfig env) {
        return kafkaService.getFilteredTopics(env);
    }

    protected List<String> getFavTopics(EnvConfig env) {
        return kafkaService.getFilteredFavTopics(env);
    }

    protected List<String> getTopics(String env, boolean isFavOnly) {
        EnvConfig config = envsHolder.getEnv(env);
        return isFavOnly ? getFavTopics(config) : getTopics(config);
    }
}
