package org.klaster.restapi.service;

/*
 * workrest
 *
 * 03.03.2020
 *
 */

import java.util.List;
import java.util.Set;
import javax.persistence.EntityNotFoundException;
import org.klaster.domain.builder.general.JobBuilder;
import org.klaster.domain.dto.JobDTO;
import org.klaster.domain.exception.EmployerProfileNotFoundException;
import org.klaster.domain.model.context.Job;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.EmployerProfile;
import org.klaster.domain.model.entity.Skill;
import org.klaster.domain.model.state.job.DeletedJobState;
import org.klaster.domain.model.state.job.FinishedJobState;
import org.klaster.domain.model.state.job.StartedJobState;
import org.klaster.domain.repository.JobRepository;
import org.klaster.domain.repository.SkillRepository;
import org.klaster.domain.util.MessageUtil;
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

  public List<Job> findAllByEmployerProfile(EmployerProfile employerProfile) {
    return jobRepository.findAllByEmployerProfile(employerProfile);
  }

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
    if (!job.belongsToEmployer(employerProfile.getId())) {
      throw new EntityNotFoundException(MessageUtil.getEntityByParentIdNotFoundMessage(User.class, employerProfile.getId()));
    }
  }
}