package org.klaster.domain.model.state.user;

import java.time.LocalDateTime;
import javax.persistence.Entity;

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
    final String message = String.format("ApplicationUser#%s was authorized at %s", getContext(), authorizedAt);
    logger.info(message);
  }
}
