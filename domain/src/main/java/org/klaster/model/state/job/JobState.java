package org.klaster.model.state.job;

import org.klaster.model.context.Job;
import org.klaster.model.state.general.State;

/**
 * JobState
 *
 * @author Nikita Lepesevich
 */

public interface JobState extends State<Job> {

  void deleteJob();

  void finishJob();

  void startJob();
}

