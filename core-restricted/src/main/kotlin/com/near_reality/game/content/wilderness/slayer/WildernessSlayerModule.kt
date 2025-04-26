package com.near_reality.game.content.wilderness.slayer

import com.google.common.eventbus.Subscribe
import com.near_reality.game.content.wilderness.event.hot_zone.WildernessHotZoneEvent
import com.near_reality.game.world.PlayerEvent
import com.near_reality.game.world.hook
import com.zenyte.game.content.skills.slayer.SlayerMaster
import com.zenyte.game.item.ItemId
import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.player.Player
import com.zenyte.plugins.events.ServerLaunchEvent
import mgi.types.config.items.ItemDefinitions

/**
 * Handles wilderness specific rewards for tasks completed for Krysilia.
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
object WildernessSlayerModule {

    @JvmStatic
    @Subscribe
    fun onServerLaunchEvent(event: ServerLaunchEvent) {
        event.worldThread.hook<PlayerEvent.SlayerTaskCompleted> {
            if (it.assignment.master == SlayerMaster.KRYSTILIA) {
                tryUpgradeEmblem(it.player)
                rewardBloodMoney(it.player)
                rewardLarransKey(it.player)
            }
        }
    }

    private fun tryUpgradeEmblem(player: Player) {
        val inventory = player.inventory
        val bestUpgradeableEmblemInInventory = WildernessSlayerEmblem.entries
            .reversed()
            .drop(1) // drop tier 10 since we cannot upgrade that
            .firstOrNull { inventory.containsItem(it.id) }
        val nextEmblem = bestUpgradeableEmblemInInventory?.nextOrNull()
        if (nextEmblem == null)
            player.sendMessage("No emblems were able to get upgraded when completing the Wilderness slayer assignment.")
        else {
            inventory.deleteItem(bestUpgradeableEmblemInInventory.id, 1)
            inventory.addItem(nextEmblem.id, 1)
            player.sendMessage("Krystilia has upgraded your ${ItemDefinitions.nameOf(nextEmblem.id)} as a reward for completing a Wilderness slayer assignment.")
        }
    }

    private fun rewardBloodMoney(player: Player) {
        var bloodMoneyRewardAmount = Utils.random(10, 50)
        if (WildernessHotZoneEvent.inHotZone(player)) {
            bloodMoneyRewardAmount *= 2
            player.sendMessage("You received double blood money for completing the task in a hot zone.")
        }
        player.inventory.addOrDrop(ItemId.BLOOD_MONEY, bloodMoneyRewardAmount)
    }

    private fun rewardLarransKey(player: Player) {
        player.inventory.addOrDrop(ItemId.LARRANS_KEY, 1)
    }
}
