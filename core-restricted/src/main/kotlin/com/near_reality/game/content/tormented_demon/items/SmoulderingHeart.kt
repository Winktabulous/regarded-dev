package com.near_reality.game.content.tormented_demon.items

import com.zenyte.game.content.consumables.ConsumableEffects
import com.zenyte.game.item.ItemId.SMOULDERING_HEART
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.variables.TickVariable
import com.zenyte.game.world.flooritem.FloorItem
import com.zenyte.plugins.flooritem.FloorItemPlugin

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-19
 */
class SmoulderingHeart : FloorItemPlugin {

    override fun getItems(): IntArray =
        intArrayOf(SMOULDERING_HEART)

    override fun handle(player: Player?, item: FloorItem?, optionId: Int, option: String?) {
        if (player == null || item == null) return
        player.animation = Animation.STOMP
        // play graphic

        player.variables.schedule(500, TickVariable.SMOULDERING_HEART)
        ConsumableEffects.applyHeart(player)
        player.sendMessage("You crush the heart. Dark energy swirls around you...")
        World.destroyFloorItem(item)
    }
}