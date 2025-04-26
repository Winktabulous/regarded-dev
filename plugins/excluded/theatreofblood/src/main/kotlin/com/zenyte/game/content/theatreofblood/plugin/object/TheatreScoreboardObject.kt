package com.zenyte.game.content.theatreofblood.plugin.`object`

import com.zenyte.game.GameInterface
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject

class TheatreScoreboardObject : ObjectAction {

	override fun handleObjectAction(
		player: Player,
		obj: WorldObject,
		name: String,
		optionId: Int,
		option: String
	) {
		GameInterface.TOB_STATS.open(player)
	}

	override fun getObjects() = TheatreScoreboardObject.objects

	private companion object {

		val objects = arrayOf(ObjectId.SCOREBOARD_32987)

	}

}
