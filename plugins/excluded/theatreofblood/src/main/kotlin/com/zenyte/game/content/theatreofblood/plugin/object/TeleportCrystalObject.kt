package com.zenyte.game.content.theatreofblood.plugin.`object`

import com.zenyte.game.content.theatreofblood.RewardRegistry
import com.zenyte.game.content.theatreofblood.VerSinhazaArea
import com.zenyte.game.content.theatreofblood.room.reward.RewardRoom
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject

/**
 * @author Jire
 */
class TeleportCrystalObject : ObjectAction {

	override fun handleObjectAction(
		player: Player,
		obj: WorldObject,
		name: String,
		optionId: Int,
		option: String
	) {
		player.options("Make sure you've collected all your stuff!") {
			"Leave the Theatre." {
                val party = VerSinhazaArea.getParty(player)
				if (party != null) {
					val raid = party.raid
					if (raid != null) {
						raid.leave(player, "You fought well.", false)
						val room = player.area as? RewardRoom
						val rewards = room?.playersLoot?.get(player)
						// if the chest still has some loot
						if (rewards != null) {
							// move it to a global map
							val registry = RewardRegistry.getRewardRegistry()
								registry.addRewardContainerForPlayer(player, rewards)
						}
					}
				}
            }
			"Stay in the Theatre."()
		}
	}

	override fun getObjects() = TeleportCrystalObject.objects

	private companion object {

		val objects = arrayOf(ObjectId.TELEPORT_CRYSTAL)

	}

}