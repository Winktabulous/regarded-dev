package com.near_reality.game.content.boss.nex.definitions

import com.near_reality.game.content.boss.nex.NexConstants.ANIMATION_BLOOD_REAVER_DEATH
import com.near_reality.game.content.boss.nex.NexConstants.ANIMATION_MAGE_DEATH
import com.near_reality.game.content.boss.nex.NexConstants.NPC_CRUOR_ID
import com.near_reality.game.content.boss.nex.NexConstants.NPC_FUMUS_ID
import com.near_reality.game.content.boss.nex.NexConstants.NPC_GLACIES_ID
import com.near_reality.game.content.boss.nex.NexConstants.NPC_UMBRA_ID
import com.near_reality.game.content.boss.nex.NexNPC
import com.near_reality.scripts.npc.definitions.NPCDefinitionsScript
import com.zenyte.game.world.entity.SoundEffect
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.combatdefs.AggressionType.AGGRESSIVE
import com.zenyte.game.world.entity.npc.combatdefs.AttackType.CRUSH
import com.zenyte.game.world.entity.npc.combatdefs.AttackType.MAGIC
import com.zenyte.game.world.entity.npc.combatdefs.AttackType.RANGED
import com.zenyte.game.world.entity.npc.combatdefs.ImmunityType
import java.util.*

class AncientNPCs : NPCDefinitionsScript() {

    init {
        NexNPC.ID.nex()
        NexNPC.NO_ATK.nex()
        NexNPC.SOULSPLIT.nex()
        NexNPC.DEFLECT_MELEE.nex()
        NexNPC.WRATH.nex()


        NPC_FUMUS_ID.bodyGuard(intArrayOf(25, 100, 100, 150, 50))
        NPC_UMBRA_ID.bodyGuard(intArrayOf(100, 25, 100, 150, 50))
        NPC_CRUOR_ID.bodyGuard(intArrayOf(100, 100, 100, 150, 25))
        NPC_GLACIES_ID.bodyGuard(intArrayOf(100, 100, 25, 150, 50))



        NpcId.BLOOD_REAVER.reaver()
        NpcId.BLOOD_REAVER_11294.reaver()



        NpcId.SPIRITUAL_WARRIOR_11290 {
            attackSpeed = 5
            attackDistance = 4
            aggressionDistance = 4
            maximumDistance = 16
            aggressionType = AGGRESSIVE
            hitpoints = 100
            stats {
                combatStats = intArrayOf(190, 190, 100, 50, 1)
                aggressiveStats = intArrayOf(0, 0, 0, 0, 0)
                defensiveStats = intArrayOf(100, 100, 80, 0, 250)
            }
            spawn {
                deathAnimation = Animation(836)
                deathSound = SoundEffect(3880, 10, 0, 1)
            }
            attack {
                type = CRUSH
                maxHit = 20
                animation = Animation(7054)
                startSound = SoundEffect(2508, 10, 0, 1)
            }
            block {
                animation = Animation(424)
                sound = SoundEffect(2508, 10, 0, 1)
            }
        }

        NpcId.SPIRITUAL_MAGE_11292 {
            attackSpeed = 4
            attackDistance = 10
            aggressionDistance = 10
            maximumDistance = 16
            aggressionType = AGGRESSIVE
            hitpoints = 125
            stats {
                combatStats = intArrayOf(1, 1, 100, 200, 1)
                aggressiveStats = intArrayOf(0, 0, 52, 0, 0)
                defensiveStats = intArrayOf(420, 400, 420, 200, 0)
            }
            spawn {
                deathSound = SoundEffect(3891, 10, 0, 1)
                deathAnimation = Animation(836)
            }
            attack {
                type = MAGIC
                maxHit = 38
                animation = Animation(1979)
            }
            block {
                animation = Animation(424)
                sound = SoundEffect(3878, 10, 0, 1)
            }
        }

        NpcId.SPIRITUAL_RANGER_11291 {
            attackSpeed = 6
            attackDistance = 8
            aggressionDistance = 8
            maximumDistance = 16
            aggressionType = AGGRESSIVE
            hitpoints = 110
            stats {
                combatStats = intArrayOf(1, 1, 100, 100, 190)
                aggressiveStats = intArrayOf(0, 0, 0, 0, 0)
                defensiveStats = intArrayOf(20, 0, 20, 300, 50)
            }
            spawn {
                deathSound = SoundEffect(3891, 10, 0, 1)
                deathAnimation = Animation(836)
            }
            attack {
                type = RANGED
                maxHit = 20
                animation = Animation(426)
            }
            block {
                animation = Animation(425)
                sound = SoundEffect(24, 10, 0, 1)
            }
        }
    }

    fun Int.reaver() = invoke {
        attackSpeed = 5
        attackDistance = 4
        aggressionDistance = 4
        maximumDistance = 16
        aggressionType = AGGRESSIVE
        hitpoints = 125
        stats {
            combatStats = intArrayOf(1, 1, 100, 190, 1)
            aggressiveStats = intArrayOf(0, 0, 0, 0, 0)
            defensiveStats = intArrayOf(20, 80, 120, 300, 55)
        }
        attack {
            type = MAGIC
            maxHit = 20
            animation = Animation(9194)
        }
        spawn {
            deathAnimation = Animation(ANIMATION_BLOOD_REAVER_DEATH)
        }
        block {
            animation = Animation(9196)
        }
    }

    fun Int.bodyGuard(defenciveBonuses: IntArray) = invoke {
        hitpoints = 500
        attackSpeed = 5
        aggressionType = AGGRESSIVE
        immunityTypes = EnumSet.allOf(ImmunityType::class.java)
        stats {
            combatStats = intArrayOf(1, 1, 200, 200, 1)
            aggressiveStats = intArrayOf(0, 0, 0, 25, 0)
            defensiveStats = defenciveBonuses
        }
        spawn {
            deathAnimation = Animation(ANIMATION_MAGE_DEATH)
        }
        attack {
            type = MAGIC
            maxHit = 29
        }
    }

    fun Int.nex() = invoke {
        hitpoints = 3400
        attackSpeed = 4
        aggressionType = AGGRESSIVE
        aggressionDistance = 64
        immunityTypes = EnumSet.allOf(ImmunityType::class.java)
        stats {
            combatStats = intArrayOf(315, 200, 260, 230, 350)
            aggressiveStats = intArrayOf(200, 20, 100, 22, 150)
            defensiveStats = intArrayOf(40, 140, 60, 300, 190)
        }
        spawn {
            deathAnimation = Animation(9184)
        }
    }

}