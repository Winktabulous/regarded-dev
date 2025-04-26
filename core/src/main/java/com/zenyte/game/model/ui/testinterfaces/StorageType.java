package com.zenyte.game.model.ui.testinterfaces;

/**
 * @author Kris | 03/07/2022
 */
public enum StorageType {
	MAGIC_WARDROBE("Magic Wardrobe", 3289),
	ARMOUR_CASE("Armour Case", 3290),
	CAPE_RACK("Cape Rack", 3292),
	FANCY_DRESS_BOX("Fancy Dress Box", 3291),
	TOY_BOX("Toy Box", 3299),
	TC_BEGINNER("Treasure Chest: Beginner rewards", 3293),
	TC_EASY("Treasure Chest: Easy rewards", 3294),
	TC_MEDIUM("Treasure Chest: Medium rewards", 3295),
	TC_HARD("Treasure Chest: Hard rewards", 3296),
	TC_ELITE("Treasure Chest: Elite rewards", 3297),
	TC_MASTER("Treasure Chest: Master rewards", 3298),
	BOSS_LAIR_JARS("Boss Lair Jars", 3300);

	public static final StorageType[] values = values();
	public static final String[] names = new String[values.length];
    private final String name;
	private final int enumId;

	StorageType(final String name, final int enumId) {
        this.name = name;
		this.enumId = enumId;
	}

    public int getEnumId() {
        return enumId;
    }

	static {
		for (int i = 0, length = values.length; i < length; i++) {
			names[i] = values[i].name;
		}
	}

}
