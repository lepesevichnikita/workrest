package org.klaster.webapplication.service;

/*
 * practice
 *
 * 12.02.2020
 *
 */

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.klaster.model.context.Job;
import org.klaster.model.entity.FreelancerProfile;
import org.klaster.model.entity.Skill;

/**
 * JobsRecommendationService
 *
 * @author Nikita Lepesevich
 */

public class JobsRecommendationService extends AbstractRecommendationService<FreelancerProfile, Job> {

  private static JobsRecommendationService jobsRecommendationService;
  private FreelancerProfile currentFreelancer;

  private JobsRecommendationService() {
    super();
  }

  public static JobsRecommendationService getInstance() {
    if (jobsRecommendationService == null) {
      synchronized (JobsRecommendationService.class) {
        jobsRecommendationService = new JobsRecommendationService();
      }
    }
    return jobsRecommendationService;
  }

  @Override
  public List<Job> getRecommended(FreelancerProfile source, long limit) {
    this.currentFreelancer = source;
    return getAll().stream()
                   .filter(job -> getSkillMatchesWithCurrentFreelancer(job) > 0)
                   .sorted(Comparator.comparingLong(this::getSkillMatchesWithCurrentFreelancer)
                                     .reversed())
                   .limit(limit)
                   .collect(Collectors.toList());
  }

  private long getSkillMatchesWithCurrentFreelancer(Job job) {
    List<Skill> currentFreelancerSkills = new LinkedList<>(currentFreelancer.getSkills());
    return job.getSkills()
              .stream()
              .filter(currentFreelancerSkills::contains)
              .count();
  }

}
