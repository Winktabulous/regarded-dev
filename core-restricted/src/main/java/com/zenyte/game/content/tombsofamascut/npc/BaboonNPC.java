package com.zenyte.game.content.tombsofamascut.npc;

import com.near_reality.game.world.entity.TargetSwitchCause;
import com.zenyte.game.content.tombsofamascut.encounter.ApmekenEncounter;
import com.zenyte.game.content.tombsofamascut.raid.EncounterStage;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPCCombat;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Savions.
 */
public class BaboonNPC extends TOANPC implements CombatScript {

	private static final Animation MELEE_ANIM = new Animation(9742);
	private static final Animation RANGE_ANIM = new Animation(9745);
	private static final Animation MAGE_ANIM = new Animation(9746);
	private static final Animation THRALL_SUMMON_ANIM = new Animation(9747);
	private static final Animation EXPLODE_ANIM = new Animation(9756);
	private static final Graphics THRALL_SUMMON_GFX = new Graphics(2017);
	private static final Graphics EXPLODE_BODY_GFX = new Graphics(2249);
	private static final Graphics EXPLODE_FIRE_GFX = new Graphics(131);
	private static final Projectile RANGE_PROJECTILE = new Projectile(2242, 17, 36, 67, 14, 20, 32, 4);
	private static final Projectile MAGE_PROJECTILE = new Projectile(2247, 20, 34, 30, 15, 20, 50, 4);
	private static final SoundEffect THRALL_SUMMON_SOUND = new SoundEffect(100, 6);
	private static final SoundEffect EXPLODE_SOUND = new SoundEffect(156, 6, 5);
	protected final ApmekenEncounter encounter;
	private int thrallSpawnTicks;
	private int thrallsCap = Utils.random(6, 8);
	private int explodeTicks;
	private int preventRandomWalkTicks = 2;

	public BaboonNPC(int id, Location tile, Direction facing, ApmekenEncounter encounter) {
		super(id, tile, facing, 30, encounter);
		this.encounter = encounter;
		setMaxDistance(64);
		setAggressionDistance(64);
		if (id == ApmekenEncounter.BABOON_SHAMAN_ID) {
			getCombat().setCombatDelay(4);
		} else if (id == ApmekenEncounter.BABOON_CURSED_ID) {
			setForceAggressive(false);
			setRadius(64);
			this.combat = new NPCCombat(this) {
				@Override
				public void setTarget(final Entity target, TargetSwitchCause cause) { }
				@Override
				public void forceTarget(final Entity target) { }
			};
		}
	}

	@Override public void processNPC() {
		super.processNPC();
		if (EncounterStage.STARTED.equals(encounter.getStage()) && !isDead() && !isFinished()) {
			if (getCombat().getTarget() == null) {
				checkAggressivity();
			}
			if (thrallSpawnTicks > 0 && --thrallSpawnTicks <= 0) {
				final List<Location> possibleSpawnLocations = new ArrayList<>();
				for (int x = -2; x <= 2; x++) {
					for (int y = -2; y <= 2; y++) {
						final Location loc = getLocation().transform(x, y);
						if (World.isTileFree(loc, 1)) {
							possibleSpawnLocations.add(loc);
						}
					}
				}
				if (possibleSpawnLocations.size() > 0) {
					final BaboonNPC thrall = new BaboonNPC(ApmekenEncounter.BABOON_THRALL_ID, possibleSpawnLocations.get(Utils.random(possibleSpawnLocations.size() - 1)), Direction.NORTH, encounter);
					thrall.setGraphics(THRALL_SUMMON_GFX);
					encounter.addThrall(thrall);
					World.spawnNPC(thrall);
					thrall.getCombat().setTarget(getCombat().getTarget());
				}
				thrallsCap--;
			}
			if (explodeTicks > 0) {
				if (--explodeTicks == 3) {
					setAnimation(EXPLODE_ANIM);
				} else if (explodeTicks == 1) {
					World.sendGraphics(EXPLODE_FIRE_GFX, getLocation());
					World.sendGraphics(EXPLODE_BODY_GFX, getLocation());
					World.sendSoundEffect(getLocation(), EXPLODE_SOUND);
					for (Player p : encounter.getChallengePlayers(p -> p.getLocation().getTileDistance(getLocation()) <= 1)) {
						p.applyHit(new Hit(this, getMaxHit(14) + Utils.random(2), HitType.DEFAULT));
					}
				} else if (explodeTicks <= 0) {
					encounter.onDeath(this);
					finish();
				}
			}
			if (id == ApmekenEncounter.BABOON_CURSED_ID) {
				if (preventRandomWalkTicks > 0 && --preventRandomWalkTicks > 0) {
					return;
				}
				if (Arrays.stream(ApmekenEncounter.VENT_LOCATIONS).noneMatch(loc -> encounter.getLocation(loc).equals(getLocation()))) {
					encounter.attemptPlacePool(getLocation());
				}
				if (routeEvent != null || !getWalkSteps().isEmpty() || isLocked() || isFrozen()) {
					return;
				}
				for (int i = 0; i < 5 && !hasWalkSteps(); i++) {
					final int moveX = Utils.random(-radius, radius);
					final int moveY = Utils.random(-radius, radius);
					final int respawnX = respawnTile.getX();
					final int respawnY = respawnTile.getY();
					addWalkStepsInteract(respawnX + moveX, respawnY + moveY, radius, getSize(), true);
				}
			}
		}
	}

	@Override public void autoRetaliate(Entity source) {
		if (id != ApmekenEncounter.BABOON_VOLATILE_ID && id != ApmekenEncounter.BABOON_CURSED_ID) {
			super.autoRetaliate(source);
		}
	}

	@Override public void sendDeath() {
		super.sendDeath();
		encounter.onDeath(this);
	}

	@Override public void setTarget(Entity target, TargetSwitchCause cause) {
		if (id != ApmekenEncounter.BABOON_CURSED_ID) {
			combat.setTarget(target, cause);
		}
	}

	@Override public void setRespawnTask() {}

	@Override public boolean checkAggressivity() {
		return explodeTicks > 0 || id == ApmekenEncounter.BABOON_CURSED_ID || super.checkAggressivity();
	}

	@Override public List<Entity> getPossibleTargets(EntityType type, int radius) {
		if (!possibleTargets.isEmpty()) {
			possibleTargets.clear();
		}
		final Player[] players = encounter.getChallengePlayers();
		final List<Entity> possibleTargets = new ArrayList<>();
		if (players.length > 0) {
			possibleTargets.add(players[players.length == 1 ? 0 : Utils.random(players.length - 1)]);
		}
		super.possibleTargets.addAll(possibleTargets);
		return possibleTargets;
	}

	@Override public float getPointMultiplier() {
		return 1.2F;
	}

	@Override public boolean isEntityClipped() {
		return id == ApmekenEncounter.BABOON_CURSED_ID;
	}

	@Override public int attack(Entity target) {
		final int maxHit = getMaxHit(combatDefinitions.getAttackDefinitions().getMaxHit());
		switch(id) {
			case ApmekenEncounter.BABOON_MELEE_ID, ApmekenEncounter.BABOON_STRONGER_MELEE_ID, ApmekenEncounter.BABOON_THRALL_ID -> {
				setAnimation(MELEE_ANIM);
				final boolean accurate = CombatUtilities.getHitAccuracy(this, target, AttackType.CRUSH) >= Utils.randomDouble();
				delayHit(0, target, new Hit(this, accurate ? CombatUtilities.getAccurateRandomMaxHit(this, target, maxHit) : 0, HitType.MELEE));
				if (accurate && id == ApmekenEncounter.BABOON_THRALL_ID && target instanceof final Player player) {
					player.getPrayerManager().drainPrayerPoints(2);
				}
			}
			case ApmekenEncounter.BABOON_RANGE_ID , ApmekenEncounter.BABOON_STRONGER_RANGE_ID -> {
				setAnimation(RANGE_ANIM);
				delayHit(RANGE_PROJECTILE.getTime(this, target), target, new Hit(this, getRandomMaxHit(this, maxHit, AttackType.RANGED, target), HitType.RANGED));
				World.sendProjectile(this, target, RANGE_PROJECTILE);
			}
			case ApmekenEncounter.BABOON_MAGE_ID , ApmekenEncounter.BABOON_STRONGER_MAGE_ID, ApmekenEncounter.BABOON_SHAMAN_ID -> {
				if (id == ApmekenEncounter.BABOON_SHAMAN_ID && thrallsCap > 0) {
					setAnimation(THRALL_SUMMON_ANIM);
					World.sendSoundEffect(getLocation(), THRALL_SUMMON_SOUND);
					thrallSpawnTicks = 1;
				} else {
					setAnimation(MAGE_ANIM);
					final Hit hit = new Hit(this, getRandomMaxHit(this, maxHit, AttackType.MAGIC, target), HitType.MAGIC);
					if (id == ApmekenEncounter.BABOON_SHAMAN_ID) {
						hit.onLand(h -> {
							if (target instanceof final Player player) {
								player.getPrayerManager().drainPrayerPoints(hit.getDamage());
							}
						});
					}
					delayHit(MAGE_PROJECTILE.getTime(this, target), target, hit);
					World.sendProjectile(this, target, MAGE_PROJECTILE);
				}
			}
			case ApmekenEncounter.BABOON_VOLATILE_ID -> {
				setForceAggressive(false);
				setCantInteract(true);
				lock();
				getCombat().setTarget(null);
				explodeTicks = 4;
				return 99;
			}
		}
		return combatDefinitions.getAttackSpeed();
	}
}
