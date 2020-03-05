package org.klaster.domain.model.state.user;

import javax.persistence.Entity;
import org.klaster.domain.constant.UserStateName;
import org.klaster.domain.model.entity.PersonalData;
import org.klaster.domain.model.entity.Token;

/**
 * UnverifiedUserState
 *
 * @author Nikita Lepesevich
 */

@Entity
public class UnverifiedUserState extends AbstractUserState {

  @Override
  public String getName() {
    return UserStateName.UNVERIFIED;
  }

  @Override
  public void authenticateUser(Token token) {
    this.successfulAuthenticateUser(token);
  }

  @Override
  public void updatePersonalData(PersonalData personalData) {
    personalData.setUser(getContext());
    getContext().setPersonalData(personalData);
  }
}
