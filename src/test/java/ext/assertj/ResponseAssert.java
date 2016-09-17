/*
 * ok-testing-reloaded
 * https://github.com/dherges/ok-testing-reloaded
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */
package ext.assertj;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assert;
import org.assertj.core.internal.Integers;
import org.assertj.core.internal.Objects;
import retrofit2.Response;

public class ResponseAssert extends AbstractAssert<ResponseAssert, Response<?>>
  implements Assert<ResponseAssert, Response<?>> {

  Objects objects = Objects.instance();
  Integers integers = Integers.instance();

  public ResponseAssert(Response<?> actual) {
    super(actual, ResponseAssert.class);
  }

  public ResponseAssert isStatusCode(int statusCode) {
    objects.assertNotNull(info, actual);
    integers.assertEqual(info, actual.code(), statusCode);

    return this;
  }

  public ResponseAssert isOk() {
    return isStatusCode(200);
  }

  public ResponseAssert hasBody() {
    objects.assertNotNull(info, actual);
    objects.assertNotNull(info, actual.body());

    return this;
  }
}
