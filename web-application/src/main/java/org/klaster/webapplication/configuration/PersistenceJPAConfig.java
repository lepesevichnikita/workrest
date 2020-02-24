package org.klaster.webapplication.configuration;

/*
 * workrest
 *
 * 24.02.2020
 *
 */

import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * PersistenceJPAConfig
 *
 * @author Nikita Lepesevich
 */

@Configuration
@EnableTransactionManagement
@ComponentScan({"org.klaster.domain", "org.klaster.webapplication"})
@EnableJpaRepositories(basePackages = {"org.klaster.webapplication.repository"})
@PropertySource(value = {"classpath:application.properties"})
public class PersistenceJPAConfig {

  @Autowired
  private Environment environment;

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    LocalContainerEntityManagerFactoryBean lcemfb = new LocalContainerEntityManagerFactoryBean();
    lcemfb.setJpaVendorAdapter(jpaVendorAdapter());
    lcemfb.setDataSource(dataSource());
    lcemfb.setPersistenceUnitName("myJpaPersistenceUnit");
    lcemfb.setPackagesToScan("org.klaster.domain");
    lcemfb.setJpaProperties(hibernateProperties());
    return lcemfb;
  }

  @Bean
  public JpaVendorAdapter jpaVendorAdapter() {
    return new HibernateJpaVendorAdapter();
  }

  @Bean
  public PlatformTransactionManager transactionManager() {
    return new JpaTransactionManager(entityManagerFactory().getObject());
  }

  @Bean
  public DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driver"));
    dataSource.setUrl(environment.getRequiredProperty("jdbc.url"));
    dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
    dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));
    return dataSource;
  }

  private Properties hibernateProperties() {
    Properties properties = new Properties();
    properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
    properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
    properties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));
    properties.put("hibernate.hbm2ddl.auto", environment.getRequiredProperty("hibernate.hbm2ddl.auto"));
    return properties;
  }

}
