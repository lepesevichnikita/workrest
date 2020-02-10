package org.klaster.model.state.user;

import java.time.LocalDateTime;
import org.klaster.model.context.User;
import org.klaster.model.state.general.State;

/**
 * UserState
 *
 * @author Nikita Lepesevich
 */

public interface UserState extends State<User> {

  void blockUser();

  void deleteUser();

  void unblockUser();

  void verifyUser();

  void authorizeUser(LocalDateTime authorizedAt);

  void createEmployerProfile();

  void createFreelancerProfile();
}
