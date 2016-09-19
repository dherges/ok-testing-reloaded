/*
 * ok-testing-reloaded
 * https://github.com/dherges/ok-testing-reloaded
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */
package oktesting.app;

import spark.servlet.SparkApplication;

import static spark.Spark.get;

public class TwitterApp implements SparkApplication {

  @Override
  public void init() {

    get("/", (req, res) -> "Hello again!");
  }
}
