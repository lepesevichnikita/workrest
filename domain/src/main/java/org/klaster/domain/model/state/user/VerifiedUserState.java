package org.klaster.domain.model.state.user;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import org.klaster.domain.constant.UserStateName;
import org.klaster.domain.model.entity.EmployerProfile;
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
  public void createEmployerProfile(EmployerProfile targetEmployerProfile) {
    if (!getContext().hasEmployerProfile()) {
      targetEmployerProfile.setOwner(getContext());
      getContext().setEmployerProfile(targetEmployerProfile);
    }
  }

  @Override
  public void createFreelancerProfile(FreelancerProfile targetFreelancerProfile) {
    if (!getContext().hasFreelancerProfile()) {
      targetFreelancerProfile.setOwner(getContext());
      getContext().setFreelancerProfile(targetFreelancerProfile);
    }
  }

  @Override
  public void authorizeUser(LocalDateTime authorizedAt) {
    getContext().getLoginInfo()
                .setLastAuthorizedAt(authorizedAt);
  }
}
