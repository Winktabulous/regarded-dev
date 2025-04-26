package com.zenyte.game.content.boss.abyssalsire

import com.zenyte.game.content.boss.abyssalsire.AbyssalSire.Companion.SIRE_THRONE_CONTROLLING_ID
import com.zenyte.game.content.boss.abyssalsire.AbyssalSire.Companion.SIRE_THRONE_STUNNED_ID
import com.zenyte.game.content.boss.abyssalsire.WeakReferenceHelper.invoke
import com.zenyte.game.util.Utils.randomDouble
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell

/**
 * @author Jire
 */
internal class AbyssalSireDisorientation(val sire: AbyssalSire) {

    private var timer = 0
    var totalDamage = 0

    private fun disorientate() = sire.run {
        stunCount++
        timer = TIMER_DELAY

        target { it.sendMessage("The Sire has been disorientated temporarily.") }
        if (id == SIRE_THRONE_CONTROLLING_ID) {
            setTransformation(SIRE_THRONE_STUNNED_ID)
            animation = Animation.STOP

            tentacles.disorientate()
        }
    }

    fun processNPC(): Boolean = sire.run {
        if (id != SIRE_THRONE_STUNNED_ID) return false
        if (--timer <= 0)
            regainControl()
        return@run true
    }

    fun reset() {
        totalDamage = 0
    }

    fun onScheduledAttack(weapon: Any?) {
        if (sire.phase != AbyssalSirePhase.AWAKE) return
        val random = randomDouble()
        if (totalDamage >= DAMAGE_THRESHOLD || random >= spellToChance.getOrDefault(weapon, 1.0)) {
            totalDamage = 0
            disorientate()
        }
    }

    private companion object {
        const val DAMAGE_THRESHOLD = 75
        const val TIMER_DELAY = 50

        val spellToChance: Map<*, Double> = mapOf(
            CombatSpell.SHADOW_BARRAGE to 0.0,
            CombatSpell.SHADOW_BLITZ to 0.25,
            CombatSpell.SHADOW_BURST to 0.5,
            CombatSpell.SHADOW_RUSH to 0.75
        )
    }

}