package com.near_reality.game.content.gauntlet.npc.definitions

import com.near_reality.scripts.npc.definitions.NPCDefinitionsScript
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.combatdefs.AggressionType
import com.zenyte.game.world.entity.npc.combatdefs.AttackType.MAGIC
import com.zenyte.game.world.entity.npc.combatdefs.AttackType.MELEE
import com.zenyte.game.world.entity.npc.combatdefs.AttackType.RANGED
import com.zenyte.game.world.entity.npc.combatdefs.ImmunityType
import com.zenyte.game.world.entity.npc.combatdefs.NPCCombatDefinitions
import java.util.*

class CrystallineNpcs : NPCDefinitionsScript() {
    init {
        NpcId.CRYSTALLINE_RAT.rat {
            stats { combatStats = intArrayOf(28, 32, 6, 6, 0) }
            attack { maxHit = 4 }
        }
        NpcId.CORRUPTED_RAT.rat {
            stats { combatStats = intArrayOf(42, 48, 6, 6, 0) }
            attack { maxHit = 14 }
        }

        NpcId.CRYSTALLINE_SPIDER.spider {
            stats { combatStats = intArrayOf(32, 26, 4, 4, 0) }
            attack { maxHit = 4 }
        }
        NpcId.CORRUPTED_SPIDER.spider {
            stats { combatStats = intArrayOf(48, 39, 4, 4, 0) }
            attack { maxHit = 14 }
        }

        NpcId.CRYSTALLINE_BAT.bat {
            stats { combatStats = intArrayOf(24, 68, 2, 2, 0) }
            attack { maxHit = 8 }
        }
        NpcId.CORRUPTED_BAT.bat {
            stats { combatStats = intArrayOf(36, 102, 2, 2, 0) }
            attack { maxHit = 11 }
        }

        NpcId.CRYSTALLINE_UNICORN.unicorn {
            stats { combatStats = intArrayOf(50, 48, 24, 24, 0) }
            attack { maxHit = 6 }
        }
        NpcId.CORRUPTED_UNICORN.unicorn {
            stats { combatStats = intArrayOf(75, 72, 24, 24, 0) }
            attack { maxHit = 11 }
        }

        NpcId.CRYSTALLINE_SCORPION.scorpion {
            stats { combatStats = intArrayOf(48, 106, 18, 18, 0) }
            attack { maxHit = 11 }
        }
        NpcId.CORRUPTED_SCORPION.scorpion {
            stats { combatStats = intArrayOf(72, 159, 18, 18, 0) }
            attack { maxHit = 17 }
        }

        NpcId.CRYSTALLINE_WOLF.wolf {
            stats { combatStats = intArrayOf(106, 66, 22, 22, 0) }
            attack { maxHit = 8 }
        }
        NpcId.CORRUPTED_WOLF.wolf {
            stats { combatStats = intArrayOf(159, 99, 22, 22, 0) }
            attack { maxHit = 19 }
        }

        NpcId.CRYSTALLINE_BEAR.bear {
            stats {
                combatStats = intArrayOf(98, 98, 98, 98, 98)
                aggressiveStats = intArrayOf(54, 106, 0, 0, 0, 0)
            }
            attack { maxHit = 28 }
        }
        NpcId.CORRUPTED_BEAR.bear {
            stats {
                combatStats = intArrayOf(147, 147, 98, 98, 147)
                aggressiveStats = intArrayOf(81, 132, 0, 0, 0, 0)
            }
            attack { maxHit = 48 }
        }

        NpcId.CRYSTALLINE_DRAGON.dragon(1701, 1703) {
            stats {
                combatStats = intArrayOf(98, 98, 98, 98, 98)
                aggressiveStats = intArrayOf(0, 0, 54, 106, 0, 0)
            }
            attack { maxHit = 28 }
        }
        NpcId.CORRUPTED_DRAGON.dragon(1702, 1704) {
            stats {
                combatStats = intArrayOf(147, 147, 98, 98, 147)
                aggressiveStats = intArrayOf(0, 0, 146, 226, 0, 0)
            }
            attack { maxHit = 48 }
        }

        NpcId.CRYSTALLINE_DARK_BEAST.darkBeast(1610) {
            stats {
                combatStats = intArrayOf(98, 98, 98, 98, 98)
                aggressiveStats = intArrayOf(0, 0, 0, 0, 54, 106)
            }
            attack { maxHit = 28 }
        }
        NpcId.CORRUPTED_DARK_BEAST.darkBeast(1606) {
            stats {
                combatStats = intArrayOf(147, 147, 98, 98, 147)
                aggressiveStats = intArrayOf(0, 0, 0, 0, 81, 132)
            }
            attack { maxHit = 48 }
        }

        NpcId.CRYSTALLINE_HUNLLEF.crystallineHunllef()
        NpcId.CRYSTALLINE_HUNLLEF_9022.crystallineHunllef()
        NpcId.CRYSTALLINE_HUNLLEF_9023.crystallineHunllef()
        NpcId.CRYSTALLINE_HUNLLEF_9024.crystallineHunllef()
        NpcId.CORRUPTED_HUNLLEF.corruptedHunllef()
        NpcId.CORRUPTED_HUNLLEF_9036.corruptedHunllef()
        NpcId.CORRUPTED_HUNLLEF_9037.corruptedHunllef()
        NpcId.CORRUPTED_HUNLLEF_9038.corruptedHunllef()
    }

    fun Int.rat(block: NPCCombatDefinitions.() -> Unit) = base(14) {
        attack {
            type = MELEE
            animation = Animation(4933)
        }
        block { animation = Animation(4934) }
        spawn { deathAnimation = Animation(4935) }
        block()
    }

    fun Int.spider(block: NPCCombatDefinitions.() -> Unit) = base(12) {
        attack {
            type = MELEE
            animation = Animation(8339)
        }
        spawn { deathAnimation = Animation(8338) }
        block()
    }

    fun Int.bat(block: NPCCombatDefinitions.() -> Unit) = base(14) {
        attack {
            type = MELEE
            animation = Animation(4915)
        }
        block { animation = Animation(4916) }
        spawn { deathAnimation = Animation(4917) }
        block()
    }

    fun Int.unicorn(block: NPCCombatDefinitions.() -> Unit) = base(44) {
        attack {
            type = MELEE
            animation = Animation(6376)
        }
        block { animation = Animation(6375) }
        spawn { deathAnimation = Animation(6377) }
        block()
    }

    fun Int.scorpion(block: NPCCombatDefinitions.() -> Unit) = base(38) {
        attack {
            type = MELEE
            animation = Animation(6254)
        }
        block { animation = Animation(6255) }
        spawn { deathAnimation = Animation(6256) }
        block()
    }

    fun Int.wolf(block: NPCCombatDefinitions.() -> Unit) = base(52) {
        attack {
            type = MELEE
            animation = Animation(6579)
        }
        block { animation = Animation(6578) }
        spawn { deathAnimation = Animation(6576) }
        block()
    }

    fun Int.bear(block: NPCCombatDefinitions.() -> Unit) = base(100) {
        attack {
            type = MELEE
            animation = Animation(4925)
        }
        block { animation = Animation(4927) }
        spawn {
            deathAnimation = Animation(4929)
        }
        block()
    }

    fun Int.dragon(projectileGraphicsId: Int, impactGraphicsId: Int, block: NPCCombatDefinitions.() -> Unit) = base(100) {
        attackDistance = 5
        stats {
            combatStats = intArrayOf(100, 98, 98, 98, 98, 98)
            aggressiveStats = intArrayOf(0, 0, 54, 106, 0, 0)
        }
        attack {
            type = MAGIC
            maxHit = 28
            animation = Animation(84)
            projectile = Projectile(projectileGraphicsId, 36, 36, 41, 14, 3, 4, 5)
            impactGraphics = Graphics(impactGraphicsId)
        }
        block { animation = Animation(89) }
        spawn {
            deathAnimation = Animation(92)
        }
        block()
    }

    fun Int.darkBeast(projectileGraphicsId: Int, block: NPCCombatDefinitions.() -> Unit) = base(100) {
        attackDistance = 5
        stats {
            combatStats = intArrayOf(100, 98, 98, 98, 98, 98)
            aggressiveStats = intArrayOf(0, 0, 0, 0, 54, 106)
        }
        attack {
            type = RANGED
            animation = Animation(2731)
            projectile = Projectile(projectileGraphicsId, 36, 36, 41, 14, 3, 4, 5)
        }
        block { animation = Animation(2732) }
        spawn {
            deathAnimation = Animation(2733)
        }
        block()
    }

    fun Int.hunllef(hitpoints: Int, block: NPCCombatDefinitions.() -> Unit) = base(hitpoints) {
        attackDistance = 5
        attackSpeed = 5
        aggressionType = AggressionType.ALWAYS_AGGRESSIVE
        aggressionDistance = 16
        maximumDistance = 16
        immunityTypes = EnumSet.of(ImmunityType.POISON, ImmunityType.VENOM)
        attackStyle = RANGED
        block()
    }
    fun Int.crystallineHunllef() = hunllef(600) {
        stats {
            combatStats = intArrayOf(240, 240, 240, 240, 240)
            aggressiveStats = intArrayOf(76, 64, 76, 64, 76, 64)
            defensiveStats = intArrayOf(20, 20, 20, 20, 20)
        }
    }
    fun Int.corruptedHunllef() = hunllef(1000) {//nerfed to compensate the combat system
        stats {
            combatStats = intArrayOf(240, 240, 240, 240, 240)
            aggressiveStats = intArrayOf(76, 64, 76, 64, 76, 64)
            defensiveStats = intArrayOf(20, 20, 20, 20, 20)
        }
    }

    fun Int.base(hitPoints: Int, block: NPCCombatDefinitions.() -> Unit) = this {
        hitpoints = hitPoints
        slayerLevel = 0
        attackSpeed = 4
        attackDistance = 1
        maximumDistance = 10
        aggressionDistance = 5
        aggressionType = AggressionType.ALWAYS_AGGRESSIVE
        targetType = Entity.EntityType.PLAYER
        spawn {
            respawnDelay = -1
        }
        block()
    }

}
