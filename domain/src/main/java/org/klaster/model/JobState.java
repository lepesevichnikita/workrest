package org.klaster.model;

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

