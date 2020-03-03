package org.klaster.domain.serializer;

/*
 * org.klaster.domain.serializer
 *
 * workrest
 *
 * 3/3/20
 *
 * Copyright(c) JazzTeam
 */

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

  public static final String LOCAL_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

  @Override
  public void serialize(LocalDateTime localDateTime, JsonGenerator generator, SerializerProvider serializerProvider) throws IOException {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_PATTERN);
    String formattedDateTime = localDateTime.format(dateTimeFormatter);
    generator.writeString(formattedDateTime);
  }
}
