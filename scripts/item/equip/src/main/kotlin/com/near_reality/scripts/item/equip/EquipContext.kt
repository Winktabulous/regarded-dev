package com.near_reality.scripts.item.equip

import com.zenyte.game.item.Item
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.Container

/**
 * Represents the context for handling an equip action.
 *
 * @author Stan van der Bend
 *
 * @param player    the [Player] subject to the equip action.
 * @param item      the [Item] being (un)equipped.
 */
sealed class EquipContext(
    val player: Player,
    val item: Item,
) {

    class Listener(
        player: Player,
        item: Item,
        val container: Container,
    ) : EquipContext(player, item)

    class Handler(
        player: Player,
        item: Item,
        val slotId: Int,
        val equipmentSlot: Int
    ) : EquipContext(player, item)

    class Login(
        player: Player,
        item: Item,
        val equipmentSlot: Int
    ) : EquipContext(player, item)
}
