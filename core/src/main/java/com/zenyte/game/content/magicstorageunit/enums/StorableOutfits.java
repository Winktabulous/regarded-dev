package com.zenyte.game.content.magicstorageunit.enums;

import com.zenyte.game.content.magicstorageunit.StorableSetPiece;
import com.zenyte.game.content.magicstorageunit.StorageUnitElement;
import com.zenyte.game.item.ItemId;

/**
 * @author Kris | 15/09/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum StorableOutfits implements StorageUnitElement {
    BLUE_MYSTIC_ROBES(ItemId.MYSTIC_HAT_10601, new StorableSetPiece(ItemId.MYSTIC_HAT), new StorableSetPiece(ItemId.MYSTIC_ROBE_TOP), new StorableSetPiece(ItemId.MYSTIC_ROBE_BOTTOM), new StorableSetPiece(ItemId.MYSTIC_GLOVES), new StorableSetPiece(ItemId.MYSTIC_BOOTS)),
    DARK_MYSTIC_ROBES(ItemId.MYSTIC_HAT_DARK_10602, new StorableSetPiece(ItemId.MYSTIC_HAT_DARK), new StorableSetPiece(ItemId.MYSTIC_ROBE_TOP_DARK), new StorableSetPiece(ItemId.MYSTIC_ROBE_BOTTOM_DARK), new StorableSetPiece(ItemId.MYSTIC_GLOVES_DARK), new StorableSetPiece(ItemId.MYSTIC_BOOTS_DARK)),
    LIGHT_MYSTIC_ROBES(ItemId.MYSTIC_HAT_LIGHT_10603, new StorableSetPiece(ItemId.MYSTIC_HAT_LIGHT), new StorableSetPiece(ItemId.MYSTIC_ROBE_TOP_LIGHT), new StorableSetPiece(ItemId.MYSTIC_ROBE_BOTTOM_LIGHT), new StorableSetPiece(ItemId.MYSTIC_GLOVES_LIGHT), new StorableSetPiece(ItemId.MYSTIC_BOOTS_LIGHT)),
    DUSK_MYSTIC_ROBES(ItemId.MYSTIC_HAT_DUSK, new StorableSetPiece(ItemId.MYSTIC_HAT_DUSK), new StorableSetPiece(ItemId.MYSTIC_ROBE_TOP_DUSK), new StorableSetPiece(ItemId.MYSTIC_ROBE_BOTTOM_DUSK),
            new StorableSetPiece(ItemId.MYSTIC_GLOVES_DUSK), new StorableSetPiece(ItemId.MYSTIC_BOOTS_DUSK)),
    SKELETAL_ROBES(ItemId.SKELETAL_HELM_10604, new StorableSetPiece(ItemId.SKELETAL_HELM), new StorableSetPiece(ItemId.SKELETAL_TOP), new StorableSetPiece(ItemId.SKELETAL_BOTTOMS), new StorableSetPiece(ItemId.SKELETAL_BOOTS), new StorableSetPiece(ItemId.SKELETAL_GLOVES)),
    INFINITY_ROBES(ItemId.INFINITY_TOP_10605, new StorableSetPiece(ItemId.INFINITY_HAT), new StorableSetPiece(ItemId.INFINITY_TOP), new StorableSetPiece(ItemId.INFINITY_BOTTOMS), new StorableSetPiece(ItemId.INFINITY_GLOVES), new StorableSetPiece(ItemId.INFINITY_BOOTS)),
    SPLITBARK_ROBES(ItemId.SPLITBARK_HELM_10606, new StorableSetPiece(ItemId.SPLITBARK_HELM), new StorableSetPiece(ItemId.SPLITBARK_BODY), new StorableSetPiece(ItemId.SPLITBARK_LEGS), new StorableSetPiece(ItemId.SPLITBARK_GAUNTLETS), new StorableSetPiece(ItemId.SPLITBARK_BOOTS)),
    GHOSTLY_ROBES(ItemId.GHOSTLY_BOOTS_10607, new StorableSetPiece(ItemId.GHOSTLY_HOOD), new StorableSetPiece(ItemId.GHOSTLY_ROBE), new StorableSetPiece(ItemId.GHOSTLY_ROBE_6108), new StorableSetPiece(ItemId.GHOSTLY_GLOVES), new StorableSetPiece(ItemId.GHOSTLY_BOOTS), new StorableSetPiece(ItemId.GHOSTLY_CLOAK)),
    MOONCLAN_ROBES(ItemId.MOONCLAN_HAT_10608, new StorableSetPiece(ItemId.MOONCLAN_HELM), new StorableSetPiece(ItemId.MOONCLAN_HAT), new StorableSetPiece(ItemId.MOONCLAN_ARMOUR), new StorableSetPiece(ItemId.MOONCLAN_SKIRT), new StorableSetPiece(ItemId.MOONCLAN_GLOVES), new StorableSetPiece(ItemId.MOONCLAN_BOOTS), new StorableSetPiece(ItemId.MOONCLAN_CAPE)),
    LUNAR_ROBES(ItemId.LUNAR_HELM_10609, new StorableSetPiece(ItemId.LUNAR_HELM), new StorableSetPiece(ItemId.LUNAR_TORSO), new StorableSetPiece(ItemId.LUNAR_LEGS), new StorableSetPiece(ItemId.LUNAR_GLOVES), new StorableSetPiece(ItemId.LUNAR_BOOTS), new StorableSetPiece(ItemId.LUNAR_CAPE), new StorableSetPiece(ItemId.LUNAR_AMULET), new StorableSetPiece(ItemId.LUNAR_RING)),
    GRACEFUL_OUTFIT(ItemId.GRACEFUL_HOOD, new StorableSetPiece(ItemId.GRACEFUL_HOOD), new StorableSetPiece(ItemId.GRACEFUL_TOP), new StorableSetPiece(ItemId.GRACEFUL_LEGS), new StorableSetPiece(ItemId.GRACEFUL_GLOVES), new StorableSetPiece(ItemId.GRACEFUL_BOOTS), new StorableSetPiece(ItemId.GRACEFUL_CAPE)),
    GRACEFUL_OUTFIT_ARCEUUS(ItemId.GRACEFUL_HOOD_13579, new StorableSetPiece(ItemId.GRACEFUL_HOOD_13579), new StorableSetPiece(ItemId.GRACEFUL_TOP_13583), new StorableSetPiece(ItemId.GRACEFUL_LEGS_13585), new StorableSetPiece(ItemId.GRACEFUL_GLOVES_13587), new StorableSetPiece(ItemId.GRACEFUL_BOOTS_13589), new StorableSetPiece(ItemId.GRACEFUL_CAPE_13581)),
    GRACEFUL_OUTFIT_HOSIDIUS(ItemId.GRACEFUL_HOOD_13627, new StorableSetPiece(ItemId.GRACEFUL_HOOD_13627), new StorableSetPiece(ItemId.GRACEFUL_TOP_13631), new StorableSetPiece(ItemId.GRACEFUL_LEGS_13633), new StorableSetPiece(ItemId.GRACEFUL_GLOVES_13635), new StorableSetPiece(ItemId.GRACEFUL_BOOTS_13637), new StorableSetPiece(ItemId.GRACEFUL_CAPE_13629)),
    GRACEFUL_OUTFIT_LOVAKENGJI(ItemId.GRACEFUL_HOOD_13603, new StorableSetPiece(ItemId.GRACEFUL_HOOD_13603), new StorableSetPiece(ItemId.GRACEFUL_TOP_13607), new StorableSetPiece(ItemId.GRACEFUL_LEGS_13609), new StorableSetPiece(ItemId.GRACEFUL_GLOVES_13611), new StorableSetPiece(ItemId.GRACEFUL_BOOTS_13613), new StorableSetPiece(ItemId.GRACEFUL_CAPE_13605)),
    GRACEFUL_OUTFIT_PISCARILIUS(ItemId.GRACEFUL_HOOD_13591, new StorableSetPiece(ItemId.GRACEFUL_HOOD_13591), new StorableSetPiece(ItemId.GRACEFUL_TOP_13595), new StorableSetPiece(ItemId.GRACEFUL_LEGS_13597), new StorableSetPiece(ItemId.GRACEFUL_GLOVES_13599), new StorableSetPiece(ItemId.GRACEFUL_BOOTS_13601), new StorableSetPiece(ItemId.GRACEFUL_CAPE_13593)),
    GRACEFUL_OUTFIT_SHAYZIEN(ItemId.GRACEFUL_HOOD_13615, new StorableSetPiece(ItemId.GRACEFUL_HOOD_13615), new StorableSetPiece(ItemId.GRACEFUL_TOP_13619), new StorableSetPiece(ItemId.GRACEFUL_LEGS_13621), new StorableSetPiece(ItemId.GRACEFUL_GLOVES_13623), new StorableSetPiece(ItemId.GRACEFUL_BOOTS_13625), new StorableSetPiece(ItemId.GRACEFUL_CAPE_13617)),
    GRACEFUL_OUTFIT_KOUREND(ItemId.GRACEFUL_HOOD_13667, new StorableSetPiece(ItemId.GRACEFUL_HOOD_13667), new StorableSetPiece(ItemId.GRACEFUL_TOP_13671), new StorableSetPiece(ItemId.GRACEFUL_LEGS_13673), new StorableSetPiece(ItemId.GRACEFUL_GLOVES_13675), new StorableSetPiece(ItemId.GRACEFUL_BOOTS_13677), new StorableSetPiece(ItemId.GRACEFUL_CAPE_13669)),
    GRACEFUL_OUTFIT_AGILITY_ARENA(ItemId.GRACEFUL_HOOD_21061, new StorableSetPiece(ItemId.GRACEFUL_HOOD_21061), new StorableSetPiece(ItemId.GRACEFUL_TOP_21067), new StorableSetPiece(ItemId.GRACEFUL_LEGS_21070), new StorableSetPiece(ItemId.GRACEFUL_GLOVES_21073), new StorableSetPiece(ItemId.GRACEFUL_BOOTS_21076), new StorableSetPiece(ItemId.GRACEFUL_CAPE_21064)),
    BLACK_NAVAL_ATTIRE(ItemId.BLACK_TRICORN_HAT, new StorableSetPiece(ItemId.BLACK_TRICORN_HAT), new StorableSetPiece(ItemId.BLACK_NAVAL_SHIRT), new StorableSetPiece(ItemId.BLACK_NAVY_SLACKS)),
    BLUE_NAVAL_ATTIRE(ItemId.BLUE_TRICORN_HAT, new StorableSetPiece(ItemId.BLUE_TRICORN_HAT), new StorableSetPiece(ItemId.BLUE_NAVAL_SHIRT), new StorableSetPiece(ItemId.BLUE_NAVY_SLACKS)),
    BROWN_NAVAL_ATTIRE(ItemId.BROWN_TRICORN_HAT, new StorableSetPiece(ItemId.BROWN_TRICORN_HAT), new StorableSetPiece(ItemId.BROWN_NAVAL_SHIRT), new StorableSetPiece(ItemId.BROWN_NAVY_SLACKS)),
    GREEN_NAVAL_ATTIRE(ItemId.GREEN_TRICORN_HAT, new StorableSetPiece(ItemId.GREEN_TRICORN_HAT), new StorableSetPiece(ItemId.GREEN_NAVAL_SHIRT), new StorableSetPiece(ItemId.GREEN_NAVY_SLACKS)),
    GREY_NAVAL_ATTIRE(ItemId.GREY_TRICORN_HAT, new StorableSetPiece(ItemId.GREY_TRICORN_HAT), new StorableSetPiece(ItemId.GREY_NAVAL_SHIRT), new StorableSetPiece(ItemId.GREY_NAVY_SLACKS)),
    PURPLE_NAVAL_ATTIRE(ItemId.PURPLE_TRICORN_HAT, new StorableSetPiece(ItemId.PURPLE_TRICORN_HAT), new StorableSetPiece(ItemId.PURPLE_NAVAL_SHIRT), new StorableSetPiece(ItemId.PURPLE_NAVY_SLACKS)),
    RED_NAVAL_ATTIRE(ItemId.RED_TRICORN_HAT, new StorableSetPiece(ItemId.RED_TRICORN_HAT), new StorableSetPiece(ItemId.RED_NAVAL_SHIRT), new StorableSetPiece(ItemId.RED_NAVY_SLACKS)),
    ELDER_CHAOS_DRUID_ROBES(ItemId.ELDER_CHAOS_HOOD, new StorableSetPiece(ItemId.ELDER_CHAOS_HOOD), new StorableSetPiece(ItemId.ELDER_CHAOS_TOP), new StorableSetPiece(ItemId.ELDER_CHAOS_ROBE)),
    EVIL_CHICKEN_COSTUME(ItemId.EVIL_CHICKEN_HEAD, new StorableSetPiece(ItemId.EVIL_CHICKEN_HEAD), new StorableSetPiece(ItemId.EVIL_CHICKEN_WINGS), new StorableSetPiece(ItemId.EVIL_CHICKEN_LEGS), new StorableSetPiece(ItemId.EVIL_CHICKEN_FEET)),
    PYROMANCER_OUTFIT(ItemId.PYROMANCER_HOOD, new StorableSetPiece(ItemId.PYROMANCER_HOOD), new StorableSetPiece(ItemId.PYROMANCER_GARB), new StorableSetPiece(ItemId.PYROMANCER_ROBE), new StorableSetPiece(ItemId.PYROMANCER_BOOTS)),
    ORNATE_OUTFIT(ItemId.ORNATE_HELM, new StorableSetPiece(ItemId.ORNATE_HELM), new StorableSetPiece(ItemId.ORNATE_TOP), new StorableSetPiece(ItemId.ORNATE_LEGS),
            new StorableSetPiece(ItemId.ORNATE_BOOTS), new StorableSetPiece(ItemId.ORNATE_GLOVES), new StorableSetPiece(ItemId.ORNATE_CAPE));

    private final int displayItem;
    private final StorableSetPiece[] pieces;

    StorableOutfits(final int displayItem, final StorableSetPiece... pieces) {
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
