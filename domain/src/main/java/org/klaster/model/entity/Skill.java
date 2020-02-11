package org.klaster.model.entity;

/**
 * Skill
 *
 * @author Nikita Lepesevich
 */

public class Skill {

  long id;

  private final String name;

  public Skill(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
