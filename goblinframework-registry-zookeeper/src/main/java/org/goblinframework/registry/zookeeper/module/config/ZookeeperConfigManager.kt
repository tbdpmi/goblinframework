package org.goblinframework.registry.zookeeper.module.config

import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject

@GoblinManagedBean("REGISTRY.ZOOKEEPER")
class ZookeeperConfigManager : GoblinManagedObject(), ZookeeperConfigManagerMXBean {

  companion object {
    @JvmField val INSTANCE = ZookeeperConfigManager()
  }

  private val configParser = ZookeeperConfigParser()

  init {
    configParser.initialize()
  }

  fun getZookeeperConfig(name: String): ZookeeperConfig? {
    return configParser.getFromBuffer(name)
  }

  fun initialize() {}

  fun destroy() {
    unregisterIfNecessary()
    configParser.destroy()
  }
}