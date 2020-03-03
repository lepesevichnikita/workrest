package org.klaster.domain.builder.general;

/*
 * workrest
 *
 * 03.03.2020
 *
 */

import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.AbstractProfile;

/**
 * ProfileBuilder
 *
 * @author Nikita Lepesevich
 */

public interface ProfileBuilder<P extends AbstractProfile> extends Builder<P> {

  ProfileBuilder<P> setDescription(String description);

  ProfileBuilder<P> setOwner(User owner);
}
