package org.klaster.webapplication.configuration;

/*
 * workrest
 *
 * 24.02.2020
 *
 */

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * MvcConfig
 *
 * @author Nikita Lepesevich
 */

@Configuration
@EnableWebMvc
@ComponentScan({"org.klaster.domain", "org.klaster.webapplication"})
public class ApplicationContext implements WebMvcConfigurer {

}
