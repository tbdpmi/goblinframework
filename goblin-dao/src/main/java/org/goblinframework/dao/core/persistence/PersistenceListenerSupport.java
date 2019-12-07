package org.goblinframework.dao.core.persistence;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

abstract public class PersistenceListenerSupport<E, ID> extends PersistenceEntityMappingSupport<E, ID> {

  protected final List<BeforeInsertListener<E>> beforeInsertListeners = new ArrayList<>();

  protected PersistenceListenerSupport() {
    registerBeforeInsertListener(e -> {
      long millis = System.currentTimeMillis();
      touchCreateTime(e, millis);
      touchUpdateTime(e, millis);
      initializeRevision(e);
    });
  }

  public void registerBeforeInsertListener(@NotNull BeforeInsertListener<E> listener) {
    beforeInsertListeners.add(listener);
  }
}