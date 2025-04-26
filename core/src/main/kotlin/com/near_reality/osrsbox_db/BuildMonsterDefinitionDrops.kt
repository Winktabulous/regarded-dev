package com.near_reality.osrsbox_db

import com.google.gson.GsonBuilder
import com.zenyte.game.item.ItemId
import com.zenyte.game.world.entity.npc.drop.matrix.Drop
import com.zenyte.game.world.entity.npc.drop.matrix.NPCDrops
import com.zenyte.tools.wikia.NPCDropExtractor
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.ObjectList
import mgi.types.config.items.ItemDefinitions
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.FileWriter
import kotlin.math.roundToInt

private val logger: Logger = LoggerFactory.getLogger("BuildMonsterDefinitionDrops")

val EXPORT: Boolean = false
val EXPORT_IDS: MutableList<Int> = mutableListOf(9430)

fun MonsterDefinition.buildFromTable(table: List<MonsterDrop>) {
    val oldDrops: ObjectList<Drop> = ObjectArrayList(table.size)
    for (drop in table) {
        val itemID = drop.id ?: continue
        val rarity = drop.rarity?.toDoubleOrNull() ?: continue
        val quantityBase = drop.quantity ?: continue

        val requirements = drop.dropRequirements
        if (requirements != null) {
            /* ignore all drops with requirements, like quests etc. */
            continue
        }

        val noted = drop.noted

        fun defineQuantity(quantityMin: Int, quantityMax: Int): Boolean {
            var actualItemID = itemID
            when (actualItemID) {
                ItemId.LARRANS_KEY,
                ItemId.BASILISK_BONE,
                ItemId.BASILISK_BONE_7901,
                ItemId.LOOTING_BAG,
                ItemId.LOOTING_BAG_22586,
                ItemId.BRIMSTONE_KEY,
                ItemId.DARK_TOTEM_BASE,
                ItemId.DARK_TOTEM_MIDDLE,
                ItemId.DARK_TOTEM_TOP,
                ItemId.ANCIENT_SHARD,
                -> return false
            }
            val itemDef = ItemDefinitions.get(actualItemID) ?: return false
            if (itemDef.name.contains("Clue scroll", true)) {
                return false // should convert these in the actual drop area and remove the existing clue drop system.
            }
            if (noted) {
                if (itemDef.isNoted) return false
                val noteID = itemDef.notedId
                if (noteID >= 0 && ItemDefinitions.get(noteID) != null) {
                    actualItemID = noteID
                } else {
                    logger.warn("No note for {} ({})", itemID, itemDef)
                    return false
                }
            }

            val rate = if (rarity < 1)
                Drop.GUARANTEED_RATE / (1.0 / rarity).roundToInt() // gets our 1/rate
            else Drop.GUARANTEED_RATE
            val oldDrop = Drop(actualItemID, rate, quantityMin, quantityMax)
            return oldDrops.add(oldDrop)
        }

        fun defineQuantityBase(quantityBase: String) {
            when {
                quantityBase.contains(',') -> {
                    val quantities = quantityBase.split(",")
                    for (quantity in quantities) {
                        defineQuantityBase(quantity)
                    }
                }

                quantityBase.contains('-') -> {
                    val split = quantityBase.split("-")
                    defineQuantity(split[0].toInt(), split[1].toInt())
                }

                else -> {
                    val quantity = quantityBase.toInt()
                    defineQuantity(quantity, quantity)
                }
            }
        }

        defineQuantityBase(quantityBase)
    }

    if (oldDrops.isNotEmpty()) {
        val oldTable = NPCDrops.DropTable(id, 0, oldDrops.toTypedArray())
        NPCDrops.initDropTable(oldTable)
        if(EXPORT && EXPORT_IDS.contains(id)) {
            val gson = GsonBuilder().setPrettyPrinting().create()
            val writer = FileWriter(NPCDropExtractor.DIRECTORY + id + ".drops.json")
            gson.toJson(oldTable, writer)
            writer.close()
            System.out.printf("Writing drops for npc id=%d\n", id)
        }
        //println("defined ${oldDrops.size} drops for $id (\"${NPCDefinitions.get(id)?.name}\")")
    }
}
