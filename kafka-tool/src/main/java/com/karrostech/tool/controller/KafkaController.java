package com.karrostech.tool.controller;

import com.karrostech.tool.util.EnvironmentUtil;
import com.karrostech.tool.model.EnvConfig;
import com.karrostech.tool.model.Environment;
import com.karrostech.tool.service.KafkaService;
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
