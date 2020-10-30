package com.marcnuri.yakc.quarkus.extension.runtime.graal;

import com.oracle.svm.core.annotate.Alias;
import com.oracle.svm.core.annotate.Substitute;
import com.oracle.svm.core.annotate.TargetClass;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.http.Streaming;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@TargetClass(className = "retrofit2.BuiltInConverters")
public final class BuiltInConvertersSubstitutions {
  @Substitute
  Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
    if (type == ResponseBody.class) {
      return UtilsSubstitutions.isAnnotationPresent(annotations, Streaming.class)
        ? StreamingResponseBodyConverterSubstitutions.INSTANCE
        : BufferingResponseBodyConverterSubstitutions.INSTANCE;
    }
    if (type == Void.class) {
      return VoidResponseBodyConverterSubstitutions.INSTANCE;
    }
    return null;
  }
}

@TargetClass(className = "retrofit2.BuiltInConverters", innerClass = "BufferingResponseBodyConverter")
final class BufferingResponseBodyConverterSubstitutions implements Converter<ResponseBody, ResponseBody> {
  @Alias
  static BufferingResponseBodyConverterSubstitutions INSTANCE;

  @Alias
  public ResponseBody convert(ResponseBody value) {
    return null;
  }
}

@TargetClass(className = "retrofit2.BuiltInConverters", innerClass = "StreamingResponseBodyConverter")
final class StreamingResponseBodyConverterSubstitutions implements Converter<ResponseBody, ResponseBody> {
  @Alias
  static StreamingResponseBodyConverterSubstitutions INSTANCE;

  @Alias
  public ResponseBody convert(ResponseBody value) {
    return null;
  }
}

@TargetClass(className = "retrofit2.BuiltInConverters", innerClass = "VoidResponseBodyConverter")
final class VoidResponseBodyConverterSubstitutions implements Converter<ResponseBody, ResponseBody> {
  @Alias
  static VoidResponseBodyConverterSubstitutions INSTANCE;

  @Alias
  public ResponseBody convert(ResponseBody value) {
    return null;
  }
}
