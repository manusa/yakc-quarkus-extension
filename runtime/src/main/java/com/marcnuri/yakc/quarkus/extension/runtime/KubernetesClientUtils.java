package com.marcnuri.yakc.quarkus.extension.runtime;

import com.marcnuri.yakc.KubernetesClient;
import com.marcnuri.yakc.config.Configuration;

public class KubernetesClientUtils {

    private static final String PREFIX = "quarkus.yakc.";

    private KubernetesClientUtils() {
    }

    public static Configuration createConfig(KubernetesClientBuildConfig buildConfig) {
        boolean hasConfig = false;
        final Configuration.ConfigurationBuilder cb = Configuration.builder();
        if (buildConfig.insecureSkipTlsVerify.isPresent()) {
            hasConfig = true;
            cb.insecureSkipTlsVerify(buildConfig.insecureSkipTlsVerify.get());
        }
        return hasConfig ? cb.build() : null;
    }

    public static KubernetesClient createClient(KubernetesClientBuildConfig buildConfig) {
        return new KubernetesClient(createConfig(buildConfig));
    }

    public static KubernetesClient createClient() {
        return new KubernetesClient();
    }
}
