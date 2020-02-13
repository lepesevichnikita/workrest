package org.klaster.model.state.job;

import java.time.LocalDateTime;
import java.util.Set;
import org.klaster.model.entity.Skill;

/**
 * PublishedJobState
 *
 * @author Nikita Lepesevich
 */

public class PublishedJobState extends AbstractJobState {

  @Override
  public void updateJob(String description, Set<Skill> skills, LocalDateTime endDateTime) {
    getContext().setEndDateTime(endDateTime);
    getContext().setDescription(description);
    getContext().setSkills(skills);
    final String message = String.format("Job #%s was updated%nDescription: %s%nSkills: %s%nEnd datetime: %s", getContext(), description, skills, endDateTime);
    logger.info(message);
  }

}
