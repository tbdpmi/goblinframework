package org.goblinframework.queue.consumer.builder

import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.queue.QueueSystem
import org.goblinframework.queue.api.QueueConsumer
import org.goblinframework.queue.api.QueueConsumerMXBean
import org.goblinframework.queue.consumer.QueueConsumerDefinition
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@GoblinManagedBean(type = "Queue")
class QueueConsumerBuilderDelegator
internal constructor(private val delegator: QueueConsumerBuilder)
  : GoblinManagedObject(), QueueConsumerBuilder, QueueConsumerBuilderMXBean {

  private val lock = ReentrantReadWriteLock()
  private val buffer = mutableMapOf<String, QueueConsumerDelegator>()

  override fun system(): QueueSystem {
    return delegator.system()
  }

  override fun consumer(definition: QueueConsumerDefinition): QueueConsumer {
    val name = definition.location.toString()

    lock.read { buffer[name] }?.let { return it }
    lock.write {
      buffer[name]?.let { return it }
      val consumer = delegator.consumer(definition)?.let { QueueConsumerDelegator(it) }
      buffer[name] = consumer
      return consumer
    }
  }

  override fun getSystem(): QueueSystem {
    return system()
  }

  override fun getConsumerList(): Array<QueueConsumerMXBean> {
    lock.read {
      return buffer.values.mapNotNull { it }.toTypedArray()
    }
  }


}