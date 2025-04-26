package com.near_reality.game.content.elven.obj

import com.near_reality.game.content.crystal.TrahaearnMineRocks
import com.zenyte.game.content.skills.mining.actions.Mining
import com.zenyte.game.content.skills.mining.actions.Prospect
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.WorldObject

/**
 * Handles the mining of rocks in the Trahaearn mine.
 *
 * @author Stan van der Bend
 */
@Suppress("UNUSED")
class TrahaearnMineRocksAction : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String,
    ) {
        val rocks = TrahaearnMineRocks.values().find { it.objectIds.contains(`object`.id) }?:return
        when(option) {
            "Mine" -> player.actionManager.action = Mining(`object`, rocks.ore)
            "Prospect" -> player.actionManager.action = Prospect(`object`, rocks.ore)
        }
    }

    override fun getObjects() = TrahaearnMineRocks.getAllRockObjectIds().toTypedArray()
}
