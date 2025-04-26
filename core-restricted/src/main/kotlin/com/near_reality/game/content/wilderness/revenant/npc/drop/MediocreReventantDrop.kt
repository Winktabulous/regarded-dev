package com.near_reality.game.content.wilderness.revenant.npc.drop

import com.zenyte.game.item.Item
import com.zenyte.game.model.item.ImmutableItem
import com.zenyte.game.util.Utils

enum class MediocreReventantDrop(val weight: Int, val item: ImmutableItem) {
    DRAGON_PLATELEGS(1, ImmutableItem(4088, 1, 2)),
    DRAGON_PLATESKIRT(1, ImmutableItem(4586, 1, 2)),
    RUNE_FULL_HELM(2, ImmutableItem(1164, 2, 2)),
    RUNE_PLATEBODY(2, ImmutableItem(1128, 2, 2)),
    RUNE_PLATELEGS(2, ImmutableItem(1080, 2, 2)),
    RUNE_KITESHIELD(2, ImmutableItem(1202, 2, 2)),
    RUNE_WARHAMMER(2, ImmutableItem(1348, 2, 2)),
    DRAGON_LONGSWORD(1, ImmutableItem(1306, 2, 2)),
    DRAGON_DAGGER(1, ImmutableItem(1216, 2, 2)),
    SUPER_RESTORES(4, ImmutableItem(3025, 4, 10)),
    ONYX_TIPS(4, ImmutableItem(9194, 9, 26)),
    DRAGONSTONE_TIPS(4, ImmutableItem(9193, 60, 120)),
    DRAGONSTONE(1, ImmutableItem(1632, 8, 14)),
    DEATH_RUNES(3, ImmutableItem(560, 90, 360)),
    BLOOD_RUNES(3, ImmutableItem(565, 90, 360)),
    LAW_RUNES(3, ImmutableItem(563, 100, 420)),
    RUNITE_ORES(6, ImmutableItem(452, 5, 12)),
    ADAMANT_BARS(6, ImmutableItem(2362, 15, 24)),
    COAL(6, ImmutableItem(454, 100, 250)),
    BATTLESTAVES(5, ImmutableItem(1392, 8, 8)),
    BLACK_DRAGONHIDE(6, ImmutableItem(1748, 13, 24)),
    MAHOGANY_PLANKS(5, ImmutableItem(8783, 19, 37)),
    MAGIC_LOGS(2, ImmutableItem(1514, 20, 55)),
    YEW_LOGS(3, ImmutableItem(1516, 70, 180)),
    MANTA_RAYS(3, ImmutableItem(392, 45, 90)),
    RUNE_BARS(6, ImmutableItem(2364, 5, 12)),
    BLIGHTED_ANCIENT_ICE_SACK(10, ImmutableItem(24607, 8, 55)),
    BLIGHTED_ENTANGLE_SACK(10, ImmutableItem(24613, 8, 55)),
    BLIGHTED_TELEPORT_SCROLL_SACK(10, ImmutableItem(24615, 8, 55)),
    BLIGHTED_VENGEANCE_SACK(10, ImmutableItem(24621, 8, 55)),
    BLIGHTED_BIND_SACK(10, ImmutableItem(24609, 8, 40)),
    BLIGHTED_SURGE_SACK(10, ImmutableItem(26705, 8, 55));

    companion object {
        private val values = entries.toTypedArray()

        fun get(): Item {
            val random = Utils.random(165)
            var roll = 0
            for (drop in values) {
                if ((drop.weight.let { roll += it; roll }) >= random) {
                    return Item(drop.item.id, Utils.random(drop.item.minAmount, drop.item.maxAmount))
                }
            }
            return Item(21817)
        }
    }
}
