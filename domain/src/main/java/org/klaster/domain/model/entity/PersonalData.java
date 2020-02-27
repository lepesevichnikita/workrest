package org.klaster.domain.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import org.klaster.domain.model.context.ApplicationUser;

/**
 * PersonalData
 *
 * @author Nikita Lepesevich
 */

@Entity
public class PersonalData {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @JsonIgnore
  @Column(nullable = false)
  private String documentNumber;

  @JsonIgnore
  @Column(nullable = false)
  private String documentName;

  @Column(nullable = false)
  private String firstName;

  @Column(nullable = false)
  private String lastName;

  @JsonIgnore
  @OneToOne(fetch = FetchType.EAGER)
  private FileInfo documentScan;

  @JsonIgnore
  @OneToOne(optional = false, fetch = FetchType.EAGER)
  private ApplicationUser user;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

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

  public FileInfo getDocumentScan() {
    return documentScan;
  }

  public void setDocumentScan(FileInfo documentScan) {
    this.documentScan = documentScan;
  }

  public ApplicationUser getUser() {
    return user;
  }

  public void setUser(ApplicationUser applicationUser) {
    this.user = applicationUser;
  }
}
