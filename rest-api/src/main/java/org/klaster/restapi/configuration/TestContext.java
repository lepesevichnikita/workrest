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
import org.klaster.restapi.service.AdministratorService;
import org.klaster.restapi.service.ApplicationUserService;
import org.klaster.restapi.service.DefaultApplicationUserService;
import org.mockito.Mockito;
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
  public AdministratorService defaultAdministratorService() {
    return Mockito.mock(AdministratorService.class);
  }

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
    return objectMapper;
  }

  @Bean
  public ApplicationUserService defaultApplicationUserService() {
    return Mockito.spy(new DefaultApplicationUserService());
  }

}
