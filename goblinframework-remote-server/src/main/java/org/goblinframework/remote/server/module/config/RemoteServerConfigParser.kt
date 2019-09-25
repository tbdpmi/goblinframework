package org.goblinframework.remote.server.module.config

import org.goblinframework.api.common.Singleton
import org.goblinframework.core.config.BufferedConfigParser
import org.goblinframework.core.config.ConfigManager
import org.goblinframework.core.mapper.JsonMapper
import org.goblinframework.core.util.NetworkUtils

@Singleton
class RemoteServerConfigParser private constructor()
  : BufferedConfigParser<RemoteServerConfig>() {

  companion object {
    private const val CONFIG_NAME = "RemoteServerConfig"
    val INSTANCE = RemoteServerConfigParser()
  }

  override fun initializeBean() {
    val configMapping = ConfigManager.INSTANCE.getMapping()
    (configMapping["remote"] as? Map<*, *>)?.run {
      val remote = this
      (remote["server"] as? Map<*, *>)?.run {
        val server = this
        val mapper = JsonMapper.getDefaultObjectMapper()
        val cm = mapper.convertValue(server, RemoteServerConfigMapper::class.java)
        putIntoBuffer(CONFIG_NAME, RemoteServerConfig(cm))
      }
    }
  }

  fun getRemoteServerConfig(): RemoteServerConfig? {
    return getFromBuffer(CONFIG_NAME)
  }

  override fun doProcessConfig(config: RemoteServerConfig) {
    val mapper = config.mapper
    mapper.host ?: kotlin.run {
      mapper.host = NetworkUtils.getLocalAddress()
    }
    mapper.port ?: kotlin.run {
      mapper.port = NetworkUtils.RANDOM_PORT
    }
  }
}