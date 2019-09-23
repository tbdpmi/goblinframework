package org.goblinframework.core.event;

import org.goblinframework.api.event.GoblinEvent;
import org.goblinframework.api.event.GoblinEventContext;
import org.goblinframework.api.event.GoblinEventFuture;
import org.goblinframework.api.event.GoblinEventListener;
import org.goblinframework.core.event.boss.EventBusBoss;
import org.goblinframework.core.event.dsl.GoblinCallback;
import org.goblinframework.core.event.dsl.GoblinCallbackEvent;
import org.goblinframework.core.event.dsl.GoblinCallbackFuture;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract public class GoblinEventBus {
  public static final Logger LOGGER = LoggerFactory.getLogger("goblin.core.EventBus");

  public static void subscribe(@NotNull GoblinEventListener listener) {
    EventBusBoss.INSTANCE.subscribe(listener);
  }

  public static void unsubscribe(@NotNull GoblinEventListener listener) {
    EventBusBoss.INSTANCE.unsubscribe(listener);
  }

  @NotNull
  public static GoblinEventFuture publish(@NotNull GoblinEvent event) {
    return EventBusBoss.INSTANCE.publish(event);
  }

  @NotNull
  public static GoblinEventFuture publish(@NotNull String channel, @NotNull GoblinEvent event) {
    return EventBusBoss.INSTANCE.publish(channel, event);
  }

  @NotNull
  public static <E> GoblinCallbackFuture<E> execute(@NotNull GoblinCallback<E> callback) {
    GoblinCallbackFuture<E> future = new GoblinCallbackFuture<>();
    GoblinCallbackEvent event = new GoblinCallbackEvent(callback);
    publish(event).addListener(f -> {
      GoblinEventContext context;
      try {
        context = f.getUninterruptibly();
      } catch (Throwable ex) {
        future.complete(null, ex);
        return;
      }
      @SuppressWarnings("unchecked") E result = (E) context.getExtension("GoblinCallback.Result");
      future.complete(result);
    });
    return future;
  }
}
