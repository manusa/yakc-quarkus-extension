package com.marcnuri.yakc.quarkus.extension.runtime.graal;

import java.lang.reflect.Method;

import com.oracle.svm.core.annotate.Substitute;
import com.oracle.svm.core.annotate.TargetClass;

@TargetClass(className = "retrofit2.Platform")
public final class PlatformSubstitutions {
    @Substitute
    Object invokeDefaultMethod(Method method, Class<?> declaringClass, Object object, Object... args) throws Throwable {
        throw new RuntimeException("Unsupported in native");
    }
}
