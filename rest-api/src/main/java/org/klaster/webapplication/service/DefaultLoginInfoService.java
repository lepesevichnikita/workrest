package org.klaster.webapplication.service;

/*
 * workrest
 *
 * 25.02.2020
 *
 */

import java.util.UUID;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.webapplication.repository.LoginInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * DefaultLoginInfoService
 *
 * @author Nikita Lepesevich
 */

@Service
public class DefaultLoginInfoService implements LoginInfoService {

  @Autowired
  private LoginInfoRepository loginInfoRepository;

  @Override
  public LoginInfo findFirstByLogin(String login) {
    return loginInfoRepository.findFirstByLogin(login)
                              .orElse(null);
  }

  @Override
  public LoginInfo findFirstByLoginAndPassword(String login, String password) {
    return loginInfoRepository.findFirstByLoginAndPassword(login, password)
                              .orElse(null);
  }

  @Override
  public LoginInfo findFirstByToken(UUID token) {
    return loginInfoRepository.findFirstByToken(token)
                              .orElse(null);
  }

  @Override
  public LoginInfo save(LoginInfo loginInfo) {
    return loginInfoRepository.save(loginInfo);
  }
}
