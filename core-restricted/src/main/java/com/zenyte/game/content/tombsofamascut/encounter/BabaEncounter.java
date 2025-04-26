package com.zenyte.game.content.tombsofamascut.encounter;

import com.near_reality.game.world.entity.TargetSwitchCause;
import com.zenyte.game.content.tombsofamascut.InvocationType;
import com.zenyte.game.content.tombsofamascut.npc.Baba;
import com.zenyte.game.content.tombsofamascut.npc.RollingBoulder;
import com.zenyte.game.content.tombsofamascut.npc.TOANPC;
import com.zenyte.game.content.tombsofamascut.raid.*;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.CollisionUtil;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.*;
import com.zenyte.game.world.entity.masks.*;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin;
import com.zenyte.game.world.region.area.plugins.PartialMovementPlugin;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;

import java.util.*;

/**
 * @author Savions.
 */
public class BabaEncounter extends TOARaidArea implements CycleProcessPlugin, PartialMovementPlugin {

	public static final String APMEKEN_BABA_DEATH_ATTRIBUTE = "apmeken_baba_pit_death";
	public static final int PIT_WALL_X_COORD = 3798;
	private static final int PIT_MIN_Y_COORD = 5406;
	private static final int SARCOPHAGUS_NPC_ID = 11788;
	private static final int RUBBLE_BASE_NPC_ID = 11784;
	private static final int RUBBLE_OBJECT_ID = 42838;
	private static final int RUBBLE_LANDING_SOUND_ID = 6014;
	private static final int RUBBLE_FALLING_BASE_GFX_ID = 2250;
	private static final int SARCOPHAGUS_OBJECT_ID = 45752;
	private static final int BABANA_PEEL_OBJECT_ID = 45755;
	private static final Animation PLAYER_SLIP_ANIM = new Animation(4030, 20);
	private static final Animation PLAYER_MOVE_ANIM = new Animation(1114);
	private static final Animation COLLISION_ANIM = new Animation(7210, 20);
	private static final Location[] SARCOPHAGUS_LOCATIONS = {new Location(3803, 5416), new Location(3808, 5416), new Location(3813, 5416), new Location(3818, 5416),
			new Location(3803, 5400), new Location(3808, 5400), new Location(3813, 5400), new Location(3818, 5400)};
	private static final Location[][] RUBBLE_LOCATIONS = {
			{new Location(3798, 5413), new Location(3798, 5407), new Location(3798, 5401), new Location(3803, 5401), new Location(3803, 5413), new Location(3805, 5407)},
			{new Location(3816, 5413), new Location(3812, 5413), new Location(3812, 5406), new Location(3812, 5401), new Location(3817, 5401), new Location(3810, 5405)}
	};
	private static final Location BABOON_SPAWN_LOCATION = new Location(3811, 5408);
	private static final SoundEffect[] START_SOUNDS = {new SoundEffect(5943, 0, 9), new SoundEffect(6007, 0, 11), new SoundEffect(6027, 0, 31),
			new SoundEffect(5947, 0, 70), new SoundEffect(6008, 0, 102), new SoundEffect(6025, 0, 109)};
	private static final SoundEffect RUBBLE_FALLING_SOUND = new SoundEffect(5949);
	private static final SoundEffect ROCK_IMPACT_SOUND = new SoundEffect(5987);
	private static final SoundEffect SARCO_PROJECTILE_IMPACT_SOUND = new SoundEffect(156, 4, 5);
	private static final SoundEffect SLIP_SOUND = new SoundEffect(2727);
	private static final SoundEffect COLLISION_SOUND = new SoundEffect(6033);
	private static final Graphics ROCK_IMPACT_GFX = new Graphics(1463, 0, 50);
	private static final Graphics SARCO_ACTIVATE_GFX = new Graphics(128, 0, 150);
	private static final Graphics SARCO_PROJECTILE_IMPACT_GFX = new Graphics(2265, 5, 0);
	private static final Graphics SLIP_GFX = new Graphics(1575, 0, 124);
	public static final Graphics PLAYER_STUN_GFX = new Graphics(245, 0, 124);
	private final boolean gottaHaveFaith;
	private final boolean mindTheGap;
	private final boolean boulderDash;
	private final boolean jungleGapes;
	private final int level;
	private Baba baba;
	private final SarcoPhagusNpc[] sarcoPhagusNpcs = new SarcoPhagusNpc[SARCOPHAGUS_LOCATIONS.length];
	private final List<Location> rockFallingLocations = new ArrayList<>();
	private final Location[] nextRubbleLocations = new Location[2];
	private final RubbleNpc[] rubbleNpcs = new RubbleNpc[2];
	private final BabaBaboon[] baboons = new BabaBaboon[2];
	private int rubbleFallingTicks;
	private List<RollingBoulder> rollingBoulders = new ArrayList<>();
	private List<WorldObject> bananaPeels = new ArrayList<>();

	public BabaEncounter(AllocatedArea allocatedArea, int copiedChunkX, int copiedChunkY, TOARaidParty party, EncounterType encounterType) {
		super(allocatedArea, copiedChunkX, copiedChunkY, party, encounterType);
		gottaHaveFaith = party.getPartySettings().isActive(InvocationType.GOTTA_HAVE_FAITH);
		mindTheGap = party.getPartySettings().isActive(InvocationType.MIND_THE_GAP);
		boulderDash = party.getPartySettings().isActive(InvocationType.BOULDERDASH);
		jungleGapes = party.getPartySettings().isActive(InvocationType.JUNGLE_JAPES);
		level = party.getBossLevels()[TOAPathType.APMEKEN.ordinal()];
	}

	@Override public void enter(Player player) {
		super.enter(player);
		for (int animId = 9737; animId <= 9756; animId++) {
			player.getPacketDispatcher().sendClientScript(1846, animId);
		}
		player.getPacketDispatcher().sendClientScript(1846, 9535);
		player.getPacketDispatcher().sendClientScript(1846, 9536);
		player.getPacketDispatcher().sendClientScript(1846, 9547);
		player.getPacketDispatcher().sendClientScript(1846, 9541);
	}

	@Override public void onRoomStart() {
		spawnBaba();
		baba.setMaxHealth();
		resetSarco();
		for (Player p : players) {
			if (p != null) {
				for (SoundEffect soundEffect : START_SOUNDS) {
					p.sendSound(soundEffect);
				}
			}
		}
		players.forEach(p -> {
			p.getHpHud().open(baba.getId(), baba.getMaxHitpoints());
		});
	}

	@Override public void onRoomEnd() {
		players.forEach(p -> p.getHpHud().close());
		party.getPathsCompleted().add(TOAPathType.APMEKEN);
		resetRocks();
		resetBaboons();
		resetRollingBoulders();
		removeBananaPeels();
		spawnTeleportNPC();
	}

	@Override public void onRoomReset() {
		players.forEach(p -> p.getHpHud().close());
		if (baba != null) {
			baba.finish();
		}
		rockFallingLocations.clear();
		resetRocks();
		resetSarco();
		resetBaboons();
		removeBananaPeels();
		resetRollingBoulders();
	}

	@Override public void process() {
		if (EncounterStage.STARTED.equals(stage) && baba != null && !baba.isDead() && !baba.isFinished()) {
			if (rubbleFallingTicks > 0 && --rubbleFallingTicks <= 0 && !baba.isPerformingBoulderSpecial()) {
				final Player[] players = getChallengePlayers();
				final int baseHit = baba.getMaxHit(19);
				for (Location location : rockFallingLocations) {
					for (Player p : players) {
						if (location.equals(p.getLocation())) {
							p.applyHit(new Hit(baba, baseHit + Utils.random(5), HitType.DEFAULT));
						}
					}
				}
				rockFallingLocations.clear();
				final int rubbleMaxHealth = 10 + (((players.length - 1) / 2) * 10);
				final List<RubbleNpc> addedRubbles = new ArrayList<>();
				for (int i = 0; i < nextRubbleLocations.length; i++) {
					if (nextRubbleLocations[i] != null) {
						rubbleNpcs[i] = new RubbleNpc(nextRubbleLocations[i], this);
						rubbleNpcs[i].getCombatDefinitions().setHitpoints(rubbleMaxHealth);
						rubbleNpcs[i].setHitpoints(rubbleMaxHealth);
						World.spawnNPC(rubbleNpcs[i]);
						addedRubbles.add(rubbleNpcs[i]);
						nextRubbleLocations[i] = null;
						for (BabaBaboon baboon : baboons) {
							if (baboon != null && !baboon.isDead() && !baboon.isFinished()
									&& CollisionUtil.collides(baboon.getX(), baboon.getY(), baboon.getSize(), rubbleNpcs[i].getX(), rubbleNpcs[i].getY(), rubbleNpcs[i].getSize())) {
								baboon.applyHit(new Hit(baba, baboon.getMaxHitpoints(), HitType.DEFAULT));
							}
						}
					}
				}
				if (addedRubbles.size() > 1 && players.length > 2 && (players.length % 2) == 1) {
					Collections.shuffle(addedRubbles);
					addedRubbles.get(0).setHitpoints(rubbleMaxHealth / 2);
				}
				playerLoop : for (Player p : players) {
					for (RubbleNpc npc : rubbleNpcs) {
						if (npc != null && CollisionUtil.collides(npc.getX(), npc.getY(), npc.getSize(), p.getX(), p.getY(), p.getSize())) {
							for (int radius = 1; radius < 4; radius++) {
								for (int x = -radius; x <= radius; x++) {
									for (int y = -radius; y <= radius; y++) {
										if (Math.abs(x) == radius || Math.abs(y) == radius) {
											final Location location = p.getLocation().transform(x, y);
											if (World.isFloorFree(location, 1) && !CollisionUtil.collides(npc.getX(), npc.getY(), npc.getSize(), location.getX(), location.getY(), p.getSize())) {
												p.stopAll();
												p.setForceTalk("Ouch!");
												p.lock(1);
												p.setAnimation(PLAYER_MOVE_ANIM);
												p.autoForceMovement(p.getLocation(), location, 0, 29);
												continue playerLoop;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	public void dropRubbles() {
		final int speed = Math.min(2, party.getBossLevels()[TOAPathType.APMEKEN.ordinal()] / 2);
		final Player[] players = getChallengePlayers();
		rubbleFallingTicks = 6 - (speed * 2);
		final Graphics gfx = new Graphics(RUBBLE_FALLING_BASE_GFX_ID + speed, 20, 0);
		for (Player p : players) {
			final Location loc = new Location(p.getLocation());
			if (!rockFallingLocations.contains(loc)) {
				rockFallingLocations.add(loc);
			}
			p.sendSound(RUBBLE_FALLING_SOUND);
			p.sendSound(new SoundEffect(RUBBLE_LANDING_SOUND_ID, 0, 150 - (speed * 60)));
		}
		for (int i = 0; i < rubbleNpcs.length; i++) {
			if (rubbleNpcs[i] == null || rubbleNpcs[i].isDying() || rubbleNpcs[i].isFinished()) {
				final List<Location> possibleLocations = new ArrayList<>(Arrays.asList(RUBBLE_LOCATIONS[i]));
				Collections.shuffle(possibleLocations);
				for (int index = 0; index < possibleLocations.size(); index++) {
					final Location location = getLocation(possibleLocations.get(index));
					if (!World.isFloorFree(location, 3) && !CollisionUtil.collides(location.getX(), location.getY(), 3, baba.getX(), baba.getY(), baba.getSize())) {
						continue;
					}
					if (!rockFallingLocations.contains(location)) {
						rockFallingLocations.add(location);
					}
					nextRubbleLocations[i] = location;
					World.sendGraphics(gfx, location);
					break;
				}
			}
		}
		for (Location loc : rockFallingLocations) {
			World.sendGraphics(gfx, loc);
		}
	}

	private void spawnBaba() {
		if (baba != null) {
			baba.finish();
		}
		baba = new Baba(this, party.getBossLevels()[TOAPathType.APMEKEN.ordinal()]);
		baba.spawn();
	}

	public void resetSarco() {
		for (int i = 0; i < sarcoPhagusNpcs.length; i++) {
			final Location loc = getLocation(SARCOPHAGUS_LOCATIONS[i]);
			if (sarcoPhagusNpcs[i] == null) {
				sarcoPhagusNpcs[i] = new SarcoPhagusNpc(getLocation(SARCOPHAGUS_LOCATIONS[i]), i, this);
				sarcoPhagusNpcs[i].spawn();
			} else {
				sarcoPhagusNpcs[i].reset();
			}
			World.spawnObject(new WorldObject(SARCOPHAGUS_OBJECT_ID, 10, i < (SARCOPHAGUS_LOCATIONS.length / 2) ? 1 : 3, loc));
		}
	}

	private void resetRocks() {
		rockFallingLocations.clear();
		rubbleFallingTicks = 0;
		finishRubbles();
	}

	public void resetAndKillRockFall() {
		rockFallingLocations.clear();
		rubbleFallingTicks = 0;
		for (int i = 0; i < rubbleNpcs.length; i++) {
			if (rubbleNpcs[i] != null && !rubbleNpcs[i].isDead() && !rubbleNpcs[i].isFinished()) {
				rubbleNpcs[i].applyHit(new Hit(rubbleNpcs[i], rubbleNpcs[i].getMaxHitpoints(), HitType.DEFAULT));
			}
		}
	}

	private void finishRubbles() {
		for (int i = 0; i < rubbleNpcs.length; i++) {
			if (rubbleNpcs[i] != null && !rubbleNpcs[i].isDead() && !rubbleNpcs[i].isFinished()) {
				rubbleNpcs[i].finish();
			}
			rubbleNpcs[i] = null;
		}
	}

	private void resetBaboons() {
		for (int i = 0; i < baboons.length; i++) {
			if (baboons[i] != null && !baboons[i].isDead() && !baboons[i].isFinished()) {
				baboons[i].finish();
			}
			baboons[i] = null;
		}
	}

	private void removeBananaPeels() {
		bananaPeels.removeIf(peel -> {
			World.removeObject(peel);
			return true;
		});
	}

	private void resetRollingBoulders() {
		rollingBoulders.removeIf(boulder -> {
			if (boulder != null && !boulder.isDead() && !boulder.isFinished()) {
				boulder.finish();
			}
			return true;
		});
	}

	public void spawnRollingBoulder(final Location location, boolean cracked) {
		final RollingBoulder rollingBoulder = new RollingBoulder(location, this, level, cracked);
		rollingBoulders.add(rollingBoulder);
		World.spawnNPC(rollingBoulder);
	}

	public void landRocks(Player[] players) {
		if(baba.isPerformingBoulderSpecial())
			return;
		final int baseDamage = baba.getMaxHit(39);
		playerLoop : for (Player p : players) {
			p.setGraphics(ROCK_IMPACT_GFX);
			p.sendSound(ROCK_IMPACT_SOUND);

			for (RubbleNpc npc : rubbleNpcs) {
				if (npc != null && !npc.isDying() && !npc.isFinished() && Utils.isOnRangeExcludingDiagonal(npc.getX(), npc.getY(), npc.getSize(), p.getX(), p.getY(), p.getSize(), 1)
						&& npc.flagBoulder()) {
					p.applyHit(new Hit(baba, (int) (baseDamage * .05F), HitType.DEFAULT));
					continue playerLoop;
				}
			}

			for (SarcoPhagusNpc npc : sarcoPhagusNpcs) {
				if (npc != null && !npc.isDying() && !npc.isFinished() && Utils.isOnRangeExcludingDiagonal(npc.getX(), npc.getY(), npc.getSize(), p.getX(), p.getY(), p.getSize(), 1)
						&& npc.flagBoulder()) {
					p.applyHit(new Hit(baba, (int) (baseDamage * .55F), HitType.DEFAULT));
					continue playerLoop;
				}
			}

			for (BabaBaboon npc : baboons) {
				if (npc != null && !npc.isDying() && !npc.isFinished() && Utils.isOnRangeExcludingDiagonal(npc.getX(), npc.getY(), npc.getSize(), p.getX(), p.getY(), p.getSize(), 1)
						&& npc.flagBoulder()) {
					p.applyHit(new Hit(baba, (int) (baseDamage * .45F), HitType.DEFAULT));
					continue playerLoop;
				}
			}

			p.applyHit(new Hit(baba, baseDamage, HitType.DEFAULT));
		}

		for (RubbleNpc npc : rubbleNpcs) {
			if (npc != null && !npc.isDying() && !npc.isFinished()) {
				npc.registerDamage();
			}
		}
		for (SarcoPhagusNpc npc : sarcoPhagusNpcs) {
			if (npc != null && !npc.isDying() && !npc.isFinished()) {
				npc.registerDamage();
			}
		}
		for (BabaBaboon npc : baboons) {
			if (npc != null && !npc.isDying() && !npc.isFinished()) {
				npc.registerDamage();
			}
		}
	}

	public void openSarcophagus(int index) {
		final Location sarcoTile = getLocation(SARCOPHAGUS_LOCATIONS[index]);
		World.spawnObject(new WorldObject(SARCOPHAGUS_OBJECT_ID + 1, 10, index < (SARCOPHAGUS_LOCATIONS.length / 2) ? 1 : 3, sarcoTile));
		WorldTasksManager.schedule(addRunningTask(() -> {
			World.sendGraphics(SARCO_ACTIVATE_GFX, sarcoTile);
			final List<Location> tiles = new ArrayList<>();
			for (int x = -4; x <= 4; x++) {
				for (int y = -8; y <= -1; y++) {
					final Location tile = sarcoTile.transform(x, index > 3 ? -y : y);
					if (World.isFloorFree(tile, 1)) {
						tiles.add(tile);
					}
				}
			}
			Collections.shuffle(tiles);
			for (int i = 0; i < Math.min(tiles.size(), 3); i++) {
				final Location tile = tiles.get(i);
				World.sendProjectile(sarcoTile, tile, new Projectile(2246, 37, 0, 0, 44, 90, 0, 0));
				WorldTasksManager.schedule(addRunningTask(() -> {
					if (baba == null || baba.isDead() || baba.isFinished()) {
						return;
					}
					World.sendGraphics(SARCO_PROJECTILE_IMPACT_GFX, tile);
					World.sendSoundEffect(tile, SARCO_PROJECTILE_IMPACT_SOUND);
					for (Player p : getChallengePlayers()) {
						if (tile.equals(p.getLocation())) {
							int baseDamage = (int) Math.floor(8 * party.getDamageMultiplier());
							if (gottaHaveFaith) {
								final int prayerPointsDiff = Math.min(0, p.getSkills().getLevelForXp(SkillConstants.PRAYER) - p.getPrayerManager().getPrayerPoints());
								baseDamage += (prayerPointsDiff * 0.15F);
							}
							p.applyHit(new Hit(baba, baseDamage + Utils.random(1), HitType.DEFAULT));
						}
					}
				}), 2);
			}
		}), 0, 4);
	}

	public boolean spawnBaboons() {
		final List<Integer> availableBaboonIndices = new ArrayList<>();
		for (int i = 0; i < baboons.length; i++) {
			if (baboons[i] == null || baboons[i].isFinished()) {
				availableBaboonIndices.add(i);
			}
		}
		if (availableBaboonIndices.size() == 0) {
			return false;
		}
		final List<Location> possibleSpawnLocations = new ArrayList<>();
		final Location baseTile = getLocation(BABOON_SPAWN_LOCATION);
		for (int x = -5; x <= 3; x++) {
			for (int y = -4; y <= 4; y++) {
				final Location tile = baseTile.transform(x, y);
				if (World.isFloorFree(baseTile, 1) && !CollisionUtil.collides(baba.getX(), baba.getY(), baba.getSize(), tile.getX(), tile.getY(), 1)) {
					possibleSpawnLocations.add(tile);
				}
			}
		}
		Collections.shuffle(possibleSpawnLocations);
		for (int i : availableBaboonIndices) {
			baboons[i] = new BabaBaboon(possibleSpawnLocations.get(i), this);
			baboons[i].spawn();
		}
		return true;
	}

	public void checkRollingBoulderCollision(Location location) {
		for (Player p : getChallengePlayers()) {
			if (!p.isLocked() && !p.isDying() && !p.getAppearance().isTransformedIntoNpc()
					&& p.getX() >= location.getX() && p.getX() <= location.getX() + 1 && (p.getY() >= location.getY() && p.getY() <= location.getY() + 2)) {
				p.sendSound(COLLISION_SOUND);
				p.faceDirection(Direction.EAST);
				final int xCoord = p.getX() - (boulderDash ? 5 : 6);
				final int minX = getX(PIT_WALL_X_COORD);
				if (mindTheGap && xCoord < minX && intoGap(p.getY())) {
					p.setLocation(new Location(minX - 1, p.getY()));
					p.getTemporaryAttributes().put(APMEKEN_BABA_DEATH_ATTRIBUTE, Boolean.TRUE);
					p.applyHit(new Hit(baba, p.getMaxHitpoints(), HitType.DEFAULT).setExecuteIfLocked());
				} else {
					p.setAnimation(COLLISION_ANIM);
					p.lock(2);
					p.setLocation(new Location(Math.max(minX, xCoord), p.getY()));
					final int baseDamage = (int) (party.getDamageMultiplier() * 14);
					p.applyHit(new Hit(baba, baseDamage, HitType.DEFAULT).setExecuteIfLocked());
				}
			}
		}
		for (BabaBaboon baboon : baboons) {
			if (baboon != null && !baboon.isDying() && !baboon.isFinished()
					&& baboon.getX() == location.getX() && (baboon.getY() >= location.getY() && baboon.getY() <= location.getY() + 2)){
				baboon.applyHit(new Hit(baboon, baboon.getMaxHitpoints(), HitType.DEFAULT));
			}
		}
	}

	public boolean intoGap(final int yCoord) {
		final int yThreshold = getY(PIT_MIN_Y_COORD);
		return yCoord >= yThreshold && yCoord <= yThreshold + 4;
	}

	private void spawnBananas(Location location) {
		if (!World.isFloorFree(location, 1) || bananaPeels.stream().anyMatch(peel -> peel.getLocation().equals(location))) {
			return;
		}
		final WorldObject object = new WorldObject(BABANA_PEEL_OBJECT_ID, 10, Utils.random(3), location);
		bananaPeels.add(object);
		World.spawnObject(object);
	}

	public SarcoPhagusNpc[] getSarcoPhagusNpcs() { return sarcoPhagusNpcs; }

	@Override public boolean processMovement(Player player, int x, int y) {
		final Optional<WorldObject> bananaOptional = bananaPeels.stream().filter(peel -> peel.getX() == x && peel.getY() == y).findFirst();
		if (bananaOptional.isPresent()) {
			final WorldObject object = bananaOptional.get();
			player.applyHit(new Hit(baba, (int) (8 * party.getDamageMultiplier()), HitType.DEFAULT));
			player.getPacketDispatcher().sendGameMessage("You slip and fall on a banana skin!", true);
			player.lock(5);
			player.setAnimation(PLAYER_SLIP_ANIM);
			player.sendSound(SLIP_SOUND);
			player.setGraphics(SLIP_GFX);
			World.removeObject(object);
			bananaPeels.remove(object);
		}
		return true;
	}

	static class BoulderTankNpc extends TOANPC {

		private final int baseReduction;
		protected final BabaEncounter encounter;
		private int hitPointsReduction;

		public BoulderTankNpc(int id, Location tile, Direction facing, int radius, int baseReduction, BabaEncounter encounter) {
			super(id, tile, facing, radius, encounter, 0, false);
			this.baseReduction = baseReduction;
			this.encounter = encounter;
		}

		public boolean flagBoulder() {
			if (hitPointsReduction >= getHitpoints()) {
				return false;
			}
			hitPointsReduction += baseReduction;
			return true;
		}

		public void registerDamage() {
			if (hitPointsReduction > 0) {
				applyHit(new Hit(this, hitPointsReduction, HitType.DEFAULT));
				hitPointsReduction = 0;
			}
		}

		@Override public void setRespawnTask() {}

		@Override public float getPointMultiplier() { return 0; }

		@Override public boolean isCycleHealable() { return false; }
	}

	static class RubbleNpc extends BoulderTankNpc {

		private final WorldObject object;

		public RubbleNpc(Location tile, BabaEncounter encounter) {
			super(RUBBLE_BASE_NPC_ID, tile, Direction.SOUTH, 0, 10, encounter);
			object = new WorldObject(RUBBLE_OBJECT_ID, 10, 2, tile);
			World.spawnObject(object);
		}

		@Override public void processNPC() {
			super.processNPC();
			getHitBars().add(hitBar);
			getUpdateFlags().flag(UpdateFlag.HIT);
		}

		@Override public void finish() {
			super.finish();
			World.removeObject(object);
		}

		@Override public boolean isEntityClipped() { return true; }

		@Override public void setTarget(Entity target, TargetSwitchCause cause) {}

		@Override public void setFaceEntity(Entity entity) {}

		@Override public void setFacedInteractableEntity(InteractableEntity facedInteractableEntity) {}

		@Override public void setFaceLocation(Location tile) {}

		@Override
		public boolean addWalkStep(final int nextX, final int nextY, final int lastX, final int lastY, final boolean check) { return false; }
	}

	static class SarcoPhagusNpc extends BoulderTankNpc {

		private final int index;

		public SarcoPhagusNpc(Location tile, int index, BabaEncounter encounter) {
			super(SARCOPHAGUS_NPC_ID, tile, Direction.SOUTH, 0, 30, encounter);
			this.index = index;
		}

		@Override protected void onFinish(Entity source) {
			super.onFinish(source);
			encounter.openSarcophagus(index);
		}

		@Override public boolean isEntityClipped() { return false; }

		@Override public void setTarget(Entity target, TargetSwitchCause cause) {}

		@Override public void setFaceEntity(Entity entity) {}

		@Override public void setFacedInteractableEntity(InteractableEntity facedInteractableEntity) {}

		@Override public void setFaceLocation(Location tile) {}

		@Override
		public boolean addWalkStep(final int nextX, final int nextY, final int lastX, final int lastY, final boolean check) { return false; }
	}

	static class BabaBaboon extends BoulderTankNpc implements CombatScript {

		private static final Animation SPAWN_ANIM = new Animation(9753);
		private static final Animation RANGE_ANIMATION = new Animation(9745);
		private static final Animation MELEE_ANIMATION = new Animation(9742);
		private static final Projectile RANGE_PROJECTILE = new Projectile(2243, 36, 36, 67, 8, 34, 128, 0);
		private static final SoundEffect RANGE_SOUNDEFFECT = new SoundEffect(871, 5);
		private static final int ID = 11781;
		private int attacks = 3;
		private boolean switchTarget = false;

		public BabaBaboon(Location tile, BabaEncounter encounter) {
			super(ID, tile, Direction.SOUTH, 64, 12, encounter);
			getCombat().setCombatDelay(6);
			setRadius(64);
			setAnimation(SPAWN_ANIM);
			lock(2);
			setForceAggressive(true);
		}

		@Override public List<Entity> getPossibleTargets(EntityType type, int radius) {
			if (!possibleTargets.isEmpty()) {
				possibleTargets.clear();
			}
			final List<Entity> possibleTargets = new ArrayList<>();
			if (attacks <= 0) {
				int distance = Integer.MAX_VALUE;
				SarcoPhagusNpc t = null;
				for (SarcoPhagusNpc npc : encounter.getSarcoPhagusNpcs()) {
					if (npc != null && !npc.isDead() && !npc.isFinished()) {
						final int d = getLocation().getTileDistance(npc.getLocation());
						if (d < distance) {
							distance = d;
							t = npc;
						}
					}
				}
				if (t != null) {
					possibleTargets.add(t);
				}
			} else {
				final Player[] players = encounter.getChallengePlayers();
				if (players.length > 0) {
					Player target = players[players.length > 1 ? Utils.random(players.length - 1) : 0];
					if (target != null) {
						possibleTargets.add(target);
					}
				}
			}
			super.possibleTargets.addAll(possibleTargets);
			return possibleTargets;
		}

		@Override public void processNPC() {
			checkAggressivity();
			super.processNPC();
			final boolean attackingNpc = getCombat().getTarget() instanceof NPC;
			if (attackingNpc && (getCombat().getTarget() == null || getCombat().getTarget().isDying() || getCombat().getTarget().isFinished())) {
				switchTarget = true;
			}
			if (switchTarget) {
				switchTarget = false;
				getCombat().setTarget(null);
				if (attackingNpc) {
					attacks = 3;
				}
				getCombatDefinitions().setAttackStyle(attackingNpc ? AttackType.RANGED : AttackType.SLASH);
				setAttackDistance(attackingNpc ? 7 : 0);
			}
		}

		@Override protected void onFinish(Entity source) {
			super.onFinish(source);
			if (encounter.jungleGapes) {
				encounter.spawnBananas(getLocation());
			}
		}

		@Override public float getPointMultiplier() { return 1; }

		@Override public void autoRetaliate(Entity source) {}

		@Override public int attack(Entity target) {
			if (target instanceof final Player player) {
				setAnimation(RANGE_ANIMATION);
				final int ticks = RANGE_PROJECTILE.getTime(this, target);
				delayHit(ticks, target, new Hit(this, getRandomMaxHit(this, 12, AttackType.RANGED, target), HitType.RANGED));
				World.sendProjectile(this, target, RANGE_PROJECTILE);
				World.sendSoundEffect(getLocation(), RANGE_SOUNDEFFECT);
				player.sendSound(new SoundEffect(860, 35 + (ticks * 30)));
				if (attacks > 0 && --attacks <= 0) {
					switchTarget = true;
				}
			} else {
				setAnimation(MELEE_ANIMATION);
				delayHit(this, 0, target, new Hit(this, 20, HitType.MELEE));
			}
			return combatDefinitions.getAttackSpeed();
		}
	}
}
