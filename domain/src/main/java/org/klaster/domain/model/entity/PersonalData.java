package org.klaster.domain.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.klaster.domain.constant.PersonalDataState;
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

  @Enumerated(EnumType.STRING)
  @JsonSerialize
  private PersonalDataState state;

  @JsonIgnore
  @OneToOne(optional = false, fetch = FetchType.EAGER)
  @Fetch(FetchMode.SELECT)
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

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public PersonalDataState getState() {
    return state;
  }

  public void setState(PersonalDataState state) {
    this.state = state;
  }
}
