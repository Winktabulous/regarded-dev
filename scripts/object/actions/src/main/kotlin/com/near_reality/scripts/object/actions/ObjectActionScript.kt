package com.near_reality.scripts.`object`.actions

import com.near_reality.scripts.`object`.ObjectScript
import com.zenyte.game.world.entity.pathfinding.events.RouteEvent
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.WorldObject
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import kotlin.script.experimental.annotations.KotlinScript

/**
 * @author Jire
 */
@KotlinScript(
    "Object Action Script",
    fileExtension = "objectaction.kts",
    compilationConfiguration = ObjectActionCompilation::class
)
abstract class ObjectActionScript : ObjectScript, ObjectAction {

    private val intHandlers: Int2ObjectMap<ObjectHandlerContext.() -> Unit> = Int2ObjectOpenHashMap()
    private val stringHandlers: Object2ObjectMap<String, ObjectHandlerContext.() -> Unit> = Object2ObjectOpenHashMap()

    private var orHandler: (ObjectHandlerContext.() -> Unit)? = null

    private lateinit var routeEventProvider: ObjectHandlerContext.(Runnable) -> RouteEvent<*, *>

    operator fun Int.invoke(handle: ObjectHandlerContext.() -> Unit) {
        intHandlers.put(this, handle)
    }

    operator fun String.invoke(handle: ObjectHandlerContext.() -> Unit) {
        stringHandlers[this] = handle
    }

    fun or(handle: ObjectHandlerContext.() -> Unit) {
        orHandler = handle
    }

    fun provideRouteEvent(provider: ObjectHandlerContext.(Runnable) -> RouteEvent<*, *>) {
        routeEventProvider = provider
    }

    override fun handle(player: Player, `object`: WorldObject, name: String?, optionId: Int, option: String?) {
        if (this::routeEventProvider.isInitialized) {
            val context = ObjectHandlerContext(player, `object`, name, optionId, option)
            player.routeEvent = routeEventProvider(context, getRunnable(player, `object`, name, optionId, option))
        } else
            super.handle(player, `object`, name, optionId, option)
    }

    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String?,
        optionId: Int,
        option: String?
    ) {
        val context = ObjectHandlerContext(player, `object`, name, optionId, option)

        val intHandler = intHandlers.get(`object`.id)
        if (intHandler != null) {
            intHandler(context)
            return
        }

        if (name != null) {
            val stringHandler = stringHandlers[name]
            if (stringHandler != null) {
                stringHandler(context)
                return
            }
        }

        orHandler?.invoke(context)
    }

    override fun getObjects(): Array<Any> = ObjectArrayList<Any>(intHandlers.size + stringHandlers.size).apply {
        addAll(intHandlers.keys)
        addAll(stringHandlers.keys)
    }.toTypedArray()

}
