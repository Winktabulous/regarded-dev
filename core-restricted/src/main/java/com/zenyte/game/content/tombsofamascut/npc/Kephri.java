package com.zenyte.game.content.tombsofamascut.npc;

import com.near_reality.game.world.entity.TargetSwitchCause;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.content.tombsofamascut.InvocationType;
import com.zenyte.game.content.tombsofamascut.encounter.KephriEncounter;
import com.zenyte.game.content.tombsofamascut.raid.EncounterStage;
import com.zenyte.game.model.CameraShakeType;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.*;
import com.zenyte.game.world.entity.masks.*;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NPCCombat;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

import java.util.*;

/**
 * @author Savions.
 */
public class Kephri extends TOANPC implements CombatScript {

	private static final int INVIS_BLOCK_ID = 32740;
	private static final int DUNG_GFX_ID = 2145;
	private static final int DUNG_SOUND_ID = 6480;
	private static final int NORMAL_EGG_NPC_ID = 11728;
	private static final int BROWN_EGG_NPC_ID = 11729;
	private static final int FINISHED_ID = 11722;
	private static final Location SPAWN_LOCATION = new Location(3549, 5406);
	private static final Graphics CONSISTENT_AWAKE_GFX = new Graphics(2141);
	private static final Graphics CONSISTENT_ASLEEP_GFX = new Graphics(2142);
	private static final Graphics TO_SLEEP_GFX = new Graphics(2143);
	private static final Graphics AWAKE_GFX = new Graphics(2144);
	private static final Graphics FLOOR_INCOMING_GFX = new Graphics(1447);
	private static final Graphics EGG_EXPLODE_GFX =new Graphics(2156);
	private static final Graphics FLY_EXPLODE_GFX = new Graphics(2148);
	private static final Graphics BOMB_EXPLODE_GFX = new Graphics(2157, 0, 38);
	private static final Graphics PLAYER_FLIES_GFX = new Graphics(2146);
	private static final Graphics PLAYER_STUN_GFX = new Graphics(245, 0, 124);
	private static final Graphics EGG_BREAK_GFX = new Graphics(2166);
	private static final SoundEffect THROW_SWING_SOUND = new SoundEffect(6435);
	private static final SoundEffect THROW_BOMB_EXPLODE_SOUND = new SoundEffect(5896, 12);
	private static final SoundEffect BOMB_EXPLODE_SOUND = new SoundEffect(6475, 6);
	private static final SoundEffect PLAYER_FLIES_SOUND = new SoundEffect(3544);
	private static final SoundEffect THROW_AREA_SOUND = new SoundEffect(6442, 9);
	private static final SoundEffect THROW_EGGS_SOUND = new SoundEffect(6483, 1, 70);
	private static final SoundEffect FLYBACK_SOUND = new SoundEffect(3201);
	private static final SoundEffect EGG_LAND_SOUND = new SoundEffect(6446, 7);
	private static final SoundEffect EGG_BREAK_SOUND = new SoundEffect(6479, 5);
	private static final SoundEffect EGG_EXPLODE_SOUND = new SoundEffect(6436, 5);
	private static final Animation THROW_SWING_ANIM = new Animation(9577);
	private static final Animation PLAYER_THROW_ANIMATION = new Animation(9799);
	private static final Animation THROW_EGGS_ANIMATION = new Animation(9578);
	private static final Animation SPAWN_HELPERS_ANIM = new Animation(9579);
	private static final Animation AWAKE_ANIM = new Animation(9581);
	private static final Animation EGG_SPAWN_ANIM = new Animation(8630);
	private static final Animation DEATH_ANIMATION = new Animation(9582);
	private static final Projectile THROW_FIRST_PROJECTILE = new Projectile(1481, 175, 250, 39, 50, 51, 0, 0);
	private static final Projectile THROW_SECOND_PROJECTILE = new Projectile(2266, 250, 9, 0, 50, 120, 0, 0);
	private static final Projectile BROWN_EGG_PROJECTILE = new Projectile(2164, 86, 0, 70, 14, 50, 96, 0);
	private static final Projectile NORMAl_EGG_PROJECTILE = new Projectile(2165, 86, 0, 70, 14, 50, 96, 0);
	private static final Projectile SCARAB_BOMB_PROJECTILE = new Projectile(2147, 112, 9, 0, 14, 120, 0, 0); //4 ticks
	private static final Location THROW_START_LOC = new Location(3550, 5409);
	private static final Location THROW_SECOND_LOC = new Location(3551, 5408);
	private static final Location[] SWARM_SPAWN_LOCATIONS = {new Location(3543, 5404), new Location(3543, 5410), new Location(3547, 5416), new Location(3553, 5416),
			new Location(3559, 5410), new Location(3559, 5404), new Location(3553, 5400), new Location(3547, 5400)};
	private static final Location[] EGG_LOCATIONS = {new Location(3551, 5404), new Location(3549, 5402), new Location(3545, 5406),
			new Location(3551, 5412), new Location(3549, 5414), new Location(3545, 5410), new Location(3547, 5408), new Location(3553, 5402),
			new Location(3557, 5406), new Location(3553, 5414), new Location(3555, 5408), new Location(3557, 5410)};
	private static final Location[] SCARAB_BOMB_LOCATIONS = {new Location(3542, 5417), new Location(3560, 5417), new Location(3560, 5398), new Location(3542, 5498)};
	private final KephriEncounter encounter;
	private final boolean aerialAssault;
	private final boolean blowingMud;
	private final boolean livelyLarvae;
	private final boolean moreOverlords;
	private final boolean medic;
	private List<TOANPC> bigScarabs = new ArrayList<>();
	private int shieldingTicks = 0;
	private int attackTicks = 6;
	private int phase;
	private boolean dungAttack = true;
	private int specialCycle = 3;
	private int playerIndex;
	private final int attackSpeed = 6;
	private int eggExplodeTicks = 0;
	private List<AgileScarab> agileScarabs = new ArrayList<>();
	private List<Egg> eggs = new ArrayList<>();
	private List<ScarabSwarm> swarms = new ArrayList<>();
	private int swarmIndex = Utils.random(SWARM_SPAWN_LOCATIONS.length - 1);
	private int swarmTicks = 5;
	private int tripleSwarmCycle;
	private int medicTicks = 12;
	private int flyBombTicks = 8;

	public Kephri(KephriEncounter encounter, int level) {
		super(KephriEncounter.KEPHRI_ID, encounter.getLocation(SPAWN_LOCATION), Direction.WEST, 0, encounter, level);
		this.encounter = encounter;
		World.spawnObject(new WorldObject(INVIS_BLOCK_ID, 10, 0, new Location(getLocation())));
		super.hitBar = new HitBar(this);
		aerialAssault = encounter.getParty().getPartySettings().isActive(InvocationType.AERIAL_ASSAULT);
		blowingMud = encounter.getParty().getPartySettings().isActive(InvocationType.BLOWING_MUD);
		livelyLarvae = encounter.getParty().getPartySettings().isActive(InvocationType.LIVELY_LARVAE);
		moreOverlords = encounter.getParty().getPartySettings().isActive(InvocationType.MORE_OVERLORDS);
		medic = encounter.getParty().getPartySettings().isActive(InvocationType.MEDIC);
	}

	@Override public float getPointMultiplier() {
		return 1F;
	}

	private void sendSound(final Player[] players, final SoundEffect soundEffect) {
		for (Player player : players) {
			if (player != null) {
				player.sendSound(soundEffect);
			}
		}
	}

	@Override protected void postHitProcess(Hit hit) {
		super.postHitProcess(hit);
		if (phase < 3 && hit.getDamage() > 0) {
			if (!HitType.HEALED.equals(hit.getHitType())) {
				hit.setHitType(HitType.SHIELD);
			} else {
				hit.setHitType(HitType.SHIELD_CHARGE);
			}
		}
	}

	@Override public boolean setHitpoints(int amount) {
		if (amount < hitpoints && shieldingTicks > 0) {
			return false;
		}
		final boolean set = super.setHitpoints(amount);
		if (encounter != null && EncounterStage.STARTED.equals(encounter.getStage())) {
			encounter.getPlayers().forEach(p -> p.getHpHud().updateValue(hitpoints));
		}
		return set;
	}

	private boolean increasePhase() {
		if (++phase == 4) {
			setFaceEntity(null);
			setAnimation(DEATH_ANIMATION);
			encounter.completeRoom();
			WorldTasksManager.schedule(() -> {
				if (EncounterStage.COMPLETED.equals(encounter.getStage()) && !isFinished()) {
					setTransformation(FINISHED_ID);
				}
				encounter.spawnTeleportNPC();
			}, 2);
			return false;
		} else if (phase == 1 || phase == 2) {
			shieldingTicks = 52;
			attackTicks = attackSpeed;
			medicTicks = 12;
			flyBombTicks = 8;
			setAnimation(SPAWN_HELPERS_ANIM);
			setGraphics(TO_SLEEP_GFX);
			blockIncomingHits(1);
			getCombat().setTarget(null);
			setFaceEntity(null);
			setHitpoints(1);
			final Queue<TOANPC> scarabs = new ArrayDeque<>();
			if (phase == 2) {
				scarabs.add(new ArcaneScarab(encounter));
			}
			if (phase == 1 || moreOverlords) {
				scarabs.add(new SpittingScarab(encounter));
			}
			if (phase == 2 || moreOverlords) {
				scarabs.add(new SoldierScarab(encounter));
			}
			if (scarabs.size() > 0) {
				WorldTasksManager.schedule(encounter.addRunningTask(new WorldTask() {

					@Override public void run() {
						if (isDead() || isFinished() || !EncounterStage.STARTED.equals(encounter.getStage())) {
							stop();
							return;
						}
						final TOANPC scarab = scarabs.poll();
						if (scarab != null) {
							scarab.spawn();
							bigScarabs.add(scarab);
							if (scarabs.size() <= 0) {
								stop();
							}
						}
					}
				}), 0, 0);
			}
		} else if (phase == 3) {
			final Player[] players = encounter.getChallengePlayers();
			combatDefinitions.setHitpoints(80);
			setMaxHealth();
			getHitBars().add(new RemoveHitBar(hitBar.getType()));
			super.hitBar = new EntityHitBar(this);
			getHitBars().add(super.hitBar);
			getUpdateFlags().flag(UpdateFlag.HIT);
			specialCycle = 2;
			for (Player p : players) {
				swingDungAttack(p);
				p.getHpHud().sendMaxHitPoints(getMaxHitpoints());
				p.getHpHud().resetColors();
			}
		}
		return true;
	}


	@Override public void processNPC() {
		super.processNPC();
		if (!EncounterStage.COMPLETED.equals(encounter.getStage()) && !isDead() && !isFinished() && getGraphics() == null) {
			setGraphics(shieldingTicks > 0 ? CONSISTENT_AWAKE_GFX : CONSISTENT_ASLEEP_GFX);
		}
		final Player[] players = encounter.getChallengePlayers();
		if (!isDying() & !isFinished() && EncounterStage.STARTED.equals(encounter.getStage()) && players != null && players.length > 0) {
			if (eggExplodeTicks > 0 && --eggExplodeTicks <= 0) {
				eggs.removeIf(egg -> {
					if (!egg.isDying() && !egg.isFinished()) {
						egg.setBadExplosion();
						egg.onFinish(null);
					}
					return true;
				});
			}
			if (shieldingTicks > 0) {
				if (shieldingTicks == 50) {
					setTransformation(KephriEncounter.KEPHRI_ID + 1);
				}
				if (shieldingTicks > 3 && shieldingTicks < 49 && flyBombTicks > 0 && --flyBombTicks <= 0) {
					flyBombTicks = 12;
					sendBombs(false);
				}
				if (--shieldingTicks == 3) {
					swarmTicks = 5;
					setGraphics(AWAKE_GFX);
					setAnimation(AWAKE_ANIM);
					flyBombTicks += 1;
					swarms.removeIf(scarab -> {
						if (!scarab.isDying() && !scarab.isFinished()) {
							scarab.fadeAway();
						}
						return true;
					});
				} else if (shieldingTicks <= 1) {
					setTransformation(KephriEncounter.KEPHRI_ID);
				} else if (--swarmTicks <= 0) {
					swarmTicks = Utils.random(2, 3);
					spawnRandomSwarm(false);
				}
			} else {
				if (medic && --medicTicks <= 0) {
					medicTicks = 12;
					spawnRandomSwarm(true);
				}
				if (--attackTicks <= 0) {
					attackTicks = attackSpeed;
					if (specialCycle-- == 0) {
						specialCycle = phase == 3 ? 2 : Utils.random(4, 5);
						if (dungAttack || agileScarabs.size() > 4 + encounter.getStartTeamSize()) {
							dungAttack = false;
							prepareDungAttack(players);
						} else {
							dungAttack = true;
							sendEggs(players);
						}
					} else {
						performRegularAttack(players);
					}
				}
			}
		}
	}

	@Override public void sendDeath() {
		final Player source = getMostDamagePlayerCheckIronman();
		onDeath(source);
		if (increasePhase()) {
			return;
		}
		resetNpcs();
	}

	@Override public void finish() {
		super.finish();
		resetNpcs();
	}

	private void resetNpcs() {
		eggs.removeIf(egg -> {
			if (!egg.isDying() && !egg.isFinished()) {
				egg.finish();
			}
			return true;
		});
		agileScarabs.removeIf(scarab -> {
			if (!scarab.isDying() && !scarab.isFinished()) {
				scarab.finish();
			}
			return true;
		});
		swarms.removeIf(swarm -> {
			if (!swarm.isDying() && !swarm.isFinished()) {
				swarm.finish();
			}
			return true;
		});
		bigScarabs.removeIf(scarab -> {
			if (!scarab.isDying() && !scarab.isFinished()) {
				scarab.finish();
			}
			return true;
		});
	}

	private void spawnRandomSwarm(boolean medic) {
		final Location spawnLocation = encounter.getLocation(SWARM_SPAWN_LOCATIONS[swarmIndex]);
		final boolean addX = swarmIndex == 2 || swarmIndex == 3 || swarmIndex == 6 || swarmIndex == 7;
		if (!medic && bossLevel > 1 && ++tripleSwarmCycle >= (bossLevel > 3 ? SWARM_SPAWN_LOCATIONS.length / 2 : SWARM_SPAWN_LOCATIONS.length)) {
			tripleSwarmCycle = 0;
			for (int i = 0; i < 3; i++) {
				spawnScarab(spawnLocation.transform(addX ? i : 0, addX ? 0 : i), medic);
			}
		} else {
			final int delta = Utils.random(3);
			spawnScarab(spawnLocation.transform(addX ? delta : 0, addX ? 0 : delta), medic);
		}

		swarmIndex += Utils.random(1, 2);
		swarmIndex %= SWARM_SPAWN_LOCATIONS.length;
	}

	private void spawnScarab(Location location, boolean medic) {
		final ScarabSwarm scarabSwarm = new ScarabSwarm(location, medic, encounter);
		scarabSwarm.spawn();
		swarms.add(scarabSwarm);
	}

	private void prepareDungAttack(final Player[] players) {
		attackTicks += 7;
		final Player firstPlayer = players[playerIndex % players.length];
		final Player secondPlayer = players[(playerIndex + 1) % players.length];
		firstPlayer.setGraphics(PLAYER_FLIES_GFX);
		firstPlayer.sendSound(PLAYER_FLIES_SOUND);
		if (blowingMud) {
			secondPlayer.setGraphics(PLAYER_FLIES_GFX);
			secondPlayer.sendSound(PLAYER_FLIES_SOUND);
		}
		WorldTasksManager.schedule(encounter.addRunningTask(new WorldTask() {
			@Override public void run() {
				if (isDead() || isFinished() || !EncounterStage.STARTED.equals(encounter.getStage())) {
					stop();
					return;
				}
				swingDungAttack(firstPlayer);
				if (blowingMud) {
					swingDungAttack(secondPlayer);
				}
			}
		}), 4);
		playerIndex += blowingMud ? 2 : 1;
	}

	private void swingDungAttack(final Player player) {
		if (shieldingTicks > 0) {
			return;
		}
		setAnimation(THROW_EGGS_ANIMATION);
		final Location middleTile = encounter.getLocation(SPAWN_LOCATION).transform(2, 2);
		World.sendSoundEffect(middleTile.transform(2, 2), THROW_AREA_SOUND);
		player.stopAll();
		if (player.isDying() || !encounter.insideChallengeArea(player)) {
			return;
		}
		final int dx = player.getX() - middleTile.getX();
		final int dy = player.getY() - middleTile.getY();
		final int adx = Math.abs(dx);
		final int ady = Math.abs(dy);
		Direction direction;
		if (Math.abs(ady - adx) <= 1) {
			direction = Direction.getDirection(Utils.capRange(-1, 1, dx), Utils.capRange(-1, 1, dy));
		} else {
			if (adx > ady) {
				direction = Direction.getDirection(Utils.capRange(-1, 1, dx), 0);
			} else {
				direction = Direction.getDirection(0, Utils.capRange(-1, 1, dy));
			}
		}
		final Location endTile = getDungEndTile(player, direction, 8 - Math.max(adx, ady));

		player.sendMessage("<col=ef0083>Kephri throws you back!</col>");
		player.setAnimation(PLAYER_THROW_ANIMATION);
		player.setGraphics(PLAYER_STUN_GFX);
		player.sendSound(FLYBACK_SOUND);
		final Direction oppositeDirection = Direction.getOppositeDirection(direction);
		player.faceDirection(oppositeDirection);
		player.autoForceMovement(player.getLocation(), endTile, 5, 29 + Math.max(1, endTile.getTileDistance(player.getLocation()) / 3) * 30, oppositeDirection.getDirection());

		final Location startTile = middleTile.transform(dx < 0 ? Math.max(-2, dx) : Math.min(2, dx), dy < 0 ? Math.max(-2, dy) : Math.min(2, dy));
		final Location[] dungPath = getDungPath(startTile, endTile);
		if (dungPath != null) {
			WorldTasksManager.schedule(encounter.addRunningTask(new WorldTask() {
				int ticks = 0;
				@Override public void run() {
					if (isDead() || isFinished() || !EncounterStage.STARTED.equals(encounter.getStage())) {
						stop();
						return;
					}
					if (++ticks == 2) {
						final Player[] players = encounter.getChallengePlayers();
						if (players == null || players.length < 1) {
							stop();
							return;
						}
						for (int i = 0; i < dungPath.length; i++) {
							World.sendGraphics(new Graphics(DUNG_GFX_ID, i * 10, 0), dungPath[i]);
							World.sendSoundEffect(dungPath[i], new SoundEffect(DUNG_SOUND_ID, 10, 7 + i * 10));
						}
						for (Player p : players) {
							p.getPacketDispatcher().resetCamera();
							p.getPacketDispatcher().sendCameraShake(CameraShakeType.LEFT_AND_RIGHT, 4, 0, 0);
							p.getPacketDispatcher().sendCameraShake(CameraShakeType.UP_AND_DOWN, 2, 0, 0);
							p.getPacketDispatcher().sendCameraShake(CameraShakeType.FRONT_AND_BACK, 2, 0, 0);
						}
						WorldTasksManager.schedule(() -> Arrays.stream(players).forEach(p -> p.getPacketDispatcher().resetCamera()));
					} else if (ticks == 4) {
						for (int i = 0; i < Math.min(3, dungPath.length); i++) {
							encounter.addDungObject(dungPath[i]);
						}
					} else if (ticks == 5) {
						if (dungPath.length > 3) {
							for (int i = 3; i < dungPath.length; i++) {
								encounter.addDungObject(dungPath[i]);
							}
						}
						stop();
					}
				}
			}), 0, 0);
		}
	}

	private Location getDungEndTile(final Player player, final Direction direction, int iterations) {
		Location endTile = player.getLocation();
		for (int i = 0; i < iterations; i++) {
			final Location nextTile = endTile.transform(direction.getOffsetX(), direction.getOffsetY());
			if (!World.isFloorFree(nextTile, 1) || encounter.onDungObject(nextTile)) {
				return endTile;
			}
			endTile = nextTile;
		}
		return endTile;
	}

	private Location[] getDungPath(final Location startTile, final Location endTile) {
		final int distance = endTile.getTileDistance(startTile);
		if (distance < 1) {
			return null;
		}
		final Location[] path = new Location[distance];
		final int dx = endTile.getX() - startTile.getX();
		final int dy = endTile.getY() - startTile.getY();
		final int adx = Math.abs(dx);
		final int ady = Math.abs(dy);
		final int increaseX = Utils.capRange(-1, 1, dx);
		final int increaseY = Utils.capRange(-1, 1, dy);
		final boolean adxGreater = adx > ady;
		final float thresholdStep = adxGreater ? (adx + 1F) / (ady + 1F) : (ady + 1F) / (adx + 1F);
		float currentThreshold = thresholdStep;
		int currentDIncrease = 0;
		Location tile = startTile;
		for (int stepCount = 0; stepCount < distance; stepCount++) {
			boolean increaseInSmallerDelta = false;
			if (adx == ady || (stepCount == 0 && Math.abs(ady - adx) <= 1) || (currentDIncrease < (adxGreater ? ady : adx) && stepCount + 1 > currentThreshold)) {
				currentThreshold += thresholdStep;
				increaseInSmallerDelta = true;
				currentDIncrease++;
			}
			final Location loc = adxGreater ? tile.transform(increaseX, increaseInSmallerDelta ? increaseY : 0)  : tile.transform(increaseInSmallerDelta ? increaseX : 0, increaseY);
			path[stepCount] = loc;
			tile = loc;
		}

		return path;
	}

	private void sendEggs(final Player[] players) {
		setAnimation(THROW_EGGS_ANIMATION);
		sendSound(players, THROW_EGGS_SOUND);
		final int brownEggs = livelyLarvae ? Utils.random(players.length * 3, players.length * 5) : players.length * 2;
		final List<Location> eggLocations = new ArrayList<>(Arrays.asList(EGG_LOCATIONS));
		Collections.shuffle(eggLocations);
		final Location fromTile = encounter.getLocation(THROW_SECOND_LOC);
		for (int i = 0; i < brownEggs; i++) {
			final Location loc = encounter.getLocation(eggLocations.get(i));
			if (!encounter.onDungObject(loc)) {
				World.sendProjectile(fromTile, loc, BROWN_EGG_PROJECTILE);
			}
		}
		for (int i = brownEggs; i < eggLocations.size(); i++) {
			final Location loc = encounter.getLocation(eggLocations.get(i));
			if (!encounter.onDungObject(loc)) {
				World.sendProjectile(fromTile, loc, NORMAl_EGG_PROJECTILE);
			}
		}
		WorldTasksManager.schedule(encounter.addRunningTask(() -> {
			if (!isDying() && !isFinished() && EncounterStage.STARTED.equals(encounter.getStage())) {
				final Player[] challengePlayers = encounter.getChallengePlayers();
				if (challengePlayers != null && challengePlayers.length > 0) {
					for (int i = 0; i < brownEggs; i++) {
						placeEgg(encounter.getLocation(eggLocations.get(i)), BROWN_EGG_NPC_ID, players);
					} for (int i = brownEggs; i < eggLocations.size(); i++) {
						placeEgg(encounter.getLocation(eggLocations.get(i)), NORMAL_EGG_NPC_ID, players);
					}
					eggExplodeTicks = 16;
				}
			}
		}), 3);
	}

	private void placeEgg(final Location location, final int id, final Player[] players) {
		if (encounter.onDungObject(location)) {
			return;
		}
		final Egg egg = new Egg(id, location, this);
		eggs.add(egg);
		egg.spawn();
		World.sendSoundEffect(location, EGG_LAND_SOUND);
		for (Player p : players) {
			if (p != null && location.equals(p.getLocation())) {
				p.applyHit(new Hit(this, getMaxHit(2), HitType.DEFAULT));
			}
		}
	}

	private void performRegularAttack(final Player[] players) {
		setAnimation(THROW_SWING_ANIM);
		sendSound(players, THROW_SWING_SOUND);
		final Location secondLocation = encounter.getLocation(THROW_SECOND_LOC);
		World.sendProjectile(encounter.getLocation(THROW_START_LOC), secondLocation, THROW_FIRST_PROJECTILE);
		WorldTasksManager.schedule(encounter.addRunningTask(new WorldTask() {

			@Override public void run() {
				if (isDead() || isFinished() || !EncounterStage.STARTED.equals(encounter.getStage()) || shieldingTicks > 0) {
					stop();
					return;
				}
				World.sendSoundEffect(secondLocation, THROW_BOMB_EXPLODE_SOUND);
				sendBombs(true);

			}
		}), 2);
	}

	private void sendBombs(boolean regular) {
		final Player[] players = encounter.getChallengePlayers();
		final Location[] fromLocations = new Location[players.length];
		if (regular) {
			final Location from = encounter.getLocation(THROW_SECOND_LOC);
			Arrays.fill(fromLocations, from);
		} else {
			for (int i = 0; i < players.length; i++) {
				int distance = 128;
				Location from = encounter.getLocation(SCARAB_BOMB_LOCATIONS[0]);
				for (Location scarabLoc : SCARAB_BOMB_LOCATIONS) {
					final Location instanceLoc = encounter.getLocation(scarabLoc);
					final int bombLoc = instanceLoc.getTileDistance(players[i].getLocation());
					if (bombLoc < distance) {
						distance = bombLoc;
						from = instanceLoc;
					}
				}
				fromLocations[i] = from;
			}
		}
		final List<Location> initialTiles = new ArrayList<>();
		final List<Location> explodeTiles = new ArrayList<>();
		for (int i = 0; i < players.length; i++) {
			final Player p = players[i];
			final Location loc = new Location(p.getLocation());
			if (!initialTiles.contains(loc)) {
				initialTiles.add(loc);
				explodeTiles.add(loc);
				World.sendGraphics(FLOOR_INCOMING_GFX, loc);
				World.sendProjectile(fromLocations[i], loc, regular ? THROW_SECOND_PROJECTILE : SCARAB_BOMB_PROJECTILE);
			}
		}
		final int range = aerialAssault ? 1 : 0;
		initialTiles.forEach(tile -> {
			for (int x = -range; x <= range; x++) {
				for (int y = -range; y <= range; y++) {
					final Location sideTile = tile.transform(x, y);
					if ((x != 0 || y != 0) && !explodeTiles.contains(sideTile)) {
						if (World.isFloorFree(sideTile, 1)) {
							World.sendGraphics(FLOOR_INCOMING_GFX, sideTile);
						}
						explodeTiles.add(sideTile);
					}
				}
			}
		});
		WorldTasksManager.schedule(encounter.addRunningTask(() -> {
			if (isDead() || isFinished() || !EncounterStage.STARTED.equals(encounter.getStage())) {
				return;
			}
			final Player[] players1 = encounter.getChallengePlayers();
			if (players1 != null) {
				explodeTiles.forEach(tile -> {
					if (World.isFloorFree(tile, 1)) {
						World.sendGraphics(BOMB_EXPLODE_GFX, tile);
						if (!regular) {
							World.sendGraphics(FLY_EXPLODE_GFX, tile);
						}
					}
					if (initialTiles.contains(tile)) {
						World.sendSoundEffect(tile, BOMB_EXPLODE_SOUND);
					}
					for (Player p : players1) {
						if (p.getLocation().equals(tile)) {
							int base1 = getMaxHit(regular ? 8 : 5);
							if (p.getPrayerManager().isActive(Prayer.PROTECT_FROM_MAGIC)) {
								base1 /= 3;
							}
							p.applyHit(new Hit(Kephri.this, Utils.random(base1, base1 * 2), HitType.MAGIC));
						}
					}
				});
			}
		}), 3);
	}

	public void healBoss(NPC source, float factor, float min, float max) {
		final int minHealth = (int) Math.floor(getMaxHitpoints() * factor * min);
		final int maxHealth = (int) Math.floor(getMaxHitpoints() * factor * max);
		final int currentHitPoints = getHitpoints();
		final int maxHitPoints = (int) Math.floor(getMaxHitpoints() * 1.15F);
		if (currentHitPoints >= maxHitPoints) {
			return;
		}
		int heal = Utils.random(minHealth, maxHealth);
		if (currentHitPoints + heal >= maxHitPoints) {
			heal = maxHitPoints - currentHitPoints;
			stopSleeping();
		}
		applyHit(new Hit(source, heal, HitType.HEALED));
	}

	@Override public void heal(int amount) {
		setHitpoints(Math.min((hitpoints + amount), ((int) Math.floor(getMaxHitpoints() * 1.15F))));
	}

	@Override public boolean checkProjectileClip(Player player, boolean melee) { return !encounter.insideChallengeArea(player); }

	@Override public void setRespawnTask() {}

	@Override public void setUnprioritizedAnimation(Animation animation) {}

	@Override public boolean isCycleHealable() { return false; }

	@Override public void setFaceEntity(Entity entity) {
		if ((!isDying() && shieldingTicks <= 0) || entity == null) {
			super.setFaceEntity(entity);
		}
	}

	@Override
	public boolean addWalkStep(final int nextX, final int nextY, final int lastX, final int lastY, final boolean check) { return false; }

	@Override public int attack(Entity target) { return 0; }

	public List<AgileScarab> getAgileScarabs() { return agileScarabs; }

	public void removeAgileScarab(AgileScarab agileScarab) {
		agileScarabs.remove(agileScarab);
	}

	public void stopSleeping() {
		shieldingTicks = 4;
	}

	public boolean inShieldingPhase() {
		return shieldingTicks > 0;
	}

	private static class HitBar extends EntityHitBar {

		public HitBar(Entity entity) {
			super(entity);
		}

		@Override public int getType() {
			return 13;
		}
	}

	static class Egg extends NPC {

		private boolean badExplosion = false;
		private final Kephri kephri;

		public Egg(int id, Location tile, Kephri kephri) {
			super(id, tile, Direction.SOUTH, 0);
			this.kephri = kephri;
			setAnimation(EGG_SPAWN_ANIM);
			deathDelay = 0;
			this.combat = new NPCCombat(this) {
				@Override
				public void setTarget(final Entity target, TargetSwitchCause cause) { }
				@Override
				public void forceTarget(final Entity target) { }
			};
		}

		@Override protected void onFinish(Entity source) {
			super.onFinish(source);
			if (id == BROWN_EGG_NPC_ID || !badExplosion) {
				World.sendGraphics(EGG_BREAK_GFX, getLocation());
			}
			if (!badExplosion) {
				World.sendSoundEffect(getLocation(), EGG_BREAK_SOUND);
			}
			if (badExplosion) {
				if (id == NORMAL_EGG_NPC_ID) {
					World.sendGraphics(EGG_EXPLODE_GFX, getLocation());
					World.sendSoundEffect(getLocation(), EGG_EXPLODE_SOUND);
					final Player[] players = kephri.encounter.getChallengePlayers();
					if (players != null) {
						for (Player p : players) {
							if (p != null && getLocation().getTileDistance(p.getLocation()) <= 1) {
								p.sendMessage("You are damaged by a nearby egg explosion!");
								p.applyHit(new Hit(kephri, kephri.getMaxHit(8), HitType.DEFAULT));
							}
						}
					}
				} else {
					final AgileScarab scarab = new AgileScarab(getLocation(), kephri.encounter);
					scarab.spawn();
					kephri.agileScarabs.add(scarab);
				}
			}
		}

		@Override public boolean isAlwaysTakeMaxHit(HitType type) { return true; }

		@Override public boolean isForceAttackable() { return true; }

		@Override public void setRespawnTask() {}

		@Override public void setTarget(Entity target, TargetSwitchCause cause) {}

		@Override public void setFaceEntity(Entity entity) {}

		@Override public void setFacedInteractableEntity(InteractableEntity facedInteractableEntity) {}

		@Override public void setFaceLocation(Location tile) {}

		@Override public boolean isEntityClipped() { return false; }

		@Override public boolean isValidAnimation(int animID) { return true; }

		@Override
		public boolean addWalkStep(final int nextX, final int nextY, final int lastX, final int lastY, final boolean check) { return false; }

		void setBadExplosion() { badExplosion = true; }
	}

}
