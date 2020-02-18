package org.klaster.webapplication.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.klaster.domain.model.entity.LoginInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
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
public class LoginInfoRepositoryTest extends AbstractTestNGSpringContextTests {

  private static final String DEFAULT_NEW_LOGIN = "NEW LOGIN";
  private static final String DEFAULT_NEW_PASSWORD_HASH = DEFAULT_NEW_LOGIN;

  @Autowired
  private LoginInfoRepository loginInfoRepository;

  @BeforeMethod
  private void initialize() {
    loginInfoRepository.deleteAll();
  }

  @Test
  public void createsUniqueLoginInfo() {
    LoginInfo newLoginInfo = new LoginInfo();
    newLoginInfo.setLogin(DEFAULT_NEW_LOGIN);
    newLoginInfo.setPasswordHash(DEFAULT_NEW_PASSWORD_HASH);
    loginInfoRepository.saveAndFlush(newLoginInfo);
    assertThat(loginInfoRepository.exists(Example.of(newLoginInfo)), is(true));
  }

  @Test
  public void isEmptyAtStart() {
    assertThat(loginInfoRepository.count(), equalTo(0));
  }
}