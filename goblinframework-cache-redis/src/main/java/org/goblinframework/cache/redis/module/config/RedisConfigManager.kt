package org.goblinframework.cache.redis.module.config

import org.goblinframework.api.common.Singleton
import org.goblinframework.api.core.GoblinManagedObject

@Singleton
class RedisConfigManager private constructor() : GoblinManagedObject(), RedisConfigManagerMXBean {

  companion object {
    @JvmField val INSTANCE = RedisConfigManager()
  }

  private val configParser = RedisConfigParser()

  init {
    configParser.initialize()
  }

  fun getRedisConfig(name: String): RedisConfig? {
    return configParser.getFromBuffer(name)
  }

  fun getRedisConfigs(): List<RedisConfig> {
    return configParser.asList()
  }

  override fun disposeBean() {
    configParser.dispose()
  }
}