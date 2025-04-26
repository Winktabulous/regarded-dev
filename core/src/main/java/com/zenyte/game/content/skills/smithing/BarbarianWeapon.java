package com.zenyte.game.content.skills.smithing;

import com.zenyte.game.item.Item;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tommeh | 31 jul. 2018 | 19:18:10
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public enum BarbarianWeapon {
	BRONZE(new Item(11367), new Item(1237), 5, 25, Smithing.BARS[0], new Item(1511)), IRON(new Item(11369), new Item(1239), 20, 50, Smithing.BARS[1], new Item(1521)), STEEL(new Item(11371), new Item(1241), 35, 75, Smithing.BARS[2], new Item(1519)), MITHRI(new Item(11373), new Item(1243), 55, 100, Smithing.BARS[3], new Item(1517)), ADAMANT(new Item(11375), new Item(1245), 75, 125, Smithing.BARS[4], new Item(1515)), RUNE(new Item(11377), new Item(1247), 90, 150, Smithing.BARS[5], new Item(1513));
	private final Item hasta;
	private final Item spear;
	private final int level;
	private final double experience;
	private final Item bar;
	private final Item logs;
	public static final BarbarianWeapon[] VALUES = values();
	public static final Map<Integer, BarbarianWeapon> WEAPONS = new HashMap<>();

	static {
		for (final BarbarianWeapon weapon : VALUES) {
			WEAPONS.put(weapon.getBar().getId(), weapon);
		}
	}

	public static BarbarianWeapon get(final int id) {
		return WEAPONS.get(id);
	}

	BarbarianWeapon(Item hasta, Item spear, int level, double experience, Item bar, Item logs) {
		this.hasta = hasta;
		this.spear = spear;
		this.level = level;
		this.experience = experience;
		this.bar = bar;
		this.logs = logs;
	}

	public Item getHasta() {
		return hasta;
	}

	public Item getSpear() {
		return spear;
	}

	public int getLevel() {
		return level;
	}

	public double getExperience() {
		return experience;
	}

	public Item getBar() {
		return bar;
	}

	public Item getLogs() {
		return logs;
	}
}
