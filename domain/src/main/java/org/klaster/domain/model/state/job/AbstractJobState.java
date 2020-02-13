package org.klaster.domain.model.state.job;

import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.MappedSuperclass;
import org.klaster.domain.model.context.Job;
import org.klaster.domain.model.entity.Skill;
import org.klaster.domain.model.state.general.AbstractState;

/**
 * AbstractJobState
 *
 * @author Nikita Lepesevich
 */

@MappedSuperclass
public abstract class AbstractJobState extends AbstractState<Job> {

  public boolean isOverDeadlines() {
    return false;
  }

  public void updateJob(String description, Set<Skill> skills, LocalDateTime endDateTime) {
    final String message = String.format("Failed attempt to update job #%s%nDescription: %s%nSkills: %s%nEndDateTime: %s", getContext(), description, skills, endDateTime);
    logger.warning(message);
  }
}
