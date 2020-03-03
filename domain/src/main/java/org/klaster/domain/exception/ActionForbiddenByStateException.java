package org.klaster.domain.exception;

/*
 * org.klaster.domain.exception
 *
 * workrest
 *
 * 3/4/20
 *
 * Copyright(c) JazzTeam
 */

import org.klaster.domain.model.state.general.AbstractState;
import org.klaster.domain.util.MessageUtil;

public class ActionForbiddenByStateException extends RuntimeException {

  public ActionForbiddenByStateException(String action, AbstractState state) {
    super(MessageUtil.getActionForbiddenByStateMessage(action, state));
  }
}
