package org.goblinframework.dao.mysql.client;

import com.zaxxer.hikari.HikariDataSource;
import org.goblinframework.core.mbean.GoblinManagedBean;
import org.goblinframework.core.mbean.GoblinManagedObject;
import org.goblinframework.core.util.ClassUtils;
import org.goblinframework.dao.mysql.module.config.DataSourceConfig;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@GoblinManagedBean(type = "DAO.MYSQL")
final class DataSource extends GoblinManagedObject implements DataSourceMXBean {

  private static final Constructor<?> dataSourceSpyConstructor;

  static {
    ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
    Constructor<?> constructor;
    try {
      Class<?> theClass = classLoader.loadClass("net.sf.log4jdbc.sql.jdbcapi.DataSourceSpy");
      constructor = theClass.getConstructor(javax.sql.DataSource.class);
    } catch (ClassNotFoundException | NoSuchMethodException ex) {
      constructor = null;
    }
    dataSourceSpyConstructor = constructor;
  }

  private final DataSourceConfig config;
  private final HikariDataSource hikariDataSource;
  private final javax.sql.DataSource effectiveDataSource;

  DataSource(@NotNull DataSourceConfig config,
             @NotNull HikariDataSource hikariDataSource) {
    this.config = config;
    this.hikariDataSource = hikariDataSource;

    javax.sql.DataSource effective;
    if (dataSourceSpyConstructor != null) {
      try {
        effective = (javax.sql.DataSource) dataSourceSpyConstructor.newInstance(hikariDataSource);
      } catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
        logger.warn("Failed to instantiate net.sf.log4jdbc.sql.jdbcapi.DataSourceSpy", ex);
        effective = hikariDataSource;
      }
    } else {
      effective = hikariDataSource;
    }
    this.effectiveDataSource = effective;
  }

  javax.sql.DataSource getDataSource() {
    return effectiveDataSource;
  }

  void destroy() {
    unregisterIfNecessary();
    hikariDataSource.close();
  }
}
