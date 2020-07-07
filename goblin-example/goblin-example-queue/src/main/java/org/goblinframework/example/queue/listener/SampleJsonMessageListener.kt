package org.goblinframework.example.queue.listener

import org.goblinframework.queue.GoblinMessage
import org.goblinframework.queue.QueueSystem
import org.goblinframework.queue.api.GoblinConsumerMode
import org.goblinframework.queue.api.GoblinQueueConsumer
import org.goblinframework.queue.api.GoblinQueueConsumers
import org.goblinframework.queue.api.QueueMessageListener
import javax.inject.Named

/**
 * @author changyuan.liu
 * @since 2020/7/7
 */
@Named
@GoblinQueueConsumers(
        consumers = [GoblinQueueConsumer(system = QueueSystem.KFK, config = "default", queue = "test.example.json.queue")],
        mode = GoblinConsumerMode.PUBSUB,
        maxPermits = 2
)
class SampleJsonMessageListener : QueueMessageListener {
    override fun handle(message: GoblinMessage?) {
        println(message)
    }
}