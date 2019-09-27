package org.goblinframework.dao.mysql.support;

import org.goblinframework.api.dao.GoblinDatabaseConnection;
import org.goblinframework.core.util.AnnotationUtils;
import org.goblinframework.core.util.ClassUtils;
import org.goblinframework.dao.mysql.client.MysqlClient;
import org.goblinframework.dao.mysql.client.MysqlClientManager;
import org.goblinframework.dao.mysql.client.MysqlMasterConnection;
import org.goblinframework.dao.mysql.client.MysqlSlaveConnection;
import org.goblinframework.dao.mysql.persistence.GoblinPersistenceException;
import org.jetbrains.annotations.NotNull;

abstract public class MysqlClientSupport<E, ID> extends MysqlEntityMappingSupport<E, ID> {

  private final MysqlClient client;

  protected MysqlClientSupport() {
    Class<?> clazz = ClassUtils.filterCglibProxyClass(getClass());
    GoblinDatabaseConnection annotation = AnnotationUtils.getAnnotation(clazz, GoblinDatabaseConnection.class);
    if (annotation == null) {
      throw new GoblinPersistenceException("No @UseMysqlClient presented on " + clazz.getName());
    }
    String name = annotation.name();
    MysqlClient client = MysqlClientManager.INSTANCE.getMysqlClient(name);
    if (client == null) {
      throw new GoblinPersistenceException("MysqlClient [" + name + "] not found");
    }
    this.client = client;
  }

  @NotNull
  public MysqlClient getClient() {
    return client;
  }

  @NotNull
  public MysqlMasterConnection getMasterConnection() {
    return client.getMasterConnection();
  }

  @NotNull
  public MysqlSlaveConnection createSlaveConnection() {
    return client.createSlaveConnection();
  }
}