package org.klaster.domain.model.state.user;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import org.klaster.domain.constant.UserStateName;
import org.klaster.domain.model.entity.PersonalData;

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
  public void authorizeUser(LocalDateTime authorizedAt) {
    getContext().getLoginInfo()
                .setLastAuthorizedAt(authorizedAt);
  }

  @Override
  public void updatePersonalData(PersonalData personalData) {
    personalData.setUser(getContext());
    getContext().setPersonalData(personalData);
  }
}
