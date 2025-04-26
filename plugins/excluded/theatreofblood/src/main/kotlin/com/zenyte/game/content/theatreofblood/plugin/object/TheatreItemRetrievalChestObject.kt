package com.zenyte.game.content.theatreofblood.plugin.`object`

import com.zenyte.game.GameInterface
import com.zenyte.game.content.ItemRetrievalService
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject

/**
 * @author Jire
 */
class TheatreItemRetrievalChestObject : ObjectAction {

	override fun handleObjectAction(
		player: Player,
		obj: WorldObject,
		name: String,
		optionId: Int,
		option: String
	) {
		val service = player.retrievalService
		if (service.type != ItemRetrievalService.RetrievalServiceType.THEATRE_OF_BLOOD || service.container.size == 0) {
			player.dialogue {
				plain("The chest seems to be empty. If it did have any of your items, but<br><br>you died before collecting them, they'll now be lost.")
			}
		} else {
			GameInterface.ITEM_RETRIEVAL_SERVICE.open(player)
		}
	}

	override fun getObjects() = TheatreItemRetrievalChestObject.objects

	private companion object {

		val objects = arrayOf(ObjectId.CHEST_32656)

	}

}