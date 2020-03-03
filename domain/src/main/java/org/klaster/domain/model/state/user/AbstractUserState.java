package org.klaster.domain.model.state.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;
import org.klaster.domain.constant.UserAction;
import org.klaster.domain.exception.ActionForbiddenByStateException;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.EmployerProfile;
import org.klaster.domain.model.entity.FreelancerProfile;
import org.klaster.domain.model.entity.PersonalData;
import org.klaster.domain.model.state.general.AbstractState;

/**
 * AbstractUserState
 *
 * @author Nikita Lepesevich
 */

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AbstractUserState extends AbstractState<User> {

  @Transient
  @JsonIgnore
  public FreelancerProfile getFreelancerProfile() {
    throw new ActionForbiddenByStateException(UserAction.ACCESS_TO_FREELANCER_PROFILE, this);
  }

  @Transient
  @JsonIgnore
  public EmployerProfile getEmployerProfile() {
    throw new ActionForbiddenByStateException(UserAction.ACCESS_TO_EMPLOYER_PROFILE, this);
  }

  public void authorizeUser(LocalDateTime authorizedAt) {
    throw new ActionForbiddenByStateException(UserAction.AUTHORIZATION, this);
  }

  public void createEmployerProfile(EmployerProfile targetEmployerProfile) {
    throw new ActionForbiddenByStateException(UserAction.EMPLOYER_PROFILE_CREATING, this);
  }

  public void createFreelancerProfile(FreelancerProfile targetEmployerProfile) {
    throw new ActionForbiddenByStateException(UserAction.FREELANCER_PROFILE_CREATING, this);
  }

  public void updatePersonalData(PersonalData personalData) {
    throw new ActionForbiddenByStateException(UserAction.PERSONAL_DATA_UPDATING, this);
  }

}
