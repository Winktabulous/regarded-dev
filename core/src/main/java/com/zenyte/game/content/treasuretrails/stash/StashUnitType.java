package com.zenyte.game.content.treasuretrails.stash;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;

/**
 * @author Kris | 26/01/2019 19:34
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum StashUnitType {

    BEGINNER(12, 150, new Item(ItemId.PLANK, 2)),
    EASY(27, 150, new Item(ItemId.PLANK, 2)),
    MEDIUM(42, 250, new Item(ItemId.OAK_PLANK, 2)),
    HARD(55, 400, new Item(ItemId.TEAK_PLANK, 2)),
    ELITE(77, 600, new Item(ItemId.MAHOGANY_PLANK, 2)),
    MASTER(88, 1500, new Item(ItemId.MAHOGANY_PLANK, 2), new Item(ItemId.GOLD_LEAF_8784));

    private final int level;
    private final float experience;
    private final Item[] materials;

    StashUnitType(final int level, final float experience, final Item... materials) {
        this.level = level;
        this.experience = experience;
        this.materials = materials;
    }

    public int getLevel() {
        return level;
    }

    public float getExperience() {
        return experience;
    }

    public Item[] getMaterials() {
        return materials;
    }

}
