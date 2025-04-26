package com.near_reality.scripts.npc.drops

import com.near_reality.scripts.npc.NPCScript
import com.near_reality.scripts.npc.drops.table.DropTable
import com.near_reality.scripts.npc.drops.table.DropTableType
import com.near_reality.scripts.npc.drops.table.chance.RollItemChance
import com.near_reality.scripts.npc.drops.table.chance.RollTableChance
import com.near_reality.scripts.npc.drops.table.dsl.NpcDropTableBuilder
import com.zenyte.game.item.Item
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor
import com.zenyte.game.world.entity.npc.spawns.NPCSpawnLoader
import com.zenyte.game.world.entity.player.Player
import com.zenyte.plugins.PluginPriority
import it.unimi.dsi.fastutil.ints.IntArraySet
import it.unimi.dsi.fastutil.ints.IntSet
import kotlin.script.experimental.annotations.KotlinScript

/**
 * Represents a [DropProcessor] that implements a new drop table system.
 *
 * @author Stan van der Bend
 */
@KotlinScript(
    "NPC Drop Table Script",
    fileExtension = "droptable.kts",
    compilationConfiguration = NPCDropTableCompilation::class
)
@PluginPriority(1_000)
abstract class NPCDropTableScript : NPCScript, DropProcessor() {

    /**
     * A set of [npcs ids][NPC.id] to which the [tableBuilder] is applied.
     */
    private val npcs: IntSet = IntArraySet()

    /**
     * The [NpcDropTableBuilder.denominator], represents the max roll, generally
     * the rarity of drops are defined as `rarity`/`denominator`.
     */
    private var denominator = 0

    /**
     * Used to configure the [tableBuilder].
     */
    private lateinit var tableConfigurer: NpcDropTableBuilder.() -> Unit

    /**
     * Optional invocation in [attach],
     * mainly used to append dynamic drops to the drop table viewer.
     */
    private lateinit var onAttachHandler: () -> Unit

    /**
     * Optional replacement for default drop mechanics..
     *
     * The arguments are the [NPC] for whom the table is rolled,
     * and a list of [items][Item] that are to be [dropped][NPC.dropItem] by the npc.
     */
    private lateinit var onDeathHandler: NPCDropsContext.() -> Unit


    /**
     * Optional provider of [com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.PredicatedDrop.information].
     */
    lateinit var viewerInfoProvider: RollItemChance.(DropTableType) -> String?

    /**
     * Lazy property for [NpcDropTableBuilder].
     */
    private val tableBuilder by lazy {
        NpcDropTableBuilder(*ids(), denominator = denominator, configurer = tableConfigurer)
    }

    /**
     * Add the argued [npcIds] to [npcs].
     */
    fun npcs(vararg npcIds: Int) {
        for (npc in npcIds)
            this.npcs.add(npc)
    }

    /**
     * Optional invocation in [attach],
     * mainly used to append dynamic drops to the drop table viewer.
     */
    fun onAttach(handler: () -> Unit) {
        onAttachHandler = handler
    }

    fun onDeath(handler: NPCDropsContext.() -> Unit) {
        onDeathHandler = handler
    }

    inline fun<reified T : RollItemChance> provideInfo(crossinline provider: T.(DropTableType) -> String?) {
        viewerInfoProvider = {
            if (this is T) {
                provider(this, it)
            } else
                null
        }
    }

    var override: Boolean = false
    /**
     * Sets the [denominator]
     */
    fun buildTable(denominator: Int = 0, overrideTable: Boolean = false, tableBuilder: NpcDropTableBuilder.() -> Unit) {
        this.denominator = denominator
        this.tableConfigurer = tableBuilder
        this.override = overrideTable
    }

    override fun attach() {

        NPCSpawnLoader.dropViewerNPCs.addAll(npcs)

        for ((type, table) in tableBuilder.staticTables) {

            table.toDisplayedDrops().forEach(::appendDrop)

            addDropViewerInfo(table, type)
        }

        if (this::onAttachHandler.isInitialized)
            onAttachHandler()
    }

    private fun addDropViewerInfo(
        table: DropTable,
        type: DropTableType,
        depth: Int = 0
    ) {
        if (depth > 100)
            error("Too deep nesting level ($depth) $type - $table (make sure there is no self-reference)")
        for (chance in table.allRolls) {
            if (chance is RollItemChance) {
                if (chance.hasDropViewerInfo()) {
                    chance.ifHasDropViewerInfo {
                        put(chance.id, PredicatedDrop(it))
                    }
                } else if (this::viewerInfoProvider.isInitialized) {
                    val info = viewerInfoProvider(chance, type)
                    if (info != null) {

                        put(chance.id, PredicatedDrop(info))
                    }
                }
            } else if (chance is RollTableChance)
                addDropViewerInfo(chance.dropTable.staticTable, type, depth + 1)
        }
    }

    override fun onDeath(npc: NPC, killer: Player) {
        val context = NPCDropsContext(npc, killer, tableBuilder)
        if (this::onDeathHandler.isInitialized)
            onDeathHandler(context)
        else for (drop in context.rollTables().values.flatten())
            drop.rollAndDrop(npc, killer)
    }

    fun onDeath(npc: NPC, killer: Player, toInvBank: Boolean = false) {
        val context = NPCDropsContext(npc, killer, tableBuilder)
        if (this::onDeathHandler.isInitialized)
            onDeathHandler(context)
        else for (drop in context.rollTables().values.flatten()) {
            if(toInvBank)
                drop.rollAndAward(npc, killer)
            else drop.rollAndDrop(npc, killer)
        }
    }

    override fun disregardTableFromDefinitions(): Boolean  {
        return override
    }

    override fun ids(): IntArray = npcs.toIntArray()
}
