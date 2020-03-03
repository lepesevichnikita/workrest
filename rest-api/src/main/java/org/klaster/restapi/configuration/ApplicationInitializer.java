package org.klaster.restapi.configuration;

/*
 * workrest
 *
 * 24.02.2020
 *
 */

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * ApplicationInitializer
 *
 * @author Nikita Lepesevich
 */

public class ApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

  @Override
  protected Class<?>[] getRootConfigClasses() {
    return new Class[]{PersistenceJPAConfig.class, SecurityConfiguration.class, ApplicationContext.class};
  }

  @Override
  protected Class<?>[] getServletConfigClasses() {
    return new Class[]{};
  }

  @Override
  protected String[] getServletMappings() {
    return new String[]{"/"};
  }
}
