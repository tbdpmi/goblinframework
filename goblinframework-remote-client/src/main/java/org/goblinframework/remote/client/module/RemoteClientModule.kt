package org.goblinframework.remote.client.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.system.GoblinSubModule
import org.goblinframework.core.system.ISubModule
import org.goblinframework.core.system.ModuleFinalizeContext
import org.goblinframework.core.system.ModuleInitializeContext
import org.goblinframework.remote.client.dispatcher.request.RemoteClientRequestDispatcher
import org.goblinframework.remote.client.dispatcher.response.RemoteClientResponseDispatcher
import org.goblinframework.remote.client.module.config.RemoteClientConfigManager
import org.goblinframework.remote.client.module.runtime.RemoteServiceInformationManager
import org.goblinframework.remote.client.service.RemoteServiceClientManager
import org.goblinframework.remote.client.service.RemoteServiceClientWarmUpManager
import org.goblinframework.remote.client.transport.RemoteTransportClientManager

@Install
class RemoteClientModule : ISubModule {

  override fun id(): GoblinSubModule {
    return GoblinSubModule.REMOTE_CLIENT
  }

  override fun initialize(ctx: ModuleInitializeContext) {
    RemoteClientConfigManager.INSTANCE.initialize()
    RemoteServiceInformationManager.INSTANCE.initialize()
    RemoteClientResponseDispatcher.INSTANCE.initialize()
    RemoteClientRequestDispatcher.INSTANCE.initialize()
    RemoteTransportClientManager.INSTANCE.initialize()
    RemoteServiceClientWarmUpManager.INSTANCE.initialize()
    RemoteServiceClientManager.INSTANCE.initialize()
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    RemoteServiceClientManager.INSTANCE.dispose()
    RemoteServiceClientWarmUpManager.INSTANCE.dispose()
    RemoteTransportClientManager.INSTANCE.dispose()
    RemoteClientRequestDispatcher.INSTANCE.dispose()
    RemoteClientResponseDispatcher.INSTANCE.dispose()
    RemoteServiceInformationManager.INSTANCE.dispose()
    RemoteClientConfigManager.INSTANCE.dispose()
  }
}