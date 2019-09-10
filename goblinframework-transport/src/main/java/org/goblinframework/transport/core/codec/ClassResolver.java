package org.goblinframework.transport.core.codec;

import io.netty.handler.codec.serialization.ClassResolvers;
import org.goblinframework.core.util.ClassUtils;
import org.jetbrains.annotations.NotNull;

final public class ClassResolver implements io.netty.handler.codec.serialization.ClassResolver {

  public static final ClassResolver INSTANCE = new ClassResolver();

  private final io.netty.handler.codec.serialization.ClassResolver delegator;

  private ClassResolver() {
    ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
    this.delegator = ClassResolvers.softCachingConcurrentResolver(classLoader);
  }

  @NotNull
  @Override
  public Class<?> resolve(String className) throws ClassNotFoundException {
    return delegator.resolve(className);
  }
}
