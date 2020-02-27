package org.klaster.domain.model.state.user;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import org.klaster.domain.model.entity.PersonalData;

/**
 * UnverifiedUserState
 *
 * @author Nikita Lepesevich
 */

@Entity
public class UnverifiedUserState extends AbstractUserState {

  @Override
  public void authorizeUser(LocalDateTime authorizedAt) {
    getContext().getLoginInfo()
                .setLastAuthorizedAt(authorizedAt);
    final String message = String.format("User#%s was authorized at %s", getContext(), authorizedAt);
    logger.info(message);
  }

  @Override
  public void updatePersonalData(PersonalData personalData) {
    final String message = String.format("Failed attempt to update personal data for user #%s", getContext());
    personalData.setUser(getContext());
    getContext().setPersonalData(personalData);
  }
}
