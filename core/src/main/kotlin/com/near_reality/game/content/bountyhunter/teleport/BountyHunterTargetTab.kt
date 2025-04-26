package com.near_reality.game.content.bountyhunter.teleport

import com.near_reality.game.content.bountyhunter.isBountyPaired
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import com.zenyte.game.world.entity.player.container.RequestResult

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-14
 */
class BountyHunterTargetTab : ItemPlugin() {
    override fun handle() {
        bind("Break") { player, item, _ ->
            if (player.isBountyPaired()) {
                if (TeleportToTarget.canTeleportToTarget(player)) {
                    if (player.inventory.deleteItem(item).result == RequestResult.SUCCESS)
                        TeleportToTarget.teleportToTarget(player)
                }
                else
                    player.sendMessage("You cannot teleport to your target right now.")
            }
            else
                player.sendMessage("You do not have a target to teleport right now.")
        }
    }

    override fun getItems(): IntArray = intArrayOf(ItemId.TARGET_TELEPORT)
}