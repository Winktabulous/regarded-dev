package com.zenyte.game;

import com.zenyte.ContentConstants;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.near_reality.game.world.info.WorldProfile;
import com.zenyte.utils.TimeUnit;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.Set;

/**
 * @author Kris | 5. march 2018 : 17:05.26
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 *      profile</a>}
 */
public class GameConstants {

	/**
	 * The current cache revision.
	 */
	public static final double REVISION = 179.3;
	public static final int CLIENT_VERSION = 3;
	public static final int WORLD_CYCLE_TIME = 600;
	public static final int LOGIN_PORT = 43596;
	public static final boolean DEV_DEBUG = false;
	public static final Location REGISTRATION_LOCATION = new Location(3360, 7206, 1);
	public static final String SERVER_NAME = ContentConstants.SERVER_NAME;
	public static final String SERVER_CHANNEL_NAME = "help";
	public static final String SERVER_WEBSITE_URL = "https://near-reality.com";
	public static final String SERVER_ACCOUNT_URL = SERVER_WEBSITE_URL + "/account";
	public static final String SERVER_FORUMS_URL = SERVER_WEBSITE_URL + "/forums";
	public static final String SERVER_STORE_URL = SERVER_WEBSITE_URL + "/store";
	public static final String SERVER_VOTE_URL = SERVER_WEBSITE_URL + "/vote";
	public static final String SERVER_RULES_URL = SERVER_WEBSITE_URL + "/rules";
	public static final String DISCORD_INVITE = "https://discord.gg/nrpk";

	/**
	 * The instance world profile.
	 */
	public static WorldProfile WORLD_PROFILE;

	public static boolean RUNESPAWN;
	public static boolean CYCLE_DEBUG = false;

	public static boolean CHECK_HUNTER_TRAPS_QUANTITY = true;

	public static boolean ANTIKNOX = false;
	public static boolean WHITELISTING = true;
	public static boolean DUEL_ARENA = true;
	public static boolean GROTESQUE_GUARDIANS = true;
	public static boolean PURGING_CHUNKS = true;

	public static final Set<String> whitelistedUsernames = new ObjectOpenHashSet<>();

	public static final boolean BOUNTY_HUNTER = false;

	public static double defenceMultiplier = 0.825;

	public static int randomEvent = (int) TimeUnit.HOURS.toTicks(5);

	public static boolean CHAMBERS_OF_XERIC = true;
	public static boolean ALCHEMICAL_HYDRA = true;

	//!Case sensitive usernames
	public static final String[] owners = new String[] {
			"jacmob", "stan", "Kryeus", "kryeus"
	};

	static {
		whitelistedUsernames.addAll(Arrays.asList(owners));
	}

	public static boolean isOwner(final Player player) {
		return ArrayUtils.contains(owners, player.getUsername().toLowerCase());
	}

	/**
	 * Whether the game should connect the forums database and validate the
	 * login on a new registration or not.
	 */
	public static final boolean REGISTER_ON_FORUMS = false;

	public static final float TICK = WORLD_CYCLE_TIME;

	public static final float CLIENT_CYCLE = 20;

	public static final float CYCLES_PER_TICK = TICK / CLIENT_CYCLE;


	public static boolean BOOSTED_XP = false;

	public static boolean BOOSTED_SKILLING_PETS = false;
	public static final double BOOSTED_SKILLING_PET_RATE = 0.25; // this is a 15% boost

	public static boolean BOOSTED_BOSS_PETS = false;
	public static final double BOOSTED_BOSS_PET_RATE = 0.25; // this is a 15% boost

	public static int BOOSTED_XP_MODIFIER = 50;

	public static boolean FILTERING_DUPLICATE_JS5_REQUESTS = false;

	public static final String UPDATE_LOG_BROADCAST = "Quality of Life & Bug Fixes! Click here to join the Discord.";

	public static final String UPDATE_LOG_URL = "discord.gg/nr";

	/**
	 * Item ids added to this list are restricted from trade, selling to shops, and posting on the GE. It is primarily used as an economy control measure
	 */
	public static final IntArrayList RESTRICTED_TRADE_ITEMS = new IntArrayList();
}
