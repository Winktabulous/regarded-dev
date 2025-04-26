package com.near_reality.game.content.dt2.npc.leviathan

import com.near_reality.game.content.dt2.area.LeviathanInstance
import com.near_reality.game.content.dt2.npc.DT2BossDifficulty
import com.near_reality.game.content.dt2.npc.entangled
import com.zenyte.game.content.boss.BossRespawnTimer
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId.THE_LEVIATHAN
import com.zenyte.game.world.entity.npc.NpcId.THE_LEVIATHAN_12215
import com.zenyte.game.world.entity.npc.combat.CombatScript
import com.zenyte.game.world.entity.npc.combatdefs.NPCCombatDefinitions
import com.zenyte.game.world.entity.npc.combatdefs.StatType
import com.zenyte.game.world.entity.player.Player

internal class LeviathanCombat(
    tile: Location,
    private val instance: LeviathanInstance,
    val difficulty: DT2BossDifficulty = instance.difficulty
) : NPC(
        if(difficulty == DT2BossDifficulty.AWAKENED)
            THE_LEVIATHAN_12215
        else
            THE_LEVIATHAN,
        tile,
        true
), CombatScript {

    override fun isForceAggressive(): Boolean = true
    override fun getRespawnDelay(): Int = BossRespawnTimer.LEVIATHAN.timer.toInteger()

    init {
        setDamageCap(200)
        possibleTargets.add(instance.player)
        instance.player.hpHud.open(id, maxHitpoints)
    }

    override fun attack(target: Entity): Int {
        val defs: NPCCombatDefinitions = this.getCombatDefinitions()

        val decreaseInSpeed = if (hitpointsAsPercentage < 20) 1 else 0
        return defs.attackSpeed - decreaseInSpeed
    }

    override fun postHitProcess(hit: Hit) {
        if (hit.source is Player) {
            instance.player.hpHud.updateValue(getHitpoints())
        }
    }

    override fun processNPC() {
        super.processNPC()
        if(instance.player.entangled) {
            val healAmt = if(difficulty == DT2BossDifficulty.AWAKENED) 7 else 3
            this.applyHit(Hit(healAmt, HitType.HEALED))
        }
    }

    override fun onFinish(source: Entity?) {
        super.onFinish(source)
        setRespawnTask()
    }

    override fun getCombatDefinitions(): NPCCombatDefinitions {
        val defs = super.getCombatDefinitions()

        val defenseReduction = 70 - ((hitpointsAsPercentage / 100) * 70)
        val baseDefense = when(difficulty) {
            DT2BossDifficulty.NORMAL -> 215
            DT2BossDifficulty.AWAKENED -> 268
            DT2BossDifficulty.QUEST -> 180
        }

        val strengthIncrease = ((hitpointsAsPercentage / 100) * 70)
        val baseStrength = when(difficulty) {
            DT2BossDifficulty.NORMAL -> 270
            DT2BossDifficulty.AWAKENED -> 391
            DT2BossDifficulty.QUEST -> 210
        }

        defs.statDefinitions.set(StatType.DEFENCE, baseDefense - defenseReduction)
        defs.statDefinitions.set(StatType.STRENGTH, baseStrength + strengthIncrease)
        return defs
    }

    private fun getMaxHitValue(): Int {
        return if (difficulty == DT2BossDifficulty.AWAKENED) 86 else 87
    }
}