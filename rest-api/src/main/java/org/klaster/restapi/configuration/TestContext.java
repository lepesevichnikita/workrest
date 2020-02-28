package org.klaster.restapi.configuration;

/*
 * workrest
 *
 * 24.02.2020
 *
 */

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * TestContext
 *
 * @author Nikita Lepesevich
 */

@Configuration
@ComponentScan({"org.klaster.domain", "org.klaster.restapi"})
public class TestContext {

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
    return objectMapper;
  }

}
