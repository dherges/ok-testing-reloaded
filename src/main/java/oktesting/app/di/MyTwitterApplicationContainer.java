/*
 * ok-testing-reloaded
 * https://github.com/dherges/ok-testing-reloaded
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */
package oktesting.app.di;

import ext.spark.SparkApplicationContainer;
import oktesting.app.TweetsDb;

public interface MyTwitterApplicationContainer extends SparkApplicationContainer {

  TweetsDb tweetsDb();
}
