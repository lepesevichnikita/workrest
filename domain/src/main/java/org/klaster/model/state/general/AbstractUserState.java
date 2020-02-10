package org.klaster.model.state.general;

import java.time.LocalDateTime;
import org.klaster.model.context.User;
import org.klaster.model.state.user.UserState;

/**
 * AbstractUserState
 *
 * @author Nikita Lepesevich
 */

public abstract class AbstractUserState extends AbstractState<User> implements UserState {

  public AbstractUserState(User context) {
    super(context);
  }

  @Override
  public void unblockUser() {
    final String message = String.format("Failed attempt to unblock user #%s", getContext());
    logger.warning(message);
  }

  @Override
  public void blockUser() {
    final String message = String.format("Failed attempt to block user #%s", getContext());
    logger.warning(message);
  }

  @Override
  public void verifyUser() {
    final String message = String.format("Failed attempt to verify user #%s", getContext());
    logger.warning(message);
  }

  @Override
  public void authorizeUser(LocalDateTime authorizedAt) {
    final String message = String.format("Failed attempt to authorize user #%s at%s", getContext(), authorizedAt);
    logger.warning(message);
  }

  @Override
  public void deleteUser() {
    final String message = String.format("Failed attempt to delete user #%s", getContext());
    logger.warning(message);
  }

  @Override
  public void createEmployerProfile() {
    final String message = String.format("Failed attempt to create employer profile for user #%s", getContext());
    logger.warning(message);
  }

  @Override
  public void createFreelancerProfile() {
    final String message = String.format("Failed attempt to create freelancer profile for user #%s", getContext());
    logger.warning(message);
  }

}
