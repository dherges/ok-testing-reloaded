/*
 * ok-testing-reloaded
 * https://github.com/dherges/ok-testing-reloaded
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */
package oktesting;

import ext.junit.SparkApplicationTest;
import ext.junit.SparkRunner;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import oktesting.app.TwitterApp;
import oktesting.app.di.TestApplicationContainer;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static ext.assertj.MyAssertions.assertThat;


@RunWith(SparkRunner.class)
@SparkApplicationTest(value = TestApplicationContainer.class, port = 4444)
public class TwitterAppTest {

  private final OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

  @Test
  public void test() throws IOException {
    final Request request = new Request.Builder()
      .get()
      .url("http://localhost:4444/statuses/retweets/123")
      .build();

    final Response response = okHttpClient.newCall(request).execute();

    assertThat(response.body().string()).startsWith("[{\"favorited\":true,\"id\":123");
  }

  @Test
  public void testAgain() throws IOException {
    final Request request = new Request.Builder()
      .get()
      .url("http://localhost:4444/statuses/retweets/200")
      .build();

    final Response response = okHttpClient.newCall(request).execute();

    assertThat(response)
      .isOk()
      .hasContentType("application/json")
      .jsonPath("$.length()", Integer.class, 100)
      .jsonPath("$[0].id", Integer.class, 200);
  }

}
