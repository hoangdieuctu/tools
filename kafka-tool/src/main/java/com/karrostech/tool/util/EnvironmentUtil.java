package com.karrostech.tool.util;

import com.karrostech.tool.model.EnvConfig;
import com.karrostech.tool.model.Environment;

import java.util.ArrayList;
import java.util.List;

public class EnvironmentUtil {

    public static final List<EnvConfig> getEnvConfigs() {
        Environment[] environments = Environment.values();
        List<EnvConfig> envConfigs = new ArrayList<>();
        for (Environment environment : environments) {
            envConfigs.add(new EnvConfig(environment.name(), environment.getBootstrapServers()));
        }

        return envConfigs;
    }

}
