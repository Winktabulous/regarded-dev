package com.near_reality.game.world

import com.zenyte.game.world.WorldThread

/**
 * Kotlin shortcut for hooking a listener for [world events][T].
 *
 * @param worldEventListener the [event listener][WorldEventListener].
 */
inline fun<reified T : WorldEvent> WorldThread.hook(worldEventListener: WorldEventListener<T>){
    hooks.register(T::class.java, worldEventListener)
}
