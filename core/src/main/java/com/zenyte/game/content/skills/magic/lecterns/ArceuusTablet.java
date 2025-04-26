package com.zenyte.game.content.skills.magic.lecterns;

import com.zenyte.game.item.Item;

import static com.zenyte.game.content.skills.magic.lecterns.TabletCreation.DARK_ESSENCE_BLOCK;
import static com.zenyte.game.content.skills.magic.spells.MagicSpell.*;

/**
 * @author Kris | 03/09/2019 08:10
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum ArceuusTablet implements LecternTablet {

    ARCEUUS_LIBRARY(1, 6, 10F, new Item(19613), new Item(EARTH_RUNE, 2), new Item(LAW_RUNE, 1), new Item(DARK_ESSENCE_BLOCK)),
    DRAYNOR_MANOR(2, 17, 16F, new Item(19615), new Item(WATER_RUNE, 1), new Item(EARTH_RUNE, 1), new Item(LAW_RUNE, 1), new Item(DARK_ESSENCE_BLOCK)),
    BATTLEFRONT(3, 23, 19F, new Item(22949), new Item(EARTH_RUNE, 1), new Item(FIRE_RUNE, 1), new Item(LAW_RUNE, 1), new Item(DARK_ESSENCE_BLOCK)),
    MIND_ALTAR(4, 28, 22F, new Item(19617), new Item(LAW_RUNE, 1), new Item(MIND_RUNE, 2), new Item(DARK_ESSENCE_BLOCK)),
    SALVE_GRAVEYARD(5, 40, 30F, new Item(19619), new Item(LAW_RUNE, 1), new Item(SOUL_RUNE, 2), new Item(DARK_ESSENCE_BLOCK)),
    FENKENSTRAINS_CASTLE(6, 48, 50F, new Item(19621), new Item(EARTH_RUNE, 1), new Item(LAW_RUNE, 1), new Item(SOUL_RUNE, 1), new Item(DARK_ESSENCE_BLOCK)),
    WEST_ARDOUGNE(7, 61, 68F, new Item(19623), new Item(LAW_RUNE, 2), new Item(SOUL_RUNE, 2), new Item(DARK_ESSENCE_BLOCK)),
    HARMONY_ISLAND(8, 65, 74F, new Item(19625), new Item(LAW_RUNE, 1), new Item(SOUL_RUNE, 1), new Item(NATURE_RUNE, 1), new Item(DARK_ESSENCE_BLOCK)),
    WILDERNESS_CEMETERY(9, 71, 82F, new Item(19627), new Item(LAW_RUNE, 1), new Item(SOUL_RUNE, 1), new Item(BLOOD_RUNE, 1), new Item(DARK_ESSENCE_BLOCK)),
    BARROWS(10, 83, 90F, new Item(19629), new Item(LAW_RUNE, 2), new Item(SOUL_RUNE, 2), new Item(BLOOD_RUNE, 1), new Item(DARK_ESSENCE_BLOCK)),
    APE_ATOLL(11, 90, 100F, new Item(19631), new Item(LAW_RUNE, 2), new Item(SOUL_RUNE, 2), new Item(BLOOD_RUNE, 2), new Item(DARK_ESSENCE_BLOCK));
    private final int level;
    private final float experience;
    private final Item[] runes;
    private final Item tab;
    private final int type;

    ArceuusTablet(int type, final int level, final float experience, final Item tab, final Item... runes) {
        this.type = type;
        this.level = level;
        this.experience = experience;
        this.tab = tab;
        this.runes = runes;
    }

    public int getLevel() {
        return level;
    }
    public int type() {
        return type;
    }

    public float getExperience() {
        return experience;
    }

    public Item[] getRunes() {
        return runes;
    }

    public Item getTab() {
        return tab;
    }

    public static final ArceuusTablet get(int type) {
        for (ArceuusTablet value : values()) {
            if (value.type == type) {
                return value;
            }
        }
        return null;
    }
}
