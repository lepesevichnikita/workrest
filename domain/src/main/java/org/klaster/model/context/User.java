package org.klaster.model.context;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Transient;
import org.klaster.model.controller.EmployerProfile;
import org.klaster.model.entity.FreelancerProfile;
import org.klaster.model.entity.LoginInfo;
import org.klaster.model.entity.PersonalData;
import org.klaster.model.state.user.AbstractUserState;
import org.klaster.model.state.user.UnverifiedUserState;

/**
 * User
 *
 * @author Nikita Lepesevich
 */

@Entity
public class User extends AbstractContext<AbstractUserState> {

  @Id
  private long id;

  @OneToOne(optional = false, fetch = FetchType.EAGER)
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

  public void setLoginInfo(LoginInfo loginInfo) {
    this.loginInfo = loginInfo;
  }
}
