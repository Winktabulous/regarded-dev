package com.zenyte.game.world.entity.player.privilege;

import com.near_reality.api.model.ApiGameMode;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.utils.TextUtils;

import java.util.HashMap;
import java.util.Map;

public enum GameMode implements IPrivilege {

	REGULAR(Crown.NONE, Crown.REALIST_REGULAR, "regular", new int[] { 150, 80, 10, 5}),
	STANDARD_IRON_MAN(Crown.STANDARD_IRON_MAN, Crown.REALIST_STANDARD_IRON_MAN, "ironman", new int[] { 80, 20, 10, 5 }),
	ULTIMATE_IRON_MAN(Crown.ULTIMATE_IRON_MAN, Crown.REALIST_ULTIMATE_IRON_MAN, "ultimate_ironman", new int[] { 80, 20, 10, 5 }),
	HARDCORE_IRON_MAN(Crown.HARDCORE_IRON_MAN, Crown.REALIST_HARDCORE_IRON_MAN, "hardcore_ironman", new int[] { 80, 20, 10, 5 }),
	GROUP_IRON_MAN(Crown.GROUP_IRON_MAN,  Crown.GROUP_IRON_MAN, "group_ironman", new int[] { 20 }),
	GROUP_HARDCORE_IRON_MAN(Crown.HARDCORE_GROUP_IRON_MAN,  Crown.HARDCORE_GROUP_IRON_MAN, "", null);

	private final Crown crown;
	private final Crown crownRealist;
	private final String hiscoresFolder;
	private final int[] hiscoresRates;
	public static final GameMode[] values = values();
	private static final Map<Integer, GameMode> MODES = new HashMap<>();

	GameMode(Crown crown, Crown crownRealist, String hiscoresFolder, int[] hiscoresRates) {
		this.crown = crown;
		this.crownRealist = crownRealist;
		this.hiscoresFolder = hiscoresFolder;
		this.hiscoresRates = hiscoresRates;
	}

	static {
		for (final GameMode mode : values) {
			MODES.put(mode.ordinal(), mode);
		}
	}

	public static String getTitle(final Player player) {
		final GameMode mode = player.getGameMode();
		final boolean male = player.getAppearance().isMale();
		return mode.equals(STANDARD_IRON_MAN) ? "<col=60636B>Iron" + (male ? "man" : "woman") + "</col>" : mode.equals(ULTIMATE_IRON_MAN) ? "<col=D8D8D8>Ultimate Iron" + (male ? "man" : "woman") + "</col>" : mode.equals(HARDCORE_IRON_MAN) ? "<col=A30920>Hardcore Iron" + (male ? "man" : "woman") + "</col>" : mode.equals(REGULAR) ? "Regular player" : "";
	}

	public static GameMode get(final int index) {
		return MODES.get(index);
	}

	@Override
	public String toString() {
		return TextUtils.capitalize(name().toLowerCase().replaceAll("_", " "));
	}

    public boolean isIronman() {
        return isNonGroupIronman() || isGroupIronman();
    }

	public boolean isGroupIronman() {
		return this == GROUP_IRON_MAN || this == GROUP_HARDCORE_IRON_MAN;
	}

	public boolean isNonGroupIronman() {
		return this == STANDARD_IRON_MAN || this == ULTIMATE_IRON_MAN || this == HARDCORE_IRON_MAN;
	}

	public String getHiscoresFolder() {
		return hiscoresFolder;
	}

	public int[] getHiscoresRates() {
		return hiscoresRates;
	}

	@Override
	public int priority() {
		return 0;
	}

	@Override
	public Crown crown() {
		return crown;
	}

	public Crown getCrownRealist() {
		return crownRealist;
	}

	public ApiGameMode toApi() {
		return ApiGameMode.valueOf(name());
	}

    public boolean isHardcore() {
		return this == HARDCORE_IRON_MAN || this == GROUP_HARDCORE_IRON_MAN;
    }
}
