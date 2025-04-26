package com.zenyte.game.content.theatreofblood.plugin.`object`

import com.zenyte.game.content.theatreofblood.RewardRegistry
import com.zenyte.game.content.theatreofblood.tobStats
import com.zenyte.game.content.theatreofblood.tobStatsHard
import com.zenyte.game.model.item.enums.RareDrop
import com.zenyte.game.world.broadcasts.BroadcastType
import com.zenyte.game.world.broadcasts.WorldBroadcasts
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.ObjectId.*
import com.zenyte.game.world.`object`.WorldObject
import java.util.*

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-08-30
 */
class RewardChestObject : ObjectAction {

	override fun handleObjectAction(player: Player, obj: WorldObject, name: String, optionId: Int, option: String) {
		val registry = RewardRegistry.getRewardRegistry()
		val rewards = registry.getContainer(player)
		if (Objects.nonNull(rewards) && rewards?.isNotEmpty() == true) {
			var banked = false
			var totalSlots = 0
			rewards.forEach { item ->
				run {
					if (item.isStackable)
						totalSlots++
					else
						totalSlots += item.amount
				}
			}
			if (player.inventory.freeSlots >= totalSlots)
				rewards.forEach { reward ->
					player.inventory.addItem(reward)
					player.collectionLog.add(reward)
					if (RareDrop.contains(reward))
						WorldBroadcasts.broadcast(player, BroadcastType.RARE_DROP, reward, ".Theatre of Blood")
				}
			else {
				banked = true
				rewards.forEach { reward ->
					player.bank.add(reward)
					player.collectionLog.add(reward)
					if (RareDrop.contains(reward))
						WorldBroadcasts.broadcast(player, BroadcastType.RARE_DROP, reward, ".Theatre of Blood")
				}
			}
			if (registry.removedPlayerFromRewardMap(player)) {
				val container = if (banked) "Bank" else "Inventory"
				val message = String.format("Your rewards have been added to your %s", container)
				player.sendMessage(message)
			}
			else {
				// TODO: send a message to a dev log that something went wrong
			}
		}
		else
			player.sendMessage("You have nothing to collect.")
	}

	override fun getObjects() = arrayOf(
		REWARDS_CHEST_41437,
		REWARDS_CHEST_41436,
		REWARDS_CHEST_41435
	)
}