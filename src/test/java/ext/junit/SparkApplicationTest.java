/*
 * ok-testing-reloaded
 * https://github.com/dherges/ok-testing-reloaded
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */
package ext.junit;

import spark.servlet.SparkApplication;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SparkApplicationTest {

  /** SparkApplication that will be tested */
  Class<? extends SparkApplication> value();

  /** Port number for the embedded web server */
  int port() default spark.Service.SPARK_DEFAULT_PORT;

}