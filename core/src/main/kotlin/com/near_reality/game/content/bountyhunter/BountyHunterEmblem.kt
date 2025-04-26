package com.near_reality.game.content.bountyhunter


import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.world.entity.player.Player
import java.util.*

/**
 * This enumeration lists all bounty hunter emblems, and centralizes their value, upgrade chance, and item ids.
 * @author John J. Woloszyk / Kryeus
 */
enum class BountyHunterEmblem(val itemId: Int, val points: Int, val upgradeChance: Int, val spriteId: Int, val index: Int){
    TIER_1(ItemId.ANTIQUE_EMBLEM_TIER_1, BountyHunterVars.T1_EMBLEM_AMOUNT, BountyHunterVars.T1_UPGRADE_CHANCE, BountyHunterVars.SKULL_T1, 1),
    TIER_2(ItemId.ANTIQUE_EMBLEM_TIER_2, BountyHunterVars.T2_EMBLEM_AMOUNT, BountyHunterVars.T2_UPGRADE_CHANCE, BountyHunterVars.SKULL_T2, 2),
    TIER_3(ItemId.ANTIQUE_EMBLEM_TIER_3, BountyHunterVars.T3_EMBLEM_AMOUNT, BountyHunterVars.T3_UPGRADE_CHANCE, BountyHunterVars.SKULL_T3, 3),
    TIER_4(ItemId.ANTIQUE_EMBLEM_TIER_4, BountyHunterVars.T4_EMBLEM_AMOUNT, BountyHunterVars.T4_UPGRADE_CHANCE, BountyHunterVars.SKULL_T4, 4),
    TIER_5(ItemId.ANTIQUE_EMBLEM_TIER_5, BountyHunterVars.T5_EMBLEM_AMOUNT, BountyHunterVars.T5_UPGRADE_CHANCE, BountyHunterVars.SKULL_T5, 5),
    TIER_6(ItemId.ANTIQUE_EMBLEM_TIER_6, BountyHunterVars.T6_EMBLEM_AMOUNT, BountyHunterVars.T6_UPGRADE_CHANCE, BountyHunterVars.SKULL_T6, 6),
    TIER_7(ItemId.ANTIQUE_EMBLEM_TIER_7, BountyHunterVars.T7_EMBLEM_AMOUNT, BountyHunterVars.T7_UPGRADE_CHANCE, BountyHunterVars.SKULL_T7, 7),
    TIER_8(ItemId.ANTIQUE_EMBLEM_TIER_8, BountyHunterVars.T8_EMBLEM_AMOUNT, BountyHunterVars.T8_UPGRADE_CHANCE, BountyHunterVars.SKULL_T8, 8),
    TIER_9(ItemId.ANTIQUE_EMBLEM_TIER_9, BountyHunterVars.T9_EMBLEM_AMOUNT, BountyHunterVars.T9_UPGRADE_CHANCE, BountyHunterVars.SKULL_T9, 9),
    TIER_10(ItemId.ANTIQUE_EMBLEM_TIER_10, BountyHunterVars.T10_EMBLEM_AMOUNT, BountyHunterVars.T10_UPGRADE_CHANCE, BountyHunterVars.SKULL_T10, 10);


    companion object {
        private val EMBLEMS: Set<BountyHunterEmblem> = Collections.unmodifiableSet(EnumSet.allOf(BountyHunterEmblem::class.java))
        private val BEST_EMBLEM_COMPARATOR: Comparator<BountyHunterEmblem> = Comparator.comparingInt { it.itemId }


        fun forId(id: Int): Optional<BountyHunterEmblem> {
            return EMBLEMS.stream().filter { it.itemId == id }.findAny()
        }

        fun getNextOrLast(current: BountyHunterEmblem) : BountyHunterEmblem {
            return valueOf(current.index + 1).orElse(TIER_10)
        }

        fun getPreviousOrFirst(current: BountyHunterEmblem) : BountyHunterEmblem {
            return valueOf(current.index - 1).orElse(TIER_1)
        }

        fun valueOf(index: Int): Optional<BountyHunterEmblem> {
            return EMBLEMS.stream().filter { it.index == index }.findFirst()
        }

        /**
         * Gets the best emblem that is currently present in the player's inventory
         */
        fun getBest(player: Player, excludeT10: Boolean): Optional<BountyHunterEmblem> {
            val emblems = EMBLEMS.filter { emblem ->
                player.inventory.containsItem(Item(emblem.itemId)) && (!excludeT10 || emblem != TIER_10)
            }

            return emblems.maxWithOrNull(BEST_EMBLEM_COMPARATOR).let { Optional.ofNullable(it) }
        }

        /**
         * Gets the best three emblems that is currently present in the player's inventory
         */
        fun getTopThree(player: Player, exclude: Boolean): List<BountyHunterEmblem> {
            val emblems: List<BountyHunterEmblem> = EMBLEMS.filter { emblem ->  player.inventory.containsItem(Item(emblem.itemId)) && (!exclude || exclude && emblem != TIER_10)}

            return if (emblems.isEmpty()) {
                emptyList()
            } else {
                emblems.sortedWith(compareByDescending { it.index }).slice(1..3)
            }
        }

        /**
         * Determines the difference in tier's based on two emblems
         */
        fun getTierDifference(
            killerEmblem: BountyHunterEmblem,
            targetEmblem: BountyHunterEmblem
        ) : Int {
            return if(killerEmblem.index > targetEmblem.index)
                0
            else
                targetEmblem.index - killerEmblem.index
        }

        /**
         * Gets a list of all items in a player's inventory
         * that match an emblem ID
         */
        fun getAllFromInventory(p: Player): List<Item> {
            return p.inventory.container.itemsAsList.filter { forId(it.id).isPresent }
        }
    }
}