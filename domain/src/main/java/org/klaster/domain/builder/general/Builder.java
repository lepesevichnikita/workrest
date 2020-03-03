package org.klaster.domain.builder.general;/*
 * workrest
 *
 * 07.02.2020
 *
 */

/**
 * Builder
 *
 * @author Nikita Lepesevich
 */

public interface Builder<T> {

  void reset();

  T build();

}
