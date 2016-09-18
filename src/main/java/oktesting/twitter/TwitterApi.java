/*
 * ok-testing-reloaded
 * https://github.com/dherges/ok-testing-reloaded
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */
package oktesting.twitter;

import com.squareup.moshi.Moshi;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.Date;
import java.util.List;

/**
 * Retrofit interface for Twitter REST API
 */
public interface TwitterApi {

  @GET("statuses/home_timeline.json")
  Call<List<Tweet>> homeTimeline();

  @GET("statuses/show/{id}")
  Call<Tweet> show(@Path("id") String id);

  @POST("statuses/update")
  Call<Tweet> tweet(@Query("status") String status);



  class Builder {
    private String baseUrl;

    public Builder() {}

    public Builder baseUrl(String baseUrl) {
      this.baseUrl = baseUrl;

      return this;
    }

    public TwitterApi build() {
      return new Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create(
          new Moshi.Builder()
            .add(Date.class, new DateJsonAdapter("EEE MMM dd kk:mm:ss z yyyy"))
            .build()
        ))
        .build()
        .create(TwitterApi.class);
    }
  }
}
