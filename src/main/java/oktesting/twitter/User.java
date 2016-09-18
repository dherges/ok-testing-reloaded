/*
 * ok-testing-reloaded
 * https://github.com/dherges/ok-testing-reloaded
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */
package oktesting.twitter;

import com.squareup.moshi.Json;

import java.util.Date;

/**
 * Representation of an User object transferred from/to Twitter REST API in JSON format.
 */
public class User {

  public String name;
  @Json(name = "created_at") public Date createdAt;
  public String location;
  public String url;
}
