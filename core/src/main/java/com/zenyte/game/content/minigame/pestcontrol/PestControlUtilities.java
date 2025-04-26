package com.zenyte.game.content.minigame.pestcontrol;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.minigame.pestcontrol.area.AbstractLanderArea;
import com.zenyte.game.packet.PacketDispatcher;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.RegionArea;

import java.util.OptionalInt;

/**
 * @author Kris | 26. juuni 2018 : 18:44:31
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class PestControlUtilities {
	public static final Location VOID_KNIGHT_LOCATION = new Location(2656, 2592, 0);
	static final Location LANDER_SQUIRE_LOCATION = new Location(2655, 2607, 0);
	private static final int GAME_OVERLAY = 408;
	public static int TIME_UNTIL_GAME_START = 50;
	public static int MINIMUM_PLAYERS_LIMIT = 5;
	public static int MAXIMUM_PLAYERS_LIMIT = 25;
	static final int GAME_DURATION = 20 * 100;
	public static final int SPINNER_HEAL = 30;
	static final int NOVICE_FLAG_MODEL = 27182;
	static final int INTERMEDIATE_FLAG_MODEL = 27187;
	static final int VETERAN_FLAG_MODEL = 27186;
	private static final int FLAG_ANIMATION_ID = 6874;
	public static final int INSTANCE_WIDTH = 10;
	public static final int INSTANCE_HEIGHT = 9;
	static final int INSTANCE_CHUNK_X = 327;
	static final int INSTANCE_CHUNK_Y = 320;
	private static final int ACTIVITY_PERCENTAGE_VARBIT = 5662;
	static final int FULL_ACTIVITY_PERCENTAGE_VALUE = 52;
	public static final int HALF_FULL_ACTIVITY_PERCENTAGE_VALUE = FULL_ACTIVITY_PERCENTAGE_VALUE / 2;
	static final int HIGH_ACTIVITY_POINTS = 10;
	public static final int MODERATE_ACTIVITY_POINTS = HIGH_ACTIVITY_POINTS / 2;
	static final int LANDER_LEAVE_SQUIRE = 2949;
	static final Location[] BARRICADES = new Location[] {new Location(2636, 2591, 0), new Location(2636, 2590, 0), new Location(2636, 2589, 0), new Location(2636, 2588, 0), new Location(2637, 2574, 0), new Location(2637, 2573, 0), new Location(2637, 2572, 0), new Location(2637, 2571, 0), new Location(2647, 2578, 0), new Location(2648, 2578, 0), new Location(2649, 2578, 0), new Location(2650, 2578, 0), new Location(2656, 2575, 0), new Location(2657, 2575, 0), new Location(2658, 2575, 0), new Location(2659, 2575, 0), new Location(2666, 2578, 0), new Location(2667, 2578, 0), new Location(2668, 2578, 0), new Location(2668, 2578, 0), new Location(2669, 2578, 0), new Location(2676, 2571, 0), new Location(2676, 2572, 0), new Location(2676, 2573, 0), new Location(2676, 2574, 0), new Location(2676, 2584, 0), new Location(2676, 2585, 0), new Location(2676, 2585, 0), new Location(2676, 2586, 0), new Location(2676, 2587, 0), new Location(2673, 2590, 0), new Location(2673, 2591, 0), new Location(2673, 2592, 0), new Location(2673, 2593, 0)};
	static final Location[] GATES = new Location[] {new Location(2643, 2593, 0), new Location(2643, 2592, 0), new Location(2642, 2593, 0), new Location(2642, 2592, 0), new Location(2657, 2585, 0), new Location(2656, 2585, 0), new Location(2657, 2584, 0), new Location(2656, 2584, 0), new Location(2670, 2593, 0), new Location(2670, 2592, 0), new Location(2671, 2593, 0), new Location(2671, 2592, 0)};
	static final int[] VALID_RAVAGABLE_OBJECTS = new int[] {14224, 14225, 14226, 14227, 14228, 14229, 14233, 14234, 14235, 14236, 14237, 14238, 14239, 14240, 14241, 14242, 14243, 14244};
	static final int[][][] WATCHTOWER_POLYGON_POINTS = new int[][][] {new int[][] {{2640, 2603}, {2640, 2599}, {2644, 2599}, {2644, 2603}}, new int[][] {{2646, 2586}, {2646, 2582}, {2650, 2582}, {2650, 2586}}, new int[][] {{2664, 2586}, {2664, 2582}, {2668, 2582}, {2668, 2586}}, new int[][] {{2670, 2603}, {2670, 2599}, {2674, 2599}, {2674, 2603}}};

	static Location getRandomLanderLocation() {
		return new Location(Utils.random(2656, 2659), Utils.random(2609, 2614), 0);
	}

	public static Location getRandomShifterLocation() {
		return new Location(Utils.random(2623, 2688), Utils.random(2558, 2598), 0);
	}

	public static Location getRandomPlatformLocation() {
		return new Location(Utils.random(2654, 2659), Utils.random(2590, 2595), 0);
	}

	public static int getRandomKnight() {
		return Utils.random(2950, 2953);
	}

	public static void sendLanderInterface(final Player player) {
		GameInterface.PEST_CONTROL_LANDER_OVERLAY.open(player);
	}

	public static void setTime(final int ticks) {
		if (TIME_UNTIL_GAME_START > ticks) {
			refreshAllLanders(OptionalInt.of(ticks));
		}
		TIME_UNTIL_GAME_START = ticks;
	}

	public static void setMinimum(final int minimum) {
		MINIMUM_PLAYERS_LIMIT = Math.min(minimum, MAXIMUM_PLAYERS_LIMIT);
		refreshAllLanders(OptionalInt.empty());
	}

	public static void setMaximum(final int maximum) {
		MAXIMUM_PLAYERS_LIMIT = Math.max(maximum, MINIMUM_PLAYERS_LIMIT);
		refreshAllLanders(OptionalInt.empty());
	}

	private static void refreshAllLanders(final OptionalInt time) {
		updateLander(time, GlobalAreaManager.get("Pest Control Novice Lander"), GlobalAreaManager.get("Pest Control Intermediate Lander"), GlobalAreaManager.get("Pest Control Veteran Lander"));
	}

	private static void updateLander(final OptionalInt time, final RegionArea... areas) {
		for (final RegionArea area : areas) {
			if (!(area instanceof AbstractLanderArea)) {
				continue;
			}
			final AbstractLanderArea lander = (AbstractLanderArea) area;
			time.ifPresent(lander::setTicks);
			lander.getPlayers().forEach(p -> updateLanderInformation(p, lander));
		}
	}

	public static void updateLanderInformation(final Player player, final AbstractLanderArea area) {
		GameInterface.PEST_CONTROL_LANDER_OVERLAY.getPlugin().ifPresent(inter -> {
			final PacketDispatcher dispatch = player.getPacketDispatcher();
			final int ticks = Math.max(50, area.getTicks());
			final GameInterface overlay = inter.getInterface();
			if (area.getPlayers().size() >= MINIMUM_PLAYERS_LIMIT) dispatch.sendComponentText(overlay, inter.getComponent("Players ready"), "Players ready: " + area.getPlayers().size());
			 else dispatch.sendComponentText(overlay, inter.getComponent("Players ready"), "<col=ff813d>Players ready: " + area.getPlayers().size() + " (" + (MINIMUM_PLAYERS_LIMIT - area.getPlayers().size()) + " needed)");
			dispatch.sendComponentText(overlay, inter.getComponent("Next departure"), String.format("Next departure:%s%s", ticks >= 100 ? (" " + (ticks / 100) + " min") : "", ticks % 100 >= 50 ? " 30 seconds" : ""));
			dispatch.sendComponentText(overlay, inter.getComponent("Pest points"), "Pest Points: " + player.getNumericAttribute("pest_control_points").intValue());
			dispatch.sendComponentModel(overlay, inter.getComponent("Lander flag"), area.getType().getFlagModel());
			dispatch.sendComponentAnimation(overlay, inter.getComponent("Lander flag"), FLAG_ANIMATION_ID);
			dispatch.sendComponentText(overlay, inter.getComponent("Lander type"), area.getType());
		});
	}

	static void sendGameInterface(final Player player) {
		GameInterface.PEST_CONTROL_GAME_OVERLAY.open(player);
	}

	static void updateActivity(final Player player, final PestControlInstance instance) {
		player.getVarManager().sendBit(ACTIVITY_PERCENTAGE_VARBIT, instance.getActivity(player));
	}

	static void updateShields(final Player player, final PestControlInstance instance) {
		final PacketDispatcher dispatch = player.getPacketDispatcher();
		GameInterface.PEST_CONTROL_GAME_OVERLAY.getPlugin().ifPresent(inter -> instance.getPortals().forEach((k, v) -> {
			dispatch.sendComponentText(GAME_OVERLAY, k.getHealthComponentName(), v.getHitpoints());
			if (v.isShieldDropped()) {
				dispatch.sendComponentVisibility(GAME_OVERLAY, k.getShieldComponentName(), true);
			}
		}));
	}

	static void updateVoidKnight(final Player player, final int hitpoints) {
		GameInterface.PEST_CONTROL_GAME_OVERLAY.getPlugin().ifPresent(inter -> player.getPacketDispatcher().sendComponentText(inter.getInterface(), inter.getComponent("Void knight health"), hitpoints));
	}

	static void updateDamageDealt(final Player player, final int amount) {
		GameInterface.PEST_CONTROL_GAME_OVERLAY.getPlugin().ifPresent(inter -> player.getPacketDispatcher().sendComponentText(inter.getInterface(), inter.getComponent("Damage dealt"), amount));
	}

	static void updateTime(final Player player, final int time) {
		GameInterface.PEST_CONTROL_GAME_OVERLAY.getPlugin().ifPresent(inter -> player.getPacketDispatcher().sendComponentText(inter.getInterface(), inter.getComponent("Time remaining"), time + " min"));
	}
}
