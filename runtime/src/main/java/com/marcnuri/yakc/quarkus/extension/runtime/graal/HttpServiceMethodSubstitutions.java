package com.marcnuri.yakc.quarkus.extension.runtime.graal;

import com.oracle.svm.core.annotate.Substitute;
import com.oracle.svm.core.annotate.TargetClass;

import retrofit2.Call;

@TargetClass(className = "retrofit2.HttpServiceMethod")
public final class HttpServiceMethodSubstitutions<ResponseT> {

    @Substitute
    Object adapt(Call<ResponseT> call, Object[] args) {
        throw new RuntimeException("This is not Kotlin. Unsupported in native mode");
    }
}
