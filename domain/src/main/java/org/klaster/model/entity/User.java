package org.klaster.model.entity;

import java.util.Set;
import org.klaster.model.context.Account;

/**
 * User
 *
 * @author Nikita Lepesevich
 */

public class User {

  private final Account account;
  private FreelancerProfile freelancerProfile;
  private EmployerProfile employerProfile;
  private Set<VerificationMessage> verificationMessages;

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
    return freelancerProfile != null;
  }

  public boolean hasEmployerProfile() {
    return employerProfile != null;
  }

  public Set<VerificationMessage> getVerificationMessages() {
    return verificationMessages;
  }

  public void setVerificationMessages(Set<VerificationMessage> verificationMessages) {
    this.verificationMessages = verificationMessages;
  }

  public void createVerificationRequest(String documentName, String documentNumber, FileInfo fileInfo) {
    verificationMessages.add(new VerificationMessage(this, documentName, documentNumber, fileInfo));
  }
}
