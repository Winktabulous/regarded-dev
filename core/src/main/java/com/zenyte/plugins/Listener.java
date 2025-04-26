package com.zenyte.plugins;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Kris | 18/10/2018 18:27
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Listener {

    ListenerType type();

}
