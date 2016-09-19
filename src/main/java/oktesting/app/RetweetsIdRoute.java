/*
 * ok-testing-reloaded
 * https://github.com/dherges/ok-testing-reloaded
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */
package oktesting.app;

import com.squareup.moshi.Moshi;
import oktesting.twitter.DateJsonAdapter;
import oktesting.twitter.Tweet;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class RetweetsIdRoute implements Route {

  @Override
  public Object handle(Request request, Response response) throws Exception {
    final String idParam = request.params(":id");
    final String count = request.queryParams("count");

    final List<Tweet> tweetList = fetchTweets(
      Long.valueOf(idParam),
      Integer.valueOf(count != null && count.length() > 0 ? count : "100")
    );

    final Moshi moshi = new Moshi.Builder()
        .add(Date.class, new DateJsonAdapter("EEE MMM dd kk:mm:ss z yyyy").nullSafe())
        .build();

    response.header("Content-Type", "application/json");

    return moshi.adapter(List.class).nullSafe().toJson(tweetList);
  }

  private List<Tweet> fetchTweets(long id, int count) {
    final List<Tweet> tweets = new ArrayList<Tweet>(count);
    for (int i = 0; i < count; i++) {
      final Tweet tweet = new Tweet();
      tweet.id = id + i;
      tweet.text = String.format("Retweet #%s of %s for %s", i, count, id);
      tweet.favorited = i % 2 == 0;

      tweets.add(tweet);
    }

    return tweets;
  }

  public static Route create() {
    return new RetweetsIdRoute();
  }
}
