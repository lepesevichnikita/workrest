package org.klaster.model.entity;

import org.klaster.model.context.Job;

/**
 * JobSkill
 *
 * @author Nikita Lepesevich
 */

public class JobSkill {

  private final Job job;
  private final Skill skill;

  public JobSkill(Job job, Skill skill) {
    this.job = job;
    this.skill = skill;
  }

  public Job getJob() {
    return job;
  }

  public Skill getSkill() {
    return skill;
  }
}
