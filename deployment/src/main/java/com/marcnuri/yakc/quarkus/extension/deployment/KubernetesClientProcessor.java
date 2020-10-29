package com.marcnuri.yakc.quarkus.extension.deployment;

import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.logging.Logger;

import com.marcnuri.yakc.quarkus.extension.runtime.KubernetesClientProducer;

import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.Feature;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.ExtensionSslNativeSupportBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.IndexDependencyBuildItem;
import io.quarkus.deployment.builditem.nativeimage.NativeImageProxyDefinitionBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;
import io.quarkus.deployment.pkg.steps.NativeBuild;
import io.quarkus.kubernetes.spi.KubernetesRoleBindingBuildItem;

public class KubernetesClientProcessor {

  private static final String YACK_GROUP_ID = "com.marcnuri.yakc";
  private static final String KUBERNETES_MODEL = "com.marcnuri.yakc.model.Model";

  private static final Logger log = Logger.getLogger(KubernetesClientProcessor.class.getName());

  @Inject
  BuildProducer<FeatureBuildItem> featureProducer;

  @Inject
  BuildProducer<ReflectiveClassBuildItem> reflectiveClasses;

  @Inject
  BuildProducer<KubernetesRoleBindingBuildItem> roleBindingProducer;

  @BuildStep(onlyIf = NativeBuild.class)
  void addDependencies(BuildProducer<IndexDependencyBuildItem> indexDependency) {
    indexDependency.produce(new IndexDependencyBuildItem(YACK_GROUP_ID, "kubernetes-client"));
    indexDependency.produce(new IndexDependencyBuildItem(YACK_GROUP_ID, "kubernetes-client-api"));
    indexDependency.produce(new IndexDependencyBuildItem(YACK_GROUP_ID, "kubernetes-api"));
    indexDependency.produce(new IndexDependencyBuildItem(YACK_GROUP_ID, "kubernetes-model"));
    indexDependency.produce(new IndexDependencyBuildItem("org.bouncycastle", "bcprov-jdk15on"));
  }

  @BuildStep(onlyIf = NativeBuild.class)
  void dynamicProxies(
    CombinedIndexBuildItem combinedIndexBuildItem,
    BuildProducer<NativeImageProxyDefinitionBuildItem> proxyDefinition) {
    log.info("Adding Dynamic proxies for YAKC API Interfaces");
    combinedIndexBuildItem.getIndex().getKnownClasses().stream()
      .filter(ci -> Modifier.isInterface(ci.flags()))
      .map(ClassInfo::name)
      .map(Objects::toString)
      .filter(apiClass -> apiClass.startsWith("com.marcnuri.yakc.api"))
      .map(NativeImageProxyDefinitionBuildItem::new)
      .forEach(proxyDefinition::produce);
  }

  @BuildStep(onlyIf = NativeBuild.class)
  void sslSupport(
    CombinedIndexBuildItem combinedIndexBuildItem,
    BuildProducer<ExtensionSslNativeSupportBuildItem> sslNativeSupport) {
    log.info("Configuring native SSL support");
    final String[] sslClasses = getAllKnownImplementorsAndSubclasses(
      combinedIndexBuildItem,
      "org.bouncycastle.jcajce.provider.util.AlhorithmProvider",
      "java.security.cert.CertStoreSpi",
      "java.security.Key",
      "java.security.KeyFactory",
      "java.security.KeyStoreSpi");
    reflectiveClasses.produce(ReflectiveClassBuildItem
      .builder(sslClasses).weak(false).constructors(true).methods(true).fields(true).build());
    sslNativeSupport.produce(new ExtensionSslNativeSupportBuildItem(Feature.KUBERNETES_CLIENT));
  }

  @BuildStep
  public void process(
    CombinedIndexBuildItem combinedIndexBuildItem,
    BuildProducer<AdditionalBeanBuildItem> additionalBeanBuildItemBuildItem) {

    featureProducer.produce(new FeatureBuildItem("YAKC"));
    roleBindingProducer.produce(new KubernetesRoleBindingBuildItem("view", true));

    // Reflection
    final String[] modelClasses = getAllKnownImplementorsAndSubclasses(combinedIndexBuildItem, KUBERNETES_MODEL);
    reflectiveClasses.produce(ReflectiveClassBuildItem
      .builder(modelClasses).weak(true).constructors(true).methods(true).fields(false).build());

    final String[] additionalClasses = new String[]{
      "com.marcnuri.yakc.config.KubeConfig",
      "com.marcnuri.yakc.config.KubeConfig$NamedCluster",
      "com.marcnuri.yakc.config.KubeConfig$Cluster",
      "com.marcnuri.yakc.config.KubeConfig$NamedContext",
      "com.marcnuri.yakc.config.KubeConfig$Context",
      "com.marcnuri.yakc.config.KubeConfig$NamedExtension",
      "com.marcnuri.yakc.config.KubeConfig$AuthInfo",
      "com.marcnuri.yakc.config.KubeConfig$NamedAuthInfo",
      "com.marcnuri.yakc.api.WatchEvent"
    };
    reflectiveClasses.produce(ReflectiveClassBuildItem
      .builder(additionalClasses).weak(true).constructors(true).methods(true).fields(false).build());

    final String[] deserializerClasses = combinedIndexBuildItem.getIndex()
      .getAllKnownSubclasses(DotName.createSimple("com.fasterxml.jackson.databind.JsonDeserializer"))
      .stream()
      .map(c -> c.name().toString())
      .filter(s -> s.startsWith(YACK_GROUP_ID))
      .toArray(String[]::new);
    reflectiveClasses.produce(new ReflectiveClassBuildItem(true, false, deserializerClasses));

    final String[] serializerClasses = combinedIndexBuildItem.getIndex()
      .getAllKnownSubclasses(DotName.createSimple("com.fasterxml.jackson.databind.JsonSerializer"))
      .stream()
      .map(c -> c.name().toString())
      .filter(s -> s.startsWith(YACK_GROUP_ID))
      .toArray(String[]::new);
    reflectiveClasses.produce(new ReflectiveClassBuildItem(true, false, serializerClasses));

    // wire up the KubernetesClient bean support
    additionalBeanBuildItemBuildItem.produce(AdditionalBeanBuildItem.unremovableOf(KubernetesClientProducer.class));
  }

  private static String[] getAllKnownImplementorsAndSubclasses(
    CombinedIndexBuildItem combinedIndexBuildItem, String... baseClasses) {
    Stream<ClassInfo> ret = Stream.empty();
    for (String baseClass : baseClasses) {
      ret = Stream.concat(ret,
        combinedIndexBuildItem.getIndex().getAllKnownImplementors(DotName.createSimple(baseClass))
          .stream());
      ret = Stream.concat(ret,
        combinedIndexBuildItem.getIndex().getAllKnownSubclasses(DotName.createSimple(baseClass))
          .stream());
    }
    return ret.map(ClassInfo::name).map(Object::toString).toArray(String[]::new);
  }
}
