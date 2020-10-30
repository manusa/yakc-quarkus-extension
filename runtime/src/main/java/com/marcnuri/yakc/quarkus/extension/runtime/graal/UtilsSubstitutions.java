package com.marcnuri.yakc.quarkus.extension.runtime.graal;

import com.oracle.svm.core.annotate.Alias;
import com.oracle.svm.core.annotate.TargetClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@TargetClass(className = "retrofit2.Utils")
public final class UtilsSubstitutions {
  @Alias
  static boolean isAnnotationPresent(Annotation[] annotations, Class<? extends Annotation> cls) {
    return false;
  }

  @Alias
  static RuntimeException parameterError(Method method, int p, String message, Object... args) {
    return null;
  }

}
