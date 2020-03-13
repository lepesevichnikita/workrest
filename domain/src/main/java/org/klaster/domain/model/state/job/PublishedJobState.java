package org.klaster.domain.model.state.job;

import javax.persistence.Entity;
import org.klaster.domain.builder.concrete.DefaultJobMessageBuilder;
import org.klaster.domain.builder.general.JobMessageBuilder;
import org.klaster.domain.constant.JobStateName;
import org.klaster.domain.exception.ActionForbiddenForUserException;
import org.klaster.domain.model.context.Job;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.FreelancerProfile;
import org.klaster.domain.model.entity.JobMessage;

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

  @Override
  public JobMessage addMessage(User author, String text) {
    if (null == author.getCurrentState()
                      .getFreelancerProfile()) {
      throw new ActionForbiddenForUserException();
    }
    JobMessageBuilder defaultJobMessageBuilder = new DefaultJobMessageBuilder();
    return defaultJobMessageBuilder.setText(text)
                                   .setAuthor(author)
                                   .setJob(getContext())
                                   .build();
  }
}
