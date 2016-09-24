/*
 * ok-testing-reloaded
 * https://github.com/dherges/ok-testing-reloaded
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */
package oktesting.app.di;

import oktesting.app.TweetsDb;

public class TestApplicationContainer extends ProductiveApplicationContainer implements MyTwitterApplicationContainer {

  @Override
  public TweetsDb tweetsDb() {
    return new MockedTweetsDb();
  }
}
