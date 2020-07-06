package org.goblinframework.queue.consumer

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.queue.api.QueueConsumer
import org.goblinframework.queue.api.QueueConsumerMXBean
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@Singleton
class QueueConsumerManager : GoblinManagedObject(), QueueConsumerManagerMXBean {

  companion object {
    @JvmField val INSTANCE = QueueConsumerManager()
  }

  private val locker = ReentrantReadWriteLock()
  private val consumers = mutableListOf<QueueConsumer>()

  override fun getConsumers(): Array<QueueConsumerMXBean> {
    locker.read {
      return consumers.map { it as QueueConsumerMXBean }.toTypedArray()
    }
  }

  fun register(consumer: QueueConsumer) {
    locker.write { consumers.add(consumer) }
  }

  fun start() {
    locker.write { consumers.forEach { it.start() } }
  }

  fun stop() {
    locker.write { consumers.forEach { it.stop() } }
  }

  override fun initializeBean() {
    locker.write { consumers.forEach { (it as GoblinManagedObject).initialize() } }
  }

  override fun disposeBean() {
    locker.write { consumers.forEach { (it as GoblinManagedObject).dispose() } }
  }
}