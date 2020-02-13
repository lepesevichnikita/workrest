package org.klaster.webapplication.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.klaster.model.entity.LoginInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;


/*
 * workrest
 *
 * 13.02.2020
 *
 */

/**
 * LoginInfoRepositoryTest
 *
 * @author Nikita Lepesevich
 */

@SpringBootTest
@EnableJpaRepositories
public class LoginInfoRepositoryTest extends AbstractTestNGSpringContextTests {

  private static final String NEW_LOGIN = "NEW LOGIN";
  private static final String NEW_PASSWORD_HASH = "NEW PASSWORD";

  @Autowired
  private LoginInfoRepository loginInfoRepository;

  @Test
  public void createsLoginInfo() {
    LoginInfo newLoginInfo = new LoginInfo();
    newLoginInfo.setLogin(NEW_LOGIN);
    newLoginInfo.setPasswordHash(NEW_PASSWORD_HASH);
    loginInfoRepository.save(newLoginInfo);
    assertThat(loginInfoRepository.findById(newLoginInfo.getId()), equalTo(newLoginInfo));
  }
}