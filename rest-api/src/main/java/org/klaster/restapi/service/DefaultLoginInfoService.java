package org.klaster.restapi.service;

/*
 * workrest
 *
 * 25.02.2020
 *
 */

import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.repository.LoginInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * DefaultLoginInfoService
 *
 * @author Nikita Lepesevich
 */

@Service
public class DefaultLoginInfoService {

  @Autowired
  private LoginInfoRepository loginInfoRepository;

  public LoginInfo findFirstByLogin(String login) {
    return loginInfoRepository.findFirstByLogin(login);
  }

  public LoginInfo findFirstByLoginAndPassword(String login, String password) {
    return loginInfoRepository.findFirstByLoginAndPassword(login, password)
                              .orElse(null);
  }
}
