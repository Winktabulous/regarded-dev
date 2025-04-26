package com.zenyte.game.model;

import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dailychallenge.challenge.DailyChallenge;
import com.zenyte.game.world.entity.player.var.VarCollection;
import com.zenyte.plugins.Listener;
import com.zenyte.plugins.ListenerType;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Consumer;

/**
 * @author Polar | 7. mai 2018 : 02:05:06
 */
public final class RuneDate {

	private static final Calendar CALENDAR = Calendar.getInstance();

	private static final ZoneId UTC = ZoneId.of("UTC");

	private static final List<Consumer<Player>> onCheckDateConsumers = new ArrayList<>();

	public static boolean addOnCheckDate(Consumer<Player> consumer) {
		return onCheckDateConsumers.add(consumer);
	}

	/**
	 * Checks the last login by the player, used to reset daily activities.
	 * 
	 * @param player
	 *            the player who to check.
	 */
	@Listener(type = ListenerType.LOBBY_CLOSE)
	public static final void checkDate(final Player player) {
		final int lastLogin = player.getVariables().getLastLogin();
        final int currentDate = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        player.getVariables().setLastLogin(currentDate);
		if (currentDate != lastLogin) {
			if (player.getVariables().getRaidAdvertsQuota() < 15) {
				player.getVariables().setRaidAdvertsQuota(15);
			}
			player.getVariables().setFountainOfRuneTeleports(0);
			player.getVariables().setArdougneFarmTeleports(0);
			player.getVariables().setFishingColonyTeleports(0);
			player.getVariables().setSherlockTeleports(0);
			player.getVariables().setFaladorPrayerRecharges(0);
			player.getVariables().setRellekkaTeleports(0);
			player.getVariables().setRunReplenishments(0);
			player.getVariables().setFreeAlchemyCasts(0);
			player.getVariables().setCabbageFieldTeleports(0);
			player.getVariables().setNardahTeleports(0);
			player.getVariables().setKourendWoodlandTeleports(0);
			player.getVariables().setMountKaruulmTeleports(0);
			player.getVariables().setSpellbookSwaps(0);
			player.getVariables().setZulrahResurrections(0);
			player.getVariables().setGrappleAndCrossbowSearches(0);
			player.getVariables().setTeletabPurchases(0);
			player.getVariables().setClaimedBattlestaves(false);
			player.getVariables().setTrollheimTeleports(0);
			player.getVariables().setMorUikRekTeleports(0);
			player.getAttributes().put("exchange-daily-tomes-remaining", 10);
			VarCollection.DAILY_BATTLESTAVES_COLLECTED.updateSingle(player);
			final DailyChallenge challenge = player.getDailyChallengeManager().getRandomChallenge();
			if (challenge != null) {
				player.getDailyChallengeManager().assignChallenge(challenge);
			}
			player.sendMessage(Colour.RS_PINK.wrap("Your daily limits have been reset."));

			for (int i = 0; i < onCheckDateConsumers.size(); i++) {
				Consumer<Player> consumer = onCheckDateConsumers.get(i);
				consumer.accept(player);
			}
		}
	}

	/**
	 * Returns the current date in minutes.
	 *
	 * @return The current date converted into minutes.
	 */
	public static int getMinutes() {
		return getMinutes(currentTimeMillis());
	}

	/**
	 * Gets the date in minutes for {@code timestamp}.
	 *
	 * @param timestamp
	 *            The time.
	 * @return The date in minutes.
	 */
	public static int getMinutes(final long timestamp) {
		return (int) (timestamp / 60000L);
	}

	/**
	 * Returns the current year.
	 *
	 * @return The current year.
	 */
	public static int getYear() {
		return getYear(currentTimeMillis());
	}

	/**
	 * Gets the year based on {@code timestamp}.
	 *
	 * @param timestamp The timestamp.
	 * @return The year the timestamp is in.
	 */
	public static int getYear(final long timestamp) {
		CALENDAR.clear();
		CALENDAR.setTime(new Date(timestamp));

		return CALENDAR.get(Calendar.YEAR);
	}

	/**
	 * Returns the current date in RuneDays.
	 *
	 * @return The current date converted into RuneDays.
	 */
	public static int getDate() {
		return (int) (currentTimeMillis() / 86400000L - 11745);
	}

	/**
	 * Converts {@code date} to the number of RuneDays.
	 *
	 * @param date
	 *            The date to convert.
	 * @return The converted amount of RuneDays.
	 */
	public static int fromDate(final LocalDate date) {
		CALENDAR.clear();
		CALENDAR.setTimeZone(TimeZone.getTimeZone(UTC));
		CALENDAR.set(Calendar.HOUR_OF_DAY, 12);
		CALENDAR.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());

		return (int) (CALENDAR.getTime().getTime() / 86400000L) - 11745;
	}

	/**
	 * @param runeDate
	 * @return
	 */
	public static LocalDate toDate(final int runeDate) {
		final long timestamp = 86400000L * (11745 + runeDate);

		CALENDAR.clear();
		CALENDAR.setTimeInMillis(timestamp);

		return CALENDAR.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	/**
	 * Determines if the given {@code year} is a leap year.
	 *
	 * @param year
	 *            The year.
	 * @return {@code true} if the year is a leap year, otherwise {@code false}.
	 */
	public static boolean isLeapYear(final int year) {
		if (year < 0) {
			return (year + 1) % 4 == 0;
		} else if (year < 1582) {
			return year % 4 == 0;
		} else if (year % 4 != 0) {
			return false;
		} else if (year % 100 != 0) {
			return true;
		} else return year % 400 == 0;
	}

	/**
	 * Gets the current time in milliseconds based on UTC timezone.
	 *
	 * @return The timestamp.
	 */
	public static long currentTimeMillis() {
		return Instant.now().atZone(UTC).toInstant().toEpochMilli();
	}

	/**
	 * Converts the given {@code timestamp} to a {@link LocalDate}.
	 *
	 * @param timestamp
	 *            The timestamp to convert.
	 * @return The local date instance.
	 */
	public static LocalDate localDateFromTimeStamp(final long timestamp) {
		return Instant.ofEpochMilli(timestamp).atZone(UTC).toLocalDate();
	}

	/**
	 * Get the time until the next {@code x} minute interval. Example usage is
	 * to find the time until the next :05 interval for delaying a task on
	 * startup.
	 *
	 * @param x
	 *            The interval to use.
	 * @return The time in milliseconds until the next interval.
	 */
	public static long getOffsetForNextInterval(final int x) {
		final LocalDateTime now = LocalDateTime.now();

		final int nextInterval = now.getMinute() + (x - now.getMinute() % x);
		final int minuteOffset = nextInterval - now.getMinute() - 1;
		final int secondOffset = 60 - now.getSecond();

		return ((minuteOffset * 60L) + secondOffset) * 1_000;
	}

}