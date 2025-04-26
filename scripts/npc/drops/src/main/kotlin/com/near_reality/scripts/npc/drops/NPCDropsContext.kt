package com.near_reality.scripts.npc.drops

import com.near_reality.scripts.npc.drops.table.DropTable
import com.near_reality.scripts.npc.drops.table.DropTableContext
import com.near_reality.scripts.npc.drops.table.DropTableType
import com.near_reality.scripts.npc.drops.table.DropTables
import com.near_reality.scripts.npc.drops.table.chance.RollItemChance
import com.near_reality.scripts.npc.drops.table.chance.immutable.StaticRollChance
import com.near_reality.scripts.npc.drops.table.dsl.NpcDropTableBuilder
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.player.Player

data class NPCDropsContext(
    val npc: NPC,
    val killer: Player,
    val tableBuilder: NpcDropTableBuilder
) {

    /**
     * Optional predicate to decide whether a table is to be excluded given the [DropTableContext].
     */
    private lateinit var excludeTablePredicate: DropTableContext.() -> Boolean

    /**
     * Optional modifier for the [StaticRollChance.rarity].
     */
    private var dropRarityModifier: (DropTableContext.(StaticRollChance) -> Int)? = null

    val playerDamageContributions by lazy {
        npc.receivedDamage.entries.mapNotNull { entry ->
            World.getPlayer(entry.key.first).map { player ->
                player to entry.value
                    .sumOf { timeDamagePair -> timeDamagePair.damage }
                    .div(npc.maxHitpoints.toDouble())
            }.orElse(null)
        }.toMap()
    }

    fun modifyDropRarity(modifier: DropTableContext.(StaticRollChance) -> Int) {
        dropRarityModifier = modifier
    }

    fun excludeTableWhen(predicate: DropTableContext.() -> Boolean) {
        excludeTablePredicate = predicate
    }

    fun rollTables(player: Player = killer): Map<DropTableType, List<RollItemChance>> {
        player.sendDeveloperMessage("Rolling tables for ${npc.name}")
        val tableMap = tableBuilder.build(player, npc)
        val dropsByTableType = mutableMapOf<DropTableType, List<RollItemChance>>()
        for ((type, table) in tableMap) {
            dropsByTableType[type] = rollTable(player, type, table)
        }
        return dropsByTableType
    }

    fun rollTablesAndDrop(player: Player = killer, location: Location = npc.middleLocation) =
        rollTables(player).values.flatten().onEach { drop -> drop.rollAndDrop(npc, player, location) }

    fun rollTablesAndAward(player: Player = killer) =
        rollTables(player).values.flatten().onEach { drop -> drop.rollAndAward(npc, player) }

    fun rollStaticTableAndDrop(player: Player, type: DropTableType, location: Location = npc.middleLocation) =
        rollStaticTable(player, type).onEach { drop -> drop.rollAndDrop(npc, player, location) }

    fun rollStaticTableAndDropBelowPlayer(player: Player, type: DropTableType) =
        rollStaticTableAndDrop(player, type, player.location)

    fun rollStaticTableAndAward(player: Player, type: DropTableType) =
        rollStaticTable(player, type).onEach { drop -> drop.rollAndAward(npc, player) }

    fun rollStaticTable(player: Player, type: DropTableType): List<RollItemChance> =
        rollTable(player, type, getStaticTable(type))

    fun rollTable(
        player: Player,
        type: DropTableType,
        table: DropTable,
    ): List<RollItemChance> {
        val drops = mutableListOf<RollItemChance>()
        val context = DropTableContext.ForPlayer.NpcKill(npc, player, type, table)
        if (!this::excludeTablePredicate.isInitialized || !excludeTablePredicate(context)) {
            player.sendDeveloperMessage("\tRolling table $type")
            DropTables.roll(table.limit, context, drops, dropRarityModifier)
        } else
            player.sendDeveloperMessage("\tNot rolling table $type (unmet predicate)")
        return drops
    }

    fun getStaticTable(type: DropTableType) = tableBuilder.staticTables[type]!!
}
