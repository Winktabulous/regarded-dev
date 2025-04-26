package com.near_reality.game.content.boss.nex;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;

public enum NexPhase {

	SMOKE(FumusNPC.ID, "Fill my soul with smoke!", new Location(2913, 5215), Direction.SOUTH_EAST, 80),
	SHADOW(UmbraNPC.ID, "Darken my shadow!", new Location(2937, 5215), Direction.SOUTH_WEST, 60),
	BLOOD(CruorNPC.ID, "Flood my lungs with blood!", new Location(2937, 5191), Direction.NORTH_WEST, 40),
	ICE(GlaciesNPC.ID, "Infuse me with the power of ice!", new Location(2913, 5191), Direction.NORTH_EAST, 20),
	ZAROS(-1, "NOW, THE POWER OF ZAROS!", null, null, 0);

	private final int npcId;
	private final String phaseStartText;
	private final Location location;
	private final Direction spawnDirection;
	private final int nextPhasePercent;

	NexPhase(int npcId, String phaseStartText, Location location, Direction spawnDirection, int nextPhasePercent) {
		this.npcId = npcId;
		this.phaseStartText = phaseStartText;
		this.location = location;
		this.spawnDirection = spawnDirection;
		this.nextPhasePercent = nextPhasePercent;
	}

	public int getNpcId() {
		return npcId;
	}

	public String getPhaseStartText() {
		return phaseStartText;
	}

	public Location getLocation() {
		return location;
	}

	public Direction getSpawnDirection() {
		return spawnDirection;
	}

	public int getNextPhasePercent() {
		return nextPhasePercent;
	}

}
