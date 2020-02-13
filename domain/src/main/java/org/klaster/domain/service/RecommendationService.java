package org.klaster.domain.service;/*
 * practice
 *
 * 12.02.2020
 *
 */

import java.util.List;

/**
 * RecommendationService
 *
 * @author Nikita Lepesevich
 */

public interface RecommendationService<S, T> {

  List<T> getRecommended(S source, long limit);

  void add(T target);

  List<T> getAll();
}
