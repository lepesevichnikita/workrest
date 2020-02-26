package org.klaster.restapi.service;

/*
 * workrest
 *
 * 25.02.2020
 *
 */

import org.klaster.domain.model.entity.LoginInfo;

/**
 * LoginInfoService
 *
 * @author Nikita Lepesevich
 */

public interface LoginInfoService {

  LoginInfo findFirstByLogin(String login);

  LoginInfo findFirstByLoginAndPassword(String login, String password);

  LoginInfo save(LoginInfo loginInfo);
}
