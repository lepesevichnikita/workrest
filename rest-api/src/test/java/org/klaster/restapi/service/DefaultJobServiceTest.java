package org.klaster.restapi.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;

import javax.persistence.EntityNotFoundException;
import org.klaster.domain.dto.EmployerProfileDTO;
import org.klaster.domain.dto.FreelancerProfileDTO;
import org.klaster.domain.dto.JobDTO;
import org.klaster.domain.exception.ActionForbiddenByStateException;
import org.klaster.domain.exception.ActionForbiddenForUserException;
import org.klaster.domain.exception.EmployerProfileNotFoundException;
import org.klaster.domain.model.context.Job;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.EmployerProfile;
import org.klaster.domain.model.entity.FreelancerProfile;
import org.klaster.domain.model.entity.JobMessage;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.state.job.DeletedJobState;
import org.klaster.restapi.configuration.ApplicationContext;
import org.klaster.restapi.factory.RandomEmployerProfileFactory;
import org.klaster.restapi.factory.RandomFreelancerProfileFactory;
import org.klaster.restapi.factory.RandomJobFactory;
import org.klaster.restapi.factory.RandomJobMessageFactory;
import org.klaster.restapi.factory.RandomLoginInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/*
 * org.klaster.restapi.service
 *
 * workrest
 *
 * 3/4/20
 *
 * Copyright(c) JazzTeam
 */

@WebAppConfiguration
@ContextConfiguration(classes = {ApplicationContext.class})
public class DefaultJobServiceTest extends AbstractTestNGSpringContextTests {

  private LoginInfo randomLoginInfo;
  private EmployerProfile randomEmployerProfile;
  private FreelancerProfile randomFreelancerProfile;
  private Job randomJob;
  private JobMessage randomJobMessage;

  private RandomLoginInfoFactory randomLoginInfoFactory;
  private RandomJobFactory randomJobFactory;
  private RandomEmployerProfileFactory randomEmployerProfileFactory;
  private RandomFreelancerProfileFactory randomFreelancerProfileFactory;
  private RandomJobMessageFactory randomJobMessageFactory;

  @Autowired
  private DefaultUserService defaultUserService;

  @Autowired
  private DefaultJobService defaultJobService;

  @BeforeClass
  public void setup() {
    randomLoginInfoFactory = RandomLoginInfoFactory.getInstance();
    randomJobFactory = RandomJobFactory.getInstance();
    randomEmployerProfileFactory = RandomEmployerProfileFactory.getInstance();
    randomFreelancerProfileFactory = RandomFreelancerProfileFactory.getInstance();
    randomJobMessageFactory = RandomJobMessageFactory.getInstance();
  }

  @BeforeMethod
  public void initialize() {
    randomLoginInfo = randomLoginInfoFactory.build();
    randomJob = randomJobFactory.build();
    randomEmployerProfile = randomEmployerProfileFactory.build();
    randomFreelancerProfile = randomFreelancerProfileFactory.build();
    randomJobMessage = randomJobMessageFactory.build();
  }

  @Test
  public void createsJobForVerifiedUserWithEmployerProfile() {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    User verifiedUser = defaultUserService.verifyById(registeredUser.getId());
    User userWithEmployerProfile = defaultUserService.updateEmployerProfile(verifiedUser,
                                                                            EmployerProfileDTO.fromEmployerProfile(randomEmployerProfile));
    Job createdJob = defaultJobService.create(JobDTO.fromJob(randomJob), userWithEmployerProfile);
    assertThat(createdJob, allOf(
        hasProperty("description", equalTo(randomJob.getDescription())),
        hasProperty("endDateTime", equalTo(randomJob.getEndDateTime())),
        hasProperty("skills",
                    hasSize(randomJob.getSkills()
                                     .size()))
    ));
  }

  @Test(expectedExceptions = ActionForbiddenByStateException.class)
  public void throwsActionForbiddenByCurrentStateAtJobCreateForBlockedUserWithEmployerProfile() {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    User verifiedUser = defaultUserService.verifyById(registeredUser.getId());
    defaultUserService.updateEmployerProfile(verifiedUser,
                                             EmployerProfileDTO.fromEmployerProfile(randomEmployerProfile));
    User blockedUser = defaultUserService.blockById(registeredUser.getId());
    defaultJobService.create(JobDTO.fromJob(randomJob), blockedUser);
  }

  @Test(expectedExceptions = ActionForbiddenByStateException.class)
  public void throwsActionForbiddenByCurrentStateAtJobCreateForDeletedUserWithEmployerProfile() {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    User verifiedUser = defaultUserService.verifyById(registeredUser.getId());
    defaultUserService.updateEmployerProfile(verifiedUser,
                                             EmployerProfileDTO.fromEmployerProfile(randomEmployerProfile));
    User deletedUser = defaultUserService.deleteById(registeredUser.getId());
    defaultJobService.create(JobDTO.fromJob(randomJob), deletedUser);
  }

  @Test(expectedExceptions = ActionForbiddenByStateException.class)
  public void throwsActionForbiddenByCurrentStateAtJobCreateForUnverifiedUser() {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    defaultJobService.create(JobDTO.fromJob(randomJob), registeredUser);
  }


  @Test(expectedExceptions = EmployerProfileNotFoundException.class)
  public void throwsEmployerProfileNotFoundAtJobCreateForUserWithoutEmployerProfile() {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    User verifiedUser = defaultUserService.verifyById(registeredUser.getId());
    defaultJobService.create(JobDTO.fromJob(randomJob), verifiedUser);
  }

  @Test
  public void deletesJobForVerifiedUser() {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    User verifiedUser = defaultUserService.verifyById(registeredUser.getId());
    User userWithEmployerProfile = defaultUserService.updateEmployerProfile(verifiedUser,
                                                                            EmployerProfileDTO.fromEmployerProfile(randomEmployerProfile));
    Job createdJob = defaultJobService.create(JobDTO.fromJob(randomJob), userWithEmployerProfile);
    Job deletedJob = defaultJobService.deleteById(createdJob.getId(), userWithEmployerProfile);
    assertThat(deletedJob, allOf(
        hasProperty("description", equalTo(createdJob.getDescription())),
        hasProperty("endDateTime", equalTo(createdJob.getEndDateTime())),
        hasProperty("skills",
                    hasSize(createdJob.getSkills()
                                      .size())),
        hasProperty("id", equalTo(createdJob.getId())),
        hasProperty("currentState", instanceOf(DeletedJobState.class))
    ));
  }

  @Test
  public void doesntDeleteTwice() {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    User verifiedUser = defaultUserService.verifyById(registeredUser.getId());
    User userWithEmployerProfile = defaultUserService.updateEmployerProfile(verifiedUser,
                                                                            EmployerProfileDTO.fromEmployerProfile(randomEmployerProfile));
    Job createdJob = defaultJobService.create(JobDTO.fromJob(randomJob), userWithEmployerProfile);
    Job deletedJob = defaultJobService.deleteById(createdJob.getId(), userWithEmployerProfile);
    Job secondTimeDeletedJob = defaultJobService.deleteById(createdJob.getId(), userWithEmployerProfile);
    assertThat(deletedJob.getCurrentState(), allOf(
        hasProperty("id",
                    equalTo(secondTimeDeletedJob.getCurrentState()
                                                .getId())),
        instanceOf(secondTimeDeletedJob.getCurrentState()
                                       .getClass())
    ));
  }

  @Test(expectedExceptions = ActionForbiddenByStateException.class)
  public void throwsActionForbiddenByCurrentStateAtJobDeleteByBlockedUser() {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    User verifiedUser = defaultUserService.verifyById(registeredUser.getId());
    User userWithEmployerProfile = defaultUserService.updateEmployerProfile(verifiedUser,
                                                                            EmployerProfileDTO.fromEmployerProfile(randomEmployerProfile));
    Job createdJob = defaultJobService.create(JobDTO.fromJob(randomJob), userWithEmployerProfile);
    User blockedUser = defaultUserService.blockById(userWithEmployerProfile.getId());
    defaultJobService.deleteById(createdJob.getId(), blockedUser);
  }

  @Test(expectedExceptions = ActionForbiddenByStateException.class)
  public void throwsActionForbiddenByCurrentStateAtJobDeleteByDeletedUser() {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    User verifiedUser = defaultUserService.verifyById(registeredUser.getId());
    User userWithEmployerProfile = defaultUserService.updateEmployerProfile(verifiedUser,
                                                                            EmployerProfileDTO.fromEmployerProfile(randomEmployerProfile));
    Job createdJob = defaultJobService.create(JobDTO.fromJob(randomJob), userWithEmployerProfile);
    User deletedUser = defaultUserService.deleteById(userWithEmployerProfile.getId());
    defaultJobService.deleteById(createdJob.getId(), deletedUser);
  }

  @Test
  public void updatesPublishedJob() {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    User verifiedUser = defaultUserService.verifyById(registeredUser.getId());
    User userWithEmployerProfile = defaultUserService.updateEmployerProfile(verifiedUser,
                                                                            EmployerProfileDTO.fromEmployerProfile(randomEmployerProfile));
    Job createdJob = defaultJobService.create(JobDTO.fromJob(randomJob), userWithEmployerProfile);
    JobDTO jobUpdate = JobDTO.fromJob(randomJobFactory.build());
    Job updatedJob = defaultJobService.updateById(createdJob.getId(), jobUpdate, userWithEmployerProfile);
    assertThat(updatedJob, allOf(
        hasProperty("description", equalTo(jobUpdate.getDescription())),
        hasProperty("endDateTime", equalTo(jobUpdate.getEndDateTime())),
        hasProperty("skills", hasSize(jobUpdate.getSkills().length))
    ));
  }


  @Test(expectedExceptions = ActionForbiddenByStateException.class)
  public void throwsActionForbiddenByCurrentStateAtJobUpdateByDeletedUser() {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    User verifiedUser = defaultUserService.verifyById(registeredUser.getId());
    User userWithEmployerProfile = defaultUserService.updateEmployerProfile(verifiedUser,
                                                                            EmployerProfileDTO.fromEmployerProfile(randomEmployerProfile));
    Job createdJob = defaultJobService.create(JobDTO.fromJob(randomJob), userWithEmployerProfile);
    User deletedUser = defaultUserService.deleteById(userWithEmployerProfile.getId());
    defaultJobService.updateById(createdJob.getId(), JobDTO.fromJob(randomJobFactory.build()), deletedUser);
  }

  @Test(expectedExceptions = ActionForbiddenByStateException.class)
  public void throwsActionForbiddenByCurrentStateAtJobUpdateByBlockedUser() {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    User verifiedUser = defaultUserService.verifyById(registeredUser.getId());
    User userWithEmployerProfile = defaultUserService.updateEmployerProfile(verifiedUser,
                                                                            EmployerProfileDTO.fromEmployerProfile(randomEmployerProfile));
    Job createdJob = defaultJobService.create(JobDTO.fromJob(randomJob), userWithEmployerProfile);
    User blockedUser = defaultUserService.blockById(userWithEmployerProfile.getId());
    defaultJobService.updateById(createdJob.getId(), JobDTO.fromJob(randomJobFactory.build()), blockedUser);
  }

  @Test(expectedExceptions = ActionForbiddenByStateException.class)
  public void throwsActionForbiddenByCurrentStateAtStartedJobUpdated() {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    User verifiedUser = defaultUserService.verifyById(registeredUser.getId());
    User userWithEmployerProfile = defaultUserService.updateEmployerProfile(verifiedUser,
                                                                            EmployerProfileDTO.fromEmployerProfile(randomEmployerProfile));
    Job createdJob = defaultJobService.create(JobDTO.fromJob(randomJob), userWithEmployerProfile);
    defaultJobService.startById(createdJob.getId(), userWithEmployerProfile);
    defaultJobService.updateById(createdJob.getId(), JobDTO.fromJob(randomJobFactory.build()), userWithEmployerProfile);
  }

  @Test(expectedExceptions = ActionForbiddenByStateException.class)
  public void throwsActionForbiddenByCurrentStateAtFinishedJobUpdated() {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    User verifiedUser = defaultUserService.verifyById(registeredUser.getId());
    User userWithEmployerProfile = defaultUserService.updateEmployerProfile(verifiedUser,
                                                                            EmployerProfileDTO.fromEmployerProfile(randomEmployerProfile));
    Job createdJob = defaultJobService.create(JobDTO.fromJob(randomJob), userWithEmployerProfile);
    defaultJobService.finishById(createdJob.getId(), userWithEmployerProfile);
    defaultJobService.updateById(createdJob.getId(), JobDTO.fromJob(randomJobFactory.build()), userWithEmployerProfile);
  }

  @Test(expectedExceptions = ActionForbiddenByStateException.class)
  public void throwsActionForbiddenByCurrentStateAtDeletedJobUpdated() {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    User verifiedUser = defaultUserService.verifyById(registeredUser.getId());
    User userWithEmployerProfile = defaultUserService.updateEmployerProfile(verifiedUser,
                                                                            EmployerProfileDTO.fromEmployerProfile(randomEmployerProfile));
    Job createdJob = defaultJobService.create(JobDTO.fromJob(randomJob), userWithEmployerProfile);
    defaultJobService.deleteById(createdJob.getId(), userWithEmployerProfile);
    defaultJobService.updateById(createdJob.getId(), JobDTO.fromJob(randomJobFactory.build()), userWithEmployerProfile);
  }

  @Test(expectedExceptions = EntityNotFoundException.class)
  public void throwsEntityNotFoundAtSomeoneElseJobUpdate() {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    User verifiedUser = defaultUserService.verifyById(registeredUser.getId());
    User userWithEmployerProfile = defaultUserService.updateEmployerProfile(verifiedUser,
                                                                            EmployerProfileDTO.fromEmployerProfile(randomEmployerProfile));
    User anotherRegisteredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfoFactory.build());
    User anotherVerifiedUser = defaultUserService.verifyById(anotherRegisteredUser.getId());
    User anotherUserWithEmployerProfile = defaultUserService.updateEmployerProfile(anotherVerifiedUser,
                                                                                   EmployerProfileDTO.fromEmployerProfile(
                                                                                       randomEmployerProfile));
    Job createdJob = defaultJobService.create(JobDTO.fromJob(randomJob), userWithEmployerProfile);
    defaultJobService.updateById(createdJob.getId(), JobDTO.fromJob(randomJobFactory.build()), anotherUserWithEmployerProfile);
  }


  @Test(expectedExceptions = EntityNotFoundException.class)
  public void throwsEntityNotFoundAtSomeoneElseJobDelete() {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    User verifiedUser = defaultUserService.verifyById(registeredUser.getId());
    User userWithEmployerProfile = defaultUserService.updateEmployerProfile(verifiedUser,
                                                                            EmployerProfileDTO.fromEmployerProfile(randomEmployerProfile));
    User anotherRegisteredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfoFactory.build());
    User anotherVerifiedUser = defaultUserService.verifyById(anotherRegisteredUser.getId());
    User anotherUserWithEmployerProfile = defaultUserService.updateEmployerProfile(anotherVerifiedUser,
                                                                                   EmployerProfileDTO.fromEmployerProfile(
                                                                                       randomEmployerProfile));
    Job createdJob = defaultJobService.create(JobDTO.fromJob(randomJob), userWithEmployerProfile);
    defaultJobService.deleteById(createdJob.getId(), anotherUserWithEmployerProfile);
  }

  @Test
  public void createsMessageForPublishedJobFromUserWithFreelancerProfile() {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    User verifiedUser = defaultUserService.verifyById(registeredUser.getId());
    User userWithEmployerProfile = defaultUserService.updateEmployerProfile(verifiedUser,
                                                                            EmployerProfileDTO.fromEmployerProfile(randomEmployerProfile));
    User anotherRegisteredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfoFactory.build());
    User anotherVerifiedUser = defaultUserService.verifyById(anotherRegisteredUser.getId());
    User userWithFreelancerProfile = defaultUserService.updateFreelancerProfile(anotherVerifiedUser, FreelancerProfileDTO.fromFreelancerProfile(randomFreelancerProfile));
    Job createdJob = defaultJobService.create(JobDTO.fromJob(randomJob), userWithEmployerProfile);
    JobMessage createdJobMessage = defaultJobService.addMessageFromUser(createdJob.getId(), userWithFreelancerProfile, randomJobMessage.getText());
    assertThat(createdJobMessage, allOf(
        hasProperty("text", equalTo(randomJobMessage.getText())),
        hasProperty("author", hasProperty("id", equalTo(userWithFreelancerProfile.getId()))),
        hasProperty("job", allOf(
            hasProperty("id", equalTo(createdJob.getId())),
            hasProperty("description", equalTo(createdJob.getDescription()))
        ))
    ));
  }

  @Test(expectedExceptions = ActionForbiddenByStateException.class)
  public void throwsActionForbiddenByCurrentStateForStartedJob() {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    User verifiedUser = defaultUserService.verifyById(registeredUser.getId());
    User userWithEmployerProfile = defaultUserService.updateEmployerProfile(verifiedUser,
                                                                            EmployerProfileDTO.fromEmployerProfile(randomEmployerProfile));
    User anotherRegisteredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfoFactory.build());
    User anotherVerifiedUser = defaultUserService.verifyById(anotherRegisteredUser.getId());
    User userWithFreelancerProfile = defaultUserService.updateFreelancerProfile(anotherVerifiedUser, FreelancerProfileDTO.fromFreelancerProfile(randomFreelancerProfile));
    Job createdJob = defaultJobService.create(JobDTO.fromJob(randomJob), userWithEmployerProfile);
    Job startedJob = defaultJobService.startById(createdJob.getId(), userWithEmployerProfile);
    defaultJobService.addMessageFromUser(startedJob.getId(), userWithFreelancerProfile, randomJobMessage.getText());
  }

  @Test(expectedExceptions = ActionForbiddenByStateException.class)
  public void throwsActionForbiddenByCurrentStateForStartedJobOnMessageCreate() {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    User verifiedUser = defaultUserService.verifyById(registeredUser.getId());
    User userWithEmployerProfile = defaultUserService.updateEmployerProfile(verifiedUser,
                                                                            EmployerProfileDTO.fromEmployerProfile(randomEmployerProfile));
    User anotherRegisteredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfoFactory.build());
    User anotherVerifiedUser = defaultUserService.verifyById(anotherRegisteredUser.getId());
    User userWithFreelancerProfile = defaultUserService.updateFreelancerProfile(anotherVerifiedUser, FreelancerProfileDTO.fromFreelancerProfile(randomFreelancerProfile));
    Job createdJob = defaultJobService.create(JobDTO.fromJob(randomJob), userWithEmployerProfile);
    Job startedJob = defaultJobService.startById(createdJob.getId(), userWithEmployerProfile);
    defaultJobService.addMessageFromUser(startedJob.getId(), userWithFreelancerProfile, randomJobMessage.getText());
  }

  @Test(expectedExceptions = ActionForbiddenByStateException.class)
  public void throwsActionForbiddenByCurrentStateForFinishedJobOnMessageCreate() {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    User verifiedUser = defaultUserService.verifyById(registeredUser.getId());
    User userWithEmployerProfile = defaultUserService.updateEmployerProfile(verifiedUser,
                                                                            EmployerProfileDTO.fromEmployerProfile(randomEmployerProfile));
    User anotherRegisteredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfoFactory.build());
    User anotherVerifiedUser = defaultUserService.verifyById(anotherRegisteredUser.getId());
    User userWithFreelancerProfile = defaultUserService.updateFreelancerProfile(anotherVerifiedUser, FreelancerProfileDTO.fromFreelancerProfile(randomFreelancerProfile));
    Job createdJob = defaultJobService.create(JobDTO.fromJob(randomJob), userWithEmployerProfile);
    Job startedJob = defaultJobService.finishById(createdJob.getId(), userWithEmployerProfile);
    defaultJobService.addMessageFromUser(startedJob.getId(), userWithFreelancerProfile, randomJobMessage.getText());
  }

  @Test(expectedExceptions = ActionForbiddenByStateException.class)
  public void throwsActionForbiddenByCurrentStateForDeletedJobOnMessageCreate() {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    User verifiedUser = defaultUserService.verifyById(registeredUser.getId());
    User userWithEmployerProfile = defaultUserService.updateEmployerProfile(verifiedUser,
                                                                            EmployerProfileDTO.fromEmployerProfile(randomEmployerProfile));
    User anotherRegisteredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfoFactory.build());
    User anotherVerifiedUser = defaultUserService.verifyById(anotherRegisteredUser.getId());
    User userWithFreelancerProfile = defaultUserService.updateFreelancerProfile(anotherVerifiedUser, FreelancerProfileDTO.fromFreelancerProfile(randomFreelancerProfile));
    Job createdJob = defaultJobService.create(JobDTO.fromJob(randomJob), userWithEmployerProfile);
    Job startedJob = defaultJobService.deleteById(createdJob.getId(), userWithEmployerProfile);
    defaultJobService.addMessageFromUser(startedJob.getId(), userWithFreelancerProfile, randomJobMessage.getText());
  }

  @Test(expectedExceptions = ActionForbiddenForUserException.class)
  public void throwsActionForbiddenForUserExceptionForDeletedJobOnMessageCreateByJobOwner() {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    User verifiedUser = defaultUserService.verifyById(registeredUser.getId());
    User userWithEmployerProfile = defaultUserService.updateEmployerProfile(verifiedUser,
                                                                            EmployerProfileDTO.fromEmployerProfile(randomEmployerProfile));
    Job createdJob = defaultJobService.create(JobDTO.fromJob(randomJob), userWithEmployerProfile);
    defaultJobService.addMessageFromUser(createdJob.getId(), userWithEmployerProfile, randomJobMessage.getText());
  }

  @Test(expectedExceptions = ActionForbiddenForUserException.class)
  public void throwsActionForbiddenForUserExceptionForTwiceMessageAdding() {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    User verifiedUser = defaultUserService.verifyById(registeredUser.getId());
    User userWithEmployerProfile = defaultUserService.updateEmployerProfile(verifiedUser,
                                                                            EmployerProfileDTO.fromEmployerProfile(randomEmployerProfile));
    User anotherRegisteredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfoFactory.build());
    User anotherVerifiedUser = defaultUserService.verifyById(anotherRegisteredUser.getId());
    User userWithFreelancerProfile = defaultUserService.updateFreelancerProfile(anotherVerifiedUser, FreelancerProfileDTO.fromFreelancerProfile(randomFreelancerProfile));
    Job createdJob = defaultJobService.create(JobDTO.fromJob(randomJob), userWithEmployerProfile);
    defaultJobService.addMessageFromUser(createdJob.getId(), userWithFreelancerProfile, randomJobMessage.getText());
    defaultJobService.addMessageFromUser(createdJob.getId(), userWithFreelancerProfile, randomJobMessage.getText());
  }

  @Test(expectedExceptions = ActionForbiddenForUserException.class)
  public void throwsActionForbiddenForUserExceptionForUserWithoutFreelacnerProfile() {
    User registeredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfo);
    User verifiedUser = defaultUserService.verifyById(registeredUser.getId());
    User userWithEmployerProfile = defaultUserService.updateEmployerProfile(verifiedUser,
                                                                            EmployerProfileDTO.fromEmployerProfile(randomEmployerProfile));
    User anotherRegisteredUser = defaultUserService.registerUserByLoginInfo(randomLoginInfoFactory.build());
    User anotherVerifiedUser = defaultUserService.verifyById(anotherRegisteredUser.getId());
    Job createdJob = defaultJobService.create(JobDTO.fromJob(randomJob), userWithEmployerProfile);
    defaultJobService.addMessageFromUser(createdJob.getId(), anotherVerifiedUser, randomJobMessage.getText());
  }
}