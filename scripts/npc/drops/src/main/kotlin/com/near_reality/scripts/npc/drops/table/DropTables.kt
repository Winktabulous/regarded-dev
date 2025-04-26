package com.near_reality.scripts.npc.drops.table

import com.google.common.base.Preconditions
import com.near_reality.scripts.npc.drops.table.chance.*
import com.near_reality.scripts.npc.drops.table.chance.dynamic.DynamicRollChance
import com.near_reality.scripts.npc.drops.table.chance.immutable.StaticRollChance
import com.near_reality.scripts.npc.drops.table.dsl.NpcDropTableBuilder
import com.zenyte.game.world.entity.npc.NPC
import mgi.types.config.npcs.NPCDefinitions
import kotlin.math.floor
import kotlin.random.Random

/**
 * Represents a manager for [npc drop tables][NpcDropTableBuilder].
 *
 * @author Stan van der Bend
 */
object DropTables {

    /**
     * A [Map] of [npc names][String] to [drop tables][NpcDropTableBuilder].
     */
    private val dropsTables = mutableMapOf<Int, NpcDropTableBuilder>()

    /**
     * Registers [npcDropsTable] to [npcIds] in [dropsTables].
     */
    fun register(vararg npcIds: Int, npcDropsTable: NpcDropTableBuilder) {
        for (npcId in npcIds) {
            Preconditions.checkArgument(
                NPCDefinitions.get(npcId) != null,
                "NPC with id $npcIds was not found in definitions."
            )
            dropsTables[npcId] = npcDropsTable
        }
    }

    /**
     * Gets the [NpcDropTableBuilder] for this [npc].
     */
    operator fun get(npc: NPC) = dropsTables[npc.id]

    /**
     * Rolls the table defined in the [context] and adds the rolled items to [droppedItems].
     */
    fun roll(
        limit: Int,
        context: DropTableContext,
        droppedItems: MutableList<RollItemChance>,
        staticRollChanceRarityTransformer: (DropTableContext.(StaticRollChance) -> Int)? = null,
    ) {
        val rolls = (context.table.staticRolls + context.table.dynamicRolls).toMutableList()

        val rollsIterator = rolls.iterator()
        while (rollsIterator.hasNext()) {
            val chance = rollsIterator.next()
            if (chance is RollItemChance) {
                if (canDrop(context, chance)) {
                    when (chance) {
                        is RollAlways -> droppedItems.add(chance)
                        is RollOneIn -> {
                            val rarity = chance.getRarity(staticRollChanceRarityTransformer, context)
                            val randomInt = floor(Math.random() * (rarity - 1 + 1) + 1).toInt()
                            if (randomInt == 1)
                                droppedItems.add(chance)
                        }

                        else -> {
                            val rarity = chance.getRarity(staticRollChanceRarityTransformer, context)
                            if (rarity == always)
                                droppedItems.add(chance)
                            else
                                continue
                        }
                    }
                }
                rollsIterator.remove()
            }
        }

        if (rolls.isNotEmpty()) {

            val dropThreshold = Random.nextInt(limit + 1)

            if (context is DropTableContext.ForPlayer)
                context.player.sendDeveloperMessage("Rolled a $dropThreshold out of $limit")

            var weight = 0
            for (chance in rolls) {

                val rarity = chance.getRarity(staticRollChanceRarityTransformer, context)
                weight += rarity

                if (context is DropTableContext.ForPlayer)
                    context.player.sendDeveloperMessage("Checking roll $chance -> rarity = $rarity, weight = $weight")

                if (weight >= dropThreshold) {
                    if (chance is RollItemChance)
                        droppedItems.add(chance)
                    else if (chance is RollTableChance) {
                        val nestedTable = when (context) {
                            is DropTableContext.ForPlayer.NpcKill -> chance.dropTable.build(context.player, context.npc)
                            else -> chance.dropTable.staticTable
                        }
                        roll(nestedTable.limit, context.nested(nestedTable), droppedItems, null)
                    }
                    break
                }
            }
        }
    }

    private fun canDrop(
        context: DropTableContext,
        chance: RollItemChance,
    ) = context !is DropTableContext.ForPlayer.NpcKill || chance.isDroppableBy(context.npc.id)

    private fun RollChance.getRarity(
        rollChanceModifier: (DropTableContext.(StaticRollChance) -> Int)?,
        context: DropTableContext,
    ) = when (this) {
        is StaticRollChance -> rollChanceModifier?.invoke(context, this) ?: rarity
        is DynamicRollChance -> if (context is DropTableContext.ForPlayer)
            rarityProvider(context.player)
        else
            error("Dynamic roll chances are not defined for non-player DropTableContext's")
        else ->
            error("Unknown chance $this")
    }
}
