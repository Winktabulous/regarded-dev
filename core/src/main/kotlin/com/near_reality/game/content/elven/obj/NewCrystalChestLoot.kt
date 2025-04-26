package com.near_reality.game.content.elven.obj

import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.world.entity.npc.drop.viewerentry.DropViewerEntry
import com.zenyte.game.world.entity.npc.drop.viewerentry.OtherDropViewerEntry
import com.zenyte.game.world.entity.player.Player
import it.unimi.dsi.fastutil.objects.ObjectArrayList

enum class NewCrystalChestLoot(
    val rarity: LootRarity,
    vararg val loot: Item,
    val enhancedOnly: Boolean = false,
    val min: Int = 1,
    val max: Int = 1,
) {
    COINS(LootRarity.ALWAYS, min = 25000, max = 75000),
    DRAGONSTONE(LootRarity.ALWAYS, Item(ItemId.UNCUT_DRAGONSTONE)),
    CRYSTAL_SHARDS(
        LootRarity.ALWAYS,
        Item(ItemId.CRYSTAL_SHARD), min = 3, max = 5, enhancedOnly = true),

    SC_POTS(LootRarity.COMMON, min = 5, max = 10, loot = arrayOf(Item(12696))),
    RANGE_POTS(LootRarity.COMMON, min = 5, max = 10, loot = arrayOf(Item(2445))),
    PRAYER_POTS(LootRarity.COMMON, min = 5, max = 10, loot = arrayOf(Item(2435))),
    LOBSTERS(LootRarity.COMMON, min = 75, max = 100, loot = arrayOf(Item(380).toNote())),
    SWORDFISH(LootRarity.COMMON, min = 50, max = 75, loot = arrayOf(Item(374).toNote())),
    MONKFISH(LootRarity.COMMON, min= 30, max = 50, loot = arrayOf(Item(7947).toNote())),
    SHARK(LootRarity.COMMON, min = 20, max = 40, loot = arrayOf(Item(386).toNote())),
    IRON_ORE(LootRarity.COMMON, min = 50, max = 100, loot = arrayOf(Item(441).toNote())),
    COAL(LootRarity.COMMON, min = 100, max = 200, loot = arrayOf(Item(454).toNote())),
    RUNE_BOOTS(LootRarity.COMMON, Item(4131)),
    RUNE_SCIM(LootRarity.COMMON, Item(1333)),
    AMULET_OF_GLORY(LootRarity.COMMON, Item(1712)),
    CHAOS_RUNES(LootRarity.COMMON, min = 200, max = 300, loot = arrayOf(Item(562))),
    DEATH_RUNES(LootRarity.COMMON, min = 150, max = 200, loot = arrayOf(Item(560))),
    BLOOD_RUNES(LootRarity.COMMON, min = 100, max = 150, loot = arrayOf(Item(565))),
    WRATH_RUNES(
        LootRarity.COMMON, min = 50, max = 100, loot = arrayOf(
            Item(
                21880
            )
        )),
    RUNE_KNIVES(
        LootRarity.COMMON, min = 100, max = 150, loot = arrayOf(
            Item(
                ItemId.RUNE_KNIFE
            )
        )),
    RUNE_ARROWS(
        LootRarity.COMMON, min = 100, max = 150, loot = arrayOf(
            Item(
                ItemId.RUNE_ARROW
            )
        )),
    BIG_BONES(
        LootRarity.COMMON, min = 100, max = 150, loot = arrayOf(
            Item(
                ItemId.BIG_BONES
            ).toNote())),
    BABY_DRAGON_BONES(
        LootRarity.COMMON, min = 50, max = 75, loot = arrayOf(
            Item(
                535
            ).toNote())),
    BRONZE_BARS(
        LootRarity.COMMON, min = 50, max = 100, loot = arrayOf(
            Item(
                ItemId.BRONZE_BAR
            ).toNote())),
    IRON_BARS(LootRarity.COMMON, min = 50, max = 90, loot = arrayOf(Item(ItemId.IRON_BAR).toNote())),
    STEEL_BARS(LootRarity.COMMON, min = 50, max = 80, loot = arrayOf(Item(ItemId.STEEL_BAR).toNote())),
    MITHRIL_BARS(
        LootRarity.COMMON, min = 25, max = 70, loot = arrayOf(
            Item(
                ItemId.MITHRIL_BAR
            ).toNote())),
    ADAMANT_BARS(
        LootRarity.COMMON, min = 25, max = 50, loot = arrayOf(
            Item(
                ItemId.ADAMANTITE_BAR
            ).toNote())),
    RUNITE_BARS(
        LootRarity.COMMON, min = 15, max = 30, loot = arrayOf(
            Item(
                ItemId.RUNITE_BAR
            ).toNote())),
    GREEN_DHIDE_SET(LootRarity.COMMON, Item(ItemId.GREEN_DRAGONHIDE_SET)),
    RED_DHIDE_SET(LootRarity.COMMON, Item(ItemId.RED_DRAGONHIDE_SET)),
    BLACK_DHIDE_SET(LootRarity.COMMON, Item(ItemId.BLACK_DRAGONHIDE_SET)),
    CRYSTAL_SHARDS_MORE(
        LootRarity.COMMON,
        Item(ItemId.CRYSTAL_SHARD), min = 10, max = 30, enhancedOnly = true),
    CRYSTAL_ACORN(LootRarity.COMMON, Item(23661), max = 3, enhancedOnly = true),


    SARA_BREWS(LootRarity.RARE, Item(ItemId.SARADOMIN_BREW4).toNote(), min = 10, max = 20),
    SUPER_RESTORES(LootRarity.RARE, Item(ItemId.SUPER_RESTORE4).toNote(), min = 10, max = 20),
    DRAGON_SCIMITAR(LootRarity.RARE, Item(ItemId.DRAGON_SCIMITAR)),
    DRAGON_DAGGER(LootRarity.RARE, Item(ItemId.DRAGON_DAGGER)),
    DRAGON_BOOTS(LootRarity.RARE, Item(ItemId.DRAGON_BOOTS)),
    DRAGON_HALBERD(LootRarity.RARE, Item(ItemId.DRAGON_HALBERD)),
    DRAGON_BONES(LootRarity.RARE, Item(ItemId.DRAGON_BONES).toNote(), min = 50, max = 75),

    ABBY_WHIP(LootRarity.SUPER_RARE, Item(ItemId.ABYSSAL_WHIP)),
    TOME_OF_XP(LootRarity.SUPER_RARE, Item(30215)),
    MYSTERY_BOX(LootRarity.SUPER_RARE, Item(6199)),
    AMMY_OF_FURY(LootRarity.SUPER_RARE, Item(6585)),
    MONKEY_NUTS(LootRarity.SUPER_RARE, Item(4012)),
    DRAGONSTONE_HELM(
        LootRarity.SUPER_RARE,
        Item(ItemId.DRAGONSTONE_FULL_HELM), enhancedOnly = true),
    DRAGONSTONE_PLATE(
        LootRarity.SUPER_RARE,
        Item(ItemId.DRAGONSTONE_PLATEBODY), enhancedOnly = true),
    DRAGONSTONE_LEGS(
        LootRarity.SUPER_RARE,
        Item(ItemId.DRAGONSTONE_PLATELEGS), enhancedOnly = true),
    DRAGONSTONE_BOOTS(
        LootRarity.SUPER_RARE,
        Item(ItemId.DRAGONSTONE_BOOTS), enhancedOnly = true),
    DRAGONSTONE_GLOVES(
        LootRarity.SUPER_RARE,
        Item(ItemId.DRAGONSTONE_GAUNTLETS), enhancedOnly = true),

    PET_BOOSTER(LootRarity.JACKPOT, Item(32152)),
    SLAYER_BOOSTER(LootRarity.JACKPOT, Item(32151)),
    RANGER_BOOTIES(LootRarity.JACKPOT, Item(2577)),
    BOND(LootRarity.JACKPOT, Item(32070)),
    ONYX(LootRarity.JACKPOT, Item(ItemId.ONYX), enhancedOnly = true)
    ;

    fun primary() : Item? {
        for (item in loot)
            return item
        return null
    }

    fun toLoots(enhanced: Boolean): List<Item> {
        val loots = mutableListOf<Item>()
        for(item in loot) {
            var amount = genAmount()
            if(enhanced && amount > 1)
                amount = amount.times(1.5).toInt()
            loots.add(item.copy(amount))
        }
        return loots
    }

    private fun genAmount(): Int {
        return (min..max).random()
    }

    companion object {
        private val always = entries.filter { it.rarity == LootRarity.ALWAYS }.toTypedArray()
        private val commons = entries.filter { it.rarity == LootRarity.COMMON }.toTypedArray()
        private val rares = entries.filter { it.rarity == LootRarity.RARE }.toTypedArray()
        private val superRares = entries.filter { it.rarity == LootRarity.SUPER_RARE }.toTypedArray()
        private val jackpot = entries.filter { it.rarity == LootRarity.JACKPOT }.toTypedArray()

        private fun getAlways(enhanced: Boolean) : Array<NewCrystalChestLoot> {
            return if(enhanced)
                always
            else always.filter { !it.enhancedOnly }.toTypedArray()
        }

        private fun getCommons(enhanced: Boolean) : Array<NewCrystalChestLoot> {
            return if(enhanced)
                commons
            else commons.filter { !it.enhancedOnly }.toTypedArray()
        }

        private fun getRares(enhanced: Boolean) : Array<NewCrystalChestLoot> {
            return if(enhanced)
                rares
            else rares.filter { !it.enhancedOnly }.toTypedArray()
        }

        private fun getSuperRares(enhanced: Boolean) : Array<NewCrystalChestLoot> {
            return if(enhanced)
                superRares
            else superRares.filter { !it.enhancedOnly }.toTypedArray()
        }

        private fun getJackpots(enhanced: Boolean) : Array<NewCrystalChestLoot> {
            return if(enhanced)
                jackpot
            else jackpot.filter { !it.enhancedOnly }.toTypedArray()
        }

        fun rollTable(player: Player, enhanced: Boolean) : List<Item> {
            val loots = mutableListOf<Item>()
            /* Adds the always loots */
            for(alwaysLoot in getAlways(enhanced)) {
                loots.addAll(alwaysLoot.toLoots(enhanced))
            }

            /* Rolls each table and gets a piece of loot from that table with a maximum of 1 total roll */
            if(LootRarity.JACKPOT.rolledTable(player, enhanced)) {
                loots.addAll(getJackpots(enhanced).random().toLoots(enhanced))
            } else if(LootRarity.SUPER_RARE.rolledTable(player, enhanced)) {
                loots.addAll(getSuperRares(enhanced).random().toLoots(enhanced))
            } else if(LootRarity.RARE.rolledTable(player, enhanced)) {
                loots.addAll(getRares(enhanced).random().toLoots(enhanced))
            } else {
                loots.addAll(getCommons(enhanced).random().toLoots(enhanced))
            }

            for(loot in loots) {
                player.collectionLog.add(loot)
            }

            return loots
        }

        val enhancedEntries: ObjectArrayList<DropViewerEntry> = ObjectArrayList()
        val normalEntries: ObjectArrayList<DropViewerEntry> = ObjectArrayList()

        @JvmStatic
        fun toEntries(enhanced: Boolean): ObjectArrayList<DropViewerEntry> {
            return if(enhanced) {
                if (enhancedEntries.isEmpty)
                    mapEntries(enhanced)
                enhancedEntries
            } else {
                if (normalEntries.isEmpty)
                    mapEntries(enhanced)
                normalEntries
            }
        }

        private fun mapEntries(enhanced: Boolean) {
            if(!enhanced) {
                for(loot in entries.filter { !it.enhancedOnly }) {
                    normalEntries.add(OtherDropViewerEntry(loot.primary()?.id ?: continue, loot.min, loot.max,
                        1.0, loot.rarity.getDivisor(enhanced).toDouble(), "This item's rate is the chance to roll the associated table."))
                }
            } else {
                for(loot in entries) {
                    enhancedEntries.add(OtherDropViewerEntry(loot.primary()?.id ?: continue, loot.min, loot.max,
                        1.0, loot.rarity.getDivisor(enhanced).toDouble(), "This item's rate is the chance to roll the associated table."))
                }
            }
        }
    }
}