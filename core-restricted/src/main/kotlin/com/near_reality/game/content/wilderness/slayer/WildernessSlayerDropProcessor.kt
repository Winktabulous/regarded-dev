package com.near_reality.game.content.wilderness.slayer

import com.near_reality.game.content.wilderness.slayer.WildernessSlayerEmblem.Companion.roll
import com.zenyte.game.content.skills.slayer.Assignment
import com.zenyte.game.content.skills.slayer.BossTask
import com.zenyte.game.content.skills.slayer.RegularTask
import com.zenyte.game.content.skills.slayer.SlayerMaster
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.combatdefs.NPCCDLoader
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor
import com.zenyte.game.world.entity.npc.spawns.NPCSpawnLoader
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.region.area.wilderness.WildernessArea
import mgi.Indice
import mgi.types.config.npcs.NPCDefinitions
import mgi.utilities.CollectionUtils
import java.util.*

/**
 * @author Kris | 20/04/2019 18:48
 * @see [Rune-Server profile](https://www.rune-server.ee/members/kris/)
 */
@Suppress("unused")
class WildernessSlayerDropProcessor : DropProcessor() {

    override fun attach() {
        for (i in allIds) {
            val definitions = NPCDefinitions.get(i) ?: continue
            val combatDefinitions = NPCCDLoader.get(i) ?: continue
            val hitpoints = combatDefinitions.hitpoints
            val name = definitions.name.lowercase(Locale.getDefault())
            attachSlayerEmblemDrop(hitpoints, i)
            attachSlayerEnhancementDrop(name, hitpoints, i)
        }
    }

    private fun attachSlayerEnhancementDrop(name: String, hitpoints: Int, npcId: Int) {
        var isBossTask = false
        for (task in BossTask.VALUES) {
            if (name == task.taskName.lowercase(Locale.getDefault())) {
                isBossTask = true
                break
            }
        }
        val enchantmentChance = (1.0f / (320 - (hitpoints * 0.8f)))
        val enchantmentPercentage = enchantmentChance * 100.0f
        val enchantmentFraction = (100.0f / enchantmentPercentage).toInt()
        append(ItemId.SLAYERS_ENCHANTMENT, (if (isBossTask) 30 else enchantmentFraction), npcId, "Only dropped by those found in Wilderness while on a slayer assignment from Krystilia.")
    }

    private fun attachSlayerEmblemDrop(hitpoints: Int, npcId: Int) {
        val emblemChance = 1.0f / (155 - (hitpoints / 2.0f))
        val percentage = emblemChance * 100.0f
        val fraction = (100.0f / percentage).toInt()
        append(ItemId.MYSTERIOUS_EMBLEM, fraction, npcId, "Only dropped by those found in Wilderness while on a slayer assignment from Krystilia. May occasionally drop as a higher tier.")
    }

    private fun append(itemId: Int, chance: Number, npcId: Int, description: String) {
        appendDrop(DisplayedDrop(itemId, 1, 1, chance.toDouble(), { _, id: Int -> id == npcId }, npcId))
        put(npcId, itemId, PredicatedDrop(description))
    }

    override fun onDeath(npc: NPC, killer: Player) {
        if (!WildernessArea.isWithinWilderness(npc.x, npc.y))
            return
        val slayer = killer.slayer
        val wildernessAssignment = slayer.assignment
            ?.takeIf { it.master == SlayerMaster.KRYSTILIA && it.isValid(killer, npc) }
            ?: return

        rollSlayerEmblemDrop(npc, killer)
        rollSlayerEnchantmentDrop(wildernessAssignment, npc, killer)
    }

    private fun rollSlayerEnchantmentDrop(
        wildernessAssignment: Assignment,
        npc: NPC,
        killer: Player,
    ) {
        val enchantmentChance = if (wildernessAssignment.task is BossTask)
            0.0333f
        else
            1.0f / (320 - (npc.maxHitpoints * 0.8f))
        if (Utils.randomDouble() <= enchantmentChance)
            npc.dropItem(killer, Item(ItemId.SLAYERS_ENCHANTMENT))
    }

    private fun rollSlayerEmblemDrop(npc: NPC, killer: Player) {
        val emblemChance = 1.0f / (155 - (npc.maxHitpoints / 2.0f))
        if (Utils.randomDouble() <= emblemChance) {
            val emblem = roll()
            if (emblem != null)
                npc.dropItem(killer, Item(emblem.id))
        }
    }

    override fun ids(): IntArray {
        val npcNamesAssignedByKrystilia = mutableSetOf<String>()
        val npcIdsAssignedByKrystilia = mutableSetOf<Int>()
        RegularTask.entries
            .filter { it.hasTaskWith(SlayerMaster.KRYSTILIA) }
            .forEach { task ->
                task.monsters?.forEach { npcNamesAssignedByKrystilia.add(it.lowercase().trim()) }
                task.monsterIds?.forEach { npcIdsAssignedByKrystilia.add(it) }
            }
        BossTask.entries.filter { it.isAssignableByKrystilia }
            .forEach { npcNamesAssignedByKrystilia.add(it.taskName.lowercase().trim())}

        for (i in 0 until CollectionUtils.getIndiceSize(Indice.NPC_DEFINITIONS)) {
            val definitions = NPCDefinitions.get(i)
            if (definitions == null || definitions.combatLevel == 0)
                continue
            if (npcNamesAssignedByKrystilia.contains(definitions.lowercaseName))
                npcIdsAssignedByKrystilia.add(i)
        }
        val npcIdsSpawnedInWilderness = mutableSetOf<Int>()
        for (spawn in NPCSpawnLoader.DEFINITIONS) {
            if (WildernessArea.isWithinWilderness(spawn.x, spawn.y)) {
                npcIdsSpawnedInWilderness.add(spawn.id)
            }
        }
        npcIdsSpawnedInWilderness.add(NpcId.VETION_REBORN)
        npcIdsSpawnedInWilderness.add(NpcId.SCORPIAS_OFFSPRING_6616)
        npcIdsSpawnedInWilderness.add(NpcId.SCORPIAS_GUARDIAN)
        return npcIdsAssignedByKrystilia.intersect(npcIdsSpawnedInWilderness).toIntArray()
    }
}
