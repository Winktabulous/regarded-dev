package com.zenyte.game.content.tombsofamascut.raid;

import com.zenyte.game.content.tombsofamascut.encounter.*;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;

/**
 * @author Savions.
 */
public enum EncounterType {

	MAIN_HALL(440, 640, MainHallEncounter.class, new Location(3550, 5161), 2, 0, null, "Beneath Cursed Sands", null, null, null),
	CRONDIS_PUZZLE(488, 656, CrondisPuzzleEncounter.class, new Location(3954, 5279), 0, 2, new Location(3943, 5280), "Test of Resourcefulness", new Location(3923, 5250), new Location(3949, 5311), null),
	CRONDIS_BOSS(488, 672, ZebakEncounter.class, new Location(3958, 5407), 0, 2, new Location(3941, 5408),  "Jaws of Gluttony",
			new Location(3904, 5387), new Location(3962, 5429), new Location(3928, 5408)) {
		@Override
		public boolean insideChallengeArea(TOARaidArea toaRaidArea, Entity entity) {
			int minX = toaRaidArea.getX(3957);
			int minY = toaRaidArea.getY(5404);
			int maxX = toaRaidArea.getX(3960);
			int maxY = toaRaidArea.getY(5414);
			if (entity.getX() >= minX && entity.getX() <= maxX && entity.getY() >= minY && entity.getY() <= maxY) {
				return false;
			}
			minX = toaRaidArea.getX(3952);
			minY = toaRaidArea.getY(5406);
			maxX = toaRaidArea.getX(3957);
			maxY = toaRaidArea.getY(5410);
			if (entity.getX() >= minX && entity.getX() <= maxX && entity.getY() >= minY && entity.getY() <= maxY) {
				return false;
			}
			return super.insideChallengeArea(toaRaidArea, entity);
		}
	},
	SCABARIS_PUZZLE(440, 656, ScabarasEncounter.class, new Location(3522, 5279), 0, 2, new Location(3575, 5280), "Test of Isolation",
			new Location(3533, 5268), new Location(3574, 5292), null),
	SCABARIS_BOSS(440, 672, KephriEncounter.class, new Location(3535, 5408), 0, 2, new Location(3544, 5408),
			"A Mother's Curse", new Location(3543, 5400), new Location(3559, 5416), new Location(3558, 5408)),
	HET_PUZZLE(456, 656, HetEncounter.class, new Location(3698, 5279), 0, 2, new Location(3667, 5280), "Test of Strength", new Location(3670, 5267), new Location(3690, 5293), null),
	HET_BOSS(456, 672, AkkhaEncounter.class, new Location(3698, 5406, 1), 0, 2, new Location(3689, 5408, 1), "Sands of Time", new Location(3670, 5395, 1), new Location(3691, 5419, 1), new Location(3673, 5407, 1)),
	APMEKEN_PUZZLE(472, 656, ApmekenEncounter.class, new Location(3792, 5279), 0, 2, new Location(3814, 5280), "Test of Companionship", new Location(3797, 5267), new Location(3819, 5293), null),
	APMEKEN_BOSS(472, 672, BabaEncounter.class, new Location(3790, 5407), 0, 2, new Location(3800, 5408), "Ape-ex Predator", new Location(3796, 5399), new Location(3823, 5418), new Location(3817, 5408)),
	WARDENS_FIRST_ROOM(472, 640, WardenEncounter.class, new Location(3807, 5176, 1), 2, 0, new Location(3808, 5166, 1), "Amascut's Promise", new Location(3792, 5137, 1), new Location(3825, 5171, 1), new Location(3808, 5158, 1)),
	WARDENS_SECOND_ROOM(488, 640, SecondWardenEncounter.class, new Location(3935, 5168, 1), 2, 0, new Location(3936, 5157, 1), "Amascut's Promise", new Location(3924, 5151, 1), new Location(3947, 5166, 1), null),
	REWARD_ROOM(456, 640, RewardEncounter.class, new Location(3680, 5170, 0), 0, 0, new Location(3680, 5170, 0), "Laid to Rest", new Location(3680, 5170, 0), new Location(3680, 5170, 0), new Location(3680, 5143, 0));

	private final int chunkX;
	private final int chunkY;
	private final Class<? extends TOARaidArea> childClass;
	private final Location spawnTile;
	private final int xRandomize;
	private final int yRandomize;
	private final Location challengeSpawnLocation;
	private final String soundTrack;
	private final Location minChallengeLocation;
	private final Location maxChallengeLocation;
	private final Location npcLocation;

	public static final EncounterType[] VALUES = values();

	EncounterType(int chunkX, int chunkY, Class<? extends TOARaidArea> childClass, final Location spawnTile, int xRandomize, int yRandomize, final Location challengeSpawnLocation,
	              String soundtrack, final Location minChallengeLocation, final Location maxChallengeLocation, final Location npcLocation) {
		this.chunkX = chunkX;
		this.chunkY = chunkY;
		this.childClass = childClass;
		this.spawnTile = spawnTile;
		this.xRandomize = xRandomize;
		this.yRandomize = yRandomize;
		this.challengeSpawnLocation = challengeSpawnLocation;
		this.soundTrack = soundtrack;
		this.minChallengeLocation = minChallengeLocation;
		this.maxChallengeLocation = maxChallengeLocation;
		this.npcLocation = npcLocation;
	}

	public static EncounterType fromBase(BaseEncounterType encounterType) {
		switch(encounterType) {
            case CRONDIS_PUZZLE -> {
                return EncounterType.CRONDIS_PUZZLE;
            }
			case MAIN_HALL -> {
				return EncounterType.MAIN_HALL;
			}
			case SCABARIS_PUZZLE -> {
				return EncounterType.SCABARIS_PUZZLE;
			}
			case SCABARIS_BOSS -> {
				return EncounterType.SCABARIS_BOSS;
			}
			case HET_PUZZLE -> {
				return EncounterType.HET_PUZZLE;
			}
			case HET_BOSS -> {
				return EncounterType.HET_BOSS;
			}
			case APMEKEN_PUZZLE -> {
				return EncounterType.APMEKEN_PUZZLE;
			}
			case APMEKEN_BOSS -> {
				return EncounterType.APMEKEN_BOSS;
			}
			case WARDENS_FIRST_ROOM -> {
				return EncounterType.WARDENS_FIRST_ROOM;
			}
			case WARDENS_SECOND_ROOM -> {
				return EncounterType.WARDENS_SECOND_ROOM;
			}
			case REWARD_ROOM -> {
				return EncounterType.REWARD_ROOM;
			}
        }
		return EncounterType.MAIN_HALL;
	}

	public int getChunkX() { return chunkX; }
	public int getChunkY() { return chunkY; }
	public Class<? extends TOARaidArea> getChildClass() { return childClass; }
	public Location getRandomizedSpawnTile() { return spawnTile.transform(Utils.random(xRandomize), Utils.random(yRandomize)); }
	public Location getChallengeSpawnLocation() { return challengeSpawnLocation; }
	public String getSoundTrack() { return soundTrack; }
	public Location getMinChallengeLocation() { return minChallengeLocation; }
	public Location getMaxChallengeLocation() { return maxChallengeLocation; }
	public Location getNpcLocation() { return npcLocation; }

    public boolean insideChallengeArea(TOARaidArea toaRaidArea, Entity entity) {
		final Location minChallengeLoc = toaRaidArea.getLocation(getMinChallengeLocation());
		final Location maxChallengeLoc = toaRaidArea.getLocation(getMaxChallengeLocation());
		return entity.getX() >= minChallengeLoc.getX() && entity.getX() <= maxChallengeLoc.getX()
				&& entity.getY() >= minChallengeLoc.getY() && entity.getY() <= maxChallengeLoc.getY();
    }

	public BaseEncounterType toBase() {
		switch(this) {
			case CRONDIS_PUZZLE -> {
				return BaseEncounterType.CRONDIS_PUZZLE;
			}
			case MAIN_HALL -> {
				return BaseEncounterType.MAIN_HALL;
			}
			case SCABARIS_PUZZLE -> {
				return BaseEncounterType.SCABARIS_PUZZLE;
			}
			case SCABARIS_BOSS -> {
				return BaseEncounterType.SCABARIS_BOSS;
			}
			case HET_PUZZLE -> {
				return BaseEncounterType.HET_PUZZLE;
			}
			case HET_BOSS -> {
				return BaseEncounterType.HET_BOSS;
			}
			case APMEKEN_PUZZLE -> {
				return BaseEncounterType.APMEKEN_PUZZLE;
			}
			case APMEKEN_BOSS -> {
				return BaseEncounterType.APMEKEN_BOSS;
			}
			case WARDENS_FIRST_ROOM -> {
				return BaseEncounterType.WARDENS_FIRST_ROOM;
			}
			case WARDENS_SECOND_ROOM -> {
				return BaseEncounterType.WARDENS_SECOND_ROOM;
			}
			case REWARD_ROOM -> {
				return BaseEncounterType.REWARD_ROOM;
			}
		}
		return BaseEncounterType.MAIN_HALL;
	}
}