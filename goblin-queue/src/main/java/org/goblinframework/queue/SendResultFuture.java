package org.goblinframework.queue;

import org.goblinframework.api.concurrent.GoblinFuture;
import org.goblinframework.core.concurrent.GoblinFutureImpl;

import java.util.concurrent.atomic.AtomicInteger;

public class SendResultFuture extends GoblinFutureImpl<Long> {

  private AtomicInteger producerNums;

  public SendResultFuture() {
    this(1);
  }

  public SendResultFuture(int producerNums) {
    this.producerNums = new AtomicInteger(producerNums);
  }

  @Override
  public GoblinFuture<Long> complete(Long result) {
    int n = producerNums.decrementAndGet();
    if (n == 0) {
      super.complete(result);
    }
    return this;
  }
}
