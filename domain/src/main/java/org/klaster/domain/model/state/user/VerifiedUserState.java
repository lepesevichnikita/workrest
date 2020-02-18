package org.klaster.domain.model.state.user;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import org.klaster.domain.model.controller.EmployerProfile;
import org.klaster.domain.model.entity.FreelancerProfile;

/**
 * VerifiedUserState
 *
 * @author Nikita Lepesevich
 */

@Entity
public class VerifiedUserState extends AbstractUserState {

  @Override
  public FreelancerProfile getAccessToFreelancerProfile() {
    return getContext().getFreelancerProfile();
  }

  @Override
  public EmployerProfile getAccessToEmployerProfile() {
    return getContext().getEmployerProfile();
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
    final String message = String.format("ApplicationUser#%s was authorized at %s", getContext(), authorizedAt);
    logger.info(message);
  }
}
