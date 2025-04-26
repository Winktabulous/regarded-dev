package com.zenyte.game.content.tombsofamascut.npc;

import com.near_reality.game.world.entity.TargetSwitchCause;
import com.zenyte.game.content.tombsofamascut.InvocationType;
import com.zenyte.game.content.tombsofamascut.encounter.BabaEncounter;
import com.zenyte.game.content.tombsofamascut.raid.EncounterStage;
import com.zenyte.game.util.CollisionUtil;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.DirectionUtil;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.*;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.player.Player;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * @author Savions.
 */
public class Baba extends TOANPC implements CombatScript {

	private static final Location BABA_SPAWN_LOCATION = new Location(3815, 5406);
	private static final Location[] ROLLING_BOULDER_START_LOCATIONS = {new Location(3813, 5401), new Location(3813, 5404), new Location(3813, 5407), new Location(3813, 5410), new Location(3813, 5413)};
	private static final int ID = 11778;
	private static final int DARKNESS_SOUND_ID = 3737;
	private static final int SHADOW_END_GFX_ID = 1103;
	private static final int RETALIATE_X_THRESHOLD = 3815;
	private static final Animation SPAWN_ANIMATION = new Animation(9752);
	private static final Animation ATTACK_ANIMATION = new Animation(9743);
	private static final Animation THROW_ROCK_ANIM = new Animation(9744);
	private static final Animation FLY_ANIMATION = new Animation(9748);
	private static final Animation THROW_ROLLING_BOULDERS_ANIM = new Animation(9749);
	private static final Animation PLAYER_FLYBACK_ANIMATION = new Animation(9799);
	public static final Animation PIT_FALL_ANIM = new Animation(4366);
	private static final SoundEffect SHADOW_END_SOUND = new SoundEffect(5972, 7);
	private static final SoundEffect THROW_ROCK_SOUND = new SoundEffect(6023, 0, 10);
	private static final SoundEffect FLYBACK_SOUND = new SoundEffect(3201);
	public static final SoundEffect ROLLING_BOULDER_SOUND = new SoundEffect(5981);
	private static final Projectile ROCK_PROJECTILE = new Projectile(2244, 100, 31, 90, 40, 120, 64, 0);
	private static final Projectile ROLLING_BOULDER_PROJECTILE = new Projectile(2245, 50, 5, 30, 35, 60, 0, 0);
	private final BabaEncounter encounter;
	private final boolean shakingThingsUp;
	private final boolean mindTheGap;
	private final boolean boulderDash;
	private boolean skipFirstBoulder;
	private int underNeathTicks = 2;
	private int shadowCycle = 2;
	private boolean performingShadowSpecial;
	private Location shadowBaseTile;
	private boolean shadowFastSpecial;
	private int shadowTicks;
	private int rockThrowTicks;
	private int rockImpactTicks;
	private int setSpawnTicks = -1;
	private Queue<Runnable> boulderActions = new ArrayDeque<>();
	private int boulderDelay = 0;
	private int lastCrackedIndex = Utils.random(4);

	public Baba(BabaEncounter encounter, int level) {
		super(ID, encounter.getLocation(BABA_SPAWN_LOCATION), Direction.WEST, 64, encounter, level, true);
		this.encounter = encounter;
		this.shakingThingsUp = encounter.getParty().getPartySettings().isActive(InvocationType.SHAKING_THINGS_UP);
		this.mindTheGap = encounter.getParty().getPartySettings().isActive(InvocationType.MIND_THE_GAP);
		this.boulderDash = encounter.getParty().getPartySettings().isActive(InvocationType.BOULDERDASH);
		setAnimation(SPAWN_ANIMATION);
		lock(3);
		setAggressionDistance(64);
		setMaxDistance(64);
		setAttackDistance(64);
		setForceFollowClose(true);
		forceCheckAggression = true;
		setRun(true);
		skipFirstBoulder = true;
	}

	@Override public List<Entity> getPossibleTargets(EntityType type, int radius) {
		if (!possibleTargets.isEmpty()) {
			possibleTargets.clear();
		}
		final List<Entity> possibleTargets = new ArrayList<>();
		final Player[] players = encounter.getChallengePlayers();
		if (players.length > 0) {
			Player target = null;
			int distance = Integer.MAX_VALUE;
			int hitpoints = Integer.MAX_VALUE;
			final Location middleTile = getMiddleLocation();
			for (Player p : players) {
				if (p != null) {
					final int pDistance = middleTile.getTileDistance(p.getLocation());
					final int targetHitPoints = p.getHitpoints();
					if (targetHitPoints < hitpoints || (targetHitPoints == hitpoints && pDistance < distance)) {
						target = p;
						distance = pDistance;
						hitpoints = targetHitPoints;
					}
				}
			}
			if (target != null) {
				possibleTargets.add(target);
			}
		}
		super.possibleTargets.addAll(possibleTargets);
		return possibleTargets;
	}

	@Override public boolean setHitpoints(int amount) {
		if (getCombatDefinitions() != null) {
			final int currentHitPoints = getHitpoints();
			final int setThreshold = (int) (getMaxHitpoints() * .75F);
			if (currentHitPoints > setThreshold && amount <= setThreshold) {
				setSpawnTicks = 1;
			}
			final int firstBoulderThreshold = (int) (getMaxHitpoints() * .66F);
			if (currentHitPoints > firstBoulderThreshold  && amount <= firstBoulderThreshold ) {
				spawnRollingBoulders();
			} else {
				final int secondBoulderThreshold = (int) (getMaxHitpoints() * .33F);
				if (currentHitPoints > secondBoulderThreshold && amount <= secondBoulderThreshold) {
					spawnRollingBoulders();
				}
			}
		}
		final boolean set = super.setHitpoints(amount);
		if (encounter != null && EncounterStage.STARTED.equals(encounter.getStage())) {
			encounter.getPlayers().forEach(p -> p.getHpHud().updateValue(hitpoints));
		}
		return set;
	}

	@Override protected void onFinish(Entity source) {
		super.onFinish(source);
		encounter.completeRoom();
	}

	@Override public void processNPC() {
		super.processNPC();
		if (!isDead() && !isFinished() && EncounterStage.STARTED.equals(encounter.getStage())) {
			final Player[] players = encounter.getChallengePlayers();
			if (!boulderActions.isEmpty() && (boulderDelay <= 0 || --boulderDelay <= 0)) {
				while(!boulderActions.isEmpty()) {
					if (boulderDelay > 0) {
						break;
					}
					boulderActions.poll().run();
				}
			} else {
				if (rockThrowTicks > 0 && --rockThrowTicks <= 0) {
					setAnimation(THROW_ROCK_ANIM);
					rockImpactTicks = 7;
					for (Player p : players) {
						p.sendMessage("<col=ef0083>Ba-Ba throws a large boulder at you.</col>");
						p.sendSound(THROW_ROCK_SOUND);
						World.sendProjectile(this, p, ROCK_PROJECTILE);
					}
				} else if (rockImpactTicks > 0 && --rockImpactTicks <= 0) {
					encounter.landRocks(players);
				} else if (setSpawnTicks > 0 && --setSpawnTicks <= 0) {
					setSpawnTicks = encounter.spawnBaboons() ? 325 : 10;
				}
				if (performingShadowSpecial && shadowTicks > 0 && --shadowTicks <= 0) {
					setAnimation(ATTACK_ANIMATION);
					performingShadowSpecial = false;
					World.sendSoundEffect(shadowBaseTile, SHADOW_END_SOUND);
					final int length = (shakingThingsUp || shadowFastSpecial ? 3 : 2);
					for (int x = -length; x <= length; x++) {
						for (int y = -length; y <= length; y++) {
							if ((Math.abs(x) == length && y != 0) || (Math.abs(y) == length && x != 0)) {
								continue;
							}
							final Location loc = shadowBaseTile.transform(x, y);
							if (World.isFloorFree(loc, 1)) {
								final int distance = 1 + loc.getTileDistance(shadowBaseTile);
								World.sendGraphics(x == 0 && y == 0 ? new Graphics(SHADOW_END_GFX_ID) : new Graphics(SHADOW_END_GFX_ID, distance * 6, 0), loc);
								final float damageMultiplier = 1F - (.1F * loc.getTileDistance(shadowBaseTile));
								for (Player p : players) {
									if (loc.equals(p.getLocation())) {
										p.applyHit(new Hit(this, (int) (damageMultiplier * getMaxHit(17)) + Utils.random(5), HitType.DEFAULT));
									}
								}
							}
						}
					}
				}
				if (getCombat().getCombatDelay() <= 0 && getCombat().getTarget() != null && !performingShadowSpecial && !isPerformingBoulderSpecial() && CollisionUtil.collides(getX(), getY(), getSize(), getCombat().getTarget().getX(), getCombat().getTarget().getY(), getCombat().getTarget().getSize()) && getCombat().getTarget() instanceof final Player player) {
					if (underNeathTicks > 0 && --underNeathTicks <= 0) {
						getCombat().setCombatDelay(2);
						sendShadowAttack(player, true);
					}
				} else {
					underNeathTicks = 2;
				}
			}
		}
	}


	private void spawnRollingBoulders() {
		if(skipFirstBoulder) {
			skipFirstBoulder = false;
			return;
		}
		resetWalkSteps();
		setDirection(combat.getTarget() != null ? DirectionUtil.getClosestFaceDirection(this, combat.getTarget()).getDirection() : Direction.WEST.getDirection());
		getCombat().setTarget(null);
		setCantInteract(true);
		rockThrowTicks = 0;
		shadowTicks = 0;
		performingShadowSpecial = false;
		underNeathTicks = 2;
		boulderDelay = 0;
		addBoulderDelay(3);
		boulderActions.add(() -> {
			setDirection(Direction.SOUTH_WEST.getDirection());
			setAnimation(FLY_ANIMATION);
			encounter.resetAndKillRockFall();
			final Location destLocation = encounter.getLocation(BABA_SPAWN_LOCATION);
			setForceMovement(new ForceMovement(new Location(getLocation()), 0, destLocation, 30, DirectionUtil.getFaceDirection(destLocation, getLocation())));
			addBoulderDelay(1);
		});
		boulderActions.add(() -> {
			setLocation(encounter.getLocation(BABA_SPAWN_LOCATION));
			addBoulderDelay(1);
		});
		boulderActions.add(() -> {
			final Player[] players = encounter.getChallengePlayers();
			for (Player p : players) {
				p.sendMessage("<col=ef0083>Ba-Ba screams and knocks you back!</col>");
				p.setAnimation(PLAYER_FLYBACK_ANIMATION);
				p.setGraphics(BabaEncounter.PLAYER_STUN_GFX);
				p.sendSound(FLYBACK_SOUND);
				p.faceDirection(Direction.EAST);
				p.lock();
				final Location location = new Location(encounter.getX(BabaEncounter.PIT_WALL_X_COORD), p.getY(), 0);
				final boolean intoGap = mindTheGap && encounter.intoGap(p.getLocation().getY());
				if (intoGap) {
					location.setLocation(location.transform(-2, 0));
				}
				if (!location.equals(p.getLocation())) {
					final int duration = 5 + (10 * location.getTileDistance(p.getLocation()));
					p.autoForceMovement(location, 0, duration, Direction.EAST.getDirection(), () -> {
						if (intoGap) {
							p.getTemporaryAttributes().put(BabaEncounter.APMEKEN_BABA_DEATH_ATTRIBUTE, Boolean.TRUE);
							p.applyHit(new Hit(Baba.this, p.getMaxHitpoints(), HitType.DEFAULT).setExecuteIfLocked());
						}
						p.lock(1);
					});
					p.setLocation(location);
				} else {
					p.lock(1);
				}
			}
			addBoulderDelay(1);
		});
		boulderActions.add(() -> {
			setDirection(Direction.EAST.getDirection());
			encounter.resetAndKillRockFall();
			setCantInteract(false);
		});
		for (int i = 0; i < (boulderDash ? 15 : 10); i++) {
			boulderActions.add(() -> {
				final Player[] players = encounter.getChallengePlayers();
				for (Player p : players) {
					p.sendSound(ROLLING_BOULDER_SOUND);
				}
				for (Location loc : ROLLING_BOULDER_START_LOCATIONS) {
					final Location baseLoc = encounter.getLocation(loc);
					final Location projLoc = baseLoc.transform(2, 0);
					World.sendProjectile(this, projLoc, ROLLING_BOULDER_PROJECTILE);
				}
				setAnimation(THROW_ROLLING_BOULDERS_ANIM);
				addBoulderDelay(2);
				lastCrackedIndex += Utils.random(4) - 2;
				lastCrackedIndex = Math.max(0, Math.min(4, lastCrackedIndex));
			});
			boulderActions.add(() -> {
				for (int bi = 0; bi < ROLLING_BOULDER_START_LOCATIONS.length; bi++) {
					encounter.spawnRollingBoulder(encounter.getLocation(ROLLING_BOULDER_START_LOCATIONS[bi]), bi == lastCrackedIndex);
				}
				addBoulderDelay(boulderDash ? 5 : 6);
			});
		}
	}

	private void addBoulderDelay(int delay) {
		boulderDelay += delay;
	}

	private void sendShadowAttack(final Player target, boolean inside) {
		faceDirection(DirectionUtil.getClosestFaceDirection(this, target));
		resetWalkSteps();
		performingShadowSpecial = true;
		getCombat().setTarget(null);
		shadowBaseTile = new Location(inside ? getLocation() : target.getLocation());
		shadowFastSpecial = CollisionUtil.collides(getX(), getY(), getSize(), shadowBaseTile.getX(), shadowBaseTile.getY(), target.getSize());
		shadowTicks = shadowFastSpecial ? 2 : 4;
		getCombat().setCombatDelay(shadowTicks + 3);
		for (int i = 0; i < 3; i++) {
			World.sendSoundEffect(shadowBaseTile, new SoundEffect(DARKNESS_SOUND_ID, 10, i * 30));
		}
		World.sendGraphics(new Graphics(shadowFastSpecial ? 2111 : 1447), shadowBaseTile);
		for (int d = 1; d < (shakingThingsUp ? 4 : 3); d++) {
			for (int i = 0; i < 4; i++) {
				final Direction dir = Direction.values[i * 2];
				final Location loc = shadowBaseTile.transform(dir.getOffsetX() * d, dir.getOffsetY() * d);
				if (World.isFloorFree(loc, 1)) {
					World.sendGraphics(new Graphics((d == 1 && !shadowFastSpecial) ? 1446 : 2111, d * 30, 0), shadowBaseTile.transform(dir.getOffsetX() * d, dir.getOffsetY() * d));
				}
			}
		}
	}

	@Override public void setUnprioritizedAnimation(Animation animation) {}

	@Override public void setTarget(Entity target, TargetSwitchCause cause) {
		if (!performingShadowSpecial && !isPerformingBoulderSpecial()) {
			super.setTarget(target, cause);
		}
	}

	@Override public void setFaceLocation(Location tile) {
		if (!performingShadowSpecial && !isPerformingBoulderSpecial()) {
			super.setFaceLocation(tile);
		}
	}

	@Override public void setDirection(int direction) {
		if (!performingShadowSpecial && !isPerformingBoulderSpecial()) {
			super.setDirection(direction);
		}
	}

	@Override public boolean checkAggressivity() {
		return performingShadowSpecial ||isPerformingBoulderSpecial() || super.checkAggressivity();
	}

	@Override public boolean addWalkStep(int nextX, int nextY, int lastX, int lastY, boolean check) {
		return !performingShadowSpecial && !isPerformingBoulderSpecial() && super.addWalkStep(nextX, nextY, lastX, lastY, check);
	}

	@Override public void autoRetaliate(Entity source) {
		if (!isCantInteract() && source instanceof Player player && player.getX() >= encounter.getX(RETALIATE_X_THRESHOLD)) {
			boulderActions.clear();
		}
	}

	@Override public boolean isFrozen() { return false; }

	@Override public void setRespawnTask() {}

	@Override public boolean isEntityClipped() { return false; }

	@Override public float getPointMultiplier() {
		return 0;
	}

	@Override public boolean isIntelligent() { return true; }

	@Override public boolean isCycleHealable() { return false; }

	@Override public double getMeleePrayerMultiplier() { return .20; }

	@Override public int attack(Entity target) {
		if (performingShadowSpecial || isPerformingBoulderSpecial()) {
			return 0;
		}
		if (target instanceof final Player player && (shadowCycle <= 0 || --shadowCycle <= 0) && rockImpactTicks <= 0) {
			sendShadowAttack(player, false);
			shadowCycle = 5;
		} else {
			if (shadowCycle == 3) {
				encounter.dropRubbles();
				rockThrowTicks = 13;
			}
			setAnimation(ATTACK_ANIMATION);
			delayHit(this, 0, target, new Hit(this, getRandomMaxHit(this, getMaxHit(24), AttackType.SLASH, target), HitType.MELEE));
		}
		return combatDefinitions.getAttackSpeed();
	}

	public boolean isPerformingBoulderSpecial() { return !boulderActions.isEmpty(); }
}
