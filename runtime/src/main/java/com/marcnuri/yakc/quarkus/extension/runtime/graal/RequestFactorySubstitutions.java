package com.marcnuri.yakc.quarkus.extension.runtime.graal;

import com.oracle.svm.core.annotate.Alias;
import com.oracle.svm.core.annotate.Substitute;
import com.oracle.svm.core.annotate.TargetClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

@TargetClass(className = "retrofit2.RequestFactory")
public final class RequestFactorySubstitutions {

}

@TargetClass(className = "retrofit2.RequestFactory", innerClass = "Builder")
final class RequestFactory_BuilderSubstitutions {

  @Alias
  Method method;

  @Alias
  ParameterHandlerSubstitutions<?> parseParameterAnnotation(int p, Type type, Annotation[] annotations, Annotation annotation) {
    return null;
  }

  @Substitute
  ParameterHandlerSubstitutions<?> parseParameter(int p, Type parameterType, Annotation[] annotations, boolean allowContinuation) {
    ParameterHandlerSubstitutions<?> result = null;
    if (annotations != null) {
      Annotation[] var6 = annotations;
      int var7 = annotations.length;

      for (int var8 = 0; var8 < var7; ++var8) {
        Annotation annotation = var6[var8];
        ParameterHandlerSubstitutions<?> annotationAction = this.parseParameterAnnotation(p, parameterType, annotations, annotation);
        if (annotationAction != null) {
          if (result != null) {
            throw UtilsSubstitutions.parameterError(this.method, p, "Multiple Retrofit annotations found, only one allowed.", new Object[0]);
          }

          result = annotationAction;
        }
      }
    }

    if (result == null) {
      if (allowContinuation) {
        throw new NativeException();
      }
      throw UtilsSubstitutions.parameterError(this.method, p, "No Retrofit annotation found.", new Object[0]);
    } else {
      return result;
    }
  }
}

@TargetClass(className = "retrofit2.ParameterHandler")
final class ParameterHandlerSubstitutions<T> {

}
