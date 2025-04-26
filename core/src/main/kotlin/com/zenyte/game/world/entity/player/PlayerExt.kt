package com.zenyte.game.world.entity.player

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @author Kris | 16/06/2022
 */
fun varp(id: Int) = object : ReadWriteProperty<Player, Int> {
    override fun getValue(thisRef: Player, property: KProperty<*>): Int = thisRef.varManager.getValue(id)
    override fun setValue(thisRef: Player, property: KProperty<*>, value: Int) {
        thisRef.varManager.sendVarInstant(id, value)
    }
}

fun varbit(id: Int) = object : ReadWriteProperty<Player, Int> {
    override fun getValue(thisRef: Player, property: KProperty<*>): Int = thisRef.varManager.getBitValue(id)
    override fun setValue(thisRef: Player, property: KProperty<*>, value: Int) {
        thisRef.varManager.sendBitInstant(id, value)
    }
}

fun booleanVarp(id: Int) = object : ReadWriteProperty<Player, Boolean> {
    override fun getValue(thisRef: Player, property: KProperty<*>): Boolean = thisRef.varManager.getValue(id) == 1

    override fun setValue(thisRef: Player, property: KProperty<*>, value: Boolean) {
        thisRef.varManager.sendVarInstant(id, if (value) 1 else 0)
    }
}

fun booleanVarbit(id: Int) = object : ReadWriteProperty<Player, Boolean> {
    override fun getValue(thisRef: Player, property: KProperty<*>): Boolean = thisRef.varManager.getBitValue(id) == 1

    override fun setValue(thisRef: Player, property: KProperty<*>, value: Boolean) {
        thisRef.varManager.sendBitInstant(id, if (value) 1 else 0)
    }
}

inline fun<reified T> typeVarbit(id: Int, crossinline get: (Int) -> T, crossinline set: (T) -> Int): ReadWriteProperty<Player, T> {
    val varbitProperty  = varbit(id)
    return object : ReadWriteProperty<Player, T> {
        override fun getValue(thisRef: Player, property: KProperty<*>): T =
            get(varbitProperty.getValue(thisRef, property))

        override fun setValue(thisRef: Player, property: KProperty<*>, value: T) {
            set(value).let { varbitProperty.setValue(thisRef, property, it) }
        }
    }
}
