package org.klaster.domain.model.context;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import org.klaster.domain.model.entity.AbstractProfile;
import org.klaster.domain.model.entity.EmployerProfile;
import org.klaster.domain.model.entity.FreelancerProfile;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.entity.PersonalData;
import org.klaster.domain.model.entity.UserAuthority;
import org.klaster.domain.model.state.user.AbstractUserState;
import org.klaster.domain.model.state.user.BlockedUserState;
import org.klaster.domain.model.state.user.DeletedUserState;
import org.klaster.domain.model.state.user.UnverifiedUserState;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * User
 *
 * @author Nikita Lepesevich
 */

@Entity
public class User extends AbstractContext<AbstractUserState> implements UserDetails {

  @OneToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
  private LoginInfo loginInfo;

  @JsonManagedReference
  @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
  private Set<UserAuthority> authorities;

  @JsonManagedReference
  @OneToOne(mappedBy = "owner", orphanRemoval = true, cascade = {CascadeType.MERGE})
  private AbstractProfile freelancerProfile;

  @JsonManagedReference
  @OneToOne(mappedBy = "owner", orphanRemoval = true, cascade = {CascadeType.MERGE})
  private AbstractProfile employerProfile;

  @JsonIgnore
  @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
  private PersonalData personalData;

  public EmployerProfile getEmployerProfile() {
    return (EmployerProfile) employerProfile;
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
    return (FreelancerProfile) freelancerProfile;
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

  @Override
  @Transient
  @JsonIgnore
  protected AbstractUserState getDefaultState() {
    return new UnverifiedUserState();
  }

  @Override
  @Transient
  @JsonIgnore
  public Set<UserAuthority> getAuthorities() {
    return authorities;
  }

  public void setAuthorities(Set<UserAuthority> authorities) {
    this.authorities = authorities;
  }

  @Override
  @Transient
  @JsonIgnore
  public String getPassword() {
    return loginInfo.getPassword();
  }

  @Override
  @Transient
  @JsonIgnore
  public String getUsername() {
    return loginInfo.getLogin();
  }

  @Override
  @Transient
  @JsonIgnore
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  @Transient
  @JsonIgnore
  public boolean isAccountNonLocked() {
    return !(getCurrentState() instanceof BlockedUserState);
  }

  @Override
  @Transient
  @JsonIgnore
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  @Transient
  @JsonIgnore
  public boolean isEnabled() {
    return !(getCurrentState() instanceof DeletedUserState);
  }
}
