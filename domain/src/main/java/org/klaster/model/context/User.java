package org.klaster.model.context;

import java.util.Set;
import org.klaster.model.entity.EmployerProfile;
import org.klaster.model.entity.FileInfo;
import org.klaster.model.entity.FreelancerProfile;
import org.klaster.model.entity.VerificationMessage;
import org.klaster.model.state.UserState;

/**
 * User
 *
 * @author Nikita Lepesevich
 */

public class User extends AbstractContext<UserState> {

  private final LogInfo logInfo;
  private FreelancerProfile freelancerProfile;
  private EmployerProfile employerProfile;
  private Set<VerificationMessage> verificationMessages;

  public User(LogInfo logInfo) {
    this.logInfo = logInfo;
  }

  public EmployerProfile getEmployerProfile() {
    return employerProfile;
  }

  public void setEmployerProfile(EmployerProfile employerProfile) {
    this.employerProfile = employerProfile;
  }

  public LogInfo getLogInfo() {
    return logInfo;
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
