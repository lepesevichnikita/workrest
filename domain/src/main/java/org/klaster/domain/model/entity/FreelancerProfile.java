package org.klaster.domain.model.entity;

import java.util.List;
import java.util.Set;
import org.klaster.domain.model.context.Job;
import org.klaster.domain.model.context.User;
import org.klaster.domain.service.JobsRecommendationService;

/**
 * FreelancerProfile
 *
 * @author Nikita Lepesevich
 */

public class FreelancerProfile extends AbstractProfile {

  private Set<Skill> skills;

  public FreelancerProfile(User owner) {
    super(owner);
  }

  public Set<Skill> getSkills() {
    return skills;
  }

  public void setSkills(Set<Skill> skills) {
    this.skills = skills;
  }

  public List<Job> getRecommendedJobs(long limit) {
    return JobsRecommendationService.getInstance()
                                    .getRecommended(this, limit);
  }
}