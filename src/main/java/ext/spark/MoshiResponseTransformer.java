/*
 * ok-testing-reloaded
 * https://github.com/dherges/ok-testing-reloaded
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */
package ext.spark;

import com.squareup.moshi.Moshi;
import spark.ResponseTransformer;

public class MoshiResponseTransformer<T> implements ResponseTransformer {

  private final Moshi moshi;
  private final Class<T> type;

  private MoshiResponseTransformer(Moshi moshi, Class<T> type) {
    this.moshi = moshi;
    this.type = type;
  }

  @SuppressWarnings("unchecked")
  @Override
  public String render(Object model) throws Exception {
    return moshi.adapter(type).toJson((T) model);
  }

  public static ResponseTransformer create(Class<?> type) {
    return create(new Moshi.Builder().build(), type);
  }

  public static ResponseTransformer create(Moshi moshi, Class<?> type) {
    return new MoshiResponseTransformer<>(moshi, type);
  }
}
