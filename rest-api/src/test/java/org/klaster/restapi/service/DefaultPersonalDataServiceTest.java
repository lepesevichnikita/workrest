package org.klaster.restapi.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.notNullValue;

import org.klaster.domain.constant.PersonalDataState;
import org.klaster.domain.constant.UserStateName;
import org.klaster.domain.exception.ActionForbiddenByStateException;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.FileInfo;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.entity.PersonalData;
import org.klaster.domain.repository.UserRepository;
import org.klaster.restapi.configuration.ApplicationContext;
import org.klaster.restapi.factory.RandomLoginInfoFactory;
import org.klaster.restapi.factory.RandomPersonalDataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/*
 * workrest
 *
 * 05.03.2020
 *
 */

/**
 * DefaultPersonalDataServiceTest
 *
 * @author Nikita Lepesevich
 */

@WebAppConfiguration
@ContextConfiguration(classes = {ApplicationContext.class})
public class DefaultPersonalDataServiceTest extends AbstractTestNGSpringContextTests {

  private LoginInfo randomLoginInfo;
  private PersonalData randomPersonalData;

  private RandomLoginInfoFactory randomLoginInfoFactory;
  private RandomPersonalDataFactory randomPersonalDataFactory;

  @Autowired
  private DefaultUserService defaultUserService;

  @Autowired
  private DefaultPersonalDataService defaultPersonalDataService;

  @Autowired
  private UserRepository userRepository;

  @BeforeClass
  public void setup() {
    randomLoginInfoFactory = RandomLoginInfoFactory.getInstance();
    randomPersonalDataFactory = RandomPersonalDataFactory.getInstance();
  }

  @BeforeMethod
  public void initialize() {
    randomLoginInfo = randomLoginInfoFactory.build();
    randomPersonalData = randomPersonalDataFactory.build();
  }

  @Test
  public void updatesUnverifiedUser() {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    PersonalData updatedPersonalData = defaultPersonalDataService.updateByUserId(registeredUser.getId(), randomPersonalData);
    FileInfo expectedAttachment = randomPersonalData.getAttachment();
    assertThat(updatedPersonalData, allOf(
        hasProperty("id", notNullValue()),
        hasProperty("firstName", equalTo(randomPersonalData.getFirstName())),
        hasProperty("lastName", equalTo(randomPersonalData.getLastName())),
        hasProperty("documentName", equalTo(randomPersonalData.getDocumentName())),
        hasProperty("documentNumber", equalTo(randomPersonalData.getDocumentNumber())),
        hasProperty("attachment", allOf(
            hasProperty("id", notNullValue()),
            hasProperty("path", equalTo(expectedAttachment.getPath())),
            hasProperty("path", equalTo(expectedAttachment.getPath()))
        ))
    ));
  }


  @Test(expectedExceptions = ActionForbiddenByStateException.class)
  public void throwsExceptionAtPersonalDataUpdateForBlockedUser() {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    defaultUserService.blockById(registeredUser.getId());
    defaultPersonalDataService.updateByUserId(registeredUser.getId(), randomPersonalData);
  }

  @Test(expectedExceptions = ActionForbiddenByStateException.class)
  public void throwsExceptionAtPersonalDataUpdateForDeletedUser() {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    defaultUserService.deleteById(registeredUser.getId());
    defaultPersonalDataService.updateByUserId(registeredUser.getId(), randomPersonalData);
  }


  @Test(expectedExceptions = ActionForbiddenByStateException.class)
  public void throwsExceptionAtPersonalDataUpdateForVerifiedUser() {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    defaultUserService.verifyById(registeredUser.getId());
    defaultPersonalDataService.updateByUserId(registeredUser.getId(), randomPersonalData);
  }

  @Test
  public void findsExistedPersonalDataByUserId() {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    defaultPersonalDataService.updateByUserId(registeredUser.getId(), randomPersonalData);
    FileInfo expectedAttachment = randomPersonalData.getAttachment();
    PersonalData foundPersonalData = defaultPersonalDataService.findByUserId(registeredUser.getId());
    assertThat(foundPersonalData, allOf(
        hasProperty("id", notNullValue()),
        hasProperty("firstName", equalTo(randomPersonalData.getFirstName())),
        hasProperty("lastName", equalTo(randomPersonalData.getLastName())),
        hasProperty("documentName", equalTo(randomPersonalData.getDocumentName())),
        hasProperty("documentNumber", equalTo(randomPersonalData.getDocumentNumber())),
        hasProperty("attachment", allOf(
            hasProperty("id", notNullValue()),
            hasProperty("path", equalTo(expectedAttachment.getPath())),
            hasProperty("path", equalTo(expectedAttachment.getPath()))
        ))
    ));
  }


  @Test
  public void approvesById() {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    PersonalData updatedPersonalData = defaultPersonalDataService.updateByUserId(registeredUser.getId(), randomPersonalData);
    PersonalData approvedPersonalData = defaultPersonalDataService.approveById(updatedPersonalData.getId());
    assertThat(approvedPersonalData.getState(), equalTo(PersonalDataState.APPROVED));
  }


  @Test
  public void rejectsById() {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    PersonalData updatedPersonalData = defaultPersonalDataService.updateByUserId(registeredUser.getId(), randomPersonalData);
    PersonalData rejectedPersonalData = defaultPersonalDataService.rejectById(updatedPersonalData.getId());
    assertThat(rejectedPersonalData.getState(), equalTo(PersonalDataState.REJECTED));
  }

  @Test
  public void verifiesUserAfterPersonalDataApprove() {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    PersonalData updatedPersonalData = defaultPersonalDataService.updateByUserId(registeredUser.getId(), randomPersonalData);
    PersonalData approvedPersonalData = defaultPersonalDataService.approveById(updatedPersonalData.getId());
    assertThat(approvedPersonalData.getUser()
                                   .getCurrentState()
                                   .getName(), equalTo(UserStateName.VERIFIED));
  }

  @Test
  public void leavesUserUnverifiedAfterPersonalDataReject() {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    PersonalData updatedPersonalData = defaultPersonalDataService.updateByUserId(registeredUser.getId(), randomPersonalData);
    PersonalData rejectedPersonalData = defaultPersonalDataService.rejectById(updatedPersonalData.getId());
    User userWithRejectedPersonalData = rejectedPersonalData.getUser();
    assertThat(userWithRejectedPersonalData.getCurrentState()
                                           .getName(), equalTo(UserStateName.UNVERIFIED));
  }
}