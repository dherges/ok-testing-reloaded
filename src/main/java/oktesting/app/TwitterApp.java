/*
 * ok-testing-reloaded
 * https://github.com/dherges/ok-testing-reloaded
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */
package oktesting.app;

import com.squareup.moshi.Moshi;
import ext.spark.ContentTypeRoute;
import ext.spark.MoshiResponseTransformer;
import oktesting.twitter.DateJsonAdapter;
import spark.servlet.SparkApplication;

import java.util.Date;
import java.util.List;

import static spark.Spark.get;

public class TwitterApp implements SparkApplication {

  private final TweetsDb tweetsDb;

  public TwitterApp(TweetsDb tweetsDb) {
    this.tweetsDb = tweetsDb;
  }

  @Override
  public void init() {

    get(
      "statuses/retweets/:id",
      "*/*",
      ContentTypeRoute.create(
        (req, res) -> {
          final String idParam = req.params(":id");
          final String count = req.queryParams("count");

          return tweetsDb.fetchTweets(
            Long.valueOf(idParam),
            Integer.valueOf(count != null && count.length() > 0 ? count : "100")
          );
        },
        "application/json",
        MoshiResponseTransformer.create(
          new Moshi.Builder()
            .add(Date.class, new DateJsonAdapter("EEE MMM dd kk:mm:ss z yyyy").nullSafe())
            .build(),
          List.class
        )
      )
    );
  }
}
