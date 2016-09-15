/*
 * ok-testing-reloaded
 * https://github.com/dherges/ok-testing-reloaded
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */
package oktesting;

import com.orhanobut.mockwebserverplus.MockWebServerPlus;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class TwitterApiTest {

  @Rule
  public MockWebServerPlus server = new MockWebServerPlus();

  private final OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

  @Test
  public void homeTimeline() throws IOException, InterruptedException {
    // replay scripted http response from yaml file
    server.enqueue("twitter/statuses/home_timeline");

    // execute request
    final Request request = new Request.Builder()
      .get()
      .url(server.url("statuses/home_timeline.json"))
      .build();
    final Response response = okHttpClient.newCall(request).execute();

    // assert response
    assertThat(response.code()).isEqualTo(200);
    assertThat(response.body().string()).startsWith("[\n  {\n    \"coordinates\"");

    // verify request
    final RecordedRequest recordedRequest = server.takeRequest();
    assertThat(recordedRequest.getMethod()).isEqualTo("GET");
    assertThat(recordedRequest.getPath()).isEqualTo("/statuses/home_timeline.json");
  }

}
