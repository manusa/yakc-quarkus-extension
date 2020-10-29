package com.marcnuri.yakc.quarkus.extension.runtime.graal;

import com.oracle.svm.core.annotate.TargetClass;

@TargetClass(className = "retrofit2.RequestFactory")
public final class RequestFactorySubstitutions {

    @TargetClass(className = "retrofit2.RequestFactory$Builder")
    public static final class Builder {

    }
}
