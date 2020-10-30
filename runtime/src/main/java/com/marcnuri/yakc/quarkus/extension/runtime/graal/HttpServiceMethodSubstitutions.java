package com.marcnuri.yakc.quarkus.extension.runtime.graal;

import com.oracle.svm.core.annotate.Substitute;
import com.oracle.svm.core.annotate.TargetClass;
import retrofit2.Call;

@TargetClass(className = "retrofit2.HttpServiceMethod")
public final class HttpServiceMethodSubstitutions<ResponseT, ReturnT> {


}

@TargetClass(className = "retrofit2.HttpServiceMethod", innerClass = "SuspendForResponse")
final class HttpServiceMethod_SuspendForResponseSubstitutions<ResponseT> {
  @Substitute
  Object adapt(Call<ResponseT> call, Object[] args) {
    throw new NativeException();
  }
}

@TargetClass(className = "retrofit2.HttpServiceMethod", innerClass = "SuspendForBody")
final class HttpServiceMethod_SuspendForBodySubstitutions<ResponseT> {
  @Substitute
  Object adapt(Call<ResponseT> call, Object[] args) {
    throw new NativeException();
  }
}