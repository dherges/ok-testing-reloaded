/*
 * ok-testing-reloaded
 * https://github.com/dherges/ok-testing-reloaded
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */
package oktesting.app;

import oktesting.twitter.Tweet;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class RetweetsIdRoute implements Route {

  @Override
  public Object handle(Request request, Response response) throws Exception {
    final String idParam = request.params(":id");
    final String count = request.queryParams("count");

    return fetchTweets(
      Long.valueOf(idParam),
      Integer.valueOf(count != null && count.length() > 0 ? count : "100")
    );
  }

  private List<Tweet> fetchTweets(long id, int count) {
    return IntStream.range(0, 99)
      .mapToObj((i) -> {
        final Tweet tweet = new Tweet();
        tweet.id = id + i;
        tweet.text = String.format("Retweet #%s of %s for %s", i, count, id);
        tweet.favorited = i % 2 == 0;

        return tweet;
      })
      .collect(Collectors.toList());
  }

  public static Route create() {
    return new RetweetsIdRoute();
  }
}
