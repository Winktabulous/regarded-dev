package com.zenyte.game.content.skills.herblore;

import com.zenyte.game.item.ItemId;
import org.checkerframework.checker.index.qual.NonNegative;

import java.util.EnumSet;

/**
 * @author Chris
 * @since August 19 2020
 */

public enum CleanableHerb {
    GUAM(ItemId.GRIMY_GUAM_LEAF, ItemId.GUAM_LEAF),
    MARRENTILL(ItemId.GRIMY_MARRENTILL, ItemId.MARRENTILL),
    TARROMIN(ItemId.GRIMY_TARROMIN, ItemId.TARROMIN),
    HARRALANDER(ItemId.GRIMY_HARRALANDER, ItemId.HARRALANDER),
    RANARR_WEED(ItemId.GRIMY_RANARR_WEED, ItemId.RANARR_WEED),
    TOADFLAX(ItemId.GRIMY_TOADFLAX, ItemId.TOADFLAX),
    IRIT_LEAF(ItemId.GRIMY_IRIT_LEAF, ItemId.IRIT_LEAF),
    AVANTOE(ItemId.GRIMY_AVANTOE, ItemId.AVANTOE),
    KWUARM(ItemId.GRIMY_KWUARM, ItemId.KWUARM),
    SNAPDRAGON(ItemId.GRIMY_SNAPDRAGON, ItemId.SNAPDRAGON),
    CADANTINE(ItemId.GRIMY_CADANTINE, ItemId.CADANTINE),
    LANTADYME(ItemId.GRIMY_LANTADYME, ItemId.LANTADYME),
    DWARF_WEED(ItemId.GRIMY_DWARF_WEED, ItemId.DWARF_WEED),
    TORSTOL(ItemId.GRIMY_TORSTOL, ItemId.TORSTOL);

    public static final EnumSet<CleanableHerb> cleanableHerbs = EnumSet.allOf(CleanableHerb.class);
    private final int grimyId;
    private final int cleanId;

    public static CleanableHerb of(@NonNegative final int id) {
        for (CleanableHerb cleanableHerb : cleanableHerbs) {
            if (cleanableHerb.getGrimyId() == id || cleanableHerb.getCleanId() == id) {
                return cleanableHerb;
            }
        }
        throw new IllegalArgumentException("Could not find cleanable herb with id: " + id);
    }
    
    CleanableHerb(int grimyId, int cleanId) {
        this.grimyId = grimyId;
        this.cleanId = cleanId;
    }
    
    public int getGrimyId() {
        return grimyId;
    }
    
    public int getCleanId() {
        return cleanId;
    }
}
