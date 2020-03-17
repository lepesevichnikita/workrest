package org.klaster.restapi.service;

/*
 * workrest
 *
 * 03.03.2020
 *
 */

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import org.klaster.domain.builder.general.JobBuilder;
import org.klaster.domain.dto.JobDTO;
import org.klaster.domain.exception.ActionForbiddenForUserException;
import org.klaster.domain.exception.EmployerProfileNotFoundException;
import org.klaster.domain.model.context.Job;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.EmployerProfile;
import org.klaster.domain.model.entity.JobMessage;
import org.klaster.domain.model.entity.Skill;
import org.klaster.domain.model.state.job.DeletedJobState;
import org.klaster.domain.model.state.job.FinishedJobState;
import org.klaster.domain.model.state.job.PublishedJobState;
import org.klaster.domain.model.state.job.StartedJobState;
import org.klaster.domain.repository.JobMessageRepository;
import org.klaster.domain.repository.JobRepository;
import org.klaster.domain.repository.SkillRepository;
import org.klaster.domain.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * DefaultJobService
 * Provides interface for job management
 * @author Nikita Lepesevich
 */

@Service
@Transactional
public class DefaultJobService {

  @Autowired
  private JobRepository jobRepository;

  @Autowired
  private SkillRepository skillRepository;

  @Autowired
  private JobBuilder defaultJobBuilder;

  @Autowired
  private DefaultUserService defaultUserService;

  @Autowired
  private JobMessageRepository jobMessageRepository;

  /**
   * Creates job from passed jobDTO with passed user as author
   *
   * @param jobDTO - DTO of Job
   * @param author - User that should be author
   * @return created job
   * @throws EmployerProfileNotFoundException if user has no employer profile
   */
  @Transactional
  public Job create(JobDTO jobDTO, User author) throws EmployerProfileNotFoundException {
    EmployerProfile employerProfile = author.getCurrentState()
                                            .getEmployerProfile();
    if (employerProfile == null) {
      throw new EmployerProfileNotFoundException();
    }
    Set<Skill> skills = skillRepository.findAllByNamesOrCreate(jobDTO.getSkills());
    Job savedJob = defaultJobBuilder.setDescription(jobDTO.getDescription())
                                    .setSkills(skills)
                                    .setEndDateTime(jobDTO.getEndDateTime())
                                    .build();
    savedJob.setEmployerProfile(employerProfile);
    return jobRepository.saveAndFlush(savedJob);
  }

  /**
   * Update job by passed id and passed user
   * User should be owner of job
   * @param jobId - updated job id
   * @param jobDTO - job's DTO
   * @param user - owner
   * @return Updated job
   */
  @Transactional
  public Job updateById(long jobId, JobDTO jobDTO, User user) {
    Job foundJob = findById(jobId);
    validateJobBelongsToUser(foundJob, user);
    Job targetJob = defaultJobBuilder.setSkills(skillRepository.findAllByNamesOrCreate(jobDTO.getSkills()))
                                     .setDescription(jobDTO.getDescription())
                                     .setEndDateTime(jobDTO.getEndDateTime())
                                     .build();
    foundJob.getCurrentState()
            .updateJob(targetJob);
    return jobRepository.saveAndFlush(foundJob);
  }

  /**
   * Sets freelancer to job
   * @param jobId updated job id
   * @param userId id of user with freelancer profile
   * @return updated job
   */
  @Transactional
  public Job setFreelancerProfile(long jobId, long userId) {
    Job foundJob = findById(jobId);
    User foundUser = defaultUserService.findFirstById(userId);
    foundJob.getCurrentState()
            .setFreelancerProfile(foundUser.getCurrentState()
                                           .getFreelancerProfile());
    return jobRepository.saveAndFlush(foundJob);
  }

  /**
   * Changes state of job to started if job belongs to user and isn't started already
   * @param jobId update job id
   * @param user owner of job
   * @return updated job
   */
  @Transactional
  public Job startById(long jobId, User user) {
    Job foundJob = findById(jobId);
    validateJobBelongsToUser(foundJob, user);
    if (!(foundJob.getCurrentState() instanceof StartedJobState)) {
      foundJob.setCurrentState(new StartedJobState());
      foundJob = jobRepository.saveAndFlush(foundJob);
    }
    return foundJob;
  }

  /**
   * Changes job state to deleted if job belongs to user and isn't deleted already
   * @param jobId updated job id
   * @param user owner of job
   * @return updated job
   */
  @Transactional
  public Job deleteById(long jobId, User user) {
    Job foundJob = findById(jobId);
    validateJobBelongsToUser(foundJob, user);
    if (!(foundJob.getCurrentState() instanceof DeletedJobState)) {
      foundJob.setCurrentState(new DeletedJobState());
      foundJob = jobRepository.saveAndFlush(foundJob);
    }
    return foundJob;
  }

  /**
   * Changes state to finished of job if it isn't finished already and user is owner of job
   * @param jobId updated job id
   * @param user owner of job
   * @return updated job
   */
  @Transactional
  public Job finishById(long jobId, User user) {
    Job foundJob = findById(jobId);
    validateJobBelongsToUser(foundJob, user);
    if (!(foundJob.getCurrentState() instanceof FinishedJobState)) {
      foundJob.setCurrentState(new FinishedJobState());
      foundJob = jobRepository.saveAndFlush(foundJob);
    }
    return foundJob;
  }

  /**
   * Finds job by id
   * @throws EntityNotFoundException if job with passed id doesn't exists
   * @param jobId id of required job
   * @return found job
   */
  public Job findById(long jobId) {
    return jobRepository.findById(jobId)
                        .orElseThrow(() -> new EntityNotFoundException(MessageUtil.getEntityByIdNotFoundMessage(Job.class, jobId)));
  }

  /**
   * Adds message to job by user with freelancer profile
   * @throws ActionForbiddenForUserException if user has already messaged to job
   * @param jobId messaged job
   * @param author author of message
   * @param text text of message
   * @return created message
   */
  @Transactional
  public JobMessage addMessageFromUser(long jobId, User author, String text) {
    Job foundJob = findById(jobId);
    validateJobDoesntBelongToUser(foundJob, author);
    if (jobMessageRepository.existsByAuthorAndJob(author, foundJob)) {
      throw new ActionForbiddenForUserException();
    }
    JobMessage jobMessage = foundJob.getCurrentState()
                                    .addMessage(author, text);
    return jobMessageRepository.save(jobMessage);
  }

  /**
   * Finds all published job
   * @return published jobs, sorted by id
   */
  @Transactional
  public List<Job> findAll() {
    List<Job> found =  jobRepository.findAll()
                        .stream()
                        .filter(job -> job.getCurrentState() instanceof PublishedJobState)
                        .sorted(Comparator.comparing(Job::getId))
                        .collect(Collectors.toList());
    return found;
  }

  /**
   * Finds all messages of job by passed id
   * @param jobId target job id
   * @return messages of target job, sorted by id
   */
  @Transactional
  public List<JobMessage> findAllMessagesByJobId(long jobId) {
    Job foundJob = findById(jobId);
    return foundJob.getMessages()
                   .stream()
                   .sorted(Comparator.comparing(JobMessage::getId))
                   .collect(Collectors.toList());
  }

  /**
   * Validates user is not owner of job
   *
   * @throws ActionForbiddenForUserException if user is owner of job
   */
  private void validateJobDoesntBelongToUser(Job job, User user) throws ActionForbiddenForUserException {
    EmployerProfile employerProfile = user.getCurrentState()
                                          .getEmployerProfile();
    if (null != employerProfile && job.belongsToEmployer(employerProfile.getId())) {
      throw new ActionForbiddenForUserException();
    }
  }

  /**
   * Validates user is owner of job
   * @throws EntityNotFoundException if user is not owner of job
   * @param job
   * @param user
   */
  private void validateJobBelongsToUser(Job job, User user) throws EntityNotFoundException {
    EmployerProfile employerProfile = user.getCurrentState()
                                          .getEmployerProfile();
    if (null == employerProfile || !job.belongsToEmployer(employerProfile.getId())) {
      throw new EntityNotFoundException(MessageUtil.getEntityByParentIdNotFoundMessage(User.class, user.getId()));
    }
  }

}
