package oktesting;

import org.junit.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class FooTest {

  @Test
  public void foo() {
    assertThat("foo").isNotEqualTo("bar");
  }
}
