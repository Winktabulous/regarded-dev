package com.near_reality.game.content.gauntlet.item.actions

import com.near_reality.game.content.gauntlet.GauntletStage
import com.near_reality.game.content.gauntlet.gauntlet
import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.ItemId
import com.zenyte.game.world.entity.Location

class TeleportCrystalItemAction : ItemActionScript() {
    init {
        items(ItemId.TELEPORT_CRYSTAL, ItemId.CORRUPTED_TELEPORT_CRYSTAL)

        "Activate" {
            val gauntlet = player.gauntlet

            when {
                gauntlet == null ->
                    player.sendMessage("This item cannot be used outside of the Gauntlet.")
                gauntlet.stage == GauntletStage.BOSS ->
                    player.sendMessage("That won't help you now. At this point in the Gauntlet, you win or die.")
                else -> {
                    player.inventory.deleteItem(item.id, 1)
                    val map = gauntlet.map
                    val x = map.getBaseXForNode(map.startingRoomX)
                    val y = map.getBaseYForNode(map.startingRoomY)
                    val teleport = GauntletCrystalTeleport(Location(x + 4, y + 4, 1))
                    teleport.teleport(player)
                }
            }
        }
    }
}
