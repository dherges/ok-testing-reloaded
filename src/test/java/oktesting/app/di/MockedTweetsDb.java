/*
 * ok-testing-reloaded
 * https://github.com/dherges/ok-testing-reloaded
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */
package oktesting.app.di;

import oktesting.app.TweetsDb;
import oktesting.twitter.Tweet;
import oktesting.twitter.User;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MockedTweetsDb extends TweetsDb {

  public List<Tweet> fetchTweets(long id, int count) {
    return IntStream.range(0, 100)
      .mapToObj((i) -> {
        final Tweet tweet = new Tweet();
        tweet.id = id + i;
        tweet.text = String.format("Mocked retweet of %s", id);
        tweet.favorited = false;
        tweet.user = new User();
        tweet.user.name = "Maria Moccachino";

        return tweet;
      })
      .collect(Collectors.toList());
  }
}
