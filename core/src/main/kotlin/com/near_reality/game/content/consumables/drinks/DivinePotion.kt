package com.near_reality.game.content.consumables.drinks

import com.zenyte.game.content.consumables.Consumable.Boost
import com.zenyte.game.content.consumables.ConsumableEffects
import com.zenyte.game.item.ItemId
import com.zenyte.game.util.Colour
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.SkillConstants.*
import com.zenyte.game.world.entity.player.privilege.MemberRank
import com.zenyte.game.world.entity.player.variables.TickVariable

/**
 * Represents a divine potion, grants the consumer a persistent skill boost for 5 minutes.
 *
 * @author Stan van der Bend
 *
 * @param ids an [IntArray] of item ids, must go from low dose id to high dose it or logic breaks.
 */
abstract class DivinePotion(ids: IntArray) : PotionAdapter(ids) {

    fun pulse(player: Player, remainingTicks: Int) {
        if (remainingTicks == 0)
            ConsumableEffects.resetSkills(player, name(), boosts().map { it.skill }.toIntArray())
        else if (remainingTicks == 25)
            player.sendMessage(Colour.RED.toString() + "Your ${name()} effect has almost ran out!")
    }

    fun isActive(player: Player) = player.variables.getTime(variable()) > 0

    /**
     * the [TickVariable] used to track the state of the potion effect.
     */
    abstract fun variable(): TickVariable

    private fun name() = "divine ${this::class.simpleName!!.lowercase()}"

    override fun onConsumption(player: Player) {
        player.variables.schedule(DURATION_IN_TICKS, variable())
        ConsumableEffects.damagePlayer(player, 1)
    }

    object Bastion : DivinePotion(divineBastionPotionIds) {
        override fun variable() = TickVariable.DIVINE_BASTION_POTION
        override fun boosts() = arrayOf(
            Boost(RANGED, 0.1F, 4),
            Boost(DEFENCE, 0.15F, 5)
        )
    }

    object BattleMage : DivinePotion(divineBattleMagePotionIds) {
        override fun variable() = TickVariable.DIVINE_BATTLEMAGE_POTION
        override fun boosts() = arrayOf(
            Boost(MAGIC, 0.0F, 4),
            Boost(DEFENCE, 0.15F, 5)
        )
    }

    object Magic : DivinePotion(divineMagicPotionIds) {
        override fun variable() = TickVariable.DIVINE_MAGIC_POTION
        override fun boosts() = arrayOf(
            Boost(MAGIC, 0.0F, 4)
        )
    }

    object Ranging : DivinePotion(divineRangingPotionIds) {
        override fun variable() = TickVariable.DIVINE_RANGING_POTION
        override fun boosts() = arrayOf(
            Boost(RANGED, 0.10F, 4)
        )
    }

    object SuperAttack : DivinePotion(divineSuperAttackPotionIds) {
        override fun variable() = TickVariable.DIVINE_SUPER_ATTACK_POTION
        override fun boosts() = arrayOf(
            Boost(ATTACK, 0.15F, 5)
        )
    }

    object SuperDefence : DivinePotion(divineSuperDefencePotionIds) {
        override fun variable() = TickVariable.DIVINE_SUPER_DEFENCE_POTION
        override fun boosts() = arrayOf(
            Boost(DEFENCE, 0.15F, 5)
        )
    }

    object SuperStrength : DivinePotion(divineSuperStrengthPotionIds) {
        override fun variable() = TickVariable.DIVINE_SUPER_STRENGTH_POTION
        override fun boosts() = arrayOf(
            Boost(STRENGTH, 0.15F, 5)
        )
    }

    object SuperCombat : DivinePotion(divineSuperCombatPotionIds) {
        override fun variable() = TickVariable.DIVINE_SUPER_COMBAT_POTION
        override fun boosts() = arrayOf(
            Boost(ATTACK, 0.15F, 5),
            Boost(DEFENCE, 0.15F, 5),
            Boost(STRENGTH, 0.15F, 5)
        )
    }

    companion object {

        private const val DURATION_IN_TICKS = 500

        private val divineBastionPotionIds = intArrayOf(
            ItemId.DIVINE_BASTION_POTION1,
            ItemId.DIVINE_BASTION_POTION2,
            ItemId.DIVINE_BASTION_POTION3,
            ItemId.DIVINE_BASTION_POTION4
        )

        private val divineBattleMagePotionIds = intArrayOf(
            ItemId.DIVINE_BATTLEMAGE_POTION1,
            ItemId.DIVINE_BATTLEMAGE_POTION2,
            ItemId.DIVINE_BATTLEMAGE_POTION3,
            ItemId.DIVINE_BATTLEMAGE_POTION4
        )

        private val divineMagicPotionIds = intArrayOf(
            ItemId.DIVINE_MAGIC_POTION1,
            ItemId.DIVINE_MAGIC_POTION2,
            ItemId.DIVINE_MAGIC_POTION3,
            ItemId.DIVINE_MAGIC_POTION4
        )

        private val divineRangingPotionIds = intArrayOf(
            ItemId.DIVINE_RANGING_POTION1,
            ItemId.DIVINE_RANGING_POTION2,
            ItemId.DIVINE_RANGING_POTION3,
            ItemId.DIVINE_RANGING_POTION4
        )

        private val divineSuperAttackPotionIds = intArrayOf(
            ItemId.DIVINE_SUPER_ATTACK_POTION1,
            ItemId.DIVINE_SUPER_ATTACK_POTION2,
            ItemId.DIVINE_SUPER_ATTACK_POTION3,
            ItemId.DIVINE_SUPER_ATTACK_POTION4
        )

        private val divineSuperDefencePotionIds = intArrayOf(
            ItemId.DIVINE_SUPER_DEFENCE_POTION1,
            ItemId.DIVINE_SUPER_DEFENCE_POTION2,
            ItemId.DIVINE_SUPER_DEFENCE_POTION3,
            ItemId.DIVINE_SUPER_DEFENCE_POTION4
        )

        private val divineSuperStrengthPotionIds = intArrayOf(
            ItemId.DIVINE_SUPER_STRENGTH_POTION1,
            ItemId.DIVINE_SUPER_STRENGTH_POTION2,
            ItemId.DIVINE_SUPER_STRENGTH_POTION3,
            ItemId.DIVINE_SUPER_STRENGTH_POTION4
        )

        private val divineSuperCombatPotionIds = intArrayOf(
            ItemId.DIVINE_SUPER_COMBAT_POTION1,
            ItemId.DIVINE_SUPER_COMBAT_POTION2,
            ItemId.DIVINE_SUPER_COMBAT_POTION3,
            ItemId.DIVINE_SUPER_COMBAT_POTION4
        )

        val all = arrayOf(Bastion, BattleMage, Magic, Ranging, SuperAttack, SuperCombat, SuperDefence, SuperStrength)

        val map = all.flatAssociateBy(DivinePotion::getIds)

        inline fun <T> Array<out T>.flatAssociateBy(keySelector: (T) -> IntArray): Map<Int, T> {
            return buildMap {
                for (element in this@flatAssociateBy) {
                    for (key in keySelector(element)) {
                        put(key, element)
                    }
                }
            }
        }

    }
}
