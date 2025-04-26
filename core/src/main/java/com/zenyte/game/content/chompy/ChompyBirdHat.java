package com.zenyte.game.content.chompy;

import com.zenyte.game.item.ItemId;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Christopher
 * @since 3/6/2020
 */

public enum ChompyBirdHat {
    OGRE_BOWMAN("Ogre bowman", ItemId.CHOMPY_BIRD_HAT, 30),
    BOWMAN("Bowman", ItemId.CHOMPY_BIRD_HAT_2979, 40),
    OGRE_YEOMAN("Ogre yeoman", ItemId.CHOMPY_BIRD_HAT_2980, 50),
    YEOMAN("Yeoman", ItemId.CHOMPY_BIRD_HAT_2981, 70),
    OGRE_MARKSMAN("Ogre marksman", ItemId.CHOMPY_BIRD_HAT_2982, 95),
    MARKSMAN("Marksman", ItemId.CHOMPY_BIRD_HAT_2983, 125),
    OGRE_WOODSMAN("Ogre woodsman", ItemId.CHOMPY_BIRD_HAT_2984, 170),
    WOODSMAN("Woodsman", ItemId.CHOMPY_BIRD_HAT_2985, 225),
    OGRE_FORESTER("Ogre forester", ItemId.CHOMPY_BIRD_HAT_2986, 300),
    FORESTER("Forester", ItemId.CHOMPY_BIRD_HAT_2987, 400),
    OGRE_BOWMASTER("Ogre bowmaster", ItemId.CHOMPY_BIRD_HAT_2988, 550),
    BOWMASTER("Bowmaster", ItemId.CHOMPY_BIRD_HAT_2989, 700),
    OGRE_EXPERT("Ogre expert", ItemId.CHOMPY_BIRD_HAT_2990, 1000),
    EXPERT("Expert", ItemId.CHOMPY_BIRD_HAT_2991, 1300),
    OGRE_DRAGON_ARCHER("Ogre dragon archer", ItemId.CHOMPY_BIRD_HAT_2992, 1700),
    DRAGON_ARCHER("Dragon archer", ItemId.CHOMPY_BIRD_HAT_2993, 2250),
    EXPERT_OGRE_DRAGON_ARCHER("Expert ogre dragon archer", ItemId.CHOMPY_BIRD_HAT_2994, 3000),
    EXPERT_DRAGON_ARCHER("Expert dragon archer", ItemId.CHOMPY_BIRD_HAT_2995, 4000);

    public static final ChompyBirdHat[] values = values();
    public static final ChompyBirdHat[] valuesReversed = values();

    static {
        ArrayUtils.reverse(valuesReversed);
    }

    private final String title;
    private final int itemId;
    private final int kills;

    public static ChompyBirdHat getBest(final int kills) {
        for (ChompyBirdHat hat : valuesReversed) {
            if (hat.getKills() <= kills) {
                return hat;
            }
        }
        return null;
    }

    ChompyBirdHat(String title, int itemId, int kills) {
        this.title = title;
        this.itemId = itemId;
        this.kills = kills;
    }

    public String getTitle() {
        return title;
    }

    public int getItemId() {
        return itemId;
    }

    public int getKills() {
        return kills;
    }
}