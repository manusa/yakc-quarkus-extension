package com.marcnuri.yakc.quarkus.extension.runtime.graal;

import java.io.IOException;
import java.io.InputStream;

import com.oracle.svm.core.annotate.Substitute;
import com.oracle.svm.core.annotate.TargetClass;

@TargetClass(className = "com.marcnuri.yakc.ssl.SSLResolver")
public final class SSLResolverSubstitutions {

    @Substitute
    static InputStream loadJavaTrustStore() throws IOException {
        // Remove unneeded warning
        return null;
    }

}
