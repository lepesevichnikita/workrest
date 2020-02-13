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
 * FreelancersRecommendationService
 *
 * @author Nikita Lepesevich
 */

public class FreelancersRecommendationService extends AbstractRecommendationService<Job, FreelancerProfile> {

  private static FreelancersRecommendationService instance;

  private Job currentJob;

  private FreelancersRecommendationService() {
    super();
  }

  public static FreelancersRecommendationService getInstance() {
    if (instance == null) {
      synchronized (FreelancersRecommendationService.class) {
        instance = new FreelancersRecommendationService();
      }
    }
    return instance;
  }

  @Override
  public List<FreelancerProfile> getRecommended(Job source, long limit) {
    this.currentJob = source;
    return getAll().stream()
                   .filter(freelancerProfile -> getSkillMatchesWithCurrentJob(freelancerProfile) > 0)
                   .sorted(Comparator.comparingLong(this::getSkillMatchesWithCurrentJob)
                                     .reversed())
                   .limit(limit)
                   .collect(Collectors.toList());
  }

  private long getSkillMatchesWithCurrentJob(FreelancerProfile freelancerProfile) {
    List<Skill> requiredSkills = new LinkedList<>(currentJob.getSkills());
    return freelancerProfile.getSkills()
                            .stream()
                            .filter(requiredSkills::contains)
                            .count();
  }
}
