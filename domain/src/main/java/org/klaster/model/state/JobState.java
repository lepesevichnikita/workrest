package org.klaster.model.state;

import org.klaster.model.context.Job;

/**
 * JobState
 *
 * workrest
 *
 * 03.02.2020
 *
 * @author Nikita Lepesevich
 */

public interface JobState extends State<Job> {

  void deleteJob();

  void finishJob();

  void startJob();
}

