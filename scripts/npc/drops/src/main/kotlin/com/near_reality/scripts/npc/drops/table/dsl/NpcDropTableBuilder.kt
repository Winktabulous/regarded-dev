package com.near_reality.scripts.npc.drops.table.dsl

import com.near_reality.scripts.npc.drops.table.DropTable
import com.near_reality.scripts.npc.drops.table.DropTableType
import com.near_reality.scripts.npc.drops.table.DropTables
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.player.Player
/**
 * Represents a builder for NPC specific drop tables.
 *
 * @param npcIds        the ids of the NPC to which this table applies.
 * @param configurer    used to configure this builder.
 *
 * @author Stan van der Bend
 */
open class NpcDropTableBuilder(
    private vararg val npcIds: Int,
    val denominator: Int,
    configurer: NpcDropTableBuilder.() -> Unit
) {

    /**
     * Represents a [Map] of [DropTableType] to [DropTableBuilder] invokers.
     */
    private val staticBuilders = mutableMapOf<DropTableType, Pair<Int, DropTableBuilder.() -> Unit>>()

    /**
     * Represents a [Map] of [DropTableType] to [DynamicDropTableBuilder] invokers.
     */
    private val dynamicBuilders = mutableMapOf<DropTableType, Pair<Int, DynamicDropTableBuilder.() -> Unit>>()

    /**
     * Represents a [Map] of [DropTableType] to [drop tables][DropTable].
     *
     * These are generated only once from the [staticBuilders] in [buildStaticTables].
     */
    val staticTables = mutableMapOf<DropTableType, DropTable>()

    init {
        configurer()
        register()
        buildStaticTables()
    }

    /**
     * [Registers][DropTables.register] this table to NPCs with an id present in [npcIds].
     */
    private fun register() {
        DropTables.register(*npcIds, npcDropsTable = this)
    }

    /**
     * Populates [staticTables] with the [staticBuilders] and then clears said builders.
     */
    private fun buildStaticTables() {
        for ((type, invokerWithDenominator) in staticBuilders) {
            val (denominator, invoker) = invokerWithDenominator
            val builder = DropTableBuilder()
            builder.invoker()
            staticTables[type] = builder.build(denominator)
        }
        staticBuilders.clear()
    }

    /**
     * Add a new builder[invoke] to [staticBuilders] for this [DropTableType].
     */
    operator fun DropTableType.invoke(
        denominator: Int = this@NpcDropTableBuilder.denominator,
        invoke : DropTableBuilder.() -> Unit
    ) {
        staticBuilders[this] = denominator to invoke
    }

    /**
     * Add a new builder[invoke] to [dynamicBuilders] for this [DropTableType].
     */
    infix fun DropTableType.dynamic(
        invoke : DynamicDropTableBuilder.() -> Unit
    ) = dynamic(this@NpcDropTableBuilder.denominator, invoke)

    fun DropTableType.dynamic(
        denominator: Int,
        invoke : DynamicDropTableBuilder.() -> Unit
    ) {
        dynamicBuilders[this] = denominator to invoke
    }

    /**
     * Builds all [drop tables][DropTable] for each [DropTableType].
     */
    fun build(player: Player, npc: NPC) : Map<DropTableType, DropTable> {
        val tables = mutableMapOf<DropTableType, DropTable>()
        tables.putAll(staticTables)
        for ((type, invokerWithDenominator) in dynamicBuilders) {
            val (denominator, invoker) = invokerWithDenominator
            val builder = DynamicDropTableBuilder(player, npc)
            builder.invoker()
            tables[type] = builder.build(denominator)
        }
        return tables
    }
}

