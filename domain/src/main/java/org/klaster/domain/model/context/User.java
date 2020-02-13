package org.klaster.domain.model.context;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Transient;
import org.klaster.domain.model.controller.EmployerProfile;
import org.klaster.domain.model.entity.FreelancerProfile;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.entity.PersonalData;
import org.klaster.domain.model.state.user.AbstractUserState;
import org.klaster.domain.model.state.user.UnverifiedUserState;

/**
 * User
 *
 * @author Nikita Lepesevich
 */

@Entity
public class User extends AbstractContext<AbstractUserState> {

  @OneToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private LoginInfo loginInfo;

  @Transient
  private FreelancerProfile freelancerProfile;

  @Transient
  private EmployerProfile employerProfile;

  @Transient
  private PersonalData personalData;

  public EmployerProfile getEmployerProfile() {
    return employerProfile;
  }

  public void setEmployerProfile(EmployerProfile employerProfile) {
    this.employerProfile = employerProfile;
  }

  public LoginInfo getLoginInfo() {
    return loginInfo;
  }

  public void setLoginInfo(LoginInfo loginInfo) {
    this.loginInfo = loginInfo;
  }

  public FreelancerProfile getFreelancerProfile() {
    return freelancerProfile;
  }

  public void setFreelancerProfile(FreelancerProfile freelancerProfile) {
    this.freelancerProfile = freelancerProfile;
  }

  public boolean hasFreelancerProfile() {
    return freelancerProfile != null;
  }

  public boolean hasEmployerProfile() {
    return employerProfile != null;
  }

  public PersonalData getPersonalData() {
    return personalData;
  }

  public void setPersonalData(PersonalData personalData) {
    this.personalData = personalData;
  }

  public boolean hasPersonalData() {
    return personalData != null;
  }

  @PrePersist
  private void setDefaultState() {
    if (getCurrentState() == null) {
      setCurrentState(getDefaultState());
    }
  }

  private AbstractUserState getDefaultState() {
    AbstractUserState defaultState = new UnverifiedUserState();
    defaultState.setContext(this);
    return defaultState;
  }
}
