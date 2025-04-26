package com.zenyte.game.world.entity.player;

import com.google.common.eventbus.Subscribe;
import com.near_reality.game.world.WorldEvent;
import com.near_reality.game.world.WorldHooks;
import com.zenyte.cores.CoresManager;
import com.zenyte.game.GameConstants;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.World;
import com.zenyte.plugins.events.ServerLaunchEvent;
import com.zenyte.utils.SQLManager;
import com.zenyte.utils.StaticInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;

@StaticInitializer
public class Analytics {

	private static final Logger log = LoggerFactory.getLogger(Analytics.class);
	private static final String LOGIN_QUERY = "INSERT INTO " + SQLManager.GAME_DATABASE_NAME + ".login (name, time, ip) VALUES(?, ?, ?)";
	private static final String LOGOUT_QUERY = "INSERT INTO " + SQLManager.GAME_DATABASE_NAME + ".logout (name, time, session_length, ornate_pool, " +
			"combat_dummy, slayer_master, elven_crystal_chest, brimstone_chest, loot_chest, shop_npcs, " +
			"afk_skilling, home_stalls, deaths_domain, teleport_interface, hiscores, game_settings, drop_viewer," +
			" daily_challenges, premium_toggles, boosters, fresh_account, total_currency, total_wealth, total_xp) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String PLAYER_QUERY = "INSERT INTO " + SQLManager.GAME_DATABASE_NAME + ".players (name, max_session_length, ip) VALUES(?, ?, ?) ON DUPLICATE KEY UPDATE max_session_length = GREATEST(max_session_length, VALUES(max_session_length)), ip = VALUES(ip)";
	private static final String WORLD_QUERY = "INSERT INTO " + SQLManager.GAME_DATABASE_NAME + ".world (time, players, new_players) VALUES(?, ?, ?)";
	public static boolean enabled = !GameConstants.WORLD_PROFILE.isDevelopment();

	public static void logLogin(Player player) {
		if (!enabled) return;
		Timestamp time = Timestamp.from(Instant.now());
		player.addTemporaryAttribute("analytics_login_timestamp", time);
		CoresManager.getServiceProvider().submit(() -> SQLManager.use(con -> {
			PreparedStatement statement = con.prepareStatement(LOGIN_QUERY);
			statement.setString(1, player.getName());
			statement.setTimestamp(2, time);
			statement.setString(3, player.getIP());
			statement.executeUpdate();
		}));
	}

	public static void logLogout(Player player) {
		if (!enabled) return;
		int freshAccount = player.getNumericTemporaryAttribute("fresh_account").intValue();
		int flags = player.getNumericTemporaryAttribute("analytics_interaction_flag").intValue();
		Timestamp time = Timestamp.from(Instant.now());
		Timestamp loginTimestamp = (Timestamp) player.getTemporaryAttributes().get("analytics_login_timestamp");
		final Timestamp sessionLength = loginTimestamp != null ? Timestamp.from(Instant.ofEpochMilli(time.getTime() - loginTimestamp.getTime())) : null;
		long totalInGameCurrency = player.getAmountOfLong(ItemId.COINS_995) + player.getAmountOfLong(ItemId.PLATINUM_TOKEN) * 1000L;
		long totalWealth = player.getTotalWealth();
		long totalXp = player.getSkills().getTotalXpLong();
		CoresManager.getServiceProvider().submit(() -> SQLManager.use(con -> {
			PreparedStatement statement = con.prepareStatement(LOGOUT_QUERY);
			statement.setString(1, player.getName());
			statement.setTimestamp(2, time);
			statement.setTimestamp(3, sessionLength);
			statement.setBoolean(4, checkInteraction(flags, InteractionType.ORNATE_POOL));
			statement.setBoolean(5, checkInteraction(flags, InteractionType.COMBAT_DUMMY));
			statement.setBoolean(6, checkInteraction(flags, InteractionType.SLAYER_MASTER));
			statement.setBoolean(7, checkInteraction(flags, InteractionType.ELVEN_CRYSTAL_CHEST));
			statement.setBoolean(8, checkInteraction(flags, InteractionType.BRIMSTONE_CHEST));
			statement.setBoolean(9, checkInteraction(flags, InteractionType.LOOT_CHEST));
			statement.setBoolean(10, checkInteraction(flags, InteractionType.SHOP_NPCS));
			statement.setBoolean(11, checkInteraction(flags, InteractionType.AFK_SKILLING));
			statement.setBoolean(12, checkInteraction(flags, InteractionType.HOME_STALLS));
			statement.setBoolean(13, checkInteraction(flags, InteractionType.DEATHS_DOMAIN));
			statement.setBoolean(14, checkInteraction(flags, InteractionType.TELEPORT_INTERFACE));
			statement.setBoolean(15, checkInteraction(flags, InteractionType.HISCORES));
			statement.setBoolean(16, checkInteraction(flags, InteractionType.GAME_SETTINGS));
			statement.setBoolean(17, checkInteraction(flags, InteractionType.DROP_VIEWER));
			statement.setBoolean(18, checkInteraction(flags, InteractionType.DAILY_CHALLENGES));
			statement.setBoolean(19, checkInteraction(flags, InteractionType.PREMIUM_TOGGLES));
			statement.setBoolean(20, checkInteraction(flags, InteractionType.CHECK_BOOSTERS));
			statement.setInt(21, freshAccount);
			statement.setLong(22, totalInGameCurrency);
			statement.setLong(23, totalWealth);
			statement.setLong(24, totalXp);
			statement.executeUpdate();

			PreparedStatement player_statement = con.prepareStatement(PLAYER_QUERY);
			player_statement.setString(1, player.getName());
			player_statement.setTimestamp(2, sessionLength);
			player_statement.setString(3, player.getIP());
			player_statement.executeUpdate();
		}));
	}

	private static boolean checkInteraction(int flags, InteractionType type) {
		return (flags & 1 << type.getIndex()) != 0;
	}

	public static void flagInteraction(Player player, InteractionType type) {
		int flags = player.getNumericTemporaryAttribute("analytics_interaction_flag").intValue();
		flags |= 1 << type.getIndex();
		player.addTemporaryAttribute("analytics_interaction_flag", flags);
	}

	private static int minute;

	@Subscribe
	public static void onServerLaunch(final ServerLaunchEvent launchEvent) {
		final WorldHooks hooks = launchEvent.getWorldThread().getHooks();
		hooks.register(WorldEvent.Tick.class, tick -> {
			if (!enabled) return;
			try {
				final int currentMinute = minute;
				minute = Calendar.getInstance().get(Calendar.MINUTE);
				if (currentMinute != minute) {
					Timestamp time = Timestamp.from(Instant.now());
					int players = World.getPlayers().size();
					int newPlayers = countNewPlayers();
					CoresManager.getServiceProvider().submit(() -> SQLManager.use(con -> {
						PreparedStatement statement = con.prepareStatement(WORLD_QUERY);
						statement.setTimestamp(1, time);
						statement.setInt(2, players);
						statement.setInt(3, newPlayers);
						statement.executeUpdate();
					}));
				}
			} catch (Throwable e) {
				log.error("Error while checking for server analytics.", e);
			}
		});
	}

	private static int countNewPlayers() {
		int newPlayers = 0;
		for (final Player player : World.getPlayers()) {
			if (player == null || player.isNulled() || !player.getTemporaryAttributes().containsKey("fresh_account")) {
				continue;
			}

			newPlayers++;
		}

		return newPlayers;
	}

	public enum InteractionType {
		ORNATE_POOL(0),
		COMBAT_DUMMY(1),
		SLAYER_MASTER(2),
		ELVEN_CRYSTAL_CHEST(3),
		BRIMSTONE_CHEST(4),
		LOOT_CHEST(5),
		SHOP_NPCS(6),
		AFK_SKILLING(7),
		HOME_STALLS(8),
		DEATHS_DOMAIN(9),
		TELEPORT_INTERFACE(10),
		HISCORES(11),
		GAME_SETTINGS(12),
		DROP_VIEWER(13),
		DAILY_CHALLENGES(14),
		PREMIUM_TOGGLES(15),
		CHECK_BOOSTERS(16),
		;

		private final int index;

		InteractionType(int index) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}

	}

}
