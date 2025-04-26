package com.zenyte.game.content.skills.thieving;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ImmutableItem;

/**
 * @author Tommeh | 19 jul. 2018 | 22:02:46
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public enum StallType {
	
	VEGETABLE_STALL(2, 10, 3, true, new ImmutableItem(1957), new ImmutableItem(1965), new ImmutableItem(1942), new ImmutableItem(1982), new ImmutableItem(1550)),
	BAKERS_STALL(5, 16, 3, false, new ImmutableItem(1891, 1, 1, 63), new ImmutableItem(1901, 1, 1, 9), new ImmutableItem(2309, 1, 1, 28)),
	CRAFTING_STALL(5, 16, 12, true, new ImmutableItem(1755), new ImmutableItem(1952), new ImmutableItem(1957)),
	FOOD_STALL(5, 16, 12, true, new ImmutableItem(1963)),
	GENERAL_STALL(5, 16, 12, true, new ImmutableItem(1931), new ImmutableItem(2347), new ImmutableItem(590)),
	TEA_STALL(5, 16, 12, true, new ImmutableItem(ItemId.CUP_OF_TEA_1978)),
	SILK_STALL(20, 24, 8, true, new ImmutableItem(950)),
	WINE_STALL(22, 27, 27, true, new ImmutableItem(1937), new ImmutableItem(1993), new ImmutableItem(1987), new ImmutableItem(3732), new ImmutableItem(7919)),
	FRUIT_STALL(25, 28, 3, false, new ImmutableItem(1955, 1, 1, 39), new ImmutableItem(5504, 1, 1, 3), new ImmutableItem(1963, 1, 1, 17), new ImmutableItem(1951, 1, 1, 7), new ImmutableItem(247, 1, 1, 10), new ImmutableItem(464, 1, 1, 3), new ImmutableItem(2120, 1, 1, 4), new ImmutableItem(2102, 1, 1, 8), new ImmutableItem(2114, 1, 1, 6), new ImmutableItem(5972, 1, 1, 1), new ImmutableItem(19653, 1, 1, 2)),
	SEED_STALL(27, 10, 30, true, new ImmutableItem(5318, 1, 3), new ImmutableItem(5319, 1, 3), new ImmutableItem(5322, 1, 2), new ImmutableItem(5320), new ImmutableItem(5323), new ImmutableItem(5321), new ImmutableItem(5305), new ImmutableItem(5307), new ImmutableItem(5308), new ImmutableItem(5306), new ImmutableItem(5309), new ImmutableItem(5310), new ImmutableItem(5311), new ImmutableItem(5096), new ImmutableItem(5097), new ImmutableItem(5098)),
	FUR_STALL(35, 36, 17, true, new ImmutableItem(958)),
	FISH_STALL(42, 42, 17, false, new ImmutableItem(331, 1, 1, 52), new ImmutableItem(359, 1, 1, 30), new ImmutableItem(377, 1, 1, 18)),
	CROSSBOW_STALL(49, 52, 16, true, new ImmutableItem(877, 1, 3), new ImmutableItem(9420), new ImmutableItem(9440)),
	SILVER_STALL(50, 54, 50, true, new ImmutableItem(442)),
	SPICE_STALL(65, 81, 133, true, new ImmutableItem(2007)),
	MAGIC_STALL(65, 100, 133, true, new ImmutableItem(556, 1, 15, 22), new ImmutableItem(557, 1, 15, 22), new ImmutableItem(554, 1, 15, 22), new ImmutableItem(555, 1, 15, 22), new ImmutableItem(563, 1, 1, 12)),
	SCIMITAR_STALL(65, 100, 133, true, new ImmutableItem(1323)),
	GEM_STALL(75, 160, 300, false, new ImmutableItem(1623, 1, 1, 44), new ImmutableItem(1621, 1, 1, 30), new ImmutableItem(1619, 1, 1, 20), new ImmutableItem(1617, 1, 1, 6)),

	COIN_STALL_BEGINNER(1, 8, 17, true),
	COIN_STALL_EASY(25, 26, 17, true),
	COIN_STALL_MEDIUM(45, 65, 17, true),
	COIN_STALL_HARD(65, 137, 17, true),
	COIN_STALL_MASTER(90, 353, 17, true),

	COIN_STALL_LDI(50, 220, 17, false, new ImmutableItem(995, 500, 2500), new ImmutableItem(1623, 1, 1, 44), new ImmutableItem(1621, 1, 1, 30), new ImmutableItem(1619, 1, 1, 20), new ImmutableItem(1617, 1, 1, 6)),

    TZHAAR_GEM_COUNTER(75, 160, 300, false, new ImmutableItem(1623, 1, 1, 23),
            new ImmutableItem(1621, 1, 1, 12), new ImmutableItem(1619, 1, 1, 10),
            new ImmutableItem(1617, 1, 1, 6), new ImmutableItem(1607, 1, 1, 23),
            new ImmutableItem(1605, 1, 1, 12), new ImmutableItem(1603, 1, 1, 10),
            new ImmutableItem(1601, 1, 1, 6), new ImmutableItem(1631, 1, 1, 3),
            new ImmutableItem(6573, 1, 1, 0.02)),

	TZHAAR_ORE_STALL(82, 180, 100, false, new ImmutableItem(440, 1, 1, 30),
            new ImmutableItem(453, 1, 1, 25), new ImmutableItem(442, 1, 1, 15),
            new ImmutableItem(444, 1, 1, 12), new ImmutableItem(447, 1, 1, 10),
            new ImmutableItem(449, 1, 1, 6), new ImmutableItem(451, 1, 1, 2));
	
	private final ImmutableItem[] items;
	private final int level, time;
	private final double experience;
    private final boolean randomize;

    StallType(final int level, final int experience, final int time, final boolean randomize, final ImmutableItem... items) {
        this.level = level;
        this.experience = experience;
        this.time = time;
        this.randomize = randomize;
        this.items = items;
    }

    public ImmutableItem[] getItems() {
        return items;
    }

    public int getLevel() {
        return level;
    }

    public int getTime() {
        return time;
    }

    public double getExperience() {
        return experience;
    }

    public boolean isRandomize() {
        return randomize;
    }


}
