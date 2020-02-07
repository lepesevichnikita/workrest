package org.klaster.builder;/*
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
