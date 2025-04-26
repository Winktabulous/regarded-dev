package com.zenyte.game.content

import com.near_reality.game.content.commands.DeveloperCommands
import com.near_reality.scripts.npc.drops.table.DropTableContext
import com.near_reality.scripts.npc.drops.table.always
import com.near_reality.scripts.npc.drops.table.chance.immutable.StaticRollChance
import com.near_reality.scripts.npc.drops.table.chance.immutable.StaticRollItemOneIn
import com.near_reality.scripts.npc.drops.table.dsl.StandaloneDropTableBuilder
import com.near_reality.scripts.npc.drops.table.noted
import com.zenyte.game.content.xamphur.Xamphur

import com.near_reality.scripts.npc.drops.NPCDropTableScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.item.ItemId
import com.zenyte.game.item.ItemId.*
import com.near_reality.scripts.npc.drops.table.DropTableType.*
import com.zenyte.game.world.entity.npc.drop.matrix.*
import com.zenyte.game.world.entity.npc.drop.matrix.Drop.*
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.*

class XamphurDropTable : NPCDropTableScript() {
    init {
        npcs(XAMPHUR, XAMPHUR_10953, XAMPHUR_10954, XAMPHUR_10955, XAMPHUR_10956)
        
        onDeath {
            if (npc is Xamphur) {
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
                        // mvp has 10% increase of common and unique drops
                        if ((type == Main || type == Unique) && mvp == player)
                            rarity = scaleRarity(dropChance, rarity, 1.25)
                    }
        
                    rarity.toInt().coerceAtLeast(1)
                }
        
                mvp?.sendMessage("You were the MVP for this fight and have earned 25% increased drop rates from this kill.")
                val damageContributionByMvp = playerDamageContributions[mvp]?:0.0
                val damageDealtByMvp = (damageContributionByMvp * npc.maxHitpoints).toInt()
                for ((player, damageContribution) in playerDamageContributions) {
                    player.sendMessage("The MVP for this fight was: ${mvp?.name} dealing $damageDealtByMvp damage!")
                    if (damageContribution >= 0.001) {
                        rollTablesAndAward(player)
                        rollStaticTableAndAward(player, Main)
                        if (DeveloperCommands.doubleXamphurDrops)
                            rollTablesAndAward(player)
                    } else
                        player.sendMessage("You did not contribute enough damage to receive any drops.")
                }
            }
        }
        
        buildTable {
            Always {
                TOME_OF_EXPERIENCE_30215 quantity 1 rarity always
            }
            Main(100_000) {
                chance(98_000) roll Commons
                chance(2_000) roll Uncommons
            }
            Unique(12_000) {
                chance(360) roll Rares
                chance(30) roll SuperRares
            }
            Tertiary {
                PET_XAMPHUR quantity 1 oneIn 3000 announce everywhere
            }
        }
    }

    fun scaleRarity(dropChance: StaticRollChance, rarity: Double, damageContribution: Double): Double =
        if (dropChance is StaticRollItemOneIn)
            rarity / damageContribution
        else
            rarity * damageContribution

    object Commons : StandaloneDropTableBuilder({
        limit = 25
        static {
            BABYDRAGON_BONES quantity 25.noted rarity 1
            //COINS quantity (100_000..250_000) rarity 1
            BLOOD_RUNE quantity 500 rarity 1
            WRATH_RUNE quantity 300 rarity 1
            DRAGON_BONES quantity (25..50).noted rarity 1
            SHERLOCKS_NOTES quantity (1..3) rarity 1
            PRAYER_POTION4 quantity (10..20).noted rarity 1
            SUPER_COMBAT_POTION4 quantity (10..20).noted rarity 1
            SARADOMIN_BREW4 quantity (10..20).noted rarity 1
            SUPER_RESTORE4 quantity (10..20).noted rarity 1
            CRUSHED_NEST quantity 30.noted rarity 1
            SNAPE_GRASS quantity 50.noted rarity 1
            RED_SPIDERS_EGGS quantity 30.noted rarity 1
            TORSTOL_SEED quantity 5 rarity 1
            RUNITE_BAR quantity 50.noted rarity 1
            ANGLERFISH quantity 100.noted rarity 1
            COOKED_KARAMBWAN quantity 100.noted rarity 1
            PURE_ESSENCE quantity (500..1000).noted rarity 1
            CANNONBALL quantity (250..500) rarity 1
            RUBY_BOLTS_E quantity 100 rarity 1
            DRAGONSTONE_BOLTS_E quantity 100 rarity 1
            DRAGON_BOLTS quantity 75 rarity 1
            DRAGON_ARROW quantity 75 rarity 1
            DRAGON_DART quantity 200 rarity 1
            SLAYER_TASK_RESET_SCROLL quantity (1..3) rarity 1
            SLAYER_BOOSTER quantity 1 rarity 1
        }
    })

    object Uncommons : StandaloneDropTableBuilder({
        limit = 400
        static {
            SCROLL_OF_IMBUING quantity 1 rarity 100
            EMOTE_SCROLL quantity 1 rarity 100
            AMULET_OF_ETERNAL_GLORY quantity 1 rarity 100
            BOOK_OF_THE_DEAD quantity 1 rarity 100
        }
    })

    object Rares : StandaloneDropTableBuilder({
        limit = 900
        static {
            PET_MYSTERY_BOX quantity 1 rarity 100
            NR_VOTE_SHARD quantity 5 rarity 100
            ENHANCED_CRYSTAL_KEY quantity 3 rarity 100
            MYSTERY_BOX quantity 1 rarity 100
            DWARF_CANNON_SET quantity 1 rarity 100
            ABYSSAL_WHIP quantity 1 rarity 100
            DRAGON_CROSSBOW quantity 1 rarity 100
            AMULET_OF_FURY quantity 1 rarity 100
            CRYSTAL_KEY quantity 1 rarity 100
        }
    })

    object SuperRares : StandaloneDropTableBuilder({
        limit = 500
        static {
            LIME_WHIP quantity 1 rarity 100 announce everywhere
            IMBUED_HEART quantity 1 rarity 100 announce everywhere
            DONATOR_PIN_10 quantity 1 rarity 100 announce everywhere
            DEATH_CAPE quantity 1 rarity 100 announce everywhere
            DRAGON_KITE quantity 1 rarity 100 announce everywhere
        }
    })
}