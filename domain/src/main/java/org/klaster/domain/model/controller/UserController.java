package org.klaster.domain.model.controller;/*
 * practice
 *
 * 11.02.2020
 *
 */

import org.klaster.domain.model.context.ApplicationUser;

/**
 * UserController
 *
 * @author Nikita Lepesevich
 */

public interface UserController extends ContextController<ApplicationUser> {

  default void deleteUser(ApplicationUser applicationUser) {
  }

  default void blockUser(ApplicationUser applicationUser) {
  }

  default void unblockUser(ApplicationUser applicationUser) {
  }

  default void verifyUser(ApplicationUser applicationUser) {
  }

}
