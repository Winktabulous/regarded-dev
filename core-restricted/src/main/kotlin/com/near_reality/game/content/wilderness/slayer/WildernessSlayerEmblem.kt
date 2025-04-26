package com.near_reality.game.content.wilderness.slayer

import com.zenyte.game.item.ItemId
import com.zenyte.game.util.Utils
import com.zenyte.utils.Ordinal

@Ordinal
enum class WildernessSlayerEmblem(@JvmField val id: Int, val chance: Int) {

    T1(ItemId.MYSTERIOUS_EMBLEM, 10_000),
    T2(ItemId.MYSTERIOUS_EMBLEM_TIER_2, 9_000),
    T3(ItemId.MYSTERIOUS_EMBLEM_TIER_3, 7_000),
    T4(ItemId.MYSTERIOUS_EMBLEM_TIER_4, 5_000),
    T5(ItemId.MYSTERIOUS_EMBLEM_TIER_5, 2_500),
    T6(ItemId.MYSTERIOUS_EMBLEM_TIER_6, 1_000),
    T7(ItemId.MYSTERIOUS_EMBLEM_TIER_7, 500),
    T8(ItemId.MYSTERIOUS_EMBLEM_TIER_8, 200),
    T9(ItemId.MYSTERIOUS_EMBLEM_TIER_9, 100),
    T10(ItemId.MYSTERIOUS_EMBLEM_TIER_10, 25);

    fun nextOrNull(): WildernessSlayerEmblem? =
        entries.getOrNull(ordinal+1)

    companion object {

        private val totalWeight by lazy { entries.sumOf { it.chance } }

        @JvmStatic
        fun roll(): WildernessSlayerEmblem? {
            val random = Utils.random(totalWeight)
            var current = 0
            for (reward in entries) {
                current += reward.chance
                if (current >= random)
                    return reward
            }
            return null
        }
    }
}
