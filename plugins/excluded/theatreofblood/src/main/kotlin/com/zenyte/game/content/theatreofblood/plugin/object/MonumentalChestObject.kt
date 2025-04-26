package com.zenyte.game.content.theatreofblood.plugin.`object`

import com.zenyte.game.GameInterface
import com.zenyte.game.content.theatreofblood.TheatreOfBloodRaid
import com.zenyte.game.content.theatreofblood.VerSinhazaArea
import com.zenyte.game.content.theatreofblood.room.reward.RewardRoom
import com.zenyte.game.content.theatreofblood.tobStats
import com.zenyte.game.content.theatreofblood.tobStatsHard
import com.zenyte.game.model.item.enums.RareDrop
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Colour
import com.zenyte.game.world.World
import com.zenyte.game.world.broadcasts.BroadcastType
import com.zenyte.game.world.broadcasts.WorldBroadcasts
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.Container
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.WorldObject

class MonumentalChestObject : ObjectAction {

	override fun handleObjectAction(
		player: Player,
		obj: WorldObject,
		name: String,
		optionId: Int,
		option: String
	) {
		val party = VerSinhazaArea.getParty(player) ?: return
		val raid = party.raid ?: return
		val room = player.area as? RewardRoom
		if (room != null) {
			val index = party.players.indexOf(player)
			val chestCoords = RewardRoom.chestCoords[index]
			val location = room.getBaseLocation(chestCoords[0], chestCoords[1])
			if (!obj.matches(location)) {
				player.sendMessage("This isn't your chest!")
				player.sendDeveloperMessage("obj=${obj}, loc=$location")
				return
			}

			val rewards = room.playersLoot[player]
			if (rewards != null) {
				if (openChests.contains(obj.id)) {
					handleOpen(player, rewards)
					return
				}

				handleClosed(player, obj, rewards, raid)
			}
		}
	}

	private fun handleClosed(player: Player, obj: WorldObject, rewards: Container, raid: TheatreOfBloodRaid) {
		player.animation = Animation(536)
		WorldTasksManager.schedule {
			val jackpot = RewardRoom.checkForJackpot(rewards)
			val transformId = if (jackpot) OPEN_CHEST_PURPLE else OPEN_CHEST_NORMAL
			World.replaceObject(obj, obj.transform(transformId))
			for (item in rewards.items.values) {
				player.collectionLog.add(item)
				if (RareDrop.contains(item)) {
					WorldBroadcasts.broadcast(player, BroadcastType.RARE_DROP, item, ".Theatre of Blood on chest ${if (raid.hardMode) player.tobStatsHard.completions else player.tobStats.completions}")
				}
			}

			handleOpen(player, rewards)
		}
	}

	private fun handleOpen(player: Player, rewards: Container) {
		player.packetDispatcher.sendUpdateItemContainer(rewards)
		GameInterface.TOB_REWARDS.open(player)
	}

	override fun getObjects() = arrayOf(
		33086, 33087, 33088, 33089, 33090, OPEN_CHEST_NORMAL, OPEN_CHEST_PURPLE
	)

	companion object {
		const val OPEN_CHEST_NORMAL = 32994
		const val OPEN_CHEST_PURPLE = 41746
		val openChests = setOf(OPEN_CHEST_NORMAL, OPEN_CHEST_PURPLE)
	}

}