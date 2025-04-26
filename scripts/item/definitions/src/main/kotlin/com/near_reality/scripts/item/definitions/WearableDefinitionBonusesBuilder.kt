package com.near_reality.scripts.item.definitions

import com.zenyte.game.world.entity.player.Bonuses
import it.unimi.dsi.fastutil.objects.Object2IntMap
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import mgi.types.config.items.WearableDefinition

/**
 * @author Jire
 */
class WearableDefinitionBonusesBuilder(private val def: WearableDefinition) {

    private val bonuses: Object2IntMap<Bonuses.Bonus> = Object2IntOpenHashMap(Bonuses.Bonus.VALUES.size)

    operator fun Bonuses.Bonus.invoke(value: Int) {
        bonuses.put(this, value)
    }

    fun apply() {
        val sb = StringBuilder()
        val lastBonus = Bonuses.Bonus.VALUES.last()
        for (value in Bonuses.Bonus.VALUES) {
            sb.append(bonuses.getInt(value))
            if (value != lastBonus) sb.append(',').append(' ')
        }

        def.bonuses = sb.toString()
    }

}