/*
 * ok-testing-reloaded
 * https://github.com/dherges/ok-testing-reloaded
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */
package ext.junit;

import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import spark.servlet.SparkApplication;

import static spark.Spark.awaitInitialization;
import static spark.Spark.port;
import static spark.Spark.stop;

public class SparkRunner extends BlockJUnit4ClassRunner {

  private Class<? extends SparkApplication> sparkApplication;
  private int sparkPort;

  public SparkRunner(Class<?> testClass) throws InitializationError {
    super(testClass);
  }

  @Override
  public void run(RunNotifier notifier) {
    scanAnnotation();
    try {
      sparkBootstrap();
    } catch (Exception e) {
      notifier.fireTestFailure(new Failure(getDescription(), e));
    }

    super.run(notifier);

    sparkShutdown();
  }

  private void scanAnnotation() {
    SparkApplicationTest annotation = getTestClass().getAnnotation(SparkApplicationTest.class);

    sparkApplication = annotation.value();
    sparkPort = annotation.port();
  }

  private void sparkBootstrap() throws IllegalAccessException, InstantiationException {
    port(sparkPort);

    sparkApplication.newInstance().init();

    awaitInitialization();
  }

  private void sparkShutdown() {
    stop();
  }

}
