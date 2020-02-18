package org.klaster.domain.model.state.user;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;
import org.klaster.domain.model.context.ApplicationUser;
import org.klaster.domain.model.controller.EmployerProfile;
import org.klaster.domain.model.entity.FreelancerProfile;
import org.klaster.domain.model.state.general.AbstractState;

/**
 * AbstractUserState
 *
 * @author Nikita Lepesevich
 */

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AbstractUserState extends AbstractState<ApplicationUser> {

  @Transient
  public FreelancerProfile getAccessToFreelancerProfile() {
    return null;
  }

  @Transient
  public EmployerProfile getAccessToEmployerProfile() {
    return null;
  }

  public void authorizeUser(LocalDateTime authorizedAt) {
    final String message = String.format("Failed attempt to authorize user #%s at%s", getContext(), authorizedAt);
    logger.warning(message);
  }

  public void createEmployerProfile() {
    final String message = String.format("Failed attempt to create employer profile for user #%s", getContext());
    logger.warning(message);
  }

  public void createFreelancerProfile() {
    final String message = String.format("Failed attempt to create freelancer profile for user #%s", getContext());
    logger.warning(message);
  }

}
