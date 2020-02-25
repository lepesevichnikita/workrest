package org.klaster.webapplication.configuration;

/*
 * workrest
 *
 * 24.02.2020
 *
 */

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * MvcConfig
 *
 * @author Nikita Lepesevich
 */

@Configuration
@EnableWebMvc
@ComponentScan(value = {"org.klaster.domain", "org.klaster.webapplication"}, excludeFilters = {
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = TestContext.class)
})
public class ApplicationContext implements WebMvcConfigurer {

  @Bean
  public MappingJackson2HttpMessageConverter converter() {
    return new MappingJackson2HttpMessageConverter();
  }


  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    converters.add(converter());
  }
}
