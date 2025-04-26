package com.zenyte.game.content.tombsofamascut.npc;

import com.zenyte.game.content.tombsofamascut.InvocationType;
import com.zenyte.game.content.tombsofamascut.encounter.AkkhaEncounter;
import com.zenyte.game.content.tombsofamascut.raid.EncounterStage;
import com.zenyte.game.content.tombsofamascut.raid.TOARaidArea;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.*;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.object.WorldObject;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.stream.IntStream;

/**
 * @author Savions.
 */
public class Akkha extends TOANPC implements CombatScript {

	private static final int MELEE_ID = 11789;
	private static final int RANGE_ID = 11791;
	private static final int MAGE_ID = 11792;
	public static final int FINAL_ID = 11795;
	public static final SoundEffect[] QUADRANT_HIT_SOUNDS = {new SoundEffect(5591, 9), new SoundEffect(3887, 9), new SoundEffect(173, 9), new SoundEffect(156, 9)};
	private static final Location SPAWN_LOCATION = new Location(3673, 5407, 1);
	private static final Location POST_MEMEORY_LOCATION = new Location(3680, 5406, 1);
	private static final WorldObject[] QUADRANT_LIGHT_OBJECTS = {
			new WorldObject(45871, 10, 0, new Location(3682, 5409, 1)),
			new WorldObject(45869, 10, 0, new Location(3682, 5406, 1)),
			new WorldObject(45870, 10, 0, new Location(3679, 5406, 1)),
			new WorldObject(45868, 10, 0, new Location(3679, 5409, 1))
	};
	private static final Animation MELEE_ANIM = new Animation(9770);
	private static final Animation SWING_RANGE_SWORD_ANIM = new Animation(9772);
	private static final Animation SWING_MAGE_SWORD_ANIM = new Animation(9774);
	public static final Animation BECOME_INVISIBLE_ANIM = new Animation(9784);
	public static final Animation BECOME_VISIBLE_ANIM = new Animation(9785);
	private static final Animation GROUND_DETONATION_ANIM = new Animation(9776);
	private static final Animation GROUND_MEMORY_ANIM = new Animation(9777);
	private static final Animation GROUND_TRAIL_ANIM = new Animation(9778);
	private static final Animation FINAL_STAND_ANIM = new Animation(9779);
	private static final Projectile RANGE_PROJECTILE = new Projectile(2255, 101, 34, 0, 12, 30, 32, 0);
	private static final Projectile MAGE_PROJECTILE = new Projectile(2253, 101, 34, 0, 12, 30, 32, 0);
	private static final SoundEffect RANGE_IMPACT_SOUND = new SoundEffect(5640, 1, 30);
	private static final SoundEffect MAGE_IMPACT_SOUND = new SoundEffect(5774, 1, 30);
	private static final SoundEffect CHANGE_STYLE_SOUND = new SoundEffect(5585, 14);
	private static final SoundEffect QUADRANT_SYMBOL_ACTIVE_SOUND = new SoundEffect(5667);
	private static final Tinting PLAYER_TRAIL_TINT = new Tinting(0, 0, 0, 112, 0, 605);
	private static final Tinting PLAYER_DETONATION_TINT = new Tinting(8, 1, 100, 112, 0, 245);
	private static final Tinting INVULNERABLE_TINT = new Tinting(0, 0, 106, 40, 0, 600);
	public static final Tinting RESET_TINT = new Tinting(-1, -1, -1, 0, 0, 0);
	private final AkkhaEncounter encounter;
	private final boolean doubleTrouble;
	private final boolean keepBack;
	private final boolean stayVigilant;
	private final boolean feelingSpecial;
	private final int memoryIterations;
	private AttackType currentAttackType = AttackType.MELEE;
	private int attackCycles = Utils.random(5, 7);
	private int specialIndex = Utils.random(2);
	private int[] memorySet;
	private int memoryIndex;
	private Queue<Runnable> memoryActions = new ArrayDeque<>();
	private int memoryDelay = 0;
	private int trailTicks;
	private int detonationTicks;
	private int deathTicks;

	public Akkha(AkkhaEncounter encounter, int level) {
		super(MELEE_ID, encounter.getLocation(SPAWN_LOCATION), Direction.SOUTH, 64, encounter, level);
		super.hitBar = new EntityHitBar(this) {
			@Override public int getType() {
				return 19;
			}
		};
		setAggressionDistance(64);
		setMaxDistance(64);
		setAttackDistance(64);
		setForceFollowClose(true);
		forceCheckAggression = true;
		this.encounter = encounter;
		doubleTrouble = encounter.getParty().getPartySettings().isActive(InvocationType.DOUBLE_TROUBLE);
		keepBack = encounter.getParty().getPartySettings().isActive(InvocationType.KEEP_BACK);
		stayVigilant = encounter.getParty().getPartySettings().isActive(InvocationType.STAY_VIGILANT);
		feelingSpecial = encounter.getParty().getPartySettings().isActive(InvocationType.FEELING_SPECIAL);
		memoryIterations = 4 + Math.min(2, (level / 2));
	}

	@Override public void processNPC() {
		super.processNPC();
		if (deathTicks > 0 && --deathTicks <= 0) {
			setTransformation(FINAL_ID);
			applyHit(new Hit(this, (int) (getMaxHitpoints() * 0.2), HitType.HEALED));
			super.sendDeath();
			return;
		}
		if (isDead() || isFinished()) {
			return;
		}
		if (EncounterStage.STARTED.equals(encounter.getStage())) {
			setTinting(encounter.onVulnerableQuadrant(getMiddleLocation()) ? INVULNERABLE_TINT : RESET_TINT);
			if (detonationTicks > 0) {
				if (--detonationTicks == 8) {
					final Player[] players = encounter.getChallengePlayers();
					for (Player p : players) {
						if (p != null) {
							p.sendMessage("<col=a53fff>You have been marked for detonation!</col>");
							p.setTinting(PLAYER_DETONATION_TINT);
						}
					}
				} else if (detonationTicks <= 0) {
					final Player[] players = encounter.getChallengePlayers();
					final boolean cross = feelingSpecial && Utils.random(1) == 0;
					final List<Location> existingTiles = new ArrayList<Location>();
					for (Player p : players) {
						if (p != null) {
							final int index = encounter.getQuadrantIndex(p.getLocation());
							for (int i = 0; i < 4; i++) {
								final Direction explodeDirection = Direction.values[i * 2 + (cross ? 1 : 0)];
								Location tile = p.getLocation();
								for (int x = 0; x < 3; x++) {
									tile = tile.transform(explodeDirection.getOffsetX(), explodeDirection.getOffsetY());
									if (World.isFloorFree(tile, 1) && !existingTiles.contains(tile)) {
										existingTiles.add(tile);
										World.sendGraphics(new Graphics(AkkhaEncounter.QUADRANT_GFX_IDS[index], 0, index == 3 ? 124 : 0), tile);
										World.sendSoundEffect(tile, QUADRANT_HIT_SOUNDS[index]);
										for (Player p2 : players) {
											if (p2 != null && p2.getLocation().equals(tile)) {
												p2.applyHit(new Hit(Akkha.this, getMaxHit(15) + Utils.random(2), HitType.DEFAULT));
												encounter.addEffect(p2, index);
											}
										}
									}
								}
							}
						}
					}
				}
			}
			if (trailTicks > 0) {
				if (--trailTicks == 20) {
					final Player[] players = encounter.getChallengePlayers();
					for (Player p : players) {
						if (p != null) {
							p.sendMessage("<col=a53fff>Magical orbs begin to materialise behind you!</col>");
							p.setTinting(PLAYER_TRAIL_TINT);
							p.getTemporaryAttributes().put("akkha_last_player_location", new Location(p.getLocation()));
						}
					}
				} else if (trailTicks <= 0) {
					final Player[] players = encounter.getChallengePlayers();
					for (Player p : players) {
						if (p != null) {
							p.sendMessage("<col=229628>The magical orbs stop materialising.</col>");
						}
					}
				}
			}
			if (!memoryActions.isEmpty() && (memoryDelay <= 0 || --memoryDelay <= 0)) {
				while(!memoryActions.isEmpty()) {
					if (memoryDelay > 0) {
						break;
					}
					memoryActions.poll().run();
				}
			}
			if (getCombat().getTarget() != null) {
				if (EncounterStage.STARTED.equals(encounter.getStage()) && hasWalkSteps() && getCombat().getCombatDelay() == 0) {
					final Player[] players = encounter.getChallengePlayers();
					if (AttackType.MELEE.equals(currentAttackType)) {
						final int distance = getMiddleLocation().getTileDistance(getCombat().getTarget().getLocation());
						for (Player p : players) {
							if (CombatUtilities.isWithinMeleeDistance(this, p) && p.getLocation().getTileDistance(getCombat().getTarget().getLocation()) <= distance) {
								getCombat().setCombatDelay(attack(p));
								break;
							}
						}
					} else {
						getCombat().setCombatDelay(attack(getCombat().getTarget()));
					}
				}
			}
		}
		if (combat.getTarget() == null) {
			checkAggressivity();
			if (routeEvent != null || !getWalkSteps().isEmpty() || isLocked() || getCombat().getTarget() != null) {
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

	@Override public void processHit(Hit hit) {
		final int currentHitPoints = getHitpoints();
		super.processHit(hit);
		for (float basePercentage = .8F; basePercentage >= 0 && getHitpoints() > 0; basePercentage -= .2F) {
			final int threshold = (int) (getMaxHitpoints() * basePercentage);
			if (currentHitPoints > threshold && getHitpoints() <= threshold) {
				encounter.spawnShadows(basePercentage == .8F);
				break;
			}
		}
	}

	@Override public void listenScheduleHit(Hit hit) {
		if (!HitType.HEALED.equals(hit.getHitType())) {
			if (encounter.onVulnerableQuadrant(getMiddleLocation())) {
				if (hit.getSource() instanceof final Player player) {
					player.sendMessage("<col=ef0083>Akkha is currently immune to your attacks, but his shadows are still disrupted.</col>");
				}
				hit.setDamage(0);
			}
			if ((id == MELEE_ID && !HitType.MAGIC.equals(hit.getHitType())) || (id == RANGE_ID && !HitType.MELEE.equals(hit.getHitType()))
					|| (id == MAGE_ID && !HitType.RANGED.equals(hit.getHitType()))) {
				hit.setDamage(0);
			}
		}
		super.listenScheduleHit(hit);
	}

	@Override public boolean setHitpoints(int amount) {
		final boolean set = super.setHitpoints(amount);
		if (id != FINAL_ID && encounter != null && EncounterStage.STARTED.equals(encounter.getStage())) {
			encounter.getPlayers().forEach(p -> p.getHpHud().updateValue(hitpoints));
		}
		return set;
	}

	@Override public double getMagicPrayerMultiplier() { return .15; }

	@Override public double getRangedPrayerMultiplier() { return .15; }

	@Override public double getMeleePrayerMultiplier() { return .25; }

	@Override public boolean isCycleHealable() { return false; }

	@Override public float getPointMultiplier() {
		return 1;
	}

	private void showSymbolLight() {
		final WorldObject object = new WorldObject(QUADRANT_LIGHT_OBJECTS[memorySet[memoryIndex++ % 4]]);
		object.setLocation(encounter.getLocation(object.getLocation()));
		World.spawnTemporaryObject(object, 1);
		final Player[] players = encounter.getChallengePlayers();
		for (Player p : players) {
			if (p != null) {
				p.sendSound(QUADRANT_SYMBOL_ACTIVE_SOUND);
			}
		}
	}

	private void cleaveMeleeAttack(final Entity target) {
		final Player[] players = encounter.getChallengePlayers();
		for (Player p : players) {
			if (p != null && (target == null || p.getLocation().getTileDistance(target.getLocation()) <= 1) && CombatUtilities.isWithinMeleeDistance(this, p)) {
				delayHit(this, 0, p, new Hit(this, getRandomMaxHit(this, getMaxHit(22), AttackType.SLASH, p), HitType.MELEE));
			}
		}
	}

	private void changeAttackStyle() {
		getCombat().setTarget(null);
		World.sendSoundEffect(getMiddleLocation(), CHANGE_STYLE_SOUND);
		if (id == MELEE_ID) {
			setTransformation(RANGE_ID);
			currentAttackType = AttackType.RANGED;
		} else if (id == RANGE_ID) {
			setTransformation(MAGE_ID);
			currentAttackType = AttackType.MAGIC;
		} else if (id == MAGE_ID) {
			setTransformation(MELEE_ID);
			currentAttackType = AttackType.MELEE;
		}
	}

	public static final void sendProjectile(TOARaidArea area, final TOANPC shooter, boolean mage, CombatScript script) {
		shooter.setAnimation(mage ? SWING_MAGE_SWORD_ANIM : SWING_RANGE_SWORD_ANIM);
		WorldTasksManager.schedule(area.addRunningTask(() -> {
			if (!shooter.isDead() && !shooter.isFinished()) {
				final Player[] players = area.getChallengePlayers();
				for (Player p : players) {
					if (p != null) {
						World.sendProjectile(shooter, p, mage ? MAGE_PROJECTILE : RANGE_PROJECTILE);
						p.sendSound(mage ? MAGE_IMPACT_SOUND : RANGE_IMPACT_SOUND);
						script.delayHit(shooter, 0, p, new Hit(shooter, script.getRandomMaxHit(shooter, shooter.getMaxHit(22), mage ? AttackType.MAGIC : AttackType.RANGED, p), mage ? HitType.MAGIC : HitType.RANGED));
					}
				}
			}
		}), 1);
	}

	@Override public void sendDeath() {
		setAnimation(FINAL_STAND_ANIM);
		final Player[] players = encounter.getChallengePlayers();
		for (Player p : players) {
			if (p != null) {
				p.sendMessage("<col=ef0083>Akkha makes his final stand!</col>");
			}
		}
		for (AkkhaShadow shadow : encounter.getShadows()) {
			if (shadow != null && !shadow.isDying() && !shadow.isFinished()) {
				shadow.sendDeath();
			}
		}
		getCombat().setTarget(null);
		setCantInteract(true);
		resetWalkSteps();
		deathTicks = 3;
	}

	@Override protected void onFinish(Entity source) {
		encounter.startLastPhase();
		super.onFinish(source);
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
			int defenceRoll = Integer.MAX_VALUE;
			final Location middleTile = getMiddleLocation();
			for (Player p : players) {
				if (p != null) {
					final int pDistance = middleTile.getTileDistance(p.getLocation());
					final int targetRoll = PlayerCombat.getTargetDefenceRoll(this, p, currentAttackType);
					if (pDistance < distance || (pDistance == distance && targetRoll < defenceRoll)) {
						target = p;
						distance = pDistance;
						defenceRoll = targetRoll;
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

	@Override public boolean checkAggressivity() {
		return deathTicks > 0 || !memoryActions.isEmpty() ||  super.checkAggressivity();
	}

	private void addMemoryDelay(int delay) {
		memoryDelay += delay;
	}

	private void performMemoryBlast() {
		lock();
		resetWalkSteps();
		setAnimation(GROUND_MEMORY_ANIM);
		memorySet = new int[4];
		memoryIndex = 0;
		memorySet[0] = Utils.random(3);
		for (int i = 1; i < 4; i++) {
			memorySet[i] = memorySet[i - 1] + (Utils.random(1) == 0 ? -1 : 1);
			if (memorySet[i] < 0) {
				memorySet[i] = 3;
			} else if (memorySet[i] > 3) {
				memorySet[i] = 0;
			}
		}
		memoryDelay = 2;
		memoryActions.add(() -> {
			final Player[] players = encounter.getChallengePlayers();
			for (Player p : players) {
				if (p != null) {
					p.sendMessage("<col=6800bf>Sections of the room start to glow...</col>");
				}
			}
			addMemoryDelay(1);
		});
		memoryActions.add(() -> {
			setCantInteract(true);
			blockIncomingHits(16);
		    resetWalkSteps();
			setAnimation(BECOME_INVISIBLE_ANIM);
			addMemoryDelay(1);
		});
		memoryActions.add(() -> {
			setLocation(encounter.getLocation(POST_MEMEORY_LOCATION).transform(0, 0, -1));
		    showSymbolLight();
			addMemoryDelay(2);
		});
		for (int i = 1; i < memoryIterations; i++) {
			final int index = i;
			memoryActions.add(() -> {
				showSymbolLight();
				if (!feelingSpecial || index != memoryIterations - 1) {
					addMemoryDelay(2);
				}
			});
		}
		for (int i = 0; i < memoryIterations; i++) {
			final int index = i;
			memoryActions.add(() -> {
				encounter.sendQuadrantBlasts(false, IntStream.rangeClosed(0, 3).filter(q -> q != memorySet[index]).toArray());
				if (index == memoryIterations - 1) {
					addMemoryDelay(3);
				} else {
					addMemoryDelay(feelingSpecial ? 2 : 4);
				}
			});
		}
		memoryActions.add(() -> {
			setLocation(encounter.getLocation(POST_MEMEORY_LOCATION));
			setAnimation(BECOME_VISIBLE_ANIM);
			addMemoryDelay(1);
		});
		memoryActions.add(() -> {
			getCombat().setCombatDelay(getCombatDefinitions().getAttackSpeed());
			setCantInteract(false);
			unlock();
		});
	}

	@Override public void setUnprioritizedAnimation(Animation animation) {}

	@Override public void autoRetaliate(Entity source) { }

	@Override public boolean isEntityClipped() { return false; }

	@Override public boolean isIntelligent() { return true; }

	@Override public void setRespawnTask() {}

	@Override public boolean isFrozen() { return false; }

	@Override public boolean isStunned() { return false; }

	@Override public void heal(int amount) {}

	@Override public int attack(Entity target) {
		if (isDying() || deathTicks > 0 || (id == MELEE_ID && !isWithinMeleeDistance(this, target))) {
			return 0;
		}
		if (attackCycles > 0 && --attackCycles <= 0) {
			attackCycles = Utils.random(5, 7);
			if (doubleTrouble) {
				specialIndex %= encounter.getChallengePlayers().length < 2 ? 1 : 2;
				if (specialIndex == 0) {
					performMemoryBlast();
				} else {
					setAnimation(GROUND_DETONATION_ANIM);
					detonationTicks = 10;
				}
				trailTicks = 22;
			} else {
				specialIndex %= encounter.getChallengePlayers().length < 2 ? 2 : 3;
				if (specialIndex == 0) {
					performMemoryBlast();
				} else if (specialIndex == 1) {
					setAnimation(GROUND_TRAIL_ANIM);
					trailTicks = 22;
				} else if (specialIndex == 2) {
					setAnimation(GROUND_DETONATION_ANIM);
					detonationTicks = 10;
				}
			}
			specialIndex++;
			if (!stayVigilant || Utils.random(2) == 0) {
				changeAttackStyle();
			}
		} else {
			switch (currentAttackType) {
				case MELEE -> {
					setAnimation(MELEE_ANIM);
					cleaveMeleeAttack(getCombat().getTarget());
				}
				case RANGED -> {
					sendProjectile(encounter, this, false, this);
					if (keepBack) {
						cleaveMeleeAttack(null);
					}
				}
				case MAGIC -> {
					sendProjectile(encounter, this, true, this);
					if (keepBack) {
						cleaveMeleeAttack(null);
					}
				}
			}
			if (stayVigilant && Utils.random(2) == 0) {
				World.sendSoundEffect(getMiddleLocation(), CHANGE_STYLE_SOUND);
				changeAttackStyle();
			}
		}
		return getCombatDefinitions().getAttackSpeed();
	}

	public void resetEffects() {
		trailTicks = 0;
		detonationTicks = 0;
	}

	public boolean performingMemoryBlast() { return !memoryActions.isEmpty(); }
	public boolean isTrailing() { return trailTicks > 0 && trailTicks <= 20; }
}
