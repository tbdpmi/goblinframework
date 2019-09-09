package org.goblinframework.embedded.core;

import org.goblinframework.embedded.core.setting.ServerSetting;
import org.jetbrains.annotations.NotNull;

public interface EmbeddedServerFactory {

  @NotNull
  EmbeddedServerMode mode();

  @NotNull
  EmbeddedServer createEmbeddedServer(@NotNull ServerSetting setting);

}
