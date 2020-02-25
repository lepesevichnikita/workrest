package org.klaster.webapplication.service;

/*
 * workrest
 *
 * 25.02.2020
 *
 */

import java.util.UUID;
import org.klaster.domain.model.entity.LoginInfo;

/**
 * LoginInfoService
 *
 * @author Nikita Lepesevich
 */

public interface LoginInfoService {

  LoginInfo findFirstByLogin(String login);

  LoginInfo findFirstByLoginAndPassword(String login, String password);

  LoginInfo findFirstByToken(UUID token);

  LoginInfo save(LoginInfo loginInfo);
}
