package com.hoangdieuctu.tools.dlogs.config;

import com.hoangdieuctu.tools.dlogs.constant.Constants;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileReader;
import java.io.IOException;

@Configuration
public class KubernetesConfig {

    @Value("${cluster.mode}")
    private boolean isInClusterMode;

    @Bean
    public ApiClient apiClient() throws IOException {
        return isInClusterMode ? ClientBuilder.cluster().build() : ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(Constants.CONFIG_FILE))).build();
    }

    @Bean
    public CoreV1Api coreV1Api(ApiClient client) {
        return new CoreV1Api(client);
    }

}