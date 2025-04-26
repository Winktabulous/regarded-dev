package com.near_reality.plugins.item.actions

import com.zenyte.game.item.Item
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.Container

/**
 * @author Jire
 */
data class ItemOptionHandler(
    val player: Player,
    val item: Item,
    val container: Container,
    val slotID: Int
)