package com.near_reality.game.content.dt2.npc

import com.near_reality.scripts.npc.definitions.NPCDefinitionsScript
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.npc.NpcId.FLOATING_COLUMN
import com.zenyte.game.world.entity.npc.NpcId.FLOATING_COLUMN_12210
import com.zenyte.game.world.entity.npc.combatdefs.AggressionType.AGGRESSIVE
import com.zenyte.game.world.entity.npc.combatdefs.AggressionType.PASSIVE
import com.zenyte.game.world.entity.npc.combatdefs.AttackType.MELEE

class Dt2Npcs : NPCDefinitionsScript() {
    init {
        12223.vardorvis(DT2BossDifficulty.NORMAL)
        12224.vardorvis(DT2BossDifficulty.AWAKENED)

        12214.leviathan(DT2BossDifficulty.NORMAL)
        12215.leviathan(DT2BossDifficulty.AWAKENED)

        12191.duke(DT2BossDifficulty.NORMAL)
        12195.duke(DT2BossDifficulty.AWAKENED)

        FLOATING_COLUMN.pillar()
        FLOATING_COLUMN_12210.pillar()
    }

    fun Int.vardorvis(difficulty: DT2BossDifficulty) = invoke {
        val awakened = difficulty == DT2BossDifficulty.AWAKENED
        hitpoints = if(awakened) 1400 else 700
        attackSpeed = 4
        aggressionType = AGGRESSIVE
        aggressionDistance = 64
        stats {
            combatStats = intArrayOf(
                if(awakened) 420 else 280,
                if(awakened) 391 else 270,
                if(awakened) 268 else 215,
                215,
                0
            )
            aggressiveStats = intArrayOf(190, 10, 0, 0, 0)
            defensiveStats = intArrayOf(215, 65, 85, 580, 580)
        }
        attack {
            type = MELEE
            maxHit = if(awakened) 50 else 35
            animation = Animation(10340)
        }
        spawn {
            deathAnimation = Animation(10347)
        }
    }

    fun Int.leviathan(difficulty: DT2BossDifficulty) = invoke {
        val awakened = difficulty == DT2BossDifficulty.AWAKENED
        hitpoints = if(awakened) 2700 else 900
        attackSpeed = 4
        aggressionType = AGGRESSIVE
        aggressionDistance = 64
        stats {
            combatStats = intArrayOf(
                if(awakened) 525 else 300,
                if(awakened) 630 else 360,
                if(awakened) 287 else 250,
                if(awakened) 280 else 160,
                if(awakened) 280 else 160
            )
            aggressiveStats = intArrayOf(200, 22, 160, 300, 0)
            defensiveStats = intArrayOf(260, 190, 230, 280, 50)
        }
        attack {
            type = MELEE
            maxHit = if(awakened) 86 else 87
            animation = Animation(10281)
        }
        spawn {
            deathAnimation = Animation(10293)
        }
    }

    fun Int.duke(difficulty: DT2BossDifficulty) = invoke {
        val awakened = difficulty == DT2BossDifficulty.AWAKENED
        hitpoints = if(awakened) 1540 else 440
        attackSpeed = 5
        aggressionType = AGGRESSIVE
        aggressionDistance = 64
        stats {
            combatStats = intArrayOf(
                if(awakened) 435 else 280,
                if(awakened) 500 else 270,
                if(awakened) 316 else 215,
                465,
                0
            )
            aggressiveStats = intArrayOf(200, 38, 0, 0, 0)
            defensiveStats = intArrayOf(255, 65, 190, 440, 320)
        }

    }

    fun Int.pillar() = invoke {
        hitpoints = 100
        aggressionType = PASSIVE
        aggressionDistance = 0
        stats {
            combatStats = intArrayOf(
                0,
                0,
                0,
                0,
                0
            )
            aggressiveStats = intArrayOf(0, 0, 0, 0, 0)
            defensiveStats = intArrayOf(0, 0, 0, 0, 0)
        }
    }
}
