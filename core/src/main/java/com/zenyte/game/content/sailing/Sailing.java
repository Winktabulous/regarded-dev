package com.zenyte.game.content.sailing;

import com.google.common.collect.ImmutableMap;
import com.zenyte.game.content.achievementdiary.diaries.FaladorDiary;
import com.zenyte.game.content.achievementdiary.diaries.KaramjaDiary;
import com.zenyte.game.content.achievementdiary.diaries.KourendDiary;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.plugins.dialogue.PlainChat;

import java.util.Map;

/**
 * @author Tommeh | 9 jul. 2018 | 21:39:29
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class Sailing {
	private static final Location PISCARILIUS_LOCATION = new Location(1824, 3694, 1);
	private static final Location LANDS_END_LOCATION = new Location(1504, 3396, 1);
	private static final Location PORT_SARIM_LOCATION = new Location(3055, 3242, 1);
	private static final int INTERFACE = 299;
	private static final Map<Integer, Location> LOCATIONS = ImmutableMap.<Integer, Location>builder().put(1, new Location(2834, 3332, 1)).put(2, new Location(3055, 3242, 1)).put(3, new Location(2853, 3235, 0)).put(5, new Location(2956, 3143, 1)).put(6, new Location(3032, 3217, 1)).put(7, new Location(2775, 3234, 1)).put(8, new Location(2683, 3268, 1)).put(14, new Location(2662, 2676, 1)).put(15, new Location(3041, 3199, 1)).put(16, new Location(2915, 3225, 0)).build();
	private static final Map<Integer, Integer> DELAYS = ImmutableMap.<Integer, Integer>builder().put(1, 11).put(2, 11).put(3, 10).put(4, 10).put(5, 7).put(6, 7).put(7, 4).put(8, 4).put(14, 10).put(15, 10).put(16, 10).build();

	public static void sail(final Player player, final String departure, final String destination) {
		final int value = getValue(departure, destination);
		final Location location = LOCATIONS.get(value);
		final Integer delay = DELAYS.get(value);
		player.lock(delay);
		player.getInterfaceHandler().closeInterface(InterfacePosition.DIALOGUE);
		player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, INTERFACE);
		player.getVarManager().sendVar(75, value);
		WorldTasksManager.schedule(() -> {
			if (destination.equals("Entrana")) {
				player.getAchievementDiaries().update(FaladorDiary.TAKE_BOAT_TO_ENTRANA);
			} else if (departure.equals("Karamja") && destination.equals("Port Sarim")) {
				player.getAchievementDiaries().update(KaramjaDiary.TRAVEL_TO_PORT_SARIM);
			} else if (departure.equals("Brimhaven") && destination.equals("Ardougne")) {
				player.getAchievementDiaries().update(KaramjaDiary.TRAVEL_TO_ARDOUGNE);
			}
			player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
			player.setLocation(location);
			player.getDialogueManager().start(new PlainChat(player, "The ship arrives at " + destination + "."));
		}, delay);
	}

	public static void sailZeah(final Player player, final String destination) {
		final Location location = destination.equals("Port Sarim") ? PORT_SARIM_LOCATION : destination.equals("Piscarilius House") ? PISCARILIUS_LOCATION : LANDS_END_LOCATION;
		new FadeScreen(player, () -> {
			if (destination.equals("Land's End")) {
				player.getAchievementDiaries().update(KourendDiary.TAKE_LANDS_END_BOAT);
			}
			player.sendMessage("You board the ship and sail to " + destination + ".");
			player.setLocation(location);
		}).fade(4);
	}

	private static int getValue(final String departure, final String destination) {
		if (departure.equals("Port Sarim") && destination.equals("Entrana")) {
			return 1;
		} else if (departure.equals("Entrana") && destination.equals("Port Sarim")) {
			return 2;
		} else if (departure.equals("Port Sarim") && destination.equals("Crandor")) {
			return 3;
		} else if (departure.equals("Crandor") && destination.equals("Port Sarim")) {
			return 4;
		} else if (departure.equals("Port Sarim") && destination.equals("Karamja")) {
			return 5;
		} else if (departure.equals("Karamja") && destination.equals("Port Sarim")) {
			return 6;
		} else if (departure.equals("Ardougne") && destination.equals("Brimhaven")) {
			return 7;
		} else if (departure.equals("Brimhaven") && destination.equals("Ardougne")) {
			return 8;
		} else if (departure.equals("Port Sarim") && destination.equals("the Void Knight outpost")) {
			return 14;
		} else if (departure.equals("the Void Knight outpost") && destination.equals("Port Sarim")) {
			return 15;
		} else if (departure.equals("Ardougne") && destination.equals("Rimmington")) {
			return 16;
		} else if (departure.equals("Brimhaven") && destination.equals("Rimmington")) {
			return 16;
		} else if (departure.equals("Rimmington") && destination.equals("Brimhaven")) {
			return 7;
		} else if (departure.equals("Rimmington") && destination.equals("Ardougne")) {
			return 8;
		}
		return -1;
	}
}
