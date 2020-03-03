package org.klaster.domain.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import org.klaster.domain.model.context.User;

/**
 * PersonalData
 *
 * @author Nikita Lepesevich
 */

@Entity
public class PersonalData extends Attachable {
  @JsonIgnore
  @NotNull
  @Column(nullable = false)
  private String documentNumber;

  @JsonIgnore
  @NotNull
  @Column(nullable = false)
  private String documentName;

  @Column(nullable = false)
  @NotNull
  private String firstName;

  @Column(nullable = false)
  @NotNull
  private String lastName;

  @JsonIgnore
  @OneToOne(optional = false, fetch = FetchType.EAGER)
  private User user;

  public String getDocumentNumber() {
    return documentNumber;
  }

  public void setDocumentNumber(String documentNumber) {
    this.documentNumber = documentNumber;
  }

  public String getDocumentName() {
    return documentName;
  }

  public void setDocumentName(String documentName) {
    this.documentName = documentName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Transient
  public FileInfo getDocumentScan() {
    return getAttachment();
  }

  @Transient
  public void setDocumentScan(FileInfo documentScan) {
    setAttachment(documentScan);
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
