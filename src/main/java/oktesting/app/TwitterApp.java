package oktesting.app;

import spark.servlet.SparkApplication;

import static spark.Spark.get;

public class TwitterApp implements SparkApplication {

  @Override
  public void init() {

    get("/", (req, res) -> "Hello again!");
  }
}
