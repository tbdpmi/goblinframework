package org.goblinframework.registry.core.manager

import org.goblinframework.api.core.Disposable
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.api.registry.Registry

@GoblinManagedBean(type = "registry")
internal class ManagedRegistry
internal constructor(private val delegator: Registry)
  : GoblinManagedObject(), Registry by delegator, RegistryMXBean {

  override fun disposeBean() {
    (delegator as? Disposable)?.dispose()
  }
}