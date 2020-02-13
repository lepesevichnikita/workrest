package org.klaster.webapplication.service;
/*
 * practice
 *
 * 12.02.2020
 *
 */

import java.util.LinkedList;
import java.util.List;

/**
 * AbstractRecommendationService
 *
 * @author Nikita Lepesevich
 */

public abstract class AbstractRecommendationService<S, T> implements RecommendationService<S, T> {

  private List<T> targets;

  public AbstractRecommendationService() {
    targets = new LinkedList<>();
  }

  @Override
  public void add(T target) {
    targets.add(target);
  }

  @Override
  public List<T> getAll() {
    return targets;
  }
}
