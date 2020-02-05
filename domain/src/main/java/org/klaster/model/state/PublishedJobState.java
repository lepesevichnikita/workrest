package org.klaster.model.state;

import org.klaster.model.context.Job;

/**
 * PublishedJobState
 *
 * workrest
 *
 * 03.02.2020
 *
 * @author Nikita Lepesevich
 */

public class PublishedJobState extends AbstractJobState {

  public PublishedJobState(Job context) {
    super(context);
  }
}
