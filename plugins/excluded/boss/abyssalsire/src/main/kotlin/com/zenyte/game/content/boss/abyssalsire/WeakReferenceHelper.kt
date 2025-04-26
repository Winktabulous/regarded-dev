package com.zenyte.game.content.boss.abyssalsire

import java.lang.ref.WeakReference

/**
 * @author Jire
 */
internal object WeakReferenceHelper {

    fun WeakReference<*>?.exists() = this != null && get() != null

    inline operator fun <T : Any, R> WeakReference<T>?.invoke(whenExists: (T) -> R) =
        this?.get()?.let(whenExists)

}