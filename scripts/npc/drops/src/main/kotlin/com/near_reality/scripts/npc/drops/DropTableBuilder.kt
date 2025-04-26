package com.near_reality.scripts.npc.drops

import com.zenyte.game.world.entity.npc.drop.matrix.Drop
import com.zenyte.game.world.entity.npc.drop.matrix.NPCDrops
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.ObjectList

/**
 * @author Jire
 */
@Deprecated("Use com.near_reality.scripts.npc.drops.table.dsl.DropTableBuilder")
class DropTableBuilder(
    private val npcID: Int,
    private val weight: Int = 0
) {

    private val drops: ObjectList<Drop> = ObjectArrayList()

    operator fun Int.invoke(
        rate: Int = Drop.GUARANTEED_RATE,
        minAmount: Int = MIN_AMOUNT_REQUIRED,
        maxAmount: Int = minAmount
    ) {
        check(minAmount >= MIN_AMOUNT_REQUIRED) { "Min amount ($minAmount) must be at least $MIN_AMOUNT_REQUIRED" }
        check(maxAmount >= MIN_AMOUNT_REQUIRED) { "Max amount ($maxAmount) must be at least $MIN_AMOUNT_REQUIRED" }
        check(minAmount <= maxAmount) { "Min amount ($minAmount) cannot be more than max amount ($maxAmount)" }

        drops.add(Drop(this, rate, minAmount, maxAmount))
    }
    operator fun Int.invoke(
        rate: Int = Drop.GUARANTEED_RATE,
        amountRange: IntRange
    )  = invoke(rate, amountRange.first, amountRange.last)

    fun buildTable() = NPCDrops.DropTable(npcID, weight, drops.sortedBy { it.baseRate }.toTypedArray())

    companion object {
        const val MIN_AMOUNT_REQUIRED = 1
    }

}
