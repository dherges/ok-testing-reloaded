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
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SparkRunner.class)
@SparkApplicationTest(value = TwitterApp.class, port = 4444)
public class TwitterAppTest {

  @Test
  public void test() throws IOException {
    final OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

    final Request request = new Request.Builder()
      .get()
      .url("http://localhost:4444")
      .build();

    final Response response = okHttpClient.newCall(request).execute();

    assertThat(response.body().string()).isEqualTo("Hello again!");
  }

}
