package com.near_reality.content.group_ironman.item

import com.near_reality.content.group_ironman.player.groupIronHelmTeleportLastTime
import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.content.skills.magic.spells.teleports.Teleport
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportType
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.world.entity.Location
import java.util.concurrent.TimeUnit

class GroupIronHelmItemAction : ItemActionScript() {

    val cooldownSeconds = 30

    init {
        items(ItemId.GROUP_IRON_HELM, ItemId.HARDCORE_GROUP_IRON_HELM)

        "teleport" {
            val now = System.currentTimeMillis()
            val previous = player.groupIronHelmTeleportLastTime
            val timeSince = TimeUnit.MILLISECONDS.toSeconds(now - previous)
            if (timeSince >= cooldownSeconds) {
                player.groupIronHelmTeleportLastTime = System.currentTimeMillis()
                val teleport: Teleport = object : Teleport {
                    override fun getType(): TeleportType  = TeleportType.NEAR_REALITY_PORTAL_TELEPORT
                    override fun getDestination(): Location = Location(3104, 3029, 0)
                    override fun getLevel(): Int  = 0
                    override fun getExperience(): Double = 0.0
                    override fun getRandomizationDistance(): Int = 0
                    override fun getRunes(): Array<Item> = emptyArray()
                    override fun getWildernessLevel(): Int = 0
                    override fun isCombatRestricted(): Boolean = false
                }
                teleport.teleport(player)
            } else {
                val waitSeconds = cooldownSeconds - timeSince
                // TODO: find correct dialogue
                player.sendMessage("You must wait $waitSeconds more seconds.")
            }
        }
    }

}