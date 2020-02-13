package org.klaster.domain.model.entity;

/**
 * Skill
 *
 * @author Nikita Lepesevich
 */

public class Skill {

  private final String name;

  public Skill(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
