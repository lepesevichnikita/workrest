package org.klaster.model.state.job;

import org.klaster.model.context.Job;
import org.klaster.model.state.general.AbstractJobState;

/**
 * PublishedJobState
 *
 * @author Nikita Lepesevich
 */

public class PublishedJobState extends AbstractJobState {

  public PublishedJobState(Job context) {
    super(context);
  }
}
