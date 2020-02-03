package org.klaster.model;

/**
 * State
 *
 * workrest
 *
 * 03.02.2020
 *
 * @author Nikita Lepesevich
 */

public interface State<C extends Context> {

  C getContext();
}
