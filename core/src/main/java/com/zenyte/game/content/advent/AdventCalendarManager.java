package com.zenyte.game.content.advent;

import com.google.common.eventbus.Subscribe;
import com.zenyte.ContentConstants;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.events.ServerLaunchEvent;
import com.zenyte.utils.StaticInitializer;
import com.zenyte.utils.TextUtils;

import java.util.Calendar;
import java.util.function.Function;

@StaticInitializer
public class AdventCalendarManager {

	private static final String REWARD_ATTRIBUTE = "completed_2022_advent";
	public static AdventCalendar adventCalendar;
	public static int currentYear;

	@Subscribe
	public static final void onLaunch(final ServerLaunchEvent event) {
		Calendar adventStart = Calendar.getInstance();
		currentYear = adventStart.get(Calendar.YEAR);
		Calendar adventEnd = Calendar.getInstance();
		adventStart.set(currentYear, Calendar.NOVEMBER, 30);
		adventEnd.set(currentYear, Calendar.DECEMBER, 25);

		final Calendar date = Calendar.getInstance();
		if (!adventStart.before(date) || !adventEnd.after(date)) {
			return;
		}

		World.spawnObject(new ReindeerObject(0, new Location(3000, 3469)));
		World.spawnObject(new ReindeerObject(1, new Location(2857, 3508)));
		World.spawnObject(new ReindeerObject(2, new Location(2838, 3505)));
		World.spawnObject(new ReindeerObject(3, new Location(1630, 3949)));
		World.spawnObject(new ReindeerObject(4, new Location(1629, 3949)));

		new SnowmanNPC(16061, new Location(2997, 3125), Direction.SOUTH, 0).spawn();
		new SnowmanNPC(16062, new Location(2654, 4723), Direction.SOUTH, 1).spawn();
		new SnowmanNPC(16063, new Location(2503, 3898), Direction.SOUTH, 2).spawn();

		new SnowmanNPC(16061, new Location(2433, 3107), Direction.SOUTH, 3).spawn();
		new SnowmanNPC(16062, new Location(2202, 3045), Direction.SOUTH, 4).spawn();
		new SnowmanNPC(16063, new Location(2164, 3866), Direction.SOUTH, 5).spawn();

		new SnowmanNPC(16061, new Location(1630, 3925), Direction.SOUTH, 6).spawn();
		new SnowmanNPC(16062, new Location(3465, 3288), Direction.SOUTH, 7).spawn();
		new SnowmanNPC(16063, new Location(3686, 2966), Direction.SOUTH, 8).spawn();

		new SnowmanNPC(16061, new Location(3305, 2781), Direction.SOUTH, 9).spawn();

		adventCalendar = new AdventCalendar(2022, new AdventDay[] {
				new AdventDay(1, 10327, "Find 10 snowman", value -> {
					int returnValue = 0;
					for (int i = 0; i < 10; i++) {
						if ((value & 1 << i) != 0) {
							returnValue++;
						}
					}
					return returnValue;
				}),
				new AdventDay(2, 10328, "Kill 50 lizardman shamans"),
				new AdventDay(3, 10329, "Loot 30 barrows chests"),
				new AdventDay(4, 10330, "Kill 2 xamphur"),
				new AdventDay(5, 10331, "Kill 20 kalphite queens"),
				new AdventDay(6, 10332, "Fish 100 anglerfish"),
				new AdventDay(7, 10333, "200 pest control points"),
				new AdventDay(8, 10334, "50k cox points"),
				new AdventDay(9, 10335, "kill 20 gwd bosses"),
				new AdventDay(10, 10336, "kill 30 dks"),
				new AdventDay(11, 10337, "participate in pvp tournament"),
				new AdventDay(12, 10338, "craft 2,5k blood runes"),
				new AdventDay(13, 10339, "pet 5 reindeer", value -> {
					int returnValue = 0;
					for (int i = 0; i < 5; i++) {
						if ((value & 1 << i) != 0) {
							returnValue++;
						}
					}
					return returnValue;
				}),
				new AdventDay(14, 10340, "open 10 clue caskets"),
				new AdventDay(15, 10341, "kill 10 vorkath"),
				new AdventDay(16, 10342, "xmas event."),
				new AdventDay(17, 10343, "cut 200 trees"),
				new AdventDay(18, 10344, "catch 100 sharks"),
				new AdventDay(19, 10345, "wave 32 fight caves"),
				new AdventDay(20, 10346, "5 gauntlets"),
				new AdventDay(21, 10347, "mine 100 coal"),
				new AdventDay(22, 10348, "10 zulrah"),
				new AdventDay(23, 10349, "100 snowballs"),
				new AdventDay(24, 10350, "burn 100 logs"),
				new AdventDay(25, 10351, "christmas tree at home"),
		});
	}

	public static Function<Integer, Boolean> dayLockedFunc = day -> {
		Calendar cal = Calendar.getInstance();
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		return day >= 23 && day != dayOfMonth;
	};

	public static void setChallenge(Player player, int day, int value) {
		setChallenge(player, currentYear, day, value);
	}

	public static void setChallenge(Player player, int year, int day, int value) {
		if (!adventEnabled() || adventCalendar.getYear() != year) {
			player.sendDeveloperMessage("Calendar null or doesn't match year.");
			return;
		}

		if (dayLockedFunc.apply(day)) {
			player.sendDeveloperMessage("The day is already passed.");
			return;
		}

		AdventDay adventDay = adventCalendar.getDay(day);
		int completedCount = adventDay.getCountToComplete();
		String attribute = adventDay.getAttribute(adventCalendar);
		int current = adventDay.getValue(player.getNumericAttribute(attribute).intValue());
		if (current < completedCount) {
			if (day != 1 && day != 13) {
				player.getAttributes().put(attribute, Math.min(value, completedCount));
			} else {
				player.getAttributes().put(attribute, value);
			}
			if (adventDay.getValue(value) >= completedCount) {
				Item reward = adventDay.getReward();
				player.sendMessage("You're awarded " + TextUtils.formatCurrency(reward.getAmount()) + "x " + reward.getName() + " for completing Day " + day + " of the Advent Calendar!");
				player.getInventory().addItem(reward).onFailure(remainder -> {
					player.sendMessage("There was no room in your inventory, so " + Colour.RED.wrap(TextUtils.formatCurrency(remainder.getAmount()) + "x " + remainder.getName()) + " have been added to your bank!");
					player.getBank().add(remainder).onFailure(remainder2 -> {
						player.sendMessage("There was no room in your bank, so " + Colour.RED.wrap(TextUtils.formatCurrency(remainder2.getAmount()) + "x " + remainder2.getName()) + " have been dropped to the floor!");
						World.spawnFloorItem(remainder2, player);
					});
				});

				checkForCompletion(player);
			}
		}
	}

	public static void checkForCompletion(Player player) {
		if (player.getBooleanAttribute(REWARD_ATTRIBUTE)) {
			return;
		}

		if (getCompletedCount(player) >= 25) {
			player.putBooleanAttribute(REWARD_ATTRIBUTE, true);
			Item reward = new Item(ItemId.CHRISTMAS_SCYTHE);
			player.sendMessage("You're awarded " + TextUtils.formatCurrency(reward.getAmount()) + "x " + reward.getName() + " for completing the Advent Calendar!");
			player.getInventory().addItem(reward).onFailure(remainder -> {
				player.sendMessage("There was no room in your inventory, so " + Colour.RED.wrap(TextUtils.formatCurrency(remainder.getAmount()) + "x " + remainder.getName()) + " have been added to your bank!");
				player.getBank().add(remainder).onFailure(remainder2 -> {
					player.sendMessage("There was no room in your bank, so " + Colour.RED.wrap(TextUtils.formatCurrency(remainder2.getAmount()) + "x " + remainder2.getName()) + " have been dropped to the floor!");
					World.spawnFloorItem(remainder2, player);
				});
			});
		}
	}

	public static int getCompletedCount(Player player) {
		int completedCount = 0;
		for (int day = 1; day <= 25; day++) {
			AdventDay adventDay = AdventCalendarManager.getDay(day);
			int progress = AdventCalendarManager.getChallengeProgress(player, day);
			if (adventDay.getValue(progress) >= adventDay.getCountToComplete()) {
				completedCount++;
			}
		}

		return completedCount;
	}

	public static int getChallengeProgress(Player player, int day) {
		return getChallengeProgress(player, currentYear, day);
	}

	public static int getChallengeProgress(Player player, int year, int day) {
		if (!adventEnabled() || adventCalendar.getYear() != year) {
			player.sendDeveloperMessage("Calendar null or doesn't match year.");
			return 0;
		}

		AdventDay adventDay = adventCalendar.getDay(day);
		String attribute = adventDay.getAttribute(adventCalendar);
		return player.getNumericAttribute(attribute).intValue();
	}

	public static void increaseChallengeProgress(Player player, int day, int value) {
		increaseChallengeProgress(player, currentYear, day, value);
	}

	public static void increaseChallengeProgress(Player player, int year, int day, int value) {
		int current = getChallengeProgress(player, year, day);
		setChallenge(player, year, day, current + value);
	}

	public static AdventDay getDay(int day) {
		return adventCalendar.getDay(day);
	}

	public static boolean adventEnabled() {
		return adventCalendar != null;
	}

}
