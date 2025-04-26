package com.zenyte.game.content.skills.runecrafting;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tommeh | 24 jul. 2018 | 23:09:57
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public enum Tiara {
	AIR(5527, 1438, Runecrafting.AIR_RUNE.getAltarObjectId(), 25),
	MIND(5529, 1448, Runecrafting.MIND_RUNE.getAltarObjectId(), 27.5),
	WATER(5531, 1444, Runecrafting.WATER_RUNE.getAltarObjectId(), 30),
	EARTH(5535, 1440, Runecrafting.EARTH_RUNE.getAltarObjectId(), 32.5),
	FIRE(5537, 1442, Runecrafting.FIRE_RUNE.getAltarObjectId(), 35),
	BODY(5533, 1446, Runecrafting.BODY_RUNE.getAltarObjectId(), 37.5),
	COSMIC(5539, 1454, Runecrafting.COSMIC_RUNE.getAltarObjectId(), 40),
	CHAOS(5543, 1452, Runecrafting.CHAOS_RUNE.getAltarObjectId(), 42.5),
	NATURE(5541, 1462, Runecrafting.NATURE_RUNE.getAltarObjectId(), 45),
	LAW(5545, 1458, Runecrafting.LAW_RUNE.getAltarObjectId(), 47.5),
	BLOOD(5549, 1450, Runecrafting.BLOOD_RUNE_REAL.getAltarObjectId(), 50),
	DEATH(5547, 1456, Runecrafting.DEATH_RUNE.getAltarObjectId(), 50),
	WRATH(22121, 22118, Runecrafting.WRATH_RUNE.getAltarObjectId(), 52.5);
	public static final Tiara[] VALUES = values();
	public static final Map<Integer, Tiara> TIARAS = new HashMap<>();

	static {
		for (final Tiara tiara : VALUES) {
			TIARAS.put(tiara.getTalisman(), tiara);
		}
	}

	private final int id;
	private final int talisman;
	private final int altar;
	private final double experience;

	Tiara(final int id, final int talisman, final int altar, final double experience) {
		this.id = id;
		this.talisman = talisman;
		this.altar = altar;
		this.experience = experience;
	}

	public static Tiara getTiara(final Player player, final Item item, final int objectId) {
		Tiara tiara;
		for (int slot = 0; slot < 28; slot++) {
			final Item i = player.getInventory().getItem(slot);
			if (i == null) {
				continue;
			}
			tiara = TIARAS.get(i.getId());
			if (tiara != null && tiara.getAltar() == objectId && item.getId() == 5525) {
				return tiara;
			}
		}
		return null;
	}

	public int getId() {
		return id;
	}

	public int getTalisman() {
		return talisman;
	}

	public int getAltar() {
		return altar;
	}

	public double getExperience() {
		return experience;
	}
}
