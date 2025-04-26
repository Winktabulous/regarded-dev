package com.near_reality.game.content.dt2.npc.theduke

import com.zenyte.game.util.CollisionUtil
import com.zenyte.game.world.Position
import com.zenyte.game.world.entity.npc.CombatScriptsHandler
import com.zenyte.game.world.entity.npc.NPCCombat

class DukeCombatController(entity: DukeSucellusEntity) : NPCCombat(entity) {
    override fun outOfRange(
        targetPosition: Position?,
        maximumDistance: Int,
        targetSize: Int,
        checkDiagonal: Boolean,
    ): Boolean {
        return false
    }

    override fun combatAttack(): Int {
        if (target == null) {
            return 0
        }
        addAttackedByDelay(target)
        return CombatScriptsHandler.specialAttack(npc, target)
    }


}