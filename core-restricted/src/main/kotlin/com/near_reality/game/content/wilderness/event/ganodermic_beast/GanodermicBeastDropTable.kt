package com.near_reality.game.content.wilderness.event.ganodermic_beast

import com.near_reality.game.item.CustomItemId
import com.near_reality.game.item.CustomNpcId
import com.near_reality.scripts.npc.drops.NPCDropTableScript
import com.near_reality.scripts.npc.drops.table.DropTableContext
import com.near_reality.scripts.npc.drops.table.DropTableType.*
import com.near_reality.scripts.npc.drops.table.always
import com.near_reality.scripts.npc.drops.table.chance.immutable.StaticRollChance
import com.near_reality.scripts.npc.drops.table.chance.immutable.StaticRollItemOneIn
import com.near_reality.scripts.npc.drops.table.noted
import com.zenyte.game.item.ItemId
import com.zenyte.game.util.Colour
import mgi.utilities.StringFormatUtil

@Suppress("unused")
class GanodermicBeastDropTable : NPCDropTableScript() {

    init {
        npcs(CustomNpcId.GANODERMIC_BEAST)
        onDeath {
            if (npc is GanodermicBeast) {
                if (playerDamageContributions.isEmpty()) {
                    killer.sendDeveloperMessage("Did not find any damage dealers, ignoring drops.")
                    return@onDeath
                }

                val mvp = playerDamageContributions.maxBy { it.value }.key
                mvp.sendDeveloperMessage("You are the MVP with a damage percentage of ${playerDamageContributions[mvp]}")

                modifyDropRarity { dropChance ->
                    // unique drops are scaled in rarity for all players based on their total damage done
                    var rarity = dropChance.rarity.toDouble()

                    if (this is DropTableContext.ForPlayer) {
                        val unique = type == Unique
                        // player drop chance is determined by percentage of damage dealt to nex
                        var rarityScale = if (unique)
                            playerDamageContributions[player] ?: 0.0
                        else
                            1.0

                        // mvp has 10% increase of common and unique drops
                        if ((type == Main || unique) && mvp == player)
                            rarityScale += 1.0

                        if ((type == Unique || type == Tertiary) && player.variables.ganoBoosterKillsLeft > 0)
                            rarityScale += 0.15

                        rarity = scaleRarity(dropChance, rarity, rarityScale)
                    }

                    rarity.toInt()
                }
                val topDamageDealers = playerDamageContributions.entries.sortedByDescending { it.value }.map { it.key }
                val top10 = topDamageDealers.take(10)
                top10.forEachIndexed { index, player ->
                    rollStaticTableAndDrop(player, type = Always)
                    if (index in 0 until 3) {
                        rollStaticTableAndDrop(player, type = Main)
                        if (player == mvp)
                            rollStaticTableAndDrop(player, type = Main)
                        rollStaticTableAndDrop(player, type = Unique)
                        rollStaticTableAndDrop(player, type = Tertiary)
                        if (player.variables.ganoBoosterKillsLeft > 0)
                            player.variables.ganoBoosterKillsLeft--
                    }
                }

                playerDamageContributions.keys.forEach { player ->
                    player.sendMessage(
                        "${mvp.name} is the MVP and dealt ${
                            Colour.ORANGE_RED.wrap(
                            StringFormatUtil.formatNumberUS(((playerDamageContributions[mvp]?:0.0) * 100.0).toInt())
                        )}% of the damage to the Ganodermic beast!"
                    )
                }
            }
        }
        buildTable {
            Always {
                ItemId.BLOOD_MONEY quantity 25..75 rarity always
                CustomItemId.POLYPORE_SPORES quantity 100..300 rarity always
            }
            Main(1300) {
                ItemId.BLOOD_MONEY quantity 25..100 rarity 100
                ItemId.SARADOMIN_BREW4 quantity (25..50).noted rarity 100
                ItemId.SUPER_RESTORE4 quantity (25..50).noted rarity 100
                ItemId.SANFEW_SERUM4 quantity (25..50).noted rarity 100
                ItemId.SUPER_COMBAT_POTION4 quantity 25.noted rarity 100
                ItemId.ANTIVENOM4 quantity 25.noted rarity 100
                ItemId.COOKED_KARAMBWAN quantity (50..100).noted rarity 100
                ItemId.ANGLERFISH quantity (50..100).noted rarity 100
                ItemId.DRAGON_DART quantity 100 rarity 100
                ItemId.DRAGON_ARROW quantity 100 rarity 100
                ItemId.DRAGON_BOLTS quantity 100 rarity 100
                ItemId.SUPERIOR_DRAGON_BONES quantity 50.noted rarity 100
                ItemId.OVERLOAD_4 quantity 3..7 rarity 100
//                ItemId.HUGE_XP_LAMP
            }
            Unique(32_000) {
                ItemId.MORRIGANS_COIF quantity 1 rarity 100 announce everywhere
                ItemId.MORRIGANS_LEATHER_BODY quantity 1 rarity 100 announce everywhere
                ItemId.MORRIGANS_LEATHER_CHAPS quantity 1 rarity 100 announce everywhere
                ItemId.VESTAS_HELM quantity 1 rarity 100 announce everywhere
                ItemId.VESTAS_CHAINBODY quantity 1 rarity 100 announce everywhere
                ItemId.VESTAS_PLATESKIRT quantity 1 rarity 100 announce everywhere
                ItemId.STATIUSS_FULL_HELM quantity 1 rarity 100 announce everywhere
                ItemId.STATIUSS_PLATEBODY quantity 1 rarity 100 announce everywhere
                ItemId.STATIUSS_PLATELEGS quantity 1 rarity 100 announce everywhere
                ItemId.ZURIELS_HOOD quantity 1 rarity 100 announce everywhere
                ItemId.ZURIELS_ROBE_TOP quantity 1 rarity 100 announce everywhere
                ItemId.ZURIELS_ROBE_BOTTOM quantity 1 rarity 100 announce everywhere
                CustomItemId.ANCIENT_EYE quantity 1 rarity 100 announce everywhere
                CustomItemId.KORASI quantity 1 rarity 100 announce everywhere
                CustomItemId.POLYPORE_STAFF_DEG quantity 1 rarity 100 announce everywhere
                CustomItemId.GANODERMIC_RUNT quantity 1 rarity 100 announce everywhere
            }
            Tertiary {
                ItemId.SCROLL_BOX_HARD quantity 1 oneIn 10
                ItemId.SCROLL_BOX_ELITE quantity 1 oneIn 15
            }
        }
    }

    private fun scaleRarity(dropChance: StaticRollChance, rarity: Double, damageContribution: Double): Double =
        if (dropChance is StaticRollItemOneIn)
            rarity / damageContribution
        else
            rarity * damageContribution

}
