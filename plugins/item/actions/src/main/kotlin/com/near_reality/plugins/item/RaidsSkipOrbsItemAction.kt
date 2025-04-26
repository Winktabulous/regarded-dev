package com.near_reality.plugins.item

import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.content.chambersofxeric.Raids1BypassTask
import com.zenyte.game.content.chambersofxeric.map.RaidArea
import com.zenyte.game.item.ItemId
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.entity.attribute
import com.zenyte.game.world.entity.player.Player

class RaidsSkipOrbsItemAction : ItemActionScript() {
    init {
        //, ItemId.ORB_OF_AMASCUT, ItemId.ORB_OF_BLOOD

        items(ItemId.ORB_OF_XERIC, ItemId.ORB_OF_BLOOD)

        "Activate" {
            when(item.id) {
                ItemId.ORB_OF_XERIC -> {
                    if(player.area !is RaidArea || (player.area is RaidArea && player.raid.isPresent && player.raid.get().originalPlayers.size != 0)) {
                        player.sendMessage("You must be in the appropriate area to use this, by yourself.")
                    } else {
                        if(!player.pendingRaidBypass) {
                            player.sendDeveloperMessage("Scheduling Raids 1 Bypass Task")
                            WorldTasksManager.schedule(Raids1BypassTask(player), 0, 0)
                        }
                    }
                }
                ItemId.ORB_OF_BLOOD -> {
                    if(player.area != null && player.area.isRaidArea) {
                        if(player.getNumericTemporaryAttribute("TOB_playerCount") != 1) {
                            player.sendMessage("You must be alone in the Theatre to use this.")
                        } else {
                            player.sendDeveloperMessage("Scheduling Raids 2 Bypass Task")
                            WorldTasksManager.schedule(Raids2BypassTask(player), 0)
                        }
                    } else {
                        player.sendMessage("You must be in the appropriate area to use this, by yourself.")
                    }
                }
            }
        }
    }
}
