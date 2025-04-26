package com.near_reality.game.content.araxxor.rewards

import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.item.ItemId.*
import java.util.concurrent.ThreadLocalRandom

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-29
 */
enum class AraxxorNormalDropTable(
    val id: Int,
    val minAmount: Int,
    val maxAmount: Int,
    val weight: Double
) {
    // Weapons and Armor
    RUNE_KITE(RUNE_KITESHIELD+1, 2, 2, 14.38),
    RUNE_LEGS(RUNE_PLATELEGS+1, 2, 2, 14.38),
    DRAGON_MACE(ItemId.DRAGON_MACE, 2, 2, 19.17),
    RUNE_2H(RUNE_2H_SWORD+1, 5, 5, 115.0),
    DRAGON_LEGS(DRAGON_PLATELEGS, 2, 2, 115.0),

    // Runes and ammo
    DRATH(DEATH_RUNE, 250, 250, 23.0),
    NATURE(NATURE_RUNE, 80, 80, 57.5),
    MUD(MUD_RUNE, 100, 100, 115.0),
    BLOOD(BLOOD_RUNE, 180, 180, 115.0),

    // Seeds
    YEW(YEW_SEED, 1, 1, 28.75),
    TOADFLAX(TOADFLAX_SEED, 4, 4, 38.33),
    RANARR(RANARR_SEED, 3, 3, 115.0),
    SNAPDRAGON(SNAPDRAGON_SEED, 3, 3, 115.0),
    MAGIC(MAGIC_SEED, 2, 2, 115.0),

    // Resources
    COAL(ItemId.COAL+1, 120, 120, 28.75),
    ADAMANITE(ItemId.ADAMANTITE_ORE+1, 85, 85, 28.75),
    RAW_SHARK_1(ItemId.RAW_SHARK+1, 21, 21, 28.75),
    YEW_LOG(YEW_LOGS+1, 70, 70, 38.33),
    RUNE_ORE(RUNITE_ORE+1, 12, 12, 57.5),
    RAW_SHARK_2(RAW_SHARK+1, 100, 100, 115.0),
    RAW_MONK(RAW_MONKFISH+1, 120, 120, 115.0),
    PURE_ESS(PURE_ESSENCE+1, 1200, 1200, 115.0),

    // Other
    SPIDER_TELEPORT(SPIDER_CAVE_TELEPORT, 3, 3, 14.38),
    EARTH_ORB(ItemId.EARTH_ORB+1, 45, 45, 19.17),
    VENOM_SACK_1(ARAXYTE_VENOM_SACK, 6, 6, 23.0),
    MYRE_FUNGUS(MORT_MYRE_FUNGUS+1, 24, 24, 28.75),
    ANTIDOTE_PP(ANTIDOTE3+1, 6, 6, 28.75),
    ZAMMY_WINE(WINE_OF_ZAMORAK+1, 8, 8, 38.33),
    SPIDER_EGG(RED_SPIDERS_EGGS+1, 40, 40, 57.5),
    VENOM_SACK_2(ARAXYTE_VENOM_SACK, 12, 12, 57.5),
    BARK(ItemId.BARK+1, 15, 15, 115.0),
    HEAD(ARAXYTE_HEAD, 1, 1, 250.0),
    VENOM_JAR(JAR_OF_VENOM, 1, 1, 1500.0)
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
            return rollForItem()
        }
    }
}