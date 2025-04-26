package com.zenyte.game.content.magicstorageunit.enums;

import com.zenyte.game.content.magicstorageunit.StorableSetPiece;
import com.zenyte.game.content.magicstorageunit.StorageUnitElement;
import com.zenyte.game.item.ItemId;

/**
 * @author Kris | 15/09/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum StorableArmour implements StorageUnitElement {
    CASTLE_WARS_ARMOUR(ItemId.DECORATIVE_ARMOUR_10610, new StorableSetPiece(ItemId.DECORATIVE_HELM, ItemId.DECORATIVE_HELM_4506, ItemId.DECORATIVE_HELM_4511), new StorableSetPiece(ItemId.DECORATIVE_ARMOUR, ItemId.DECORATIVE_ARMOUR_4504, ItemId.DECORATIVE_ARMOUR_4509), new StorableSetPiece(ItemId.DECORATIVE_ARMOUR_4070, ItemId.DECORATIVE_ARMOUR_4505, ItemId.DECORATIVE_ARMOUR_4510), new StorableSetPiece(ItemId.DECORATIVE_SHIELD, ItemId.DECORATIVE_SHIELD_4507, ItemId.DECORATIVE_SHIELD_4512)) {
        @Override
        public boolean singular() {
            return true;
        }
    },
    VOID_KNIGHT_ARMOUR(ItemId.VOID_KNIGHT_TOP_10611, new StorableSetPiece(ItemId.VOID_KNIGHT_TOP), new StorableSetPiece(ItemId.VOID_KNIGHT_ROBE), new StorableSetPiece(ItemId.VOID_KNIGHT_GLOVES)),
    ELITE_VOID_KNIGHT_ARMOUR(ItemId.ELITE_VOID_TOP, new StorableSetPiece(ItemId.ELITE_VOID_TOP), new StorableSetPiece(ItemId.ELITE_VOID_ROBE), new StorableSetPiece(ItemId.VOID_KNIGHT_GLOVES)),
    VOID_MELEE_HELM(ItemId.VOID_MELEE_HELM_11676, new StorableSetPiece(ItemId.VOID_MELEE_HELM)),
    VOID_RANGER_HELM(ItemId.VOID_RANGER_HELM_11675, new StorableSetPiece(ItemId.VOID_RANGER_HELM)),
    VOID_MAGE_HELM(ItemId.VOID_MAGE_HELM_11674, new StorableSetPiece(ItemId.VOID_MAGE_HELM)),
    ROGUE_ARMOUR(ItemId.ROGUE_MASK_10612, new StorableSetPiece(ItemId.ROGUE_MASK), new StorableSetPiece(ItemId.ROGUE_TOP), new StorableSetPiece(ItemId.ROGUE_TROUSERS), new StorableSetPiece(ItemId.ROGUE_GLOVES), new StorableSetPiece(ItemId.ROGUE_BOOTS)),
    SPINED_ARMOUR(ItemId.SPINED_HELM_10614, new StorableSetPiece(ItemId.SPINED_HELM), new StorableSetPiece(ItemId.SPINED_BODY), new StorableSetPiece(ItemId.SPINED_CHAPS), new StorableSetPiece(ItemId.SPINED_GLOVES), new StorableSetPiece(ItemId.SPINED_BOOTS)),
    ROCKSHELL_ARMOUR(ItemId.ROCKSHELL_HELM_10613, new StorableSetPiece(ItemId.ROCKSHELL_HELM), new StorableSetPiece(ItemId.ROCKSHELL_PLATE), new StorableSetPiece(ItemId.ROCKSHELL_LEGS), new StorableSetPiece(ItemId.ROCKSHELL_GLOVES), new StorableSetPiece(ItemId.ROCKSHELL_BOOTS)),
    POISON_TRIBAL_MASK(ItemId.TRIBAL_MASK_10615, new StorableSetPiece(ItemId.TRIBAL_MASK)),
    DISEASE_TRIBAL_MASK(ItemId.TRIBAL_MASK_10616, new StorableSetPiece(ItemId.TRIBAL_MASK_6337)),
    COMBAT_TRIBAL_MASK(ItemId.TRIBAL_MASK_10617, new StorableSetPiece(ItemId.TRIBAL_MASK_6339)),
    WHITE_KNIGHT_ARMOUR(ItemId.WHITE_PLATEBODY_10618, new StorableSetPiece(ItemId.WHITE_FULL_HELM), new StorableSetPiece(ItemId.WHITE_PLATEBODY), new StorableSetPiece(ItemId.WHITE_PLATELEGS, ItemId.WHITE_PLATESKIRT), new StorableSetPiece(ItemId.WHITE_GLOVES), new StorableSetPiece(ItemId.WHITE_BOOTS), new StorableSetPiece(ItemId.WHITE_KITESHIELD)),
    INITIATE_ARMOUR(ItemId.INITIATE_HAUBERK_10619, new StorableSetPiece(ItemId.INITIATE_SALLET), new StorableSetPiece(ItemId.INITIATE_HAUBERK), new StorableSetPiece(ItemId.INITIATE_CUISSE)),
    PROSELYTE_ARMOUR(ItemId.PROSELYTE_HAUBERK_10620, new StorableSetPiece(ItemId.PROSELYTE_SALLET), new StorableSetPiece(ItemId.PROSELYTE_HAUBERK), new StorableSetPiece(ItemId.PROSELYTE_CUISSE, ItemId.PROSELYTE_TASSET)),
    MOURNER_GEAR(ItemId.MOURNER_TOP_10621, new StorableSetPiece(ItemId.GAS_MASK), new StorableSetPiece(ItemId.MOURNER_TOP), new StorableSetPiece(ItemId.MOURNER_TROUSERS_6067), new StorableSetPiece(ItemId.MOURNER_GLOVES), new StorableSetPiece(ItemId.MOURNER_BOOTS), new StorableSetPiece(ItemId.MOURNER_CLOAK)),
    GRAAHK_HUNTER_GEAR(ItemId.GRAAHK_TOP_10624, new StorableSetPiece(ItemId.GRAAHK_HEADDRESS), new StorableSetPiece(ItemId.GRAAHK_TOP), new StorableSetPiece(ItemId.GRAAHK_LEGS)),
    LARUPIA_HUNTER_GEAR(ItemId.LARUPIA_TOP_10623, new StorableSetPiece(ItemId.LARUPIA_HAT), new StorableSetPiece(ItemId.LARUPIA_TOP), new StorableSetPiece(ItemId.LARUPIA_LEGS)),
    KYATT_HUNTER_GEAR(ItemId.KYATT_TOP_10622, new StorableSetPiece(ItemId.KYATT_HAT), new StorableSetPiece(ItemId.KYATT_TOP), new StorableSetPiece(ItemId.KYATT_LEGS)),
    POLAR_CAMOUFLAGE_GEAR(ItemId.POLAR_CAMO_TOP_10628, new StorableSetPiece(ItemId.POLAR_CAMO_TOP), new StorableSetPiece(ItemId.POLAR_CAMO_LEGS)),
    JUNGLE_CAMOUFLAGE_GEAR(ItemId.JUNGLE_CAMO_TOP_10626, new StorableSetPiece(ItemId.JUNGLE_CAMO_TOP), new StorableSetPiece(ItemId.JUNGLE_CAMO_LEGS)),
    WOODLAND_CAMOUFLAGE_GEAR(ItemId.WOOD_CAMO_TOP_10625, new StorableSetPiece(ItemId.WOOD_CAMO_TOP), new StorableSetPiece(ItemId.WOOD_CAMO_LEGS)),
    DESERT_CAMOUFLAGE_GEAR(ItemId.DESERT_CAMO_TOP_10627, new StorableSetPiece(ItemId.DESERT_CAMO_TOP), new StorableSetPiece(ItemId.DESERT_CAMO_LEGS)),
    BUILDERS_COSTUME(ItemId.HARD_HAT_10883, new StorableSetPiece(ItemId.HARD_HAT), new StorableSetPiece(ItemId.BUILDERS_SHIRT), new StorableSetPiece(ItemId.BUILDERS_TROUSERS), new StorableSetPiece(ItemId.BUILDERS_BOOTS)),
    LUMBERJACK_COSTUME(ItemId.LUMBERJACK_TOP_10945, new StorableSetPiece(ItemId.LUMBERJACK_HAT), new StorableSetPiece(ItemId.LUMBERJACK_TOP), new StorableSetPiece(ItemId.LUMBERJACK_LEGS), new StorableSetPiece(ItemId.LUMBERJACK_BOOTS)),
    BOMBER_JACKER_COSTUME(ItemId.BOMBER_JACKET_11135, new StorableSetPiece(ItemId.BOMBER_CAP, ItemId.CAP_AND_GOGGLES), new StorableSetPiece(ItemId.BOMBER_JACKET)),
    HAM_ROBES(ItemId.HAM_SHIRT_11274, new StorableSetPiece(ItemId.HAM_HOOD), new StorableSetPiece(ItemId.HAM_SHIRT), new StorableSetPiece(ItemId.HAM_ROBE), new StorableSetPiece(ItemId.HAM_GLOVES), new StorableSetPiece(ItemId.HAM_BOOTS), new StorableSetPiece(ItemId.HAM_CLOAK, ItemId.HAM_LOGO)),
    PROSPECTOR_KIT(ItemId.PROSPECTOR_HELMET, new StorableSetPiece(ItemId.PROSPECTOR_HELMET), new StorableSetPiece(ItemId.PROSPECTOR_JACKET), new StorableSetPiece(ItemId.PROSPECTOR_LEGS), new StorableSetPiece(ItemId.PROSPECTOR_BOOTS)),
    ANGLERS_OUTFIT(ItemId.ANGLER_HAT, new StorableSetPiece(ItemId.ANGLER_HAT), new StorableSetPiece(ItemId.ANGLER_TOP), new StorableSetPiece(ItemId.ANGLER_WADERS), new StorableSetPiece(ItemId.ANGLER_BOOTS)),
    SHAYZIEN_ARMOUR_T1(ItemId.SHAYZIEN_HELM_1, new StorableSetPiece(ItemId.SHAYZIEN_HELM_1), new StorableSetPiece(ItemId.SHAYZIEN_PLATEBODY_1), new StorableSetPiece(ItemId.SHAYZIEN_GREAVES_1), new StorableSetPiece(ItemId.SHAYZIEN_GLOVES_1), new StorableSetPiece(ItemId.SHAYZIEN_BOOTS_1)),
    SHAYZIEN_ARMOUR_T2(ItemId.SHAYZIEN_HELM_2, new StorableSetPiece(ItemId.SHAYZIEN_HELM_2), new StorableSetPiece(ItemId.SHAYZIEN_PLATEBODY_2), new StorableSetPiece(ItemId.SHAYZIEN_GREAVES_2), new StorableSetPiece(ItemId.SHAYZIEN_GLOVES_2), new StorableSetPiece(ItemId.SHAYZIEN_BOOTS_2)),
    SHAYZIEN_ARMOUR_T3(ItemId.SHAYZIEN_HELM_3, new StorableSetPiece(ItemId.SHAYZIEN_HELM_3), new StorableSetPiece(ItemId.SHAYZIEN_PLATEBODY_3), new StorableSetPiece(ItemId.SHAYZIEN_GREAVES_3), new StorableSetPiece(ItemId.SHAYZIEN_GLOVES_3), new StorableSetPiece(ItemId.SHAYZIEN_BOOTS_3)),
    SHAYZIEN_ARMOUR_T4(ItemId.SHAYZIEN_HELM_4, new StorableSetPiece(ItemId.SHAYZIEN_HELM_4), new StorableSetPiece(ItemId.SHAYZIEN_PLATEBODY_4), new StorableSetPiece(ItemId.SHAYZIEN_GREAVES_4), new StorableSetPiece(ItemId.SHAYZIEN_GLOVES_4), new StorableSetPiece(ItemId.SHAYZIEN_BOOTS_4)),
    SHAYZIEN_ARMOUR_T5(ItemId.SHAYZIEN_HELM_5, new StorableSetPiece(ItemId.SHAYZIEN_HELM_5), new StorableSetPiece(ItemId.SHAYZIEN_PLATEBODY_5), new StorableSetPiece(ItemId.SHAYZIEN_GREAVES_5), new StorableSetPiece(ItemId.SHAYZIEN_GLOVES_5), new StorableSetPiece(ItemId.SHAYZIEN_BOOTS_5)),
    XERICIAN_ROBES(ItemId.XERICIAN_HAT, new StorableSetPiece(ItemId.XERICIAN_HAT), new StorableSetPiece(ItemId.XERICIAN_TOP), new StorableSetPiece(ItemId.XERICIAN_ROBE)),
    FARMERS_OUTFIT(ItemId.FARMERS_STRAWHAT, new StorableSetPiece(ItemId.FARMERS_STRAWHAT, ItemId.FARMERS_STRAWHAT_13647), new StorableSetPiece(ItemId.FARMERS_JACKET, ItemId.FARMERS_SHIRT), new StorableSetPiece(ItemId.FARMERS_BORO_TROUSERS, ItemId.FARMERS_BORO_TROUSERS_13641), new StorableSetPiece(ItemId.FARMERS_BOOTS, ItemId.FARMERS_BOOTS_13645)) {
        @Override
        public boolean singular() {
            return true;
        }
    },
    CLUE_HUNTER_OUTFIT(ItemId.CLUE_HUNTER_GARB, new StorableSetPiece(ItemId.CLUE_HUNTER_GARB), new StorableSetPiece(ItemId.CLUE_HUNTER_TROUSERS), new StorableSetPiece(ItemId.CLUE_HUNTER_GLOVES), new StorableSetPiece(ItemId.CLUE_HUNTER_BOOTS), new StorableSetPiece(ItemId.CLUE_HUNTER_CLOAK)),
    CORRUPTED_ARMOUR(ItemId.CORRUPTED_HELM, new StorableSetPiece(ItemId.CORRUPTED_HELM), new StorableSetPiece(ItemId.CORRUPTED_PLATEBODY), new StorableSetPiece(ItemId.CORRUPTED_PLATELEGS, ItemId.CORRUPTED_PLATESKIRT), new StorableSetPiece(ItemId.CORRUPTED_KITESHIELD)),
    ANCESTRAL_ROBES(ItemId.ANCESTRAL_HAT, new StorableSetPiece(ItemId.ANCESTRAL_HAT), new StorableSetPiece(ItemId.ANCESTRAL_ROBE_TOP), new StorableSetPiece(ItemId.ANCESTRAL_ROBE_BOTTOM)),
    OBSIDIAN_ARMOUR(ItemId.OBSIDIAN_HELMET, new StorableSetPiece(ItemId.OBSIDIAN_HELMET), new StorableSetPiece(ItemId.OBSIDIAN_PLATEBODY), new StorableSetPiece(ItemId.OBSIDIAN_PLATELEGS)),
    HELM_OF_RAEDWALD(ItemId.HELM_OF_RAEDWALD, new StorableSetPiece(ItemId.HELM_OF_RAEDWALD)),
    PENANCE_GLOVES(ItemId.PENANCE_GLOVES, new StorableSetPiece(ItemId.PENANCE_GLOVES)),
    PENANCE_SKIRT(ItemId.PENANCE_SKIRT, new StorableSetPiece(ItemId.PENANCE_SKIRT)),
    FIGHTER_TORSO(ItemId.FIGHTER_TORSO, new StorableSetPiece(ItemId.FIGHTER_TORSO)),
    RUNNER_BOOTS(ItemId.RUNNER_BOOTS, new StorableSetPiece(ItemId.RUNNER_BOOTS)),
    RUNNER_HAT(ItemId.RUNNER_HAT, new StorableSetPiece(ItemId.RUNNER_HAT)),
    FIGHTER_HAT(ItemId.FIGHTER_HAT, new StorableSetPiece(ItemId.FIGHTER_HAT)),
    HEALER_HAT(ItemId.HEALER_HAT, new StorableSetPiece(ItemId.HEALER_HAT)),
    RANGER_HAT(ItemId.RANGER_HAT, new StorableSetPiece(ItemId.RANGER_HAT)),
    JUSTICIAR_ARMOUR(ItemId.JUSTICIAR_FACEGUARD, new StorableSetPiece(ItemId.JUSTICIAR_FACEGUARD), new StorableSetPiece(ItemId.JUSTICIAR_CHESTGUARD), new StorableSetPiece(ItemId.JUSTICIAR_LEGGUARDS)),
    ;

    private final int displayItem;
    private final StorableSetPiece[] pieces;

    StorableArmour(final int displayItem, final StorableSetPiece... pieces) {
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
