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
import org.klaster.domain.model.entity.FreelancerProfile;
import org.klaster.domain.model.entity.JobMessage;
import org.klaster.domain.model.entity.Skill;
import org.klaster.domain.model.state.job.DeletedJobState;
import org.klaster.domain.model.state.job.FinishedJobState;
import org.klaster.domain.model.state.job.PublishedJobState;
import org.klaster.domain.model.state.job.StartedJobState;
import org.klaster.domain.repository.FreelancerProfileRepository;
import org.klaster.domain.repository.JobMessageRepository;
import org.klaster.domain.repository.JobRepository;
import org.klaster.domain.repository.SkillRepository;
import org.klaster.domain.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * DefaultJobService
 *
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
  private FreelancerProfileRepository freelancerProfileRepository;

  @Autowired
  private DefaultUserService defaultUserService;

  @Autowired
  private JobMessageRepository jobMessageRepository;

  @Transactional
  public Job create(JobDTO jobDTO, User author) {
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

  @Transactional
  public Job setFreelancerProfile(long jobId, long userId) {
    Job foundJob = findById(jobId);
    User foundUser = defaultUserService.findFirstById(userId);
    foundJob.getCurrentState()
            .setFreelancerProfile(foundUser.getCurrentState().getFreelancerProfile());
    return jobRepository.saveAndFlush(foundJob);
  }

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

  public Job findById(long jobId) {
    return jobRepository.findById(jobId)
                        .orElseThrow(() -> new EntityNotFoundException(MessageUtil.getEntityByIdNotFoundMessage(Job.class, jobId)));
  }

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

  public List<Job> findAll() {
    return jobRepository.findAll()
                        .stream()
                        .filter(job -> job.getCurrentState() instanceof PublishedJobState)
                        .sorted(Comparator.comparing(Job::getId))
                        .collect(Collectors.toList());
  }

  private void validateJobDoesntBelongToUser(Job job, User user) {
    EmployerProfile employerProfile = user.getCurrentState()
                                          .getEmployerProfile();
    if (null != employerProfile && job.belongsToEmployer(employerProfile.getId())) {
      throw new ActionForbiddenForUserException();
    }
  }

  private void validateJobBelongsToUser(Job job, User user) {
    EmployerProfile employerProfile = user.getCurrentState()
                                          .getEmployerProfile();
    if (null == employerProfile || !job.belongsToEmployer(employerProfile.getId())) {
      throw new EntityNotFoundException(MessageUtil.getEntityByParentIdNotFoundMessage(User.class, user.getId()));
    }
  }

  @Transactional
  public List<JobMessage> findAllMessagesByJobId(long jobId) {
    Job foundJob = findById(jobId);
    return foundJob.getMessages()
                   .stream()
                   .sorted(Comparator.comparing(JobMessage::getId))
                   .collect(Collectors.toList());
  }
}
