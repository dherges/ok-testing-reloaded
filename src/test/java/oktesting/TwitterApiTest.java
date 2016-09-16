/*
 * ok-testing-reloaded
 * https://github.com/dherges/ok-testing-reloaded
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */
package oktesting;

import com.orhanobut.mockwebserverplus.MockWebServerPlus;
import okhttp3.ResponseBody;
import okhttp3.mockwebserver.RecordedRequest;
import oktesting.app.twitter.TwitterApi;
import org.junit.Rule;
import org.junit.Test;
import retrofit2.Response;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class TwitterApiTest {

  @Rule
  public MockWebServerPlus server = new MockWebServerPlus();


  @Test
  public void homeTimeline() throws IOException, InterruptedException {
    // replay scripted http response from yaml file
    server.enqueue("twitter/statuses/home_timeline");

    // execute request
    final TwitterApi twitterApi = new TwitterApi.Builder()
      .baseUrl(server.url("/"))
      .build();
    final Response<ResponseBody> response = twitterApi.homeTimeline().execute();

    // assert response
    assertThat(response.code()).isEqualTo(200);
    assertThat(response.body().string()).startsWith("[\n  {\n    \"coordinates\"");

    // verify request
    final RecordedRequest recordedRequest = server.takeRequest();
    assertThat(recordedRequest.getMethod()).isEqualTo("GET");
    assertThat(recordedRequest.getPath()).isEqualTo("/statuses/home_timeline.json");
  }

}
