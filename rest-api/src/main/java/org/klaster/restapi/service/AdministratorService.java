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
import org.klaster.domain.model.context.ApplicationUser;
import org.klaster.domain.model.entity.LoginInfo;

public interface AdministratorService {

  ApplicationUser registerAdministrator(LoginInfo loginInfo);

  List<ApplicationUser> findAll();

  ApplicationUser findById(long id);

  ApplicationUser deleteById(long id);
}
