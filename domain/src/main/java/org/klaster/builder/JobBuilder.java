package org.klaster.builder;/*
 * practice
 *
 * 11.02.2020
 *
 */

import java.time.LocalDateTime;
import java.util.Set;
import org.klaster.model.context.Job;
import org.klaster.model.controller.EmployerProfile;
import org.klaster.model.entity.Skill;

/**
 * JobBuilder
 *
 * @author Nikita Lepesevich
 */

public interface JobBuilder extends Builder<Job> {

  JobBuilder setEmployerProfile(EmployerProfile employerProfile);

  JobBuilder setDescription(String description);

  JobBuilder setEndDateTime(LocalDateTime endDateTime);

  JobBuilder setSkills(Set<Skill> skills);
}
