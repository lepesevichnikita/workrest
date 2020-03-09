package org.klaster.domain.deserializer;

/*
 * org.klaster.domain.deserializer
 *
 * workrest
 *
 * 3/3/20
 *
 * Copyright(c) JazzTeam
 */

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

  public static final String LOCAL_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm";

  @Override
  public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_PATTERN);
    String localDateTime = jsonParser.getText();
    System.out.println(localDateTime);
    System.out.println(LocalDateTime.parse(localDateTime, dateTimeFormatter));
    return LocalDateTime.parse(localDateTime, dateTimeFormatter);
  }

}
