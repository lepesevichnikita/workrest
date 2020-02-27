package org.klaster.domain.model.context;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Transient;
import org.klaster.domain.model.controller.EmployerProfile;
import org.klaster.domain.model.entity.FreelancerProfile;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.entity.PersonalData;
import org.klaster.domain.model.entity.Role;
import org.klaster.domain.model.state.user.AbstractUserState;
import org.klaster.domain.model.state.user.BlockedUserState;
import org.klaster.domain.model.state.user.DeletedUserState;
import org.klaster.domain.model.state.user.UnverifiedUserState;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * ApplicationUser
 *
 * @author Nikita Lepesevich
 */

@Entity
public class ApplicationUser extends AbstractContext<AbstractUserState> implements UserDetails {

  @OneToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private LoginInfo loginInfo;

  @JsonManagedReference
  @ManyToMany(fetch = FetchType.EAGER)
  private Set<Role> roles;

  @JsonIgnore
  @Transient
  private FreelancerProfile freelancerProfile;

  @JsonIgnore
  @Transient
  private EmployerProfile employerProfile;

  @JsonIgnore
  @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
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
    return new UnverifiedUserState();
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  @Override
  @Transient
  @JsonIgnore
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles
        .stream()
        .map(Role::getName)
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toSet());
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
