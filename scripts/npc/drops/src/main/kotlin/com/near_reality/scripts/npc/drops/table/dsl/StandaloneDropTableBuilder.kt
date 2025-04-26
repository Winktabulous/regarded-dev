package com.near_reality.scripts.npc.drops.table.dsl

import com.near_reality.scripts.npc.drops.table.DropQuantity
import com.near_reality.scripts.npc.drops.table.DropQuantityModifier
import com.near_reality.scripts.npc.drops.table.DropTable
import com.near_reality.scripts.npc.drops.table.chance.immutable.StaticRollChance
import com.near_reality.scripts.npc.drops.table.chance.immutable.StaticRollItemChance
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.player.Player
import kotlin.random.Random

/**
 * Represents a drop table builder that is unlike [NpcDropTableBuilder] not tied to a specific NPC.
 *
 * This class can be used to create drop tables shared by NPCs (or other standalone drop tables).
 *
 * @author Stan van der Bend
 */
open class StandaloneDropTableBuilder(configure : StandaloneDropTableBuilder.() -> Unit = {}) {

    /**
     * Represents an optional [DynamicDropTableBuilder] invoker.
     */
    lateinit var dynamicBuilder : DynamicDropTableBuilder.() -> Unit

    /**
     * Represents an optional [static drop table][DropTable].
     */
    lateinit var staticTable : DropTable

    lateinit var quantityModifier : DropQuantityModifier

    var limit: Int? = null

    init {
        configure()
        requireNotNull(limit) {
            "Please set a limit!"
        }
    }

    fun modify(chance: Int, amountModifier: (Int) -> Int) : StandaloneDropTableBuilder {
        quantityModifier = DropQuantityModifier(chance, amountModifier)
        return this
    }

    /**
     * Sets this [StandaloneDropTableBuilder.dynamicBuilder] to [dynamicBuilder].
     */
    fun dynamic(dynamicBuilder : DynamicDropTableBuilder.() -> Unit) {
        this.dynamicBuilder = dynamicBuilder
    }

    /**
     * Sets [staticTable] to a new [DropTable] build by [staticBuilder].
     */
    fun static(staticBuilder : DropTableBuilder.() -> Unit) {
        staticTable = DropTableBuilder().let {
            staticBuilder(it)
            if (limit == null)
                limit = it.staticRolls.sumOf { chance -> chance.rarity }
            it.build(limit!!)
        }
    }

    /**
     * Builds a [DropTable] from the optional [dynamicBuilder] and/or [staticTable].
     *
     * @throws Exception if neither [dynamicBuilder] nor [staticTable] is initialised.
     */
    fun build(player: Player, npc: NPC) : DropTable {
        return when {
            this::quantityModifier.isInitialized || this::dynamicBuilder.isInitialized -> {
                val quantity = if (this::quantityModifier.isInitialized)
                    quantityModifier.amountForChance.invoke(1+Random.nextInt(quantityModifier.maxRoll))
                else
                    null
                val rolls = mutableListOf<StaticRollChance>()
                if (this::dynamicBuilder.isInitialized) {
                    val builder = DynamicDropTableBuilder(player, npc)
                    builder.dynamicBuilder()
                    if (quantity != null) {
                        for (roll in builder.staticRolls) {
                            if (roll is StaticRollItemChance)
                                rolls.add(StaticRollItemChance(roll.id, DropQuantity(quantity..quantity, roll.quantity.noted)))
                            else
                                rolls.add(roll)
                        }
                    } else
                        rolls.addAll(builder.staticRolls)
                }
                if (this::staticTable.isInitialized) {
                    if (quantity != null) {
                        for (roll in staticTable.staticRolls) {
                            if (roll is StaticRollItemChance)
                                rolls.add(StaticRollItemChance(roll.id, DropQuantity(quantity..quantity, roll.quantity.noted)))
                            else
                                rolls.add(roll)
                        }
                    } else
                        rolls.addAll(staticTable.staticRolls)
                }
                if (limit == null)
                    limit = rolls.sumOf { chance -> chance.rarity }
                DropTable(limit!!, rolls, emptyList())
            }
            this::staticTable.isInitialized -> staticTable
            else -> throw Exception("No table initialised, please initialise a static or dynamic table.")
        }
    }
}
