package org.goblinframework.dao.mysql.persistence.internal;

import org.bson.types.ObjectId;
import org.goblinframework.api.dao.GoblinId;
import org.goblinframework.core.util.RandomUtils;
import org.goblinframework.dao.mysql.exception.GoblinMysqlPersistenceException;
import org.goblinframework.database.core.mapping.EntityIdField;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

abstract public class MysqlPersistencePrimaryKeySupport<E, ID> extends MysqlPersistenceTableSupport<E, ID> {

  private static final EnumSet<GoblinId.Generator> supported;

  static {
    supported = EnumSet.of(GoblinId.Generator.NONE, GoblinId.Generator.AUTO_INC, GoblinId.Generator.OBJECT_ID);
  }

  protected final GoblinId.Generator idGenerator;

  protected MysqlPersistencePrimaryKeySupport() {
    EntityIdField idField = entityMapping.idField;
    assert idField != null;
    GoblinId annotation = idField.getAnnotation(GoblinId.class);
    assert annotation != null;

    GoblinId.Generator generator = annotation.value();
    if (!supported.contains(generator)) {
      throw new GoblinMysqlPersistenceException("Id generator not supported: " + generator);
    }
    this.idGenerator = generator;

    registerBeforeInsertListener(e -> {
      generateEntityId(e);
      requireEntityId(e);
    });
  }

  protected void generateEntityId(@NotNull E entity) {
    ID id = getEntityId(entity);
    if (id == null && idGenerator == GoblinId.Generator.OBJECT_ID) {
      Class<?> idClass = entityMapping.idClass;
      if (idClass == ObjectId.class) {
        entityMapping.setId(entity, new ObjectId());
      } else if (idClass == String.class) {
        entityMapping.setId(entity, RandomUtils.nextObjectId());
      }
    }
  }

  protected void requireEntityId(@NotNull E entity) {
    if (idGenerator == GoblinId.Generator.AUTO_INC) {
      return;
    }
    ID id = getEntityId(entity);
    if (id == null) {
      throw new GoblinMysqlPersistenceException("Entity id is required");
    }
  }
}