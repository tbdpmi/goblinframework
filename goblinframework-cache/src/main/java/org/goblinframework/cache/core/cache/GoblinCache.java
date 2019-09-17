package org.goblinframework.cache.core.cache;

import org.jetbrains.annotations.Nullable;

public interface GoblinCache extends CacheSystemLocationProvider {

  @Nullable
  <T> GetResult<T> get(@Nullable String key);

  @Nullable
  Boolean delete(@Nullable String key);

  @Nullable
  <T> Boolean add(@Nullable String key,
                  int expirationInSeconds,
                  @Nullable T value);

}
