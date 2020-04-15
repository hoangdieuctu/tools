package com.hoangdieuctu.tools.klogs.config;

import com.hoangdieuctu.tools.klogs.constant.Constants;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileReader;
import java.io.IOException;

@Configuration
public class KubernetesConfig {

    private Logger logger = LoggerFactory.getLogger(KubernetesConfig.class);

    @Value("${current.context}")
    private String context;

    @Bean
    public ApiClient apiClient() throws IOException {
        logger.info("Init kubernetes context: {}", context);

        String configPath = System.getProperty("user.home") + Constants.SPLASH + Constants.CONFIG_FILE;
        KubeConfig kubeConfig = KubeConfig.loadKubeConfig(new FileReader(configPath));
        kubeConfig.setContext(context);

        return ClientBuilder.kubeconfig(kubeConfig).build();
    }

    @Bean
    public CoreV1Api coreV1Api(ApiClient client) {
        return new CoreV1Api(client);
    }

}