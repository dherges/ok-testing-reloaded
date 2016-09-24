/*
 * ok-testing-reloaded
 * https://github.com/dherges/ok-testing-reloaded
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */
package oktesting.app.di;

import oktesting.app.TweetsDb;
import oktesting.app.TwitterApp;
import spark.servlet.SparkApplication;

public class ProductiveApplicationContainer implements MyTwitterApplicationContainer {

  @Override
  public SparkApplication sparkApplication() {
    return new TwitterApp(tweetsDb());
  }

  @Override
  public TweetsDb tweetsDb() {
    return new TweetsDb();
  }

}
