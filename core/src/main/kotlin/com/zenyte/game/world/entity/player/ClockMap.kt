package com.zenyte.game.world.entity.player

import com.zenyte.game.gameClock
import com.zenyte.game.world.entity.Entity
import kotlin.math.max
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @author Kris | 27/03/2022
 */
class ClockMap {
    private val map = mutableMapOf<String, Int>()
    operator fun get(name: String): Int = map.getOrDefault(name, 0)
    operator fun set(name: String, value: Int) = map.put(name, value)
    fun increment(name: String, value: Int) {
        set(name, gameClock() + value)
    }

    fun future(name: String): Boolean = get(name) > gameClock()
    fun now(name: String): Boolean = get(name) == gameClock()
    fun past(name: String): Boolean = get(name) < gameClock()
    fun upcoming(name: String): Boolean = get(name) == gameClock() + 1
    fun remaining(name: String): Int = max(0, get(name) - gameClock())
}

fun clock(key: String): ReadWriteProperty<Entity, Int> = object : ReadWriteProperty<Entity, Int> {
    override fun getValue(thisRef: Entity, property: KProperty<*>): Int = thisRef.clocks[key]

    override fun setValue(thisRef: Entity, property: KProperty<*>, value: Int) {
        thisRef.clocks[key] = value
    }
}
