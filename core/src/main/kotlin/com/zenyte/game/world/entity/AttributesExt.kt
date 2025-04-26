package com.zenyte.game.world.entity

import com.google.gson.internal.LinkedTreeMap
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.login.LoginManager
import java.lang.ref.WeakReference
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.isSubclassOf

/*
 * Handles creation of Kotlin delegates for entity attributes,
 * these can only be persistent for player entities.
 *
 * @author Kris | 11/06/2022
 * @author Stan van der Bend
 */

inline fun <reified T: Any?> persistentAttribute(key: String, defaultValue: T? = null) =
    attribute<Player, T>(key, defaultValue, persistent = true)

inline fun <reified E : Entity, reified T : Any?> attribute(
    key: String,
    defaultValue: T? = null,
    persistent: Boolean = false,
): ReadWriteProperty<E, T> =
    attribute(key, persistent) { defaultValue as T }

inline fun <reified E : Entity, reified T : Any?> attribute(
    key: String,
    persistent: Boolean = false,
    crossinline defaultValueFunction: (E.() -> T),
): ReadWriteProperty<E, T> {

    if (persistent && !E::class.isSubclassOf(Player::class))
        error("Persistent attributes are only supported for Player entities (key=$key)")

    return object : ReadWriteProperty<E, T> {

        override fun getValue(thisRef: E, property: KProperty<*>): T {
            var value = if (persistent)
                getPersistentAttributes(thisRef)[key]
            else
                thisRef.temporaryAttributes[key]
            if (value == null){
                value = defaultValueFunction(thisRef)
                if (value != null) {
                    if (persistent)
                        getPersistentAttributes(thisRef)[key] = value
                    else
                        thisRef.temporaryAttributes[key] = value
                }
            }
            return (when {
                value is Number && value::class != T::class -> when(T::class) {
                    Byte::class -> value.toByte()
                    Short::class -> value.toShort()
                    Int::class -> value.toInt()
                    Float::class -> value.toFloat()
                    Double::class -> value.toDouble()
                    Long::class -> value.toLong()
                    else -> error("Invalid number type (key=$key, type=${T::class})")
                }
                value is List<*> ||
                value is LinkedTreeMap<*, *> -> {
                    val gson = LoginManager.gson.get()
                    val json = gson.toJson(value)
                    val deserializedValue: T = gson.fromJson(json, T::class.java)
                    if (persistent)
                        getPersistentAttributes(thisRef)[key] = deserializedValue
                    else
                        thisRef.temporaryAttributes[key] = deserializedValue
                    deserializedValue
                }
                else -> value
            }) as T
        }

        override fun setValue(thisRef: E, property: KProperty<*>, value: T) {
            if (value == null){
                if (persistent)
                    getPersistentAttributes(thisRef).remove(key)
                else
                    thisRef.temporaryAttributes.remove(key)
            } else {
                if (persistent)
                    getPersistentAttributes(thisRef)[key] = value
                else
                    thisRef.temporaryAttributes[key] = value
            }
        }

        private fun getPersistentAttributes(thisRef: E) = if (thisRef !is Player)
            error("Persistent attributes are only supported for Player entities (key=$key, ref=$thisRef)")
        else
            thisRef.attributes
    }
}


inline fun <reified T> weakReferenceAttribute(key: String): ReadWriteProperty<Entity, T?> =
    object : ReadWriteProperty<Entity, T?> {
        override fun getValue(thisRef: Entity, property: KProperty<*>): T? {
            val element = thisRef.temporaryAttributes[key]
            if (element is WeakReference<*>) {
                val result = element.get()
                if (result is T) {
                    return result
                }
            }
            return null
        }

        override fun setValue(thisRef: Entity, property: KProperty<*>, value: T?) {
            if (value == null) {
                thisRef.temporaryAttributes.remove(key)
            } else {
                thisRef.temporaryAttributes[key] = WeakReference(value)
            }
        }
    }
