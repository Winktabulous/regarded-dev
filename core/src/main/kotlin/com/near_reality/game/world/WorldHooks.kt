package com.near_reality.game.world

import org.slf4j.LoggerFactory
import kotlin.reflect.KClass
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

/**
 * Systems for handling world hooks, mainly useful for plugin system.
 *
 * @author Stan van der Bend
 */
@OptIn(ExperimentalTime::class)
class WorldHooks(private val slowHookDuration: Duration) {

    constructor() : this(10.milliseconds)

    private val logger = LoggerFactory.getLogger(WorldHooks::class.java)
    private val hooks = mutableMapOf<KClass<out WorldEvent>, MutableList<WorldEventListener<in WorldEvent>>>()

    /**
     * Submits the [event] to all its listeners.
     */
    fun post(event: WorldEvent) {
        val clazz = event::class
        hooks[clazz]?.forEach { listener ->
            val duration = measureTime {
                try {
                    listener.on(event)
                } catch (e: Throwable) {
                    logger.error("Failed to handle world hook {}. (listener={})", event, listener, e)
                }
            }
            if (duration > slowHookDuration)
                logger.warn("Slow world hook {} took {}ms to handle. (listener={})", event, duration, listener)
        }
    }

    /**
     * Registers the [listener] for [events][T].
     */
    fun<T : WorldEvent> register(javaClass: Class<out T>, listener: WorldEventListener<T>) {
        register(javaClass.kotlin, listener)
    }

    /**
     * Registers the [listener] for [events][T].
     */
    @Suppress("UNCHECKED_CAST")
    fun<T : WorldEvent> register(kClass: KClass<out T>, listener: WorldEventListener<T>) {
        hooks
            .getOrPut(kClass) { mutableListOf() }
            .add(listener as WorldEventListener<in WorldEvent>)

        /*
        Register sealed subclasses, if any, in case we have listeners that needs
        to trigger on child classes of a sealed world hook type.
         */
        if (kClass.isSealed)
            for (sub in kClass.sealedSubclasses)
                register(sub, listener)
    }

    /**
     * Removes the [listener] for [events][T].
     */
    fun<T : WorldEvent> remove(javaClass: Class<out T>, listener: WorldEventListener<T>) {
        remove(javaClass.kotlin, listener)
    }

    /**
     * Removes the [listener] for [events][T].
     */
    @Suppress("UNCHECKED_CAST")
    fun<T : WorldEvent> remove(kClass: KClass<out T>, listener: WorldEventListener<T>) {

        val listenerList = hooks[kClass]
        if (listenerList != null) {
            listenerList.remove(listener as WorldEventListener<in WorldEvent>)
            if (listenerList.isEmpty())
                hooks.remove(kClass)
        }

        if (kClass.isSealed)
            for (sub in kClass.sealedSubclasses)
                remove(sub, listener)
    }

    /**
     * Check if there are any listeners for [events][T].
     */
    fun<T : WorldEvent> hasListenersFor(javaClass: Class<out T>) =
        hasListenersFor(javaClass.kotlin)

    /**
     * Check if there are any listeners for [events][T].
     */
    fun<T : WorldEvent> hasListenersFor(kClass: KClass<out T>) =
        hooks.containsKey(kClass)

}
