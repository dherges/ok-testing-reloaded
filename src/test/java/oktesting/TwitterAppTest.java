package oktesting;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import oktesting.app.TwitterApp;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static spark.Spark.awaitInitialization;
import static spark.Spark.port;
import static spark.Spark.stop;

public class TwitterAppTest {

  /** bootstraps the spark application by starting an embedded web server */
  @Before
  public void before() {
    port(4444);

    new TwitterApp().init();

    awaitInitialization();
  }

  /** after test run, shuts down the web server */
  @After
  public void after() {
    stop();
  }

  @Test
  public void test() throws IOException {
    final OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

    final Request request = new Request.Builder()
      .get()
      .url("http://localhost:4444")
      .build();

    final Response response = okHttpClient.newCall(request).execute();

    assertThat(response.body().string()).isEqualTo("Hello again!");
  }

}
