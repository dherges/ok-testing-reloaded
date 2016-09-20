/*
 * ok-testing-reloaded
 * https://github.com/dherges/ok-testing-reloaded
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */
package ext.assertj;

import com.jayway.jsonpath.JsonPath;
import okhttp3.Response;
import org.assertj.core.api.*;
import org.assertj.core.internal.Integers;
import org.assertj.core.internal.Objects;
import org.assertj.core.internal.Strings;

import java.io.IOException;

import static org.assertj.core.api.Assertions.fail;

public class OkHttpResponseAssert extends AbstractAssert<OkHttpResponseAssert, Response>
  implements Assert<OkHttpResponseAssert, Response> {

  Strings strings = Strings.instance();
  Integers integers = Integers.instance();
  Objects objects = Objects.instance();

  private String responseBody = null;

  public OkHttpResponseAssert(Response actual) {
    super(actual, OkHttpResponseAssert.class);
  }

  /* consumes the response body to a string in memory - response body can be consumed only once */
  private String consumeResponseBody() {
    if (responseBody == null) {
      try {
        responseBody = actual.body().string();
      } catch (IOException e) {
        fail("Cannot read response body", e);
      }
    }

    return responseBody;
  }

  public OkHttpResponseAssert isOk() {
    return isStatusCode(200);
  }

  public OkHttpResponseAssert isStatusCode(int expected) {
    objects.assertNotNull(info, actual);
    integers.assertEqual(info, actual.code(), expected);

    return this;
  }

  public OkHttpResponseAssert hasContentType(String mediaType) {
    strings.assertEqualsIgnoringCase(info, actual.body().contentType().toString(), mediaType);

    return this;
  }

  public OkHttpResponseAssert hasContentTypeMedia(String type) {
    strings.assertEqualsIgnoringCase(info, actual.body().contentType().type(), type);

    return this;
  }

  public OkHttpResponseAssert hasContentTypeSubtype(String subtype) {
    strings.assertEqualsIgnoringCase(info, actual.body().contentType().subtype(), subtype);

    return this;
  }

  public OkHttpResponseAssert isContentLength(long contentLength) {
    integers.assertEqual(info, actual.body().contentLength(), contentLength);

    return this;
  }

  public <T> OkHttpResponseAssert jsonPath(String jsonPath, Class<T> type, T expected) {
    T object = JsonPath.parse(consumeResponseBody()).read(jsonPath, type);

    objects.assertNotNull(info, object);
    objects.assertEqual(info, object, expected);

    return this;
  }

}
