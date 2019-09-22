package org.goblinframework.management.server

import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject
import org.goblinframework.core.module.spi.ManagementServerLifecycle
import org.goblinframework.embedded.core.EmbeddedServerMode
import org.goblinframework.embedded.core.manager.EmbeddedServerManager
import org.goblinframework.embedded.core.setting.ServerSetting
import java.util.concurrent.atomic.AtomicReference

@GoblinManagedBean(type = "management")
class ManagementServerManager private constructor() : GoblinManagedObject(), ManagementServerLifecycle {

  companion object {
    private const val SERVER_NAME = "GoblinManagementServer"
    @JvmField val INSTANCE = ManagementServerManager()
  }

  private val setting = AtomicReference<ServerSetting>()

  @Synchronized
  override fun start() {
    if (this.setting.get() != null) {
      return
    }
    val setting = ServerSetting.builder()
        .name(SERVER_NAME)
        .mode(EmbeddedServerMode.JDK)
        .applyHandlerSetting {
          it.contextPath("/")
          it.servletHandler(ManagementServletHandler.INSTANCE)
        }
        .build()
    val serverManager = EmbeddedServerManager.INSTANCE
    serverManager.createServer(setting).start()
    this.setting.set(setting)
  }

  override fun stop() {
    setting.getAndSet(null)?.run {
      val serverManager = EmbeddedServerManager.INSTANCE
      serverManager.closeServer(this.name())
    }
  }

  override fun isRunning(): Boolean {
    return setting.get() != null
  }

  class Installer : ManagementServerLifecycle by INSTANCE
}