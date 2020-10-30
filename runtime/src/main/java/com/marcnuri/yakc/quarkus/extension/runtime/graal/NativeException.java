package com.marcnuri.yakc.quarkus.extension.runtime.graal;

public class NativeException extends UnsupportedOperationException {

  public NativeException() {
    super("Operation not supported in Quarkus native mode");
  }
}
