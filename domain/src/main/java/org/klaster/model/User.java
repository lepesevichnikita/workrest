package org.klaster.model;

/**
 * User
 *
 * workrest
 *
 * 03.02.2020
 *
 * @author Nikita Lepesevich
 */

public class User {

  private final Account account;
  private FreelancerProfile freelancerProfile;
  private EmployerProfile employerProfile;

  public User(Account account) {
    this.account = account;
  }

  public EmployerProfile getEmployerProfile() {
    return employerProfile;
  }

  public void setEmployerProfile(EmployerProfile employerProfile) {
    this.employerProfile = employerProfile;
  }

  public Account getAccount() {
    return account;
  }

  public FreelancerProfile getFreelancerProfile() {
    return freelancerProfile;
  }

  public void setFreelancerProfile(FreelancerProfile freelancerProfile) {
    this.freelancerProfile = freelancerProfile;
  }

  public boolean hasFreelancerProfile() {
    return freelancerProfile == null;
  }

  public boolean hasEmployerProfile() {
    return employerProfile == null;
  }
}
