package com.zenyte.game.world.entity.player;

import com.zenyte.game.records.RecordManager;
import com.zenyte.game.records.SpeedRecord;
import com.zenyte.game.util.Utils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Kris | 22. march 2018 : 0:17.02
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class BossTimer {

	BossTimer(final Player player) {
		this.player = player;
	}
	
	private transient final Player player;
	private final Map<String, Integer> timers = new HashMap<>();
	private transient String currentBoss;
	private transient long currentTracker;
	
	/**
	 * Sets the boss timer data.
	 * @param clone the object to obtain data from.
	 */
	public void setBossTimers(final BossTimer clone) {
		timers.putAll(clone.timers);
	}
	
	/**
	 * Starts the tracking process at the moment of the method being called.
	 * @param currentBoss the name of the boss being tracked.
	 */
	public void startTracking(final String currentBoss) {
		this.currentBoss = currentBoss;
		currentTracker = Utils.currentTimeMillis();
	}
	
	/**
	 * Finishes tracking the current boss and informs the player about the time.
	 * @param currentBoss the name of the current boss. Upon mismatch, method returns.
	 */
	public void finishTracking(final String currentBoss) {
		if (!currentBoss.equals(this.currentBoss))
			return;
		inform(currentBoss, System.currentTimeMillis() - currentTracker);
	}

	public void inform(final String currentBoss, final long duration) {
		inform(currentBoss, duration, "Fight");
	}

	public void inform(final String currentBoss, final long duration, final String header) {
        final int lengthInSeconds = (int) TimeUnit.MILLISECONDS.toSeconds(duration);
        final int oldRecord = timers.getOrDefault(currentBoss, -1);
        final int minutes = lengthInSeconds / 60;
        final int seconds = lengthInSeconds % 60;
        if (oldRecord == -1 || lengthInSeconds < oldRecord) {
            player.sendMessage("%s duration: <col=ff0000>%d:%s%d </col>(new personal best)"
				.formatted(header, minutes, seconds < 10 ? "0" : "", seconds));
            timers.put(currentBoss, lengthInSeconds);
        }
		else {
            final int oldMinutes = oldRecord / 60;
            final int oldSeconds = oldRecord % 60;
            player.sendMessage("%s duration: <col=ff0000>%d:%s%d</col>. Personal best: %d:%s%d"
				.formatted(header, minutes, seconds < 10 ? "0" : "", seconds, oldMinutes, oldSeconds < 10 ? "0" : "", oldSeconds));
        }
		// not track admins in this list
		if (player.isAdministrator()) return;

		var recMgr = RecordManager.getInstance();
		var newRecord = new SpeedRecord(currentBoss, player.getName(), LocalDate.now(), (long) lengthInSeconds, true);
		if (recMgr.isEventRecorded(currentBoss)) {
			if (recMgr.getRecordByEventName(currentBoss).isPresent()) {
				var cachedRecord = recMgr.getRecordByEventName(currentBoss).get();
				var cachedSpeed = cachedRecord.recordedSpeed();
				if (cachedSpeed > lengthInSeconds)
					recMgr.addRecord(newRecord);
			}
		} // else we're adding a new event
		else recMgr.addRecord(newRecord);
    }

	public long getCurrentTracker() {
		return currentTracker;
	}

	public String personalBest(final String currentBoss) {
		return formatBestTime(timers.getOrDefault(currentBoss, -1));
	}

	public static String formatBestTime(final int seconds) {
		if (seconds == -1) {
			return "-";
		}

		final int oldMinutes = seconds / 60;
		final int oldSeconds = seconds % 60;
		return oldMinutes + ":" + (oldSeconds < 10 ? "0" : "") + oldSeconds;
	}
	
}
