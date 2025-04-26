package com.zenyte.game.content.boss.nightmare;

import com.zenyte.utils.TimeUnit;

public class NightmareStatistics {

	private long globalKillCount;
	private long globalDeathCount;
	private int globalBestKillTimeSeconds = -1;

	public void increaseGlobalKill(long killTime) {
		globalKillCount++;
		int lengthInSeconds = (int) TimeUnit.MILLISECONDS.toSeconds(killTime);
		int oldRecord = globalBestKillTimeSeconds;
		if (oldRecord <= 0 || lengthInSeconds < oldRecord) {
			globalBestKillTimeSeconds = lengthInSeconds;
		}
	}

	public void increaseGlobalDeath() {
		this.globalDeathCount++;
	}

	public long getGlobalKillCount() {
		return globalKillCount;
	}

	public long getGlobalDeathCount() {
		return globalDeathCount;
	}

	public int getGlobalBestKillTimeSeconds() {
		return globalBestKillTimeSeconds;
	}

}
