package org.klaster.model.controller;/*
 * practice
 *
 * 11.02.2020
 *
 */

import org.klaster.model.context.User;

/**
 * UserController
 *
 * @author Nikita Lepesevich
 */

public interface UserController extends ContextController<User> {

  default void deleteUser(User user) {
  }

  default void blockUser(User user) {
  }

  default void unblockUser(User user) {
  }

  default void verifyUser(User user) {
  }

}
