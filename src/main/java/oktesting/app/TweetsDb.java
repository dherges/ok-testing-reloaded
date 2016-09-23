/*
 * ok-testing-reloaded
 * https://github.com/dherges/ok-testing-reloaded
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */
package oktesting.app;

import oktesting.twitter.Tweet;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/** A database for querying tweets :-) */
public class TweetsDb {

  public List<Tweet> fetchTweets(long id, int count) {
    return IntStream.range(0, 100)
      .mapToObj((i) -> {
        final Tweet tweet = new Tweet();
        tweet.id = id + i;
        tweet.text = String.format("Retweet #%s of %s for %s", i, count, id);
        tweet.favorited = i % 2 == 0;

        return tweet;
      })
      .collect(Collectors.toList());
  }

}
