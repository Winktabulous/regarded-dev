package com.zenyte.game.content.tombsofamascut.raid;

/**
 * @author Savions.
 */
public class ChallengeResult {

	private final long time;

	private final String mvpUsername;
	private final EncounterType encounterType;

	public ChallengeResult(long time, String mvpUsername, EncounterType encounterType) {
		this.time = time;
		this.mvpUsername = mvpUsername;
		this.encounterType = encounterType;
	}

	public long getTime() { return time; }

	public String getMvpUsername() { return mvpUsername; }

	public EncounterType getEncounterType() { return encounterType; }
}