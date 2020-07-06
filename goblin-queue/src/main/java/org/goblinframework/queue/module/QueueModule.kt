package org.goblinframework.queue.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.system.*
import org.goblinframework.queue.consumer.QueueConsumerManager
import org.goblinframework.queue.consumer.processor.QueueListenerProcessor
import org.goblinframework.queue.producer.processor.QueueProducerProcessor

@Install
class QueueModule : IModule {
    override fun id(): GoblinModule {
        return GoblinModule.QUEUE
    }

    override fun install(ctx: ModuleInstallContext) {
        ctx.createSubModules()
                .module(GoblinSubModule.QUEUE_KAFKA)
                .module(GoblinSubModule.QUEUE_RABBITMQ)
                .install(ctx)
        ctx.registerContainerBeanPostProcessor(QueueProducerProcessor.INSTANCE)
        ctx.subscribeEventListener(QueueListenerProcessor.INSTANCE)
    }

    override fun initialize(ctx: ModuleInitializeContext) {
        QueueChannelManager.INSTANCE.initialize()
        ctx.createSubModules()
                .module(GoblinSubModule.QUEUE_KAFKA)
                .module(GoblinSubModule.QUEUE_RABBITMQ)
                .initialize(ctx)
    }

    override fun finalize(ctx: ModuleFinalizeContext) {
        QueueChannelManager.INSTANCE.shutdown()
        QueueConsumerManager.INSTANCE.stop()
        ctx.createSubModules()
                .module(GoblinSubModule.QUEUE_KAFKA)
                .module(GoblinSubModule.QUEUE_RABBITMQ)
                .finalize(ctx)
    }
}