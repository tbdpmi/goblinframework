package org.goblinframework.cache.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheKeyPrefix {

  String prefix();

}
