/*
 * ok-testing-reloaded
 * https://github.com/dherges/ok-testing-reloaded
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */
package oktesting;

import com.orhanobut.mockwebserverplus.MockWebServerPlus;
import okhttp3.mockwebserver.RecordedRequest;
import oktesting.app.twitter.Tweet;
import oktesting.app.twitter.TwitterApi;
import org.junit.Rule;
import org.junit.Test;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class TwitterApiTest {

  @Rule
  public MockWebServerPlus server = new MockWebServerPlus();

  private TwitterApi twitterApi() {
    return new TwitterApi.Builder()
      .baseUrl(server.url("/"))
      .build();
  }

  @Test
  public void homeTimeline() throws IOException, InterruptedException {
    // replay scripted http response from yaml file
    server.enqueue("twitter/statuses/home_timeline");

    // execute request
    final Response<List<Tweet>> response = twitterApi().homeTimeline().execute();

    // assert response
    assertThat(response.code()).isEqualTo(200);
    assertThat(response.body().size()).isEqualTo(3);
    assertThat(response.body().get(0).createdAt).isEqualTo("2012-08-28T23:16:23.000");
    assertThat(response.body().get(0).user.name).isEqualTo("OAuth Dancer");

    // verify request
    final RecordedRequest recordedRequest = server.takeRequest();
    assertThat(recordedRequest.getMethod()).isEqualTo("GET");
    assertThat(recordedRequest.getPath()).isEqualTo("/statuses/home_timeline.json");
  }

  @Test
  public void show() throws IOException, InterruptedException {
    server.enqueue("twitter/statuses/show/success");

    final Response<Tweet> response = twitterApi().show("123").execute();

    assertThat(response.body()).isNotNull();

    final RecordedRequest recordedRequest = server.takeRequest();
    assertThat(recordedRequest.getMethod()).isEqualTo("GET");
    assertThat(recordedRequest.getPath()).isEqualTo("/statuses/show/123");
  }

}
