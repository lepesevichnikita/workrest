package org.klaster.restapi.configuration;

/*
 * workrest
 *
 * 24.02.2020
 *
 */

import java.util.Properties;
import javax.sql.DataSource;
import org.klaster.restapi.constant.HibernatePropertyKey;
import org.klaster.restapi.constant.JdbcPropertyKey;
import org.klaster.restapi.constant.PackageName;
import org.klaster.restapi.constant.PropertyClassPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
@EnableJpaRepositories(basePackages = {PackageName.DOMAIN_REPOSITORY})
@PropertySource(value = {PropertyClassPath.APPLICATION})
public class PersistenceJPAConfig {

  public static final String JPA_PERSISTENCE_UNIT_NAME = "myJpaPersistenceUnit";
  @Autowired
  private Environment environment;

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
    entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter());
    entityManagerFactoryBean.setDataSource(dataSource());
    entityManagerFactoryBean.setPersistenceUnitName(JPA_PERSISTENCE_UNIT_NAME);
    entityManagerFactoryBean.setPackagesToScan(PackageName.DOMAIN);
    entityManagerFactoryBean.setJpaProperties(hibernateProperties());
    return entityManagerFactoryBean;
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
    dataSource.setDriverClassName(environment.getRequiredProperty(JdbcPropertyKey.JDBC_DRIVER));
    dataSource.setUrl(environment.getRequiredProperty(JdbcPropertyKey.JDBC_URL));
    dataSource.setUsername(environment.getRequiredProperty(JdbcPropertyKey.JDBC_USERNAME));
    dataSource.setPassword(environment.getRequiredProperty(JdbcPropertyKey.JDBC_PASSWORD));
    return dataSource;
  }

  private Properties hibernateProperties() {
    Properties properties = new Properties();
    properties.put(HibernatePropertyKey.HIBERNATE_DIALECT, environment.getRequiredProperty(HibernatePropertyKey.HIBERNATE_DIALECT));
    properties.put(HibernatePropertyKey.HIBERNATE_SHOW_SQL, environment.getRequiredProperty(HibernatePropertyKey.HIBERNATE_SHOW_SQL));
    properties.put(HibernatePropertyKey.HIBERNATE_FORMAT_SQL, environment.getRequiredProperty(HibernatePropertyKey.HIBERNATE_FORMAT_SQL));
    properties.put(HibernatePropertyKey.HIBERNATE_HBM_2_DDL_AUTO, environment.getRequiredProperty(HibernatePropertyKey.HIBERNATE_HBM_2_DDL_AUTO));
    return properties;
  }

}
