package com.near_reality.plugins.item.actions

import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import it.unimi.dsi.fastutil.ints.IntArraySet
import it.unimi.dsi.fastutil.ints.IntSet

/**
 * @author Jire
 */
abstract class ItemActionPlugin : ItemPlugin() {

    private val items: IntSet = IntArraySet()

    fun item(id: Int): Boolean =
        items.add(id)

    fun items(vararg ids: Int) {
        for (id in ids) {
            item(id)
        }
    }

    fun items(ids: Collection<Int>) {
        items.addAll(ids)
    }

    override fun getItems(): IntArray = items.toIntArray()

    override fun handle() {
        // covered by `init` in this
    }

    operator fun String.invoke(handler: OptionHandler) {
        bind(this, handler)
    }

    operator fun String.invoke(handler: BasicOptionHandler) {
        bind(this, handler)
    }

    operator fun String.invoke(handle: ItemOptionHandler.() -> Unit) {
        invoke { player, item, container, slotId ->
            ItemOptionHandler(player, item, container, slotId).handle()
        }
    }

    fun death(block: KotlinItemDeathPlugin.() -> Unit) {
        onDeath { player, item, protectedCount, deepWilderness, pvp ->
            val deathPlugin = KotlinItemDeathPlugin(player, item, protectedCount, deepWilderness, pvp)
            deathPlugin.block()
            deathPlugin.build()
        }
    }

}