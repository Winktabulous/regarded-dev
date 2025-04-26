package com.zenyte.game.content.tombsofamascut.raid;

import com.zenyte.game.world.entity.Location;

/**
 * @author Savions.
 */
public class TOAPlayerLogoutState {

	private final BaseEncounterType encounterType;
	private final boolean duringChallenge;
	private final Location logoutLocation;
	private final boolean safeDeath;

	public TOAPlayerLogoutState(BaseEncounterType encounterType, boolean duringChallenge, Location logoutLocation, boolean safeDeath) {
		this.encounterType = encounterType;
		this.duringChallenge = duringChallenge;
		this.logoutLocation = logoutLocation;
		this.safeDeath = safeDeath;
	}

	public BaseEncounterType getEncounterType() { return encounterType; }

	public boolean isDuringChallenge() { return duringChallenge; }

	public Location getLogoutLocation() { return logoutLocation; }

	public boolean isSafeDeath() { return safeDeath; }
}
