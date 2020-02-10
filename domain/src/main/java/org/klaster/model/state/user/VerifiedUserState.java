package org.klaster.model.state.user;

import java.time.LocalDateTime;
import org.klaster.model.context.User;
import org.klaster.model.entity.EmployerProfile;
import org.klaster.model.entity.FreelancerProfile;
import org.klaster.model.state.general.AbstractUserState;

/**
 * VerifiedUserState
 *
 * @author Nikita Lepesevich
 */

public class VerifiedUserState extends AbstractUserState {

  public VerifiedUserState(User context) {
    super(context);
  }

  @Override
  public void blockUser() {
    final String message = String.format("User #%s was blocked", getContext());
    logger.info(message);
    getContext().setCurrentState(new org.klaster.model.state.user.BlockedUserState(getContext()));
  }

  @Override
  public void deleteUser() {
    final String message = String.format("User #%s was deleted", getContext());
    logger.info(message);
    getContext().setCurrentState(new org.klaster.model.state.user.DeletedUserState(getContext()));
  }

  @Override
  public void createEmployerProfile() {
    if (!getContext().hasEmployerProfile()) {
      EmployerProfile employerProfile = new EmployerProfile(getContext());
      getContext().setEmployerProfile(employerProfile);
      final String message = String.format("Employer profile #%s was created for user #%s", employerProfile, getContext());
      logger.info(message);
    }
  }

  @Override
  public void createFreelancerProfile() {
    if (!getContext().hasFreelancerProfile()) {
      FreelancerProfile freelancerProfile = new FreelancerProfile(getContext());
      getContext().setFreelancerProfile(freelancerProfile);
      final String message = String.format("Freelancer profile #%s was created for user #%s", freelancerProfile, getContext());
      logger.info(message);
    }
  }

  @Override
  public void authorizeUser(LocalDateTime authorizedAt) {
    getContext().getLoginInfo()
                .setLastAuthorizedAt(authorizedAt);
    final String message = String.format("User#%s was authorized at %s", getContext(), authorizedAt);
    logger.info(message);
  }
}
