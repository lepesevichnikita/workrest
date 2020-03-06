package org.klaster.domain.model.state.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import org.klaster.domain.model.entity.Token;
import org.klaster.domain.model.state.general.AbstractState;

/**
 * AbstractUserState
 *
 * @author Nikita Lepesevich
 */

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
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

  public void authenticateUser(Token token) {
    throw new ActionForbiddenByStateException(UserAction.AUTHORIZATION, this);
  }

  public void updateEmployer(EmployerProfile targetEmployerProfile) {
    throw new ActionForbiddenByStateException(UserAction.EMPLOYER_PROFILE_UPDATING, this);
  }

  public void updateFreelancerProfile(FreelancerProfile targetEmployerProfile) {
    throw new ActionForbiddenByStateException(UserAction.FREELANCER_PROFILE_UPDATING, this);
  }

  public void updatePersonalData(PersonalData personalData) {
    throw new ActionForbiddenByStateException(UserAction.PERSONAL_DATA_UPDATING, this);
  }

  protected void successfulAuthenticateUser(Token token) {
    getContext().getLoginInfo()
                .addToken(token);
  }

}
