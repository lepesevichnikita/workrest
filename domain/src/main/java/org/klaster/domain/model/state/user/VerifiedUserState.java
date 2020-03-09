package org.klaster.domain.model.state.user;

import javax.persistence.Entity;
import org.klaster.domain.constant.UserStateName;
import org.klaster.domain.model.entity.EmployerProfile;
import org.klaster.domain.model.entity.FreelancerProfile;
import org.klaster.domain.model.entity.Token;

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
  public void updateEmployer(EmployerProfile targetEmployerProfile) {
    EmployerProfile employerProfile = getEmployerProfile();
    if (null == employerProfile) {
      targetEmployerProfile.setOwner(getContext());
      getContext().setEmployerProfile(targetEmployerProfile);
    } else {
      employerProfile.setDescription(targetEmployerProfile.getDescription());
    }
  }

  @Override
  public void updateFreelancerProfile(FreelancerProfile targetFreelancerProfile) {
    FreelancerProfile freelancerProfile = getFreelancerProfile();
    if (null == freelancerProfile) {
      targetFreelancerProfile.setOwner(getContext());
      getContext().setFreelancerProfile(targetFreelancerProfile);
    } else {
      freelancerProfile.setSkills(targetFreelancerProfile.getSkills());
      freelancerProfile.setDescription(targetFreelancerProfile.getDescription());
    }
  }

  @Override
  public void authenticateUser(Token token) {
    this.successfulAuthenticateUser(token);
  }
}
