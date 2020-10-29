package com.marcnuri.yakc.quarkus.extension.runtime;

import java.util.Optional;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(name = "yakc", phase = ConfigPhase.BUILD_AND_RUN_TIME_FIXED)
public class KubernetesClientBuildConfig {

    /**
     * Disables TLS certificate verification when communicating with the server
     */
    @ConfigItem
    public Optional<Boolean> insecureSkipTlsVerify;

}
