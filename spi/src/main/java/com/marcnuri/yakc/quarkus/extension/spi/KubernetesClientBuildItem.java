package com.marcnuri.yakc.quarkus.extension.spi;

import com.marcnuri.yakc.KubernetesClient;

import io.quarkus.builder.item.SimpleBuildItem;

public final class KubernetesClientBuildItem extends SimpleBuildItem {

    private final KubernetesClient client;

    public KubernetesClientBuildItem(KubernetesClient client) {
        this.client = client;
    }

    public KubernetesClient getClient() {
        return this.client;
    }

}
