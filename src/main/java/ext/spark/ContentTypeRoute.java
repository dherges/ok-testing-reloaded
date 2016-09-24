/*
 * ok-testing-reloaded
 * https://github.com/dherges/ok-testing-reloaded
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */
package ext.spark;

import spark.*;

import java.util.Optional;

public class ContentTypeRoute implements Route {

  private final String contentType;
  private final Route delegate;
  private final Optional<ResponseTransformer> responseTransformer;

  private ContentTypeRoute(String contentType, Route delegate, ResponseTransformer responseTransformer) {
    this.contentType = contentType;
    this.delegate = delegate;
    this.responseTransformer = Optional.ofNullable(responseTransformer);
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    final Object result = delegateAndTransform(request, response);

    response.header("Content-Type", contentType);

    return result;
  }

  private Object delegateAndTransform(Request request, Response response) throws Exception {
    final Object data = delegate.handle(request, response);

    if (responseTransformer.isPresent()) {
      return responseTransformer.get().render(data);
    }

    return data;
  }

  public static Route create(Route delegate, String contentType) {
    return create(delegate, contentType, null);
  }

  public static Route create(Route delegate, String contentType, ResponseTransformer responseTransformer) {
    return new ContentTypeRoute(contentType, delegate, responseTransformer);
  }
}
