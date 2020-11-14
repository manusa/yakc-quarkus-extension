# YAKC - Yet Another Kubernetes Client - Quarkus Extension
[![Maven Central](https://img.shields.io/maven-central/v/com.marcnuri.yakc/quarkus-yakc-extension)
](https://search.maven.org/search?q=g:com.marcnuri.yakc%20a:quarkus-yakc-extension)
[![javadoc](https://javadoc.io/badge2/com.marcnuri.yakc/quarkus-yakc-extension/javadoc.svg)
](https://javadoc.io/doc/com.marcnuri.yakc/quarkus-yakc-extension)

Use this [Quarkus](https://www.quarkus.io) extension to integrate
[YAKC](https://github.com/manusa/yakc) in your Quarkus application.

This extension provides the requirements to be able to produce native images for your
application running with YAKC.

## Quickstart

Add the extension dependency to your `pom.xml`:
```xml
  <!-- ... -->
  <dependencies>
    <!-- ... -->
    <dependency>
      <groupId>com.marcnuri.yakc</groupId>
      <artifactId>quarkus-yakc-extension</artifactId>
      <version>0.0.4</version>
    </dependency>
  </dependencies>
```

## Release process
- Tag release with the current `pom.xml` version e.g. `v0.0.0`
- Wait for action to complete and artifacts to be published in Maven Central
- Increment version:
  `mvn versions:set -DnewVersion=0.0.5 -DgenerateBackupPoms=false`
- Commit `[RELEASE] v0.0.4 released, prepare for next development iteration`
