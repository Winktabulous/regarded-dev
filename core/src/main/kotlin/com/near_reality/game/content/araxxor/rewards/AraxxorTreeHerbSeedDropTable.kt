package com.near_reality.game.content.araxxor.rewards

import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId.*
import java.util.concurrent.ThreadLocalRandom

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-29
 */
enum class AraxxorTreeHerbSeedDropTable(
    val id: Int,
    val minAmount: Int,
    val maxAmount: Int,
    val weight: Double
) {
    RANARR(RANARR_SEED, 1, 1, 958.3),
    SNAPDRAGON(SNAPDRAGON_SEED, 1, 1, 1027.0),
    TORSTOL(TORSTOL_SEED, 1, 1, 1307.0),
    WATERMELON(WATERMELON_SEED, 15, 15, 1369.0),
    WILLOW(WILLOW_SEED, 1, 1, 1438.0),
    MAHOGANY(MAHOGANY_SEED, 1, 1, 1597.0),
    MAPLE(MAPLE_SEED, 1, 1, 1597.0),
    TEAK(TEAK_SEED, 1, 1, 1597.0),
    YEW(YEW_SEED, 1, 1, 1597.0),
    PAPAYA(PAPAYA_TREE_SEED, 1, 1, 2054.0),
    MAGIC(MAGIC_SEED, 1, 1, 2614.0),
    PALM(PALM_TREE_SEED, 1, 1, 2875.0),
    SPIRIT(SPIRIT_SEED, 1, 1, 3594.0),
    DRAGONFRUIT(DRAGONFRUIT_TREE_SEED, 1, 1, 4792.0),
    CELASTRUS(CELASTRUS_SEED, 1, 1, 7188.0),
    REDWOOD(REDWOOD_TREE_SEED, 1, 1, 7188.0)
    ;

    companion object {
        fun rollForItem(): Item? {
            val diceRole = ThreadLocalRandom.current().nextDouble()
            val sortedDrops = AraxxorNormalDropTable.entries.toTypedArray()
                .sortedByDescending { it.weight }
                .shuffled()

            sortedDrops.forEach { drop ->
                val required = 1 / drop.weight
                if (diceRole < required)
                    return Item(drop.id, ThreadLocalRandom.current().nextInt(drop.minAmount, drop.maxAmount + 1))
            }
            return null
        }
    }
}