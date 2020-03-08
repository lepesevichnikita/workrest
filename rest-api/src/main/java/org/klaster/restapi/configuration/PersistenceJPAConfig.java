package org.klaster.restapi.configuration;

/*
 * workrest
 *
 * 24.02.2020
 *
 */

import javax.persistence.EntityManagerFactory;
import org.klaster.restapi.constant.PackageName;
import org.klaster.restapi.constant.PropertyClassPath;
import org.klaster.restapi.properties.DataSourceProperties;
import org.klaster.restapi.properties.HibernateProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
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
@ComponentScan(basePackages = {PackageName.DOMAIN_REPOSITORY, PackageName.REST_API_PROPERTIES, PackageName.REST_API_SERVICE})
@EnableJpaRepositories(basePackages = {PackageName.DOMAIN_REPOSITORY})
@EnableTransactionManagement
@PropertySource(value = {PropertyClassPath.APPLICATION})
public class PersistenceJPAConfig {

  public static final String JPA_PERSISTENCE_UNIT_NAME = "myJpaPersistenceUnit";

  @Bean
  @Autowired
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(DriverManagerDataSource driverManagerDataSource,
                                                                     HibernateProperties hibernateProperties) {
    LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
    entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter());
    entityManagerFactoryBean.setDataSource(driverManagerDataSource);
    entityManagerFactoryBean.setPersistenceUnitName(JPA_PERSISTENCE_UNIT_NAME);
    entityManagerFactoryBean.setPackagesToScan(PackageName.DOMAIN, PackageName.REST_API);
    entityManagerFactoryBean.setJpaProperties(hibernateProperties);
    return entityManagerFactoryBean;
  }

  @Bean
  public JpaVendorAdapter jpaVendorAdapter() {
    return new HibernateJpaVendorAdapter();
  }

  @Bean
  @Autowired
  public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
    return new JpaTransactionManager(entityManagerFactory);
  }

  @Bean
  @Autowired
  public DriverManagerDataSource driverManagerDataSource(DataSourceProperties dataSourceProperties) {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
    dataSource.setUrl(dataSourceProperties.getUrl());
    dataSource.setUsername(dataSourceProperties.getUsername());
    dataSource.setPassword(dataSourceProperties.getPassword());
    return dataSource;
  }
}
