package org.klaster.webapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan("org.klaster.domain")
@ComponentScan({"org.klaster.domain", "org.klaster.webapplication.*"})
public class WebApplication {

  public static void main(String[] args) {
    SpringApplication.run(WebApplication.class);
  }

}
