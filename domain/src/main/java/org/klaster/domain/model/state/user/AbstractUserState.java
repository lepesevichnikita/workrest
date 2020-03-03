package org.klaster.domain.model.state.user;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.controller.EmployerProfile;
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
  public FreelancerProfile getFreelancerProfile() {
    return null;
  }

  @Transient
  public EmployerProfile getEmployerProfile() {
    return null;
  }

  public void authorizeUser(LocalDateTime authorizedAt) {
  }

  public void createEmployerProfile() {
  }

  public void createFreelancerProfile() {
  }

  public void updatePersonalData(PersonalData personalData) {
  }

}
