package org.klaster.model.state;

import org.klaster.model.context.User;

/**
 * UserState
 *
 * workrest
 *
 * 03.02.2020
 *
 * @author Nikita Lepesevich
 */

public interface UserState extends State<User> {

  void blockUser();

  void deleteUser();

  void unblockUser();

  void verifyUser();

  void authorizeUser();
}
