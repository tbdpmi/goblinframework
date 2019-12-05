package org.goblinframework.database.mongo.support

import org.bson.types.ObjectId
import org.goblinframework.api.database.Id
import org.goblinframework.core.util.RandomUtils
import org.goblinframework.database.core.GoblinDatabaseException
import java.util.*

abstract class MongoPrimaryKeySupport<E, ID> : MongoNamespaceSupport<E, ID>() {

  companion object {
    private val SUPPORTED = EnumSet.of(Id.Generator.NONE, Id.Generator.OBJECT_ID)
  }

  private val generator: Id.Generator

  init {
    val idField = entityMapping.idField
    val annotation = idField.getAnnotation(Id::class.java)!!
    generator = annotation.value
    if (!SUPPORTED.contains(generator)) {
      throw GoblinDatabaseException("Id.Generator [$generator] not supported")
    }
  }

  fun generateEntityId(entity: E) {
    val id = getEntityId(entity)
    if (id == null && generator === Id.Generator.OBJECT_ID) {
      val idClass = entityMapping.idClass
      if (idClass == ObjectId::class.java) {
        entityMapping.setId(entity, ObjectId())
      } else if (idClass == String::class.java) {
        entityMapping.setId(entity, RandomUtils.nextObjectId())
      }
    }
  }

  protected fun requireEntityId(entity: E) {
    if (generator === Id.Generator.AUTO_INC) {
      return
    }
    getEntityId(entity) ?: throw GoblinDatabaseException("Entity id is required")
  }
}