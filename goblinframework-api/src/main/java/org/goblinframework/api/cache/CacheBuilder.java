package org.goblinframework.api.cache;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CacheBuilder {

  @NotNull
  CacheSystem system();

  @Nullable
  Cache cache(@NotNull String name);

}
