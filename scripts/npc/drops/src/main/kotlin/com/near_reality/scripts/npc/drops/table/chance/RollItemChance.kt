package com.near_reality.scripts.npc.drops.table.chance

import com.near_reality.scripts.npc.drops.table.DropQuantity
import com.zenyte.game.item.Item
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.player.Player
import mgi.types.config.items.ItemDefinitions

abstract class RollItemChance(
    val id: Int,
    val quantity: DropQuantity
) : RollChance {

    private lateinit var applicableNpcsIds: IntArray

    lateinit var transformer: Player.(Item) -> Item?

    lateinit var viewerInfo: String

    /**
     * Represents a lazy-loaded [ItemDefinitions], should not be `null`.
     */
    private val definition by lazy { ItemDefinitions.get(id) }

    fun rollAndDrop(npc: NPC, player: Player, dropLocation: Location = npc.middleLocation, amountMultiplier: Number = 1.0) {
        val item = rollItem(player, amountMultiplier)
        if (item != null)
            npc.dropItem(player, item, dropLocation, false)
    }

    fun rollAndAward(npc: NPC, player: Player, amountMultiplier: Number = 1.0) {
        val item = rollItem(player, amountMultiplier)
        if (item != null) {
            npc.dropItem(player, item, true)
        }
    }

    fun rollItem(player: Player, amountMultiplier: Number = 1.0): Item? {
        var item = rollItem(amountMultiplier)
        if (this::transformer.isInitialized) {
            val transformedDrop = transformer(player, item)
            player.sendDeveloperMessage("Your original drop $item was transformed into $transformedDrop")
            if (transformedDrop == null)
                return null
            item = transformedDrop
        }
        return item
    }

    /**
     * Creates a new [Item] from the [name] and [quantity].
     */
    fun rollItem(amountMultiplier: Number = 1.0) : Item {
        requireNotNull(definition) {
            "Not definition found for item with id $id"
        }
        val itemId = if (quantity.noted) definition.notedId else definition.id
        require(itemId != -1) {
            "Item id was -1 for $this"
        }
        val itemAmount = quantity.rollQuantity()
        return Item(itemId, (itemAmount * amountMultiplier.toDouble()).toInt().coerceAtLeast(1))
    }

    fun addNpcIds(vararg npcIds: Int) {
        if (!this::applicableNpcsIds.isInitialized)
            applicableNpcsIds = npcIds
        else
            applicableNpcsIds += npcIds
    }

    fun ifOnlySpecificNpcs(handler: (IntArray) -> Unit) {
        if (this::applicableNpcsIds.isInitialized)
            handler(applicableNpcsIds)
    }

    fun isDroppableBy(npcId: Int) =
        !this::applicableNpcsIds.isInitialized || applicableNpcsIds.contains(npcId)


    fun ifHasDropViewerInfo(handler: (String) -> Unit) {
        if (this::viewerInfo.isInitialized)
            handler(viewerInfo)
    }

    fun hasDropViewerInfo() = this::viewerInfo.isInitialized

    override fun toString() = "$quantity ${definition.name}"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RollItemChance) return false

        if (id != other.id) return false
        if (quantity != other.quantity) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + quantity.hashCode()
        return result
    }
}
