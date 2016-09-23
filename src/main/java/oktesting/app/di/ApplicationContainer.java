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

public interface ApplicationContainer {

  TwitterApp twitterApp();

  TweetsDb tweetsDb();
}
