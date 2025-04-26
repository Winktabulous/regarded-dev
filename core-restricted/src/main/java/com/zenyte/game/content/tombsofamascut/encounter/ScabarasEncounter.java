package com.zenyte.game.content.tombsofamascut.encounter;

import com.zenyte.game.content.tombsofamascut.npc.ScabarasObelisk;
import com.zenyte.game.content.tombsofamascut.npc.Scarab;
import com.zenyte.game.content.tombsofamascut.raid.*;
import com.zenyte.game.model.CameraShakeType;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin;
import com.zenyte.game.world.region.area.plugins.FullMovementPlugin;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;

import java.util.*;

/**
 * @author Savions.
 */
public class ScabarasEncounter extends TOARaidArea implements CycleProcessPlugin, FullMovementPlugin {

	private static final Location[] BASE_PUZZLE_LOCATIONS = {new Location(3539, 5283), new Location(3556, 5283), new Location(3539, 5271), new Location(3556, 5271)};
	private static final Location[] NORTH_FIRE_LOCATIONS = {new Location(3549, 5286), new Location(3564, 5286)};
	private static final Location[] SOUTH_FIRE_LOCATIONS = {new Location(3549, 5274), new Location(3564, 5274)};
	private static final Location[] SCARAB_SPAWN_LOCATIONS = {new Location(3535, 5274), new Location(3552, 5274), new Location(3535, 5286), new Location(3552, 5286)};
	private static final Location[] MATCH_BASE_LOCATIONS = {new Location(3568, 5271), new Location(3568, 5283)};
	private static final int[][] MEMORY_OFFSETS = {{2, 4}, {1, 3}, {2, 2}, {3, 5}, {3, 3}, {3, 1}, {4, 4}, {4, 2}, {5, 3}};
	private static final int[][][] SUM_OFFSETS = {{{1, 1}, {4, 4}, {5, 4}}, {{2, 2}, {4, 2}, {4, 5}}, {{2, 5}, {3, 3}, {5, 1}},
			{{4, 1}, {5, 2}, {3, 4}}, {{3, 1}, {5, 3}}, {{1, 2}, {2, 3}, {2, 4}}, {{1, 4}, {3, 2}, {3, 5}}, {{1, 3}, {4, 3}}, {{1, 5}, {2, 1}, {5, 5}}};
	private static final int[][] OBELISK_OFFSETS = {{1, 6}, {3, 6}, {5, 6}, {1, 0}, {3, 0}, {5, 0}};
	private static final int[][] LIGHT_OFFSETS = {{1, 1}, {1, 3}, {1, 5}, {3, 5}, {5, 5}, {5, 3}, {5, 1}, {3, 1}};
	private static final int[][] MATCH_OFFSETS = {{1, 1}, {3, 1}, {5, 1}, {1, 3}, {3, 3}, {5, 3}, {1, 5}, {3, 5}, {5, 5}};
	private static final int[] PRESSURE_BUTTON_OFFSETS = {-1, 5};
	private static final int[] ROCKFALL_SOUND_OFFSETS = {3, 3};
	private static final Location EXIT_FIRE_BASE_LOCATION = new Location(3575, 5272);
	private static final int FIRE_OBJECT_ID = 45335;
	public static final int PRESSURE_BUTTON_ID = 45338;
	private static final int PRESSURE_PLATE_ID = 45340;
	public static final int LIGHT_PLATE_ID = 45344;
	private static final int LIGHT_ACTIVE_ID = 45384;
	public static final int SUM_TABLET_ID = 45339;
	private static final int SUM_PRESSURE_PLATE_ID = 45345;
	public static final int MATCH_PRESSURE_ID = 45360;
	private static final int[] SUM_PRESSURE_LIGHT_IDS = { 45388, 45389, 45390, 45387, 45392, 45393, 45386, 45394, 45395};
	private static final int STATUE_ID = 45205;
	private static final int MEMORY_YELLOW_ID = 45341;
	private static final int MEMORY_RED_ID = 45342;
	private static final Location STATUE_LOC = new Location(3569, 5279);
	private static final Animation FLIP_FINAL_ANIM = new Animation(827);
	private static final Animation FLIP_ANIM = new Animation(9490);
	private static final SoundEffect FLIP_SOUND = new SoundEffect(6560);
	private static final SoundEffect PRESSURE_SOUND = new SoundEffect(6561, 7);
	private static final SoundEffect PUZZLE_COMPLETION_SOUND = new SoundEffect(2655);
	private static final SoundEffect ROCK_FALL_SOUND = new SoundEffect(4459, 3, 155);
	private static final SoundEffect FAILURE_SOUND_START = new SoundEffect(6575, 6, 0);
	private static final SoundEffect FAILURE_SOUND_IMPACT = new SoundEffect(6556, 6, 180);
	private static final SoundEffect RUMBLING_SOUND = new SoundEffect(6578);
	private static final SoundEffect TILE_ACTIVATE_SOUND = new SoundEffect(6553);
	private static final SoundEffect MATCH_FAILURE_SOUND = new SoundEffect(6554, 4);
	private static final SoundEffect SUM_FAILURE_SOUND = new SoundEffect(6558, 0, 10);
	private static final SoundEffect BUTTON_ACTIVE_SOUND = new SoundEffect(6559, 0, 10);
	private static final SoundEffect RUMBLING_OLD_SOUND = new SoundEffect(1678);
	private static final SoundEffect MEMORY_FAILURE_SOUND = new SoundEffect(6557);
	private static final Graphics SMALL_ROCKS_GFX = new Graphics(302, 30, 0);
	private static final Graphics ROCK_FALL_GFX = new Graphics(317, 20, 0);
	private static final Animation BUTTON_ACTIVE_ANIM = new Animation(832);
	private List<ScabarasPuzzleType> puzzleTypeRotation;
	private ScabarasObelisk[] obelisks = new ScabarasObelisk[6];
	private int[] obeliskSet = new int[5];
	private int obeliskIndex = 0;
	private int pillarActionTicks;
	private long pillarRockfallTick;
	private int sumGoal;
	private int currentSum;
	private final boolean[] lights = new boolean[8];
	private List<Scarab> scarabs = new ArrayList<>();
	private int scarabTicks;
	private boolean[] completed = new boolean[4];
	private List<Integer> matchNorthList = new ArrayList<>();
	private List<Integer> matchSouthList = new ArrayList<>();
	private int northPlate = -1;
	private int southPlate = -1;
	private int completedMatches;
	private int[] sequence;
	private int sequenceIndex = 0;
	private int sequenceDisplayIndex = -1;

	public ScabarasEncounter(AllocatedArea allocatedArea, int copiedChunkX, int copiedChunkY, TOARaidParty party, EncounterType encounterType) {
		super(allocatedArea, copiedChunkX, copiedChunkY, party, encounterType);
	}

	@Override public void constructed() {
		super.constructed();
		constructPuzzles();
	}

	@Override public void enter(Player player) {
		super.enter(player);
		for (int animId = 9529; animId <= 9609; animId++) {
			player.getPacketDispatcher().sendClientScript(1846, animId);
		}
	}

	@Override public void onRoomStart() {
		World.removeObject(new WorldObject(STATUE_ID, 10, 3, getLocation(STATUE_LOC)));
		World.removeObject(new WorldObject(STATUE_ID, 10, 3, getLocation(STATUE_LOC.transform(-1, 0))));
		World.spawnObject(new WorldObject(STATUE_ID, 10, 3, getLocation(STATUE_LOC.transform(teamSize > 1 ? -1 : 0, 0))));

		if (teamSize < 2) {
			toggleMatchPlate(true, matchNorthList.indexOf(0), null, true);
			toggleMatchPlate(false, matchSouthList.indexOf(0), null, true);
			for (int i = 4; i < 6; i++) {
				toggleMatchPlate(true, matchNorthList.indexOf(i), null, true);
				toggleMatchPlate(false, matchSouthList.indexOf(i), null, true);
			}
		}

		final Location baseLightLocation = getLocation(BASE_PUZZLE_LOCATIONS[getPuzzleIndex(ScabarasPuzzleType.LIGHT)]);
		for (int i = 0; i < LIGHT_OFFSETS.length; i++) {
			if (Utils.random(1) == 0) {
				toggleLight(baseLightLocation.transform(LIGHT_OFFSETS[i][0], LIGHT_OFFSETS[i][1]), i);
			}
		}
	}

	@Override public void onRoomEnd() {
		scarabs.stream().filter(scarab -> scarab != null && !scarab.isDead() && !scarab.isFinished()).forEach(NPC::finish);
		for (int i = 0; i < 17; i++) {
			World.removeObject(new WorldObject(FIRE_OBJECT_ID, 10, 3, getLocation(EXIT_FIRE_BASE_LOCATION).transform(0, i)));
		}
		Arrays.stream(NORTH_FIRE_LOCATIONS).forEach(loc -> World.removeObject(new WorldObject(FIRE_OBJECT_ID, 10, 3, getLocation(loc))));
		Arrays.stream(SOUTH_FIRE_LOCATIONS).forEach(loc -> World.removeObject(new WorldObject(FIRE_OBJECT_ID, 10, 3, getLocation(loc))));
		Arrays.fill(completed, true);
	}

	@Override public void onRoomReset() {
		scarabs.stream().filter(scarab -> scarab != null && !scarab.isDead() && !scarab.isFinished()).forEach(NPC::finish);
		resetPuzzle();
	}

	private void constructPuzzles() {
		puzzleTypeRotation = new ArrayList<>();
		puzzleTypeRotation.addAll(ScabarasPuzzleType.list);
		Collections.shuffle(puzzleTypeRotation);

		final Location baseLightLocation = getLocation(BASE_PUZZLE_LOCATIONS[getPuzzleIndex(ScabarasPuzzleType.LIGHT)]);
		Arrays.stream(LIGHT_OFFSETS).forEach(offsets -> World.spawnObject(new WorldObject(LIGHT_PLATE_ID , 22, 0, baseLightLocation.transform(offsets[0], offsets[1]))));

		final Location baseSumLocation = getLocation(BASE_PUZZLE_LOCATIONS[getPuzzleIndex(ScabarasPuzzleType.SUM)]);
		for (int i = 0; i < SUM_OFFSETS.length; i++) {
			for (int[] offsets : SUM_OFFSETS[i]) {
				World.spawnObject(new WorldObject(SUM_PRESSURE_PLATE_ID + i, 22, 1, baseSumLocation.transform(offsets[0], offsets[1])));
			}
		}
		World.spawnObject(new WorldObject(SUM_TABLET_ID, 10, 0, baseSumLocation.transform(PRESSURE_BUTTON_OFFSETS[0], PRESSURE_BUTTON_OFFSETS[1])));

		final Location basePillarLocation = getLocation(BASE_PUZZLE_LOCATIONS[getPuzzleIndex(ScabarasPuzzleType.PILLAR)]);
		for (int i = 0; i < 5; i++) {
			obelisks[i] = new ScabarasObelisk(basePillarLocation.transform(OBELISK_OFFSETS[i][0], OBELISK_OFFSETS[i][1]), this, false);
			obelisks[i].spawn();
		}
		obelisks[5] = new ScabarasObelisk(basePillarLocation.transform(OBELISK_OFFSETS[5][0], OBELISK_OFFSETS[5][1]), this, true);
		obelisks[5].spawn();

		final Location baseMemoryLocation = getLocation(BASE_PUZZLE_LOCATIONS[getPuzzleIndex(ScabarasPuzzleType.MEMORY)]);
		World.spawnObject(new WorldObject(PRESSURE_BUTTON_ID, 10, 0, baseMemoryLocation.transform(PRESSURE_BUTTON_OFFSETS[0], PRESSURE_BUTTON_OFFSETS[1])));
		Arrays.stream(MEMORY_OFFSETS).forEach(offsets -> World.spawnObject(new WorldObject(PRESSURE_PLATE_ID, 22, 0, baseMemoryLocation.transform(offsets[0], offsets[1]))));

		matchNorthList = new ArrayList<>();
		matchSouthList = new ArrayList<>();
		for (int i = 0; i < 9; i++) {
			matchNorthList.add(i);
			matchSouthList.add(i);
		}
		Collections.shuffle(matchNorthList);
		Collections.shuffle(matchSouthList);

		resetPuzzle();
	}

	@Override public void destroyRegion() {
		super.destroyRegion();
		for (int i = 0; i < obelisks.length; i++) {
			if (obelisks[i] != null && !obelisks[i].isFinished()) {
				obelisks[i].finish();
			}
		}
	}

	private void resetPuzzle() {
		Arrays.fill(completed, false);
		Arrays.stream(NORTH_FIRE_LOCATIONS).forEach(loc -> World.spawnObject(new WorldObject(FIRE_OBJECT_ID, 10, 3, getLocation(loc))));
		Arrays.stream(SOUTH_FIRE_LOCATIONS).forEach(loc -> World.spawnObject(new WorldObject(FIRE_OBJECT_ID, 10, 3, getLocation(loc))));
		for (int i = 0; i < 17; i++) {
			World.spawnObject(new WorldObject(FIRE_OBJECT_ID, 10, 3, getLocation(EXIT_FIRE_BASE_LOCATION).transform(0, i)));
		}
		for (int i = 0; i < 5; i++) {
			if (obelisks[i] != null) {
				obelisks[i].resetObelisk();
			}
		}
		obeliskIndex = 0;
		final List<Integer> shuffleList = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			shuffleList.add(i);
		}
		Collections.shuffle(shuffleList);
		obeliskSet = shuffleList.stream().mapToInt(i -> i).toArray();
		final Location baseLightLocation = getLocation(BASE_PUZZLE_LOCATIONS[getPuzzleIndex(ScabarasPuzzleType.LIGHT)]);
		for (int i = 0; i < lights.length; i++) {
			if (lights[i]) {
				toggleLight(baseLightLocation.transform(LIGHT_OFFSETS[i][0], LIGHT_OFFSETS[i][1]), i);
			}
		}
		pillarRockfallTick = 0;
		sumGoal = Utils.random(20, 45);
		currentSum = 0;
		toggleAllSumLights(getLocation(BASE_PUZZLE_LOCATIONS[getPuzzleIndex(ScabarasPuzzleType.LIGHT)]), false);
		scarabTicks = 30;

		final Location baseMemoryLocation = getLocation(BASE_PUZZLE_LOCATIONS[getPuzzleIndex(ScabarasPuzzleType.MEMORY)]);
		for (int[] offsets : MEMORY_OFFSETS) {
			World.removeObject(new WorldObject(MEMORY_YELLOW_ID, 10, 0, baseMemoryLocation.transform(offsets[0], offsets[1])));
		}

		Arrays.stream(MATCH_BASE_LOCATIONS).forEach(loc -> {
			final Location baseLocation = getLocation(loc);
			for (int i = 0; i < 9; i++) {
				World.spawnObject(new WorldObject(MATCH_PRESSURE_ID, 22, 1, baseLocation.transform(MATCH_OFFSETS[i][0], MATCH_OFFSETS[i][1])));
				World.removeObject(new WorldObject(SUM_PRESSURE_LIGHT_IDS[matchNorthList.get(i)], 10, 1, baseLocation.transform(MATCH_OFFSETS[i][0], MATCH_OFFSETS[i][1])));
				World.removeObject(new WorldObject(SUM_PRESSURE_LIGHT_IDS[matchSouthList.get(i)], 10, 1, baseLocation.transform(MATCH_OFFSETS[i][0], MATCH_OFFSETS[i][1])));
			}
		});

		northPlate = -1;
		southPlate = -1;
		completedMatches = 0;
		resetSequence();
	}

	public int getPuzzleIndex(ScabarasPuzzleType puzzleType) {
		for (int i = 0; i < puzzleTypeRotation.size(); i++) {
			if (puzzleType.equals(puzzleTypeRotation.get(i))) {
				return i;
			}
		}
		return 0;
	}

	public boolean hasCompleted(ScabarasPuzzleType puzzleType) {
		return completed[getPuzzleIndex(puzzleType)];
	}

	public void complete(ScabarasPuzzleType puzzleType) {
		if (hasCompleted(puzzleType)) {
			return;
		}
		final int index = getPuzzleIndex(puzzleType);
		completed[index] = true;
		World.removeObject(new WorldObject(FIRE_OBJECT_ID, 10, 3, getLocation(index < 2 ? NORTH_FIRE_LOCATIONS[index] : SOUTH_FIRE_LOCATIONS[index - 2])));
		int puzzleNumber = index + 1;
//		if (puzzleNumber == 2) {
//			puzzleNumber = 3;
//		} else if (puzzleNumber == 3) {
//			puzzleNumber = 2;
//		}
		for (Player p : getChallengePlayers()) {
			p.sendMessage("<col=06600c>Puzzle " + puzzleNumber + " has been completed!");
		}
		Arrays.stream(getRoomPlayers(puzzleType)).forEach(p -> p.sendSound(PUZZLE_COMPLETION_SOUND));
	}
	
	public boolean canUse(Player player, ScabarasPuzzleType puzzleType, String faultMessage) {
		if (!EncounterStage.STARTED.equals(stage)) {
			return false;
		}
		final int index = getPuzzleIndex(puzzleType);
		if ((index == 1 && !completed[2]) || (index == 3 && !completed[0])) {
			player.sendMessage(faultMessage == null ? "This device won't function yet." : faultMessage);
			return false;
		}
		return true;
	}

	private void toggleLight(final Location tile, int lightIndex) {
		lights[lightIndex] = !lights[lightIndex];
		final WorldObject lightObject = new WorldObject(LIGHT_ACTIVE_ID, 10, 1, tile);
		if (!lights[lightIndex]) {
			World.removeObject(lightObject);
		} else {
			World.spawnObject(lightObject);
		}

	}

	@Override public void process() {
		if (EncounterStage.STARTED.equals(stage)) {
			if (--scarabTicks <= 0) {
				scarabTicks = 10;
				final int minScarabs = (teamSize + 1) / 2;
				int southSpawned = 0;
				int northSpawned = 0;
				for (Scarab scarab : scarabs) {
					if (scarab.isSouthernSpawned()) {
						southSpawned++;
					} else {
						northSpawned++;
					}
				}
				if (northSpawned < minScarabs) {
					spawnScarab(false);
				}
				if (southSpawned < minScarabs) {
					spawnScarab(true);
				}
			}
			if (!hasCompleted(ScabarasPuzzleType.PILLAR)) {
				if (obeliskIndex < 5 && pillarActionTicks > 0 && --pillarActionTicks == 0) {
					for (int i = 0; i < 5; i++) {
						obelisks[i].resetObelisk();
					}
					obeliskIndex = 0;
				}
			}
			if (!hasCompleted(ScabarasPuzzleType.MEMORY) && sequenceDisplayIndex != -1 && sequence != null && sequence.length >= 5) {
				final Location baseLocation = getLocation(BASE_PUZZLE_LOCATIONS[getPuzzleIndex(ScabarasPuzzleType.MEMORY)]);
				World.spawnTemporaryObject(new WorldObject(MEMORY_YELLOW_ID, 10, 0,
						baseLocation.transform(MEMORY_OFFSETS[sequence[sequenceDisplayIndex]][0], MEMORY_OFFSETS[sequence[sequenceDisplayIndex]][1])), 1);
				World.sendSoundEffect(baseLocation.transform(-1, 4), PRESSURE_SOUND);
				if (++sequenceDisplayIndex >= 5) {
					sequenceDisplayIndex = -1;
				}
			}
		}
	}

	private void spawnScarab(boolean south) {
		final Location spawnLocation = getLocation(SCARAB_SPAWN_LOCATIONS[south ? 0 : 2]);
		final Scarab scarab = new Scarab(spawnLocation, this, south);
		scarabs.add(scarab);
		scarab.spawn();
	}


	public boolean validateObelisk(Location location) {
		final int[] offsets = getOffsets(location, ScabarasPuzzleType.PILLAR);
		if (obeliskIndex > 4) {
			return true;
		}
		if (offsets[0] == OBELISK_OFFSETS[obeliskSet[obeliskIndex]][0] && offsets[1] == OBELISK_OFFSETS[obeliskSet[obeliskIndex]][1]) {
			obeliskIndex++;
			pillarActionTicks = 5;
			return true;
		}
		obeliskIndex = 0;
		for (int i = 0; i < 5; i++) {
			obelisks[i].resetObelisk();
		}
		if (pillarRockfallTick < WorldThread.getCurrentCycle()) {
			pillarRockfallTick = WorldThread.getCurrentCycle() + 5;
			final int index = getPuzzleIndex(ScabarasPuzzleType.PILLAR);
			final Location baseLocation = getLocation(BASE_PUZZLE_LOCATIONS[index]);
			final Location gateLocation = getLocation(index < 2 ? NORTH_FIRE_LOCATIONS[index] : SOUTH_FIRE_LOCATIONS[index - 2]);
			final List<Location> possibleTiles = new ArrayList<>();
			for (int x = baseLocation.getX() - 6; x < gateLocation.getX(); x++) {
				for (int y = baseLocation.getY(); y <= baseLocation.getY() + 6; y++) {
					final Location loc = new Location(x, y);
					if (World.getObjectWithType(loc, 10) == null && World.isFloorFree(loc, 1)) {
						possibleTiles.add(loc);
					}
				}
			}
			Collections.shuffle(possibleTiles);
			final List<Location> tiles = new ArrayList<>(possibleTiles.subList(0, Math.min(possibleTiles.size(), 7)));
			final Player[] roomPlayers = getRoomPlayers(ScabarasPuzzleType.PILLAR);
			for (Player p : roomPlayers) {
				if (!tiles.contains(p.getLocation())) {
					tiles.add(new Location(p.getLocation()));
				}
				p.getPacketDispatcher().resetCamera();
				p.getPacketDispatcher().sendCameraShake(CameraShakeType.LEFT_AND_RIGHT, 6, 0, 0);
				p.getPacketDispatcher().sendCameraShake(CameraShakeType.UP_AND_DOWN, 5, 0, 0);
				p.getPacketDispatcher().sendCameraShake(CameraShakeType.FRONT_AND_BACK, 6, 0, 0);
				p.sendSound(RUMBLING_SOUND);
			}
			final Location soundLocation = baseLocation.transform(ROCKFALL_SOUND_OFFSETS[0], ROCKFALL_SOUND_OFFSETS[1]);
			World.sendSoundEffect(soundLocation, ROCK_FALL_SOUND);
			World.sendSoundEffect(soundLocation, FAILURE_SOUND_START);
			World.sendSoundEffect(soundLocation, FAILURE_SOUND_IMPACT);
			tiles.forEach(tile -> World.sendGraphics(ROCK_FALL_GFX, tile));
			WorldTasksManager.schedule(() -> {
				Arrays.stream(roomPlayers).forEach(p -> p.getPacketDispatcher().resetCamera());
			}, 2);
			WorldTasksManager.schedule(addRunningTask(() -> {
				final Player[] allPlayers = getChallengePlayers();
				tiles.forEach(tile -> {
					for (Player p : allPlayers) {
						if (tile.equals(p.getLocation()) && !p.isDying()) {
							final int base = (int) Math.floor(7 * party.getDamageMultiplier());
							p.applyHit(new Hit(p, Utils.random(base, base + 6), HitType.DEFAULT));
						}
					}
				});
			}), 5);
		}
		return false;
	}

	private boolean handleSumMovement(Player player, Location newLocation) {
		if (hasCompleted(ScabarasPuzzleType.SUM)) {
			return false;
		}
		final Location baseSumLocation = getLocation(BASE_PUZZLE_LOCATIONS[getPuzzleIndex(ScabarasPuzzleType.SUM)]);
		for (int index = 0; index < SUM_OFFSETS.length; index++) {
			for (int[] offsets : SUM_OFFSETS[index]) {
				final Location currentLoc = baseSumLocation.transform(offsets[0], offsets[1]);
				if (currentLoc.equals(newLocation)) {
					if (canUse(player, ScabarasPuzzleType.SUM, null)) {
						currentSum += index + 1;
						if (currentSum == sumGoal) {
							player.sendSound(PRESSURE_SOUND);
							complete(ScabarasPuzzleType.SUM);
							toggleAllSumLights(baseSumLocation, true);
						} else if (currentSum > sumGoal) {
							toggleAllSumLights(baseSumLocation, false);
							sendSmallFallingRocks(player);
							currentSum = 0;
						} else {
							World.spawnObject(new WorldObject(SUM_PRESSURE_LIGHT_IDS[index], 10, 1, currentLoc));
							player.sendSound(PRESSURE_SOUND);
						}
					}
					return true;
				}
			}
		}
		return false;
	}

	public void handleMatchPlate(Player player, WorldObject object) {
		for (int locIndex = 0; locIndex < MATCH_BASE_LOCATIONS.length; locIndex++) {
			for (int matchIndex = 0; matchIndex < MATCH_OFFSETS.length; matchIndex++) {
				final Location location = getLocation(MATCH_BASE_LOCATIONS[locIndex]).transform(MATCH_OFFSETS[matchIndex][0], MATCH_OFFSETS[matchIndex][1]);
				if (location.equals(object.getLocation())) {
					toggleMatchPlate(locIndex == 1, matchIndex, player, false);
					return;
				}
			}
		}
	}

	private void toggleMatchPlate(boolean north, int matchIndex, Player player, boolean initial) {
		final int key = north ? matchNorthList.get(matchIndex) : matchSouthList.get(matchIndex);
		final Location baseLocation = getLocation(MATCH_BASE_LOCATIONS[north ? 1 : 0]);
		World.spawnObject(new WorldObject(SUM_PRESSURE_PLATE_ID + key, 22, 1, baseLocation.transform(MATCH_OFFSETS[matchIndex][0], MATCH_OFFSETS[matchIndex][1])));
		if (north) {
			if (northPlate != -1) {
				final int northOffsetIndex = matchNorthList.indexOf(northPlate);
				World.spawnObject(new WorldObject(MATCH_PRESSURE_ID, 22, 1, baseLocation.transform(MATCH_OFFSETS[northOffsetIndex][0], MATCH_OFFSETS[northOffsetIndex][1])));
			}
			northPlate = key;
		} else {
			if (southPlate != -1) {
				final int southOffsetIndex = matchSouthList.indexOf(southPlate);
				World.spawnObject(new WorldObject(MATCH_PRESSURE_ID, 22, 1, baseLocation.transform(MATCH_OFFSETS[southOffsetIndex][0], MATCH_OFFSETS[southOffsetIndex][1])));
			}
			southPlate = key;
		}
		if (player != null) {
			player.sendSound(TILE_ACTIVATE_SOUND);
		}
		if (northPlate != -1 && southPlate != -1) {
			final int northMatch = northPlate;
			final int southMatch = southPlate;
			final int northOffsetIndex = matchNorthList.indexOf(northMatch);
			final int southOffsetIndex = matchSouthList.indexOf(southMatch);
			final Location baseNorthLocation = getLocation(MATCH_BASE_LOCATIONS[1]);
			final Location baseSouthLocation = getLocation(MATCH_BASE_LOCATIONS[0]);
			final Runnable task = () -> {
				if (northMatch == southMatch) {
					if (++completedMatches >= MATCH_OFFSETS.length) {
						for (Player p : getChallengePlayers()) {
							p.sendMessage("<col=06600c>Puzzle 5 has been completed!");
						}
						completeRoom();
					}
					if (player != null) {
						player.sendSound(PRESSURE_SOUND);
					}
					World.spawnObject(new WorldObject(SUM_PRESSURE_LIGHT_IDS[northMatch], 10, 1, baseNorthLocation.transform(MATCH_OFFSETS[northOffsetIndex][0], MATCH_OFFSETS[northOffsetIndex][1])));
					World.spawnObject(new WorldObject(SUM_PRESSURE_LIGHT_IDS[southMatch], 10, 1, baseSouthLocation.transform(MATCH_OFFSETS[southOffsetIndex][0], MATCH_OFFSETS[southOffsetIndex][1])));
				} else {
					if (player != null) {
						player.sendSound(MATCH_FAILURE_SOUND);
					}
					World.spawnObject(new WorldObject(MATCH_PRESSURE_ID, 22, 1, baseNorthLocation.transform(MATCH_OFFSETS[northOffsetIndex][0], MATCH_OFFSETS[northOffsetIndex][1])));
					World.spawnObject(new WorldObject(MATCH_PRESSURE_ID, 22, 1, baseSouthLocation.transform(MATCH_OFFSETS[southOffsetIndex][0], MATCH_OFFSETS[southOffsetIndex][1])));
				}
				northPlate = -1;
				southPlate = -1;
			};
			if (!initial) {
				WorldTasksManager.schedule(addRunningTask(task::run), 0);
			} else {
				task.run();
			}
		}
	}

	private void toggleAllSumLights(final Location baseLocation, boolean on) {
		for (int index = 0; index < SUM_OFFSETS.length; index++) {
			for (int[] offsets : SUM_OFFSETS[index]) {
				final Location currentLoc = baseLocation.transform(offsets[0], offsets[1]);
				if (on) {
					World.spawnObject(new WorldObject(SUM_PRESSURE_LIGHT_IDS[index], 10, 1, currentLoc));
				} else {
					World.removeObject(new WorldObject(SUM_PRESSURE_LIGHT_IDS[index], 10, 1, currentLoc));
				}
			}
		}
	}

	public void flipLightTablet(final Player player, final Location plateLocation) {
		player.lock(5);
		player.sendMessage("You attempt to get a grip of the tile in order to flip it.");
		player.setAnimation(FLIP_ANIM);
		player.sendSound(FLIP_SOUND);
		WorldTasksManager.schedule(() -> {
			if (!player.isDying() && !player.isFinished() && insideChallengeArea(player)) {
				if (handleLightMovement(player, plateLocation, true)) {
					player.sendMessage("You manage to flip the tile over.");
					player.applyHit(new Hit(player, 20, HitType.DEFAULT));
					player.setAnimation(FLIP_FINAL_ANIM);
				} else {
					player.sendMessage("You fail to flip the tile over.");
				}
			}
		}, 4);
	}

	private boolean handleLightMovement(final Player player, Location newLocation, boolean flip) {
		if (hasCompleted(ScabarasPuzzleType.LIGHT)) {
			return false;
		}
		final Location baseSumLocation = getLocation(BASE_PUZZLE_LOCATIONS[getPuzzleIndex(ScabarasPuzzleType.LIGHT)]);
		for (int i = 0; i < LIGHT_OFFSETS.length; i++) {
			final Location tile = baseSumLocation.transform(LIGHT_OFFSETS[i][0], LIGHT_OFFSETS[i][1]);
			if (tile.equals(newLocation)) {
				if (canUse(player, ScabarasPuzzleType.LIGHT, null)) {
					player.sendSound(PRESSURE_SOUND);
					toggleLight(tile, i);
					if (!flip) {
						final int secondLightIndex = (i + lights.length - 1) % lights.length;
						toggleLight(baseSumLocation.transform(LIGHT_OFFSETS[secondLightIndex][0], LIGHT_OFFSETS[secondLightIndex][1]), secondLightIndex);
						final int thirdLightIndex = (i + 1) % lights.length;
						toggleLight(baseSumLocation.transform(LIGHT_OFFSETS[thirdLightIndex][0], LIGHT_OFFSETS[thirdLightIndex][1]), thirdLightIndex);
					}
					for (int indx = 0; indx < lights.length; indx++) {
						if (!lights[indx]) {
							return true;
						}
					}
					complete(ScabarasPuzzleType.LIGHT);
				}
				return true;
			}
		}
		return false;
	}

	private void sendSmallFallingRocks(Player player) {
		player.applyHit(new Hit(player, (int) (party.getDamageMultiplier() * 6F), HitType.DEFAULT));
		player.sendSound(SUM_FAILURE_SOUND);
		player.getPacketDispatcher().resetCamera();
		player.getPacketDispatcher().sendCameraShake(CameraShakeType.LEFT_AND_RIGHT, 5, 0, 0);
		player.getPacketDispatcher().sendCameraShake(CameraShakeType.UP_AND_DOWN, 5, 0, 0);
		player.getPacketDispatcher().sendCameraShake(CameraShakeType.FRONT_AND_BACK, 7, 0, 0);
		WorldTasksManager.schedule(() -> player.getPacketDispatcher().resetCamera(), 2);
		World.sendGraphics(SMALL_ROCKS_GFX, player.getLocation());
	}

	public boolean handleMemoryMovement(Player player, Location newLocation) {
		if(hasCompleted(ScabarasPuzzleType.MEMORY)) {
			return false;
		}
		if (sequence == null) {
			return false;
		}
		if (!canUse(player, ScabarasPuzzleType.MEMORY, null)) {
			return false;
		}
		final Location baseLocation = getLocation(BASE_PUZZLE_LOCATIONS[getPuzzleIndex(ScabarasPuzzleType.MEMORY)]);
		for (int i = 0; i < MEMORY_OFFSETS.length; i++) {
			final Location location = baseLocation.transform(MEMORY_OFFSETS[i][0], MEMORY_OFFSETS[i][1]);
			if (newLocation.equals(location)) {
				final WorldObject object = new WorldObject(MEMORY_RED_ID, 10, 0, location);
				World.spawnObject(object);
				WorldTasksManager.schedule(() -> {
					if (!hasCompleted(ScabarasPuzzleType.MEMORY)) {
						World.removeObject(object);
					}
				});
				if (sequence[sequenceIndex] == i) {
					if (++sequenceIndex == 5) {
						complete(ScabarasPuzzleType.MEMORY);
						for (int[] offsets : MEMORY_OFFSETS) {
							World.spawnObject(new WorldObject(MEMORY_YELLOW_ID, 10, 0, baseLocation.transform(offsets[0], offsets[1])));
						}
					}
				} else {
					player.sendSound(MEMORY_FAILURE_SOUND);
					sendSmallFallingRocks(player);
					resetSequence();
				}
				return true;
			}
		}
		return false;
	}

	public void pressAncientButton(Player player) {
		if (!canUse(player, ScabarasPuzzleType.MEMORY, null)) {
			return;
		}
		if (hasCompleted(ScabarasPuzzleType.MEMORY)) {
			player.sendMessage("You have already completed this puzzle.");
			return;
		}
		if (sequenceDisplayIndex != -1) {
			player.sendMessage("You must wait until the sequence has finished before you can request another.");
			return;
		}
		resetSequence();
		player.setAnimation(BUTTON_ACTIVE_ANIM);
		player.sendSound(BUTTON_ACTIVE_SOUND);
		final List<Integer> sequenceList = new ArrayList<>();
		for (int i = 0; i < MEMORY_OFFSETS.length; i++) {
			sequenceList.add(i);
		}
		Collections.shuffle(sequenceList);
		sequence = new int[5];
		for (int i = 0; i < 5; i++) {
			sequence[i] = sequenceList.get(i);
		}
		sequenceDisplayIndex = 0;
	}

	private void resetSequence() {
		sequence = null;
		sequenceIndex = 0;
		sequenceDisplayIndex = -1;
	}

	private int[] getOffsets(Location location, ScabarasPuzzleType type) {
		final Location baseLocation = getLocation(BASE_PUZZLE_LOCATIONS[getPuzzleIndex(type)]);
		return new int[] {location.getX() - baseLocation.getX(), location.getY() - baseLocation.getY()};
	}

	private Player[] getRoomPlayers(ScabarasPuzzleType type) {
		return getRoomPlayers(getPuzzleIndex(type));
	}

	private Player[] getRoomPlayers(int index) {
		final Location baseLocation = getLocation(BASE_PUZZLE_LOCATIONS[index]);
		final Location gateLocation = getLocation(index < 2 ? NORTH_FIRE_LOCATIONS[index] : SOUTH_FIRE_LOCATIONS[index - 2]);
		return players.stream().filter(p -> p != null && !p.isDying() && insideChallengeArea(p) &&
				p.getX() >= baseLocation.getX() - 4 && p.getX() < gateLocation.getX() && p.getY() >= baseLocation.getY() && p.getY() <= baseLocation.getY() + 6).toArray(Player[]::new);
	}

	public void removeScarab(Scarab scarab) {
		scarabs.remove(scarab);
	}

	public int getObeliskIndex() { return obeliskIndex; }

	public int getSumGoal() { return sumGoal; }

	@Override public boolean processMovement(Player player, int x, int y) {
		final Location newLocation = new Location(x, y);
		if (handleSumMovement(player, newLocation)) {
			return true;
		}
		if (handleLightMovement(player, newLocation, false)) {
			return true;
		}
		handleMemoryMovement(player, newLocation);
		return true;
	}
}