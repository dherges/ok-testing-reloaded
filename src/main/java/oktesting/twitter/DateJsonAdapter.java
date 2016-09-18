/*
 * ok-testing-reloaded
 * https://github.com/dherges/ok-testing-reloaded
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */
package oktesting.twitter;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateJsonAdapter extends JsonAdapter<Date> {

  private final String simpleDateFormat;

  public DateJsonAdapter(String simpleDateFormat) {
    this.simpleDateFormat = simpleDateFormat;
  }

  @Override
  public synchronized Date fromJson(JsonReader reader) throws IOException {
    final String string = reader.nextString();

    try {
      return new SimpleDateFormat(simpleDateFormat).parse(string);
    } catch (ParseException e) {
      throw new IOException(e);
    }
  }

  @Override
  public synchronized void toJson(JsonWriter writer, Date value) throws IOException {
    final String string = new SimpleDateFormat(simpleDateFormat).format(value);

    writer.value(string);
  }
}
