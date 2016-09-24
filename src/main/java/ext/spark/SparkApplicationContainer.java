/*
 * ok-testing-reloaded
 * https://github.com/dherges/ok-testing-reloaded
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */
package ext.spark;

import spark.servlet.SparkApplication;

public interface SparkApplicationContainer {

  SparkApplication sparkApplication();
}
