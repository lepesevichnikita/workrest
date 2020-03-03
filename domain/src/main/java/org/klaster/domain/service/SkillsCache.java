package org.klaster.domain.service;

/*
 * practice
 *
 * 12.02.2020
 *
 */

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.klaster.domain.model.entity.Skill;

/**
 * SkillsCache
 *
 * @author Nikita Lepesevich
 */

public class SkillsCache {

  private static SkillsCache instance;
  private Map<String, Skill> skills;

  private SkillsCache() {
    skills = Collections.synchronizedMap(new LinkedHashMap<>());
  }

  public static SkillsCache getInstance() {
    if (instance == null) {
      synchronized (SkillsCache.class) {
        instance = new SkillsCache();
      }
    }
    return instance;
  }

  public Skill get(String skillName) {
    return skills.get(skillName);
  }

  public Set<Skill> getAll(String... skillNames) {
    return Arrays.stream(skillNames)
                 .map(this::get)
                 .collect(Collectors.toSet());
  }

  public Skill add(String skillName) {
    return skills.computeIfAbsent(skillName, (name) -> {
      Skill newSkill = new Skill();
      newSkill.setName(name);
      return newSkill;
    });
  }


  public Set<Skill> addAll(String... skillNames) {
    return Arrays.stream(skillNames)
                 .map(this::add)
                 .collect(Collectors.toSet());
  }
}
