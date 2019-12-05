package org.goblinframework.core.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.compression.CompressorManager
import org.goblinframework.core.container.ContainerRefreshedEventListener
import org.goblinframework.core.module.management.ConfigManagement
import org.goblinframework.core.module.management.CoreManagement
import org.goblinframework.core.module.management.EventManagement
import org.goblinframework.core.serialization.SerializerManager
import org.goblinframework.core.system.GoblinModule
import org.goblinframework.core.system.IModule
import org.goblinframework.core.system.ModuleFinalizeContext
import org.goblinframework.core.system.ModuleInstallContext

@Install
class CoreModule : IModule {

  override fun id(): GoblinModule {
    return GoblinModule.CORE
  }

  override fun managementEntrance(): String? {
    return "/goblin/core/index.do"
  }

  override fun install(ctx: ModuleInstallContext) {
    ctx.subscribeEventListener(ContainerRefreshedEventListener.INSTANCE)
    ctx.registerManagementController(CoreManagement.INSTANCE)
    ctx.registerManagementController(ConfigManagement.INSTANCE)
    ctx.registerManagementController(EventManagement.INSTANCE)
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    CompressorManager.INSTANCE.dispose()
    SerializerManager.INSTANCE.dispose()
  }
}