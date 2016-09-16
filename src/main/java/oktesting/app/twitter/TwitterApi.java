package oktesting.app.twitter;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Retrofit interface for Twitter REST API
 */
public interface TwitterApi {

  @GET("statuses/home_timeline.json")
  Call<ResponseBody> homeTimeline();

  @GET("statuses/show/:id")
  Call<ResponseBody> show(@Path("id") String id);

  @POST("statuses/update")
  Call<ResponseBody> tweet(@Query("status") String status);



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
        .build()
        .create(TwitterApi.class);
    }
  }
}
