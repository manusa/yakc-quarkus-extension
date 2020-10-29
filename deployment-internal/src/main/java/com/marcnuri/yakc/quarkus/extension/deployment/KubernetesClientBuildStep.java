package com.marcnuri.yakc.quarkus.extension.deployment;

import static com.marcnuri.yakc.quarkus.extension.runtime.KubernetesClientUtils.*;

import com.marcnuri.yakc.quarkus.extension.runtime.KubernetesClientBuildConfig;
import com.marcnuri.yakc.quarkus.extension.spi.KubernetesClientBuildItem;

import io.quarkus.deployment.annotations.BuildStep;

public class KubernetesClientBuildStep {

    private KubernetesClientBuildConfig buildConfig;

    @BuildStep
    public KubernetesClientBuildItem process() {
        return new KubernetesClientBuildItem(createClient(buildConfig));
    }
}
