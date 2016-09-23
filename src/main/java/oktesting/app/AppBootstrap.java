/*
 * ok-testing-reloaded
 * https://github.com/dherges/ok-testing-reloaded
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */
package oktesting.app;

import oktesting.app.di.ProductiveApplicationContainer;

public class AppBootstrap {

  public static void main(String[] args) {
    new ProductiveApplicationContainer()
      .twitterApp()
      .init();
  }
}
