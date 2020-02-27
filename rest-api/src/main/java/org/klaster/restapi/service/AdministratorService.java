package org.klaster.restapi.service;/*
 * org.klaster.restapi.service
 *
 * workrest
 *
 * 2/20/20
 *
 * Copyright(c) Nikita Lepesevich
 */

import java.util.List;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.LoginInfo;

public interface AdministratorService {

  User registerAdministrator(LoginInfo loginInfo);

  List<User> findAll();

  User findById(long id);

  User deleteById(long id);
}
