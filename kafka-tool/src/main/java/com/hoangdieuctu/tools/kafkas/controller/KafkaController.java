package com.hoangdieuctu.tools.kafkas.controller;

import com.hoangdieuctu.tools.kafkas.model.EnvConfig;
import com.hoangdieuctu.tools.kafkas.model.Environment;
import com.hoangdieuctu.tools.kafkas.util.EnvironmentUtil;
import com.hoangdieuctu.tools.kafkas.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import java.util.List;

public class KafkaController {

    @Autowired
    private KafkaService kafkaService;

    protected void setEnvsAndTopics(Model model) {
        List<EnvConfig> envConfigs = EnvironmentUtil.getEnvConfigs();
        model.addAttribute("environments", envConfigs);
    }

    protected List<String> getTopics(Environment env) {
        return kafkaService.getFilteredTopics(env);
    }

    protected List<String> getFavTopics(Environment env) {
        return kafkaService.getFilteredFavTopics(env);
    }

    protected List<String> getTopics(Environment env, boolean isFavOnly) {
        return isFavOnly ? getFavTopics(env) : getTopics(env);
    }
}
