package org.klaster.restapi.service;

/*
 * workrest
 *
 * 03.03.2020
 *
 */

import javax.persistence.EntityNotFoundException;
import org.klaster.domain.builder.general.JobBuilder;
import org.klaster.domain.dto.JobDTO;
import org.klaster.domain.model.context.Job;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.controller.EmployerProfile;
import org.klaster.domain.model.state.job.DeletedJobState;
import org.klaster.domain.model.state.job.FinishedJobState;
import org.klaster.domain.model.state.job.StartedJobState;
import org.klaster.domain.repository.JobRepository;
import org.klaster.domain.repository.SkillRepository;
import org.klaster.restapi.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * DefaultJobService
 *
 * @author Nikita Lepesevich
 */

@Service
public class DefaultJobService {

  @Autowired
  private JobRepository jobRepository;

  @Autowired
  private SkillRepository skillRepository;

  @Autowired
  private JobBuilder defaultJobBuilder;

  public Job create(JobDTO jobDTO, User author) {
    EmployerProfile employerProfile = author.getCurrentState()
                                            .getEmployerProfile();
    Job savedJob = defaultJobBuilder.setDescription(jobDTO.getDescription())
                                    .setSkills(skillRepository.findAllByNamesOrCreate(jobDTO.getSkills()))
                                    .setEndDateTime(jobDTO.getEndDateTime())
                                    .build();
    savedJob.setEmployerProfile(employerProfile);
    return jobRepository.save(savedJob);
  }

  public Job update(Job sourceJob, JobDTO jobDTO, User user) {
    validateJobBelongsToUser(sourceJob,
                             user.getCurrentState()
                                 .getEmployerProfile());
    Job targetJob = defaultJobBuilder.setSkills(skillRepository.findAllByNamesOrCreate(jobDTO.getSkills()))
                                     .setDescription(jobDTO.getDescription())
                                     .setEndDateTime(jobDTO.getEndDateTime())
                                     .build();
    sourceJob.getCurrentState()
             .updateJob(targetJob);
    return jobRepository.save(sourceJob);
  }

  public Job start(Job job, User user) {
    validateJobBelongsToUser(job,
                             user.getCurrentState()
                                 .getEmployerProfile());
    job.setCurrentState(new StartedJobState());
    return jobRepository.save(job);
  }

  public Job delete(Job job, User user) {
    validateJobBelongsToUser(job,
                             user.getCurrentState()
                                 .getEmployerProfile());
    job.setCurrentState(new DeletedJobState());
    return jobRepository.save(job);
  }

  public Job finish(Job job, User user) {
    validateJobBelongsToUser(job,
                             user.getCurrentState()
                                 .getEmployerProfile());
    job.setCurrentState(new FinishedJobState());
    return jobRepository.save(job);
  }

  public Job findById(long id) {
    return jobRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException(MessageUtil.getEntityByIdNotFoundMessage(Job.class, id)));
  }

  private void validateJobBelongsToUser(Job job, EmployerProfile employerProfile) {
    if (!job.belongsToUser(employerProfile.getId())) {
      throw new EntityNotFoundException(MessageUtil.getEntityByParentIdNotFoundMessage(User.class, employerProfile.getId()));
    }
  }
}
