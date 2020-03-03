package org.klaster.domain.model.state.user;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import org.klaster.domain.constant.UserStateName;
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
  public String getName() {
    return UserStateName.VERIFIED;
  }

  @Override
  public FreelancerProfile getFreelancerProfile() {
    return getContext().getFreelancerProfile();
  }

  @Override
  public EmployerProfile getEmployerProfile() {
    return getContext().getEmployerProfile();
  }

  @Override
  public void createEmployerProfile() {
    if (!getContext().hasEmployerProfile()) {
      EmployerProfile employerProfile = new EmployerProfile();
      employerProfile.setOwner(getContext());
      getContext().setEmployerProfile(employerProfile);
    }
  }

  @Override
  public void createFreelancerProfile() {
    if (!getContext().hasFreelancerProfile()) {
      FreelancerProfile freelancerProfile = new FreelancerProfile();
      freelancerProfile.setOwner(getContext());
      getContext().setFreelancerProfile(freelancerProfile);
    }
  }

  @Override
  public void authorizeUser(LocalDateTime authorizedAt) {
    getContext().getLoginInfo()
                .setLastAuthorizedAt(authorizedAt);
  }
}
