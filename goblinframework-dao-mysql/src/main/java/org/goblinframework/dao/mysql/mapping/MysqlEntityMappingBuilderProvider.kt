package org.goblinframework.dao.mysql.mapping

import org.goblinframework.api.annotation.Install
import org.goblinframework.dao.core.annotation.GoblinDatabaseSystem
import org.goblinframework.dao.core.mapping.EntityMappingBuilder
import org.goblinframework.dao.core.mapping.EntityMappingBuilderProvider

@Install
class MysqlEntityMappingBuilderProvider : EntityMappingBuilderProvider {

  override fun getDatabaseSystem(): GoblinDatabaseSystem {
    return GoblinDatabaseSystem.MSQ
  }

  override fun getEntityMappingBuilder(): EntityMappingBuilder {
    return EntityMappingBuilder(MysqlEntityFieldScanner.INSTANCE, MysqlEntityFieldNameResolver.INSTANCE)
  }
}