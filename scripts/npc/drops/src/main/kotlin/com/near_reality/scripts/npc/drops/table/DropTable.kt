package com.near_reality.scripts.npc.drops.table

import com.near_reality.scripts.npc.drops.table.chance.RollAlways
import com.near_reality.scripts.npc.drops.table.chance.RollChance
import com.near_reality.scripts.npc.drops.table.chance.RollItemChance
import com.near_reality.scripts.npc.drops.table.chance.RollOneIn
import com.near_reality.scripts.npc.drops.table.chance.dynamic.DynamicRollChance
import com.near_reality.scripts.npc.drops.table.chance.immutable.StaticRollChance
import com.near_reality.scripts.npc.drops.table.chance.immutable.StaticRollTableChance
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.DisplayedDrop
import com.zenyte.game.world.entity.player.Player

/**
 * Represents a table of possible [roll chances][StaticRollChance].
 *
 * @param staticRolls a list of [StaticRollChance] objects.
 *
 * @author Stan van der Bend
 */
open class DropTable(
    val limit: Int,
    val staticRolls: List<StaticRollChance>,
    val dynamicRolls: List<DynamicRollChance>
) {

    private val debugDropTables: Boolean = false

    val allRolls
        get() = staticRolls + dynamicRolls

    fun roll(
        type: DropTableType = DropTableType.Main,
        staticRollChanceRarityTransformer: (DropTableContext.(StaticRollChance) -> Int)? = null
    ): MutableList<RollItemChance> =
        roll(DropTableContext.Standalone(type, this), staticRollChanceRarityTransformer)

    fun roll(
        player: Player,
        type: DropTableType = DropTableType.Main,
        staticRollChanceRarityTransformer: (DropTableContext.(StaticRollChance) -> Int)? = null
    ): MutableList<RollItemChance> =
        roll(DropTableContext.ForPlayer.Misc(player, type, this), staticRollChanceRarityTransformer)

    fun roll(
        player: Player,
        npc: NPC,
        type: DropTableType = DropTableType.Main,
        staticRollChanceRarityTransformer: (DropTableContext.(StaticRollChance) -> Int)? = null
    ): MutableList<RollItemChance> =
        roll(DropTableContext.ForPlayer.NpcKill(npc, player, type, this), staticRollChanceRarityTransformer)

    fun roll(
        context: DropTableContext,
        staticRollChanceRarityTransformer: (DropTableContext.(StaticRollChance) -> Int)? = null
    ): MutableList<RollItemChance> {
        val items = mutableListOf<RollItemChance>()
        DropTables.roll(limit, context, items, staticRollChanceRarityTransformer)
        return items
    }

    fun toDisplayedDrops(depth: Int = 0, rarityMultiplier: Double = 1.0): List<DisplayedDrop> {
        if (depth > 100)
            error("Too deep nesting level ($depth) $this (make sure there is no self-reference)")
        val displayedDrops = mutableListOf<DisplayedDrop>()
        allRolls.forEach { rollChance ->
            when (rollChance) {
                is RollItemChance -> {
                    val quantity = rollChance.quantity.range
                    val displayedDrop = when (rollChance) {
                        is StaticRollChance -> {
                            val rate = rollChance.getDisplayedRate(rollChance.rarity, rarityMultiplier)
                            if(debugDropTables)
                                println("${rollChance.id} -> Rarity: ${rollChance.rarity} ($rarityMultiplier, $limit, $rate) -> ${1.0 / rate * 100}")
                            DisplayedDrop(rollChance.id, quantity.first, quantity.last, rate)
                        }
                        is DynamicRollChance -> {
                            object: DisplayedDrop(rollChance.id, quantity.first, quantity.last) {
                                override fun getRate(player: Player, id: Int): Double =
                                    rollChance.getDisplayedRate(rollChance.rarityProvider(player), rarityMultiplier)
                            }
                        }
                        else -> error("Unknown chance type $rollChance")
                    }
                    rollChance.ifOnlySpecificNpcs(displayedDrop::setNpcIds)
                    displayedDrops += displayedDrop
                }
                is StaticRollTableChance -> {
                    val rarity = rollChance.rarity.toDouble().div(limit)
                    displayedDrops += rollChance.dropTable.staticTable.toDisplayedDrops(depth + 1, rarityMultiplier * rarity)
                }
            }
        }
        return displayedDrops
    }

    private fun RollChance.getDisplayedRate(
        rarity: Int,
        rarityMultiplier: Double,
    ) = when (this) {
        is RollAlways -> 1.0
        is RollOneIn -> rarity.toDouble().div(rarityMultiplier)
        else -> limit.toDouble().div(rarity).div(rarityMultiplier)
    }
}
