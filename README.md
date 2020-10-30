# YAKC - Yet Another Kubernetes Client - Quarkus Extension

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
      <artifactId>quarkues-yakc-extension</artifactId>
      <version>0.0.0</version>
    </dependency>
  </dependencies>
```

## Release process
- Tag release with the current `pom.xml` version e.g. 'v0.0.0'
- Wait for action to complete and artifacts to be published in Maven Central
- Increment version:
  `mvn versions:set -DnewVersion=4.11-SNAPSHOT -DgenerateBackupPoms=false`
- Commit `[RELEASE] v0.0.0 released, prepare for next development iteration`
