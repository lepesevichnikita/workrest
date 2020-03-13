package org.klaster.domain.builder.general;

/*
 * workrest
 *
 * 13.03.2020
 *
 */

import org.klaster.domain.model.context.Job;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.JobMessage;

/**
 * JobMessageBuilder
 *
 * @author Nikita Lepesevich
 */

public interface JobMessageBuilder extends Builder<JobMessage> {

  JobMessageBuilder setText(String text);

  JobMessageBuilder setAuthor(User author);

  JobMessageBuilder setJob(Job job);
}
