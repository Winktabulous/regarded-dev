package com.near_reality.game.content.gauntlet.npc

import com.near_reality.game.content.gauntlet.gauntletStrongMonsterKills
import com.near_reality.game.content.gauntlet.gauntletWeakMonsterKills
import com.near_reality.game.content.gauntlet.map.GauntletMap
import com.zenyte.game.util.Direction
import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.player.Player

class GauntletNPC(id: Int, tile: Location?, facing: Direction?, radius: Int, private val map: GauntletMap) :
    NPC(id, tile, facing, radius) {

    init {
        isSpawned = true
        supplyCache = false
    }

    override fun canMove(fromX: Int, fromY: Int, direction: Int): Boolean {
        val next = Location(fromX + Utils.DIRECTION_DELTA_X[direction], fromY + Utils.DIRECTION_DELTA_Y[direction], plane)
        return !map.inSafeRoom(next) && super.canMove(fromX, fromY, direction)
    }

    override fun onMovement() {
        super.onMovement()
        if (getCombat().underCombat() && map.inSafeRoom(location))
            getCombat().removeTarget()
    }

    override fun onFinish(source: Entity?) {
        reset()
        finish()
    }

    override fun onDeath(source: Entity?) {
        super.onDeath(source)

        if (source is Player) {
            val demiBoss = GauntletMonsterType.values().find { id == it.npcId || id == it.corruptedNpcId }
            if (demiBoss != null) {
                when (demiBoss) {
                    GauntletMonsterType.RAT,
                    GauntletMonsterType.SPIDER,
                    GauntletMonsterType.BAT -> source.gauntletWeakMonsterKills++
                    GauntletMonsterType.UNICORN,
                    GauntletMonsterType.SCORPION,
                    GauntletMonsterType.WOLF -> source.gauntletStrongMonsterKills++
                    else -> {}
                }
            }
        }
        drop(middleLocation)
    }

}
