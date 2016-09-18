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
 * Representation of a Tweet object transferred from/to Twitter REST API in JSON format.
 */
public class Tweet {

  public long id;
  public String text;
  public User user;
  @Json(name="created_at") public Date createdAt;
  public boolean favorited;

}
