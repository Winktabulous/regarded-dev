package com.zenyte.game.content.tombsofamascut.raid;

import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Savions.
 */
public enum TOAPathType {

	APMEKEN("Apmeken", new Location(3562, 5146), EncounterType.APMEKEN_PUZZLE, new Location(3561, 5146), 0, 2, Direction.WEST),
	SCABARAS("Scabaras", new Location(3559, 5155), EncounterType.SCABARIS_BOSS, new Location(3558, 5154), 0, 0, Direction.SOUTH_WEST),
	HET("Het", new Location(3539, 5146), EncounterType.HET_PUZZLE, new Location(3541, 5146), 0, 2, Direction.EAST),
	CRONDIS("Crondis", new Location(3541, 5155), EncounterType.CRONDIS_PUZZLE, new Location(3544, 5154), 0, 0, Direction.SOUTH_EAST);

	public static final TOAPathType[] VALUES = values();

	private final Location entranceLocation;
	private final String properName;
	private final EncounterType firstEncounter;
	private final Location spawnTile;
	private final int xRandomize;
	private final int yRandomize;
	private final Direction faceDirection;

	TOAPathType(final String properName, final Location entranceLocation, final EncounterType firstEncounter, final Location spawnTile, int xRandomize, int yRandomize, Direction faceDirection) {
		this.properName = properName;
		this.entranceLocation = entranceLocation;
		this.firstEncounter = firstEncounter;
		this.spawnTile = spawnTile;
		this.xRandomize = xRandomize;
		this.yRandomize = yRandomize;
		this.faceDirection = faceDirection;
	}

	public static TOAPathType getForIndex(int i) {
		return values()[i];
	}

	public final String getPathName() { return this.properName; }
	public final Location getEntranceLocation() { return entranceLocation; }
	public final EncounterType getFirstEncounter() { return firstEncounter; }
	public final Location getRandomizedSpawnTile() { return spawnTile.transform(Utils.random(xRandomize), Utils.random(yRandomize)); }
	public Direction getFaceDirection() { return faceDirection; }
}
