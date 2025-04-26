package com.near_reality.game.content.wilderness.king_black_dragon

import com.zenyte.game.content.skills.magic.spells.teleports.structures.LeverTeleport
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject

/**
 * @author Tommeh | 29 mei 2018 | 21:00:05
 * @see [Rune-Server profile](https://www.rune-server.ee/members/tommeh/)}
 */
@Suppress("unused")
class KingBlackDragonExitObject : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        LeverTeleport(
            KingBlackDragonInstance.outsideTile,
            `object`,
            "... and teleport out of the Dragon's lair.",
            null
        ).teleport(player)
    }

    override fun getObjects(): Array<Any> =
        arrayOf(ObjectId.LEVER_1817)
}
