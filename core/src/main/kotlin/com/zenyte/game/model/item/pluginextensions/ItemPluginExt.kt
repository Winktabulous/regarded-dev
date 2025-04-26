package com.zenyte.game.model.item.pluginextensions

import com.zenyte.game.item.Item
import com.zenyte.game.model.item.pluginextensions.ItemPlugin.OptionHandler
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.Container

/**
 * Context to pass [bindKt] lambda, represents the parameters of [OptionHandler.handle] function.
 */
data class OptionContext(val player: Player, val item: Item, val container: Container, val slotId: Int)

/**
 * Kotlin wrapper for [ItemPlugin.bind] function call.
 */
fun ItemPlugin.bindKt(option: String, handler: OptionContext.() -> Unit) {
    bind(option) { player, item, container, slotId ->
        handler.invoke(OptionContext(player, item, container, slotId))
    }
}