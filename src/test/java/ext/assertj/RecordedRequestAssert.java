/*
 * ok-testing-reloaded
 * https://github.com/dherges/ok-testing-reloaded
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */
package ext.assertj;

import okhttp3.mockwebserver.RecordedRequest;
import org.assertj.core.api.*;
import org.assertj.core.internal.Objects;
import org.assertj.core.internal.Strings;

public class RecordedRequestAssert extends AbstractAssert<RecordedRequestAssert, RecordedRequest>
  implements Assert<RecordedRequestAssert, RecordedRequest> {

  Strings strings = Strings.instance();
  Objects objects = Objects.instance();

  public RecordedRequestAssert(RecordedRequest actual) {
    super(actual, RecordedRequestAssert.class);
  }

  public RecordedRequestAssert isHttp(String verb, String path) {
    objects.assertNotNull(info, actual);
    strings.assertEqualsIgnoringCase(info, actual.getMethod(), verb);
    strings.assertEqualsIgnoringCase(info, actual.getPath(), path);

    return this;
  }

}
