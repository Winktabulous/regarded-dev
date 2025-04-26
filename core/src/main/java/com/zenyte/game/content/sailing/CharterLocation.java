package com.zenyte.game.content.sailing;

import com.zenyte.game.world.entity.Location;
import com.zenyte.utils.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tommeh | 7 jul. 2018 | 21:04:47
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public enum CharterLocation {
	BRIMHAVEN(new Location(2763, 3238, 1), "Brimhaven", -1, 480, 680, 480, 3900, 400, 2900, 1600, 3200, 400, 3450),
	CATHERBY(new Location(2792, 3417, 1), "Catherby", 480, -1, 1000, 480, 3500, 1600, 3500, 1000, 3200, 1600, 3560),
	CORSAIR_COVE(new Location(2592, 2851, 1), "Corsair Cove", 680, 1000, -1, 800, 4080, 600, 4040, 1200, 3200, 800, 1420),
	MUSA_POINT(new Location(2957, 3158, 1), "Karamja", 200, 480, 800, -1, 1100, 400, 1100, -1, 3200, 200, 4400),
	MOS_LE_HARMLESS(new Location(3668, 2931, 1), "Mos LeHarmless", 3900, 2500, 2040, 4100, -1, 4100, 1280, 3200, 1600, 4950),
	PORT_KHAZARD(new Location(2674, 3141, 1), "Port Khazard", 1600, 1600, 600, 1600, 4100, -1, 4100, 1280, 3200, 1600, 2800),
	PORT_PHASMATYS(new Location(3705, 3503, 1), "Port Phasmatys", 2900, 3500, 4040, 1100, -1, 4100, -1, 1300, 3200, 3200, 5200),
	PORT_SARIM(new Location(3038, 3189, 1), "Port Sarim", 1600, 1000, 1200, -1, 1300, 1280, 1300, -1, 3200, 400, 4800),
	PORT_TYRAS(new Location(2142, 3125, 1), "Port Tyras", 3200, 3200, 3200, 3200, 3200, 3200, 3200, 3200, -1, 3200, 3200),
	SHIPYARD(new Location(2998, 3032, 1), "Ship Yard", 400, 1600, 800, 200, 1100, 720, 1100, 400, 3200, -1, 4000),
	PRIFDDINAS(new Location(2157, 3333, 1), "Prifddinas", 3450, 3560, 1420, 4400, 4950, 2800, 5200, 4800, 3200, 4000, -1);
	public static final Map<String, CharterLocation> LOCATIONS = new HashMap<>();
	private static final CharterLocation[] VALUES = values();

	static {
		for (final CharterLocation location : VALUES) {
			LOCATIONS.put(location.toString(), location);
		}
	}

	private final String shopPrefix;
	private final Location location;
	private final int[] costs;

	CharterLocation(final Location location, final String shopPrefix, final int... costs) {
		this.location = location;
		this.shopPrefix = shopPrefix;
		this.costs = costs;
	}

	public static CharterLocation get(final String string) {
		return LOCATIONS.get(string);
	}

	public static CharterLocation getLocation(final Location location) {
		for (final CharterLocation charterLocation : VALUES) {
			if (charterLocation.getLocation().withinDistance(location.getX(), location.getY(), 15)) {
				return charterLocation;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return this.equals(MOS_LE_HARMLESS) ? "Mos Le'Harmless" : TextUtils.capitalize(name().toLowerCase().replace("_", " "));
	}

	public Location getLocation() {
		return location;
	}

	public String getShopPrefix() {
		return shopPrefix;
	}

	public int[] getCosts() {
		return costs;
	}
}
