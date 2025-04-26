package com.zenyte.game.content.magicstorageunit.enums;

import com.zenyte.game.content.magicstorageunit.StorableSetPiece;
import com.zenyte.game.content.magicstorageunit.StorageUnitElement;
import com.zenyte.game.item.ItemId;

/**
 * @author Kris | 15/09/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum StorableCapes implements StorageUnitElement {
    CAPE_OF_LEGENDS(ItemId.CAPE_OF_LEGENDS_10635, new StorableSetPiece(ItemId.CAPE_OF_LEGENDS)),
    OBSIDIAN_CAPE(ItemId.OBSIDIAN_CAPE_10636, new StorableSetPiece(ItemId.OBSIDIAN_CAPE)),
    FIRE_CAPE(ItemId.FIRE_CAPE_10637, new StorableSetPiece(ItemId.FIRE_CAPE)),
    SARADOMIN_CAPE(ItemId.SARADOMIN_CAPE, new StorableSetPiece(ItemId.SARADOMIN_CAPE)),
    GUTHIX_CAPE(ItemId.GUTHIX_CAPE, new StorableSetPiece(ItemId.GUTHIX_CAPE)),
    ZAMORAK_CAPE(ItemId.ZAMORAK_CAPE, new StorableSetPiece(ItemId.ZAMORAK_CAPE)),
    ATTACK_CAPE(ItemId.ATTACK_CAPE_10639, new StorableSetPiece(ItemId.ATTACK_CAPE, ItemId.ATTACK_CAPET), new StorableSetPiece(ItemId.ATTACK_HOOD)),
    DEFENCE_CAPE(ItemId.DEFENCE_CAPE_10641, new StorableSetPiece(ItemId.DEFENCE_CAPE, ItemId.DEFENCE_CAPET), new StorableSetPiece(ItemId.DEFENCE_HOOD)),
    STRENGTH_CAPE(ItemId.STRENGTH_CAPE_10640, new StorableSetPiece(ItemId.STRENGTH_CAPE, ItemId.STRENGTH_CAPET), new StorableSetPiece(ItemId.STRENGTH_HOOD)),
    HITPOINTS_CAPE(ItemId.HITPOINTS_CAPE_10647, new StorableSetPiece(ItemId.HITPOINTS_CAPE, ItemId.HITPOINTS_CAPET), new StorableSetPiece(ItemId.HITPOINTS_HOOD)),
    AGILITY_CAPE(ItemId.AGILITY_CAPE_10648, new StorableSetPiece(ItemId.AGILITY_CAPE, ItemId.AGILITY_CAPET), new StorableSetPiece(ItemId.AGILITY_HOOD)),
    COOKING_CAPE(ItemId.COOKING_CAPE_10658, new StorableSetPiece(ItemId.COOKING_CAPE, ItemId.COOKING_CAPET), new StorableSetPiece(ItemId.COOKING_HOOD)),
    CONSTRUCTION_CAPE(ItemId.CONSTRUCT_CAPE_10654, new StorableSetPiece(ItemId.CONSTRUCT_CAPE, ItemId.CONSTRUCT_CAPET), new StorableSetPiece(ItemId.CONSTRUCT_HOOD)),
    CRAFTING_CAPE(ItemId.CRAFTING_CAPE_10651, new StorableSetPiece(ItemId.CRAFTING_CAPE, ItemId.CRAFTING_CAPET), new StorableSetPiece(ItemId.CRAFTING_HOOD)),
    FARMING_CAPE(ItemId.FARMING_CAPE_10661, new StorableSetPiece(ItemId.FARMING_CAPE, ItemId.FARMING_CAPET), new StorableSetPiece(ItemId.FARMING_HOOD)),
    FIREMAKING_CAPE(ItemId.FIREMAKING_CAPE_10659, new StorableSetPiece(ItemId.FIREMAKING_CAPE, ItemId.FIREMAKING_CAPET), new StorableSetPiece(ItemId.FIREMAKING_HOOD)),
    FISHING_CAPE(ItemId.FISHING_CAPE_10657, new StorableSetPiece(ItemId.FISHING_CAPE, ItemId.FISHING_CAPET), new StorableSetPiece(ItemId.FISHING_HOOD)),
    FLETCHING_CAPE(ItemId.FLETCHING_CAPE_10652, new StorableSetPiece(ItemId.FLETCHING_CAPE, ItemId.FLETCHING_CAPET), new StorableSetPiece(ItemId.FLETCHING_HOOD)),
    HERBLORE_CAPE(ItemId.HERBLORE_CAPE_10649, new StorableSetPiece(ItemId.HERBLORE_CAPE, ItemId.HERBLORE_CAPET), new StorableSetPiece(ItemId.HERBLORE_HOOD)),
    MAGIC_CAPE(ItemId.MAGIC_CAPE_10644, new StorableSetPiece(ItemId.MAGIC_CAPE, ItemId.MAGIC_CAPET), new StorableSetPiece(ItemId.MAGIC_HOOD)),
    MINING_CAPE(ItemId.MINING_CAPE_10655, new StorableSetPiece(ItemId.MINING_CAPE, ItemId.MINING_CAPET), new StorableSetPiece(ItemId.MINING_HOOD)),
    PRAYER_CAPE(ItemId.PRAYER_CAPE_10643, new StorableSetPiece(ItemId.PRAYER_CAPE, ItemId.PRAYER_CAPET), new StorableSetPiece(ItemId.PRAYER_HOOD)),
    RANGING_CAPE(ItemId.RANGING_CAPE_10642, new StorableSetPiece(ItemId.RANGING_CAPE, ItemId.RANGING_CAPET), new StorableSetPiece(ItemId.RANGING_HOOD)),
    RUNECRAFT_CAPE(ItemId.RUNECRAFT_CAPE_10645, new StorableSetPiece(ItemId.RUNECRAFT_CAPE, ItemId.RUNECRAFT_CAPET), new StorableSetPiece(ItemId.RUNECRAFTING_HOOD)),
    SLAYER_CAPE(ItemId.SLAYER_CAPE_10653, new StorableSetPiece(ItemId.SLAYER_CAPE, ItemId.SLAYER_CAPET), new StorableSetPiece(ItemId.SLAYER_HOOD)),
    SMITHING_CAPE(ItemId.SMITHING_CAPE_10656, new StorableSetPiece(ItemId.SMITHING_CAPE, ItemId.SMITHING_CAPET), new StorableSetPiece(ItemId.SMITHING_HOOD)),
    THIEVING_CAPE(ItemId.THIEVING_CAPE_10650, new StorableSetPiece(ItemId.THIEVING_CAPE, ItemId.THIEVING_CAPET), new StorableSetPiece(ItemId.THIEVING_HOOD)),
    WOODCUTTING_CAPE(ItemId.WOODCUTTING_CAPE_10660, new StorableSetPiece(ItemId.WOODCUTTING_CAPE, ItemId.WOODCUT_CAPET), new StorableSetPiece(ItemId.WOODCUTTING_HOOD)),
    HUNTER_CAPE(ItemId.HUNTER_CAPE_10646, new StorableSetPiece(ItemId.HUNTER_CAPE, ItemId.HUNTER_CAPET), new StorableSetPiece(ItemId.HUNTER_HOOD)),
    QUEST_POINT_CAPE(ItemId.QUEST_POINT_CAPE_10662, new StorableSetPiece(ItemId.QUEST_POINT_CAPE, ItemId.QUEST_POINT_CAPE_T), new StorableSetPiece(ItemId.QUEST_POINT_HOOD)),
    ACHIEVEMENT_DIARY_CAPE(ItemId.ACHIEVEMENT_DIARY_CAPE, new StorableSetPiece(ItemId.ACHIEVEMENT_DIARY_CAPE, ItemId.ACHIEVEMENT_DIARY_CAPE_T), new StorableSetPiece(ItemId.ACHIEVEMENT_DIARY_HOOD)),
    MUSIC_CAPE(ItemId.MUSIC_CAPE_13224, new StorableSetPiece(ItemId.MUSIC_CAPE, ItemId.MUSIC_CAPET), new StorableSetPiece(ItemId.MUSIC_HOOD)),
    SPOTTED_HUNTING_CAPE(ItemId.SPOTTED_CAPE_10663, new StorableSetPiece(ItemId.SPOTTED_CAPE)),
    SPOTTIER_HUNTING_CAPE(ItemId.SPOTTIER_CAPE_10664, new StorableSetPiece(ItemId.SPOTTIER_CAPE)),
    MAX_CAPE(ItemId.MAX_CAPE_13282, new StorableSetPiece(ItemId.MAX_CAPE_13342), new StorableSetPiece(ItemId.MAX_HOOD)),
    ANCIENT_MAX_CAPE(ItemId.ANCIENT_MAX_CAPE, new StorableSetPiece(ItemId.ANCIENT_MAX_CAPE)),
    ARMADYL_MAX_CAPE(ItemId.ARMADYL_MAX_CAPE, new StorableSetPiece(ItemId.ARMADYL_MAX_CAPE)),
    BANDOS_MAX_CAPE(ItemId.BANDOS_MAX_CAPE, new StorableSetPiece(ItemId.BANDOS_MAX_CAPE)),
    SEREN_MAX_CAPE(ItemId.SEREN_MAX_CAPE, new StorableSetPiece(ItemId.SEREN_MAX_CAPE)),
    INFERNAL_CAPE(ItemId.INFERNAL_CAPE, new StorableSetPiece(ItemId.INFERNAL_CAPE)),
    CHAMPIONS_CAPE(ItemId.CHAMPIONS_CAPE, new StorableSetPiece(ItemId.CHAMPIONS_CAPE)),
    IMBUED_SARADOMIN_CAPE(ItemId.IMBUED_SARADOMIN_CAPE, new StorableSetPiece(ItemId.IMBUED_SARADOMIN_CAPE)),
    IMBUED_GUTHIX_CAPE(ItemId.IMBUED_GUTHIX_CAPE, new StorableSetPiece(ItemId.IMBUED_GUTHIX_CAPE)),
    IMBUED_ZAMORAK_CAPE(ItemId.IMBUED_ZAMORAK_CAPE, new StorableSetPiece(ItemId.IMBUED_ZAMORAK_CAPE)),
    MYTHICAL_CAPE(ItemId.MYTHICAL_CAPE_22114, new StorableSetPiece(ItemId.MYTHICAL_CAPE_22114)),
    XERICS_GUARD(ItemId.XERICS_GUARD, new StorableSetPiece(ItemId.XERICS_GUARD)),
    XERICS_CHAMPION(ItemId.XERICS_CHAMPION, new StorableSetPiece(ItemId.XERICS_CHAMPION)),
    XERICS_WARRIOR(ItemId.XERICS_WARRIOR, new StorableSetPiece(ItemId.XERICS_WARRIOR)),
    XERICS_SENTINEL(ItemId.XERICS_SENTINEL, new StorableSetPiece(ItemId.XERICS_SENTINEL)),
    XERICS_GENERAL(ItemId.XERICS_GENERAL, new StorableSetPiece(ItemId.XERICS_GENERAL)),
    SINHAZA_SHROUD_TIER_1(ItemId.SINHAZA_SHROUD_TIER_1, new StorableSetPiece(ItemId.SINHAZA_SHROUD_TIER_1)),
    SINHAZA_SHROUD_TIER_2(ItemId.SINHAZA_SHROUD_TIER_2, new StorableSetPiece(ItemId.SINHAZA_SHROUD_TIER_2)),
    SINHAZA_SHROUD_TIER_3(ItemId.SINHAZA_SHROUD_TIER_3, new StorableSetPiece(ItemId.SINHAZA_SHROUD_TIER_3)),
    SINHAZA_SHROUD_TIER_4(ItemId.SINHAZA_SHROUD_TIER_4, new StorableSetPiece(ItemId.SINHAZA_SHROUD_TIER_4)),
    SINHAZA_SHROUD_TIER_5(ItemId.SINHAZA_SHROUD_TIER_5, new StorableSetPiece(ItemId.SINHAZA_SHROUD_TIER_5)),
    WILDERNESS_CAPE(ItemId.TEAM1_CAPE_10638, new StorableSetPiece(ItemId.TEAM1_CAPE, ItemId.TEAM2_CAPE, ItemId.TEAM3_CAPE, ItemId.TEAM4_CAPE, ItemId.TEAM5_CAPE, ItemId.TEAM6_CAPE, ItemId.TEAM7_CAPE,
            ItemId.TEAM8_CAPE, ItemId.TEAM9_CAPE, ItemId.TEAM10_CAPE, ItemId.TEAM11_CAPE, ItemId.TEAM12_CAPE, ItemId.TEAM13_CAPE, ItemId.TEAM14_CAPE, ItemId.TEAM15_CAPE, ItemId.TEAM16_CAPE,
            ItemId.TEAM17_CAPE, ItemId.TEAM18_CAPE, ItemId.TEAM19_CAPE, ItemId.TEAM20_CAPE, ItemId.TEAM21_CAPE, ItemId.TEAM22_CAPE, ItemId.TEAM23_CAPE, ItemId.TEAM24_CAPE, ItemId.TEAM25_CAPE,
            ItemId.TEAM26_CAPE, ItemId.TEAM27_CAPE, ItemId.TEAM28_CAPE, ItemId.TEAM29_CAPE, ItemId.TEAM30_CAPE, ItemId.TEAM31_CAPE, ItemId.TEAM32_CAPE, ItemId.TEAM33_CAPE, ItemId.TEAM34_CAPE,
            ItemId.TEAM35_CAPE, ItemId.TEAM36_CAPE, ItemId.TEAM37_CAPE, ItemId.TEAM38_CAPE, ItemId.TEAM39_CAPE, ItemId.TEAM40_CAPE, ItemId.TEAM41_CAPE, ItemId.TEAM42_CAPE, ItemId.TEAM43_CAPE,
            ItemId.TEAM44_CAPE, ItemId.TEAM45_CAPE, ItemId.TEAM46_CAPE, ItemId.TEAM47_CAPE, ItemId.TEAM48_CAPE, ItemId.TEAM49_CAPE, ItemId.TEAM50_CAPE)),
    ;

    private final int displayItem;
    private final StorableSetPiece[] pieces;

    StorableCapes(final int displayItem, final StorableSetPiece... pieces) {
        this.displayItem = displayItem;
        this.pieces = pieces;
    }

    public int getDisplayItem() {
        return displayItem;
    }

    public StorableSetPiece[] getPieces() {
        return pieces;
    }
}
