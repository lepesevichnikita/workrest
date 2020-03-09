package org.klaster.domain.model.state.job;

import javax.persistence.Entity;
import org.klaster.domain.constant.JobStateName;
import org.klaster.domain.model.context.Job;
import org.klaster.domain.model.entity.FreelancerProfile;

/**
 * PublishedJobState
 *
 * @author Nikita Lepesevich
 */

@Entity
public class PublishedJobState extends AbstractJobState {

  @Override
  public void updateJob(Job job) {
    getContext().setEndDateTime(job.getEndDateTime());
    getContext().setDescription(job.getDescription());
    getContext().setSkills(job.getSkills());
  }

  @Override
  public String getName() {
    return JobStateName.PUBLISHED;
  }

  @Override
  public void setFreelancerProfile(FreelancerProfile freelancerProfile) {
    getContext().setFreelancerProfile(freelancerProfile);
    freelancerProfile.getJobs().add(getContext());
  }
}
