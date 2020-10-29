package com.marcnuri.yakc.quarkus.extension.runtime;

import javax.annotation.PreDestroy;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import com.marcnuri.yakc.KubernetesClient;
import com.marcnuri.yakc.config.Configuration;

@Singleton
public class KubernetesClientProducer {

    private KubernetesClient client;

    @Singleton
    @Produces
    public Configuration config(KubernetesClientBuildConfig buildConfig) {
        return KubernetesClientUtils.createConfig(buildConfig);
    }

    @Singleton
    @Produces
    public KubernetesClient kubernetesClient(Configuration configuration) {
        client = new KubernetesClient(configuration);
        return client;
    }

    @PreDestroy
    public void destroy() {
        if (client != null) {
            client.close();
        }
    }
}
