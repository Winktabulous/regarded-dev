package com.zenyte.plugins;

import java.lang.annotation.*;

/**
 * @author Jire
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface PluginPriority {

    int DEFAULT = 10_000;

    int value() default DEFAULT;

}
