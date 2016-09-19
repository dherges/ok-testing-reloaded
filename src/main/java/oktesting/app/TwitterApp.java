/*
 * ok-testing-reloaded
 * https://github.com/dherges/ok-testing-reloaded
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */
package oktesting.app;

import com.squareup.moshi.Moshi;
import ext.spark.MoshiResponseTransformer;
import oktesting.twitter.DateJsonAdapter;
import spark.servlet.SparkApplication;

import java.util.Date;
import java.util.List;

import static spark.Spark.get;

public class TwitterApp implements SparkApplication {

  @Override
  public void init() {

    get(
      "statuses/retweets/:id",
      "*/*",
      ContentTypeRoute.create(
        RetweetsIdRoute.create(),
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
