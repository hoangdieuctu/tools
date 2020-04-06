package com.hoangdieuctu.tools.kafkas.util;

import com.hoangdieuctu.tools.kafkas.model.Environment;
import com.hoangdieuctu.tools.kafkas.model.EnvConfig;

import java.util.ArrayList;
import java.util.List;

public class EnvironmentUtil {

    public static List<EnvConfig> getEnvConfigs() {
        Environment[] environments = Environment.values();
        List<EnvConfig> envConfigs = new ArrayList<>();
        for (Environment environment : environments) {
            envConfigs.add(new EnvConfig(environment.name(), environment.getBootstrapServers()));
        }

        return envConfigs;
    }

}
