package com.near_reality.game.content.custom

import com.zenyte.game.content.boons.impl.SlayersSovereignty
import com.zenyte.game.content.skills.slayer.SlayerMountType
import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot

/**
 * Handles custom effects that apply to slayer helmets.
 *
 * @author Stan van der Bend
 */
object SlayerHelmetEffects {

    /**
     * The amount to transform the outgoing damage by.
     */
    private const val DAMAGE_MULTIPLIER = 2.0

    /**
     * If the [target] is assigned as a slayer task to the [player],
     * there is a x/100 chance determined by [getBonusDamageChance] that the [damage]
     * is multiplied by [DAMAGE_MULTIPLIER].
     *
     * @return the [damage] or multiplied [damage].
     */
    fun rollBonusDamage(player: Player, target: Entity, damage: Double): Double {
        if(target is Player)
            return damage

        var damageModified = damage

        val headWear = player.equipment.getItem(EquipmentSlot.HELMET) ?: return damageModified
        if (!headWear.name.contains("slayer helmet", ignoreCase = true))
            return damageModified

        return if (Utils.random(100) == getBonusDamageChance(player))
            damageModified * DAMAGE_MULTIPLIER
        else
            damageModified
    }



    fun transformAccuracy(player: Player, target: Entity, acc: Double): Double {
        if(target is Player)
            return acc

        var accModified = acc
        val headWear = player.equipment.getItem(EquipmentSlot.HELMET) ?: return accModified
        if (!headWear.name.contains("slayer helmet", ignoreCase = true))
            return accModified

        return accModified
    }


    /**
     * If the [target] is assigned as a slayer task to the [player],
     * and the [player] is wearing a Green slayer helmet, they are immune to poison.
     */
    fun immuneToPoison(player: Player, target: Entity?): Boolean {
        if (wearsCombatAchievementSlayerHelmet(player))
            return true
        return (hydraHelmetNoAssignment(player, false) || greenHelmetNoAssignment(player, false)) && (player.slayer.isCurrentAssignment(target) || (target !is Player && player.hasBoon(SlayersSovereignty::class.java)))
    }

    fun greenHelmetNoAssignment(player: Player, checkImbued: Boolean): Boolean {
        return SlayerMountType.GREEN.mounted(player) || wearsSlayerHelmet(player, "green", checkImbued)
    }

    fun hydraHelmetNoAssignment(player: Player, checkImbued: Boolean): Boolean {
        return SlayerMountType.HYDRA.mounted(player) || wearsSlayerHelmet(player, "hydra", checkImbued)
    }

    /**
     * If the [target] is assigned as a slayer task to the [player],
     * and the [player] is wearing a Black slayer helmet, they are immune to dragon-fire.
     */
    fun immuneToDragonfire(player: Player, target: Entity): Boolean {
        if (wearsCombatAchievementSlayerHelmet(player))
            return true
        return (hydraHelmetNoAssignment(player, false) || blackHelmetNoAssignment(player, false)) && (player.slayer.isCurrentAssignment(target) || (target !is Player && player.hasBoon(SlayersSovereignty::class.java)))    }

    fun blackHelmetNoAssignment(player: Player, checkImbued: Boolean): Boolean {
        return SlayerMountType.BLACK.mounted(player) || wearsSlayerHelmet(player, "black", checkImbued)
    }

    fun twistedHelmet(player: Player, checkImbued: Boolean): Boolean {
        return SlayerMountType.TWISTED.mounted(player) || wearsSlayerHelmet(player, "twisted", checkImbued)
    }

    fun redHelmet(player: Player, target: Entity?): Boolean {
        return redHelmetNoAssignment(player, false) && (player.slayer.isCurrentAssignment(target) || (target !is Player && player.hasBoon(SlayersSovereignty::class.java)))
    }

    fun redHelmetNoAssignment(player: Player, checkImbued: Boolean): Boolean {
        return SlayerMountType.RED.mounted(player) || wearsSlayerHelmet(player, "red", checkImbued)
    }

    fun purpleHelmet(player: Player, target: Entity?): Boolean {
        return purpleHelmetNoAssignment(player, false) && (player.slayer.isCurrentAssignment(target) || (target !is Player && player.hasBoon(SlayersSovereignty::class.java)))
    }

    fun purpleHelmetNoAssignment(player: Player, checkImbued: Boolean): Boolean {
        return SlayerMountType.PURPLE.mounted(player) || wearsSlayerHelmet(player, "purple", checkImbued)
    }

    fun turquoiseHelmetNoAssignment(player: Player, checkImbued: Boolean): Boolean {
        return SlayerMountType.TURQUOISE.mounted(player) || wearsSlayerHelmet(player, "turquoise", checkImbued)
    }

    private fun wearsSlayerHelmet(player: Player, type: String, checkImbued: Boolean): Boolean {
        val helm = player.equipment.getItem(EquipmentSlot.HELMET) ?: return false
        val name = helm.name ?: return false
        if (checkImbued) {
            return name.contains("$type slayer helmet (i)", ignoreCase = true)
        }
        return name.contains("$type slayer helmet", ignoreCase = true)
    }

    fun wearsTztokSlayerHelmet(player: Player, checkImbued: Boolean = false): Boolean {
        return SlayerMountType.TZTOK.mounted(player) || wearsSlayerHelmet(player, "tztok", checkImbued)
    }

    fun wearsVampiricSlayerHelmet(player: Player, checkImbued: Boolean = false): Boolean {
        return SlayerMountType.VAMPYRIC.mounted(player) || wearsSlayerHelmet(player, "vampiric", checkImbued)
    }

    fun wearsTzkalSlayerHelmet(player: Player, checkImbued: Boolean = false): Boolean {
        return SlayerMountType.TZKAL.mounted(player) || wearsSlayerHelmet(player, "tzkal", checkImbued)
    }

    fun wearsCombatAchievementSlayerHelmet(player: Player, checkImbued: Boolean = false): Boolean {
        return wearsTztokSlayerHelmet(player, checkImbued) || wearsVampiricSlayerHelmet(player, checkImbued) || wearsTzkalSlayerHelmet(player, checkImbued)
    }

    fun transformSlayerExperience(player: Player, experience: Double): Double {
        return when {
            wearsTztokSlayerHelmet(player) -> 1.05F * experience
            wearsVampiricSlayerHelmet(player) -> 1.10F * experience
            wearsTzkalSlayerHelmet(player) -> 1.20F * experience
            else -> experience
        }
    }

    fun shouldNotDegrade(player: Player): Boolean {
        return when {
            wearsTztokSlayerHelmet(player) -> Utils.randomBoolean(5)
            wearsVampiricSlayerHelmet(player) -> Utils.randomBoolean(15)
            wearsTzkalSlayerHelmet(player) -> Utils.randomBoolean(25)
            else -> false
        }
    }

    private fun getBonusDamageChance(player: Player): Int {
        return when {
            wearsVampiricSlayerHelmet(player) -> 2
            wearsTzkalSlayerHelmet(player) -> 3
            else -> 1
        }
    }


    fun getBonusDropRoll(player: Player): Boolean {
        return when {
            wearsTztokSlayerHelmet(player) -> Utils.randomBoolean(1)
            wearsVampiricSlayerHelmet(player) -> Utils.randomBoolean(2)
            wearsTzkalSlayerHelmet(player) -> Utils.randomBoolean(5)
            else -> false
        }
    }

}
