package com.zenyte.game.world.entity.player;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.InterfacePosition;

import static com.zenyte.game.GameInterface.ORBS;

/**
 * An enum containing the different saved settings for player. If the setting
 * constant has a runnable attached to it, it won't be refreshed within the loop
 * in {@link Settings#refresh()}.
 * 
 * @author Kris | 16. veebr 2018 : 21:22.33
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 *      profile</a>}
 */
public enum Setting {
	WILDERNESS_KD(4143, false, (player, state) -> player.getVarManager().sendBit(4143, state ? 1 : 0)),
	MOBILE_DEPOSIT_BOX_AMOUNT(4430, false),
	MINIMIZE_MINIMAP(6254, false, (player, state) -> {
		player.getVarManager().sendVarInstant(1021, player.getVarManager().getValue(1021));
		ORBS.open(player);
	}),
	GODWARS_ENTRANCE_ROPE(3966, false),
	SARADOMIN_TOP_ROPE(3967, false),
	SARADOMIN_BOTTOM_ROPE(3968, false),
	SARADOMIN_LIGHT(4733, false),
	EXPERIENCE_TRACKER(4702, false, (player, state) -> {
		if (state) {
			GameInterface.EXPERIENCE_DROPS_WINDOW.open(player);
		} else {
			player.getInterfaceHandler().closeInterface(InterfacePosition.XP_TRACKER);
		}
	}),
	WILDERNESS_DITCH,
	WORLD_MAP_GUIDE(10294, false),
	EDGEVILLE_RESPAWN_POINT,
	PUBLIC_FILTER,
	PRIVATE_FILTER,
	TRADE_FILTER,
	GAME_FILTER(26, false),
	CHANNEL_FILTER(928, false),
	CLAN_FILTER(929, false),
	HARD_LEATHER_TAN,
	ROW_CURRENCY_COLLECTOR,
	BONECRUSHING_INACTIVE,
	AUTO_MUSIC(18, true),
	LOOP_MUSIC(4137, false),
	VARROCK_TELEPORT_CONFIGURATION(4585, false),
	CAMELOT_TELEPORT_CONFIGURATION(4560, false),
	WATCHTOWER_TELEPORT_CONFIGURATION(4548, false),
	KALPHITE_LAIR_TUNNEL_ENTRANCE(4586, false),
	KALPHITE_QUEEN_TUNNEL_ENTRANCE(11705, false),
	BIGGER_AND_BADDER_SLAYER_REWARD(5362, false),
	STOP_THE_WYVERN_SLAYER_REWARD(6251, false),
	SHOW_COMBAT_SPELLS(6605, false),
	SHOW_TELEPORT_SPELLS(6609, false),
	SHOW_UTILITY_SPELLS(6606, false),
	SHOW_SPELLS_YOU_LACK_THE_MAGIC_LEVEL_TO_CAST(6607, false),
	SHOW_SPELLS_YOU_LACK_THE_RUNES_TO_CAST(6608, false),
	SPELL_FILTERING_DISABLED(6718, false),
	UNLOCKED_RUNEFEST_HOME_TELEPORT_ANIMATION(1771, false),
	USING_RUNEFEST_TELEPORT_ANIMATION,
	RIGOUR(5451, false),
	AUGURY(5452, false),
	PRESERVE(5453, false),
	FRIEND_LIST_TOGGLE(6516, false, (player, state) -> player.getInterfaceHandler().sendInterface(InterfacePosition.FRIENDS_TAB, state ? 432 : 429)),
	MOBILE_FUNCTION_BUTTON_ENABLED(6256, false),
	MOBILE_SHOP_QUANTITY(6348, false),
	SHOW_SPELLS_YOU_LACK_THE_REQUIREMENTS_TO_CAST(12137, false),
	ENABLE_ICON_RESIZING(6548, false),
	;
	private final int id;
	private final boolean varp;
	private final SettingRunnable runnable;
	public static final Setting[] SETTINGS = values();

	Setting() {
		this(-1, false, null);
	}

	Setting(final int varbitId, final boolean varp) {
		this(varbitId, varp, null);
	}

	Setting(final int varbitId, final boolean varp, final SettingRunnable runnable) {
		id = varbitId;
		this.varp = varp;
		this.runnable = runnable;
	}

	@Override
	public String toString() {
		return name();
	}


	interface SettingRunnable {
		void run(final Player player, final boolean state);
	}

	public int getId() {
		return id;
	}

	public boolean isVarp() {
		return varp;
	}

	public SettingRunnable getRunnable() {
		return runnable;
	}
}
