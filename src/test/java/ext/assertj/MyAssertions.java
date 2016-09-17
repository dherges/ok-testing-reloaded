/*
 * ok-testing-reloaded
 * https://github.com/dherges/ok-testing-reloaded
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */
package ext.assertj;

import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Response;

public final class MyAssertions {

  public static RecordedRequestAssert assertThat(RecordedRequest recordedRequest) {
    return new RecordedRequestAssert(recordedRequest);
  }

  public static ResponseAssert assertThat(Response<?> response) {
    return new ResponseAssert(response);
  }
}
