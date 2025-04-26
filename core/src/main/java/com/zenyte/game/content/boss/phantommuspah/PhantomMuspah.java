package com.zenyte.game.content.boss.phantommuspah;

import com.zenyte.game.content.boons.impl.IgnoranceIsBliss;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.content.xamphur.PhantomHandCorruptionKt;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.ProjectileUtils;
import com.zenyte.game.util.RSColour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.*;
import com.zenyte.game.world.entity.masks.*;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessorLoader;
import com.zenyte.game.world.entity.npc.drop.matrix.NPCDrops;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.utils.TimeUnit;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author Savions.
 */
public class PhantomMuspah extends NPC implements CombatScript {

	static final int MUSPAH_RANGE_ID = NpcId.PHANTOM_MUSPAH;
	static final int MUSPAH_MELEE_ID = NpcId.PHANTOM_MUSPAH_12078;
	static final int MUSPAH_SHIELD_ID = NpcId.PHANTOM_MUSPAH_12079;
	static final int MUSPAH_RANGE_FAST_ID = NpcId.PHANTOM_MUSPAH_12080;
	static final int MUSPAH_TELEPORT_ID = NpcId.PHANTOM_MUSPAH_12082;
	private static final Animation RANGE_ANIM = new Animation(9922);
	private static final Animation MELEE_ANIM = new Animation(9920);
	private static final Animation MAGE_ANIM = new Animation(9918);
	private static final Animation TRANSFORM_ANIM = new Animation(9937);
	private static final Animation TELEPORT_SWING_ANIM = new Animation(9928);
	private static final Animation TELEPORT_LAND_ANIM = new Animation(9930);
	private static final Animation TELEPORT_END_ANIM = new Animation(9932);
	private static final Animation GROUND_ATTACk_ANIM = new Animation(9925);
	private static final Graphics RANGE_IMPACT_GFX = new Graphics(2293, 60, 0);
	private static final Graphics MAGE_SWING_GFX = new Graphics(2319);
	private static final Graphics TRANSFORM_GFX = new Graphics(2334);
	private static final Graphics TELEPORT_SWING_GFX = new Graphics(2332);
	private static final Graphics TELEPORT_LAND_GFX = new Graphics(2333);
	private static final Graphics TELEPORT_END_GFX = new Graphics(2234);
	private static final Graphics GROUND_ATTACK_GFX = new Graphics(2322);
	private static final Graphics GROUND_HIT_GFX = new Graphics(2323);
	private static final SoundEffect MELEE_SOUND = new SoundEffect(6674, 1, 30);
	private static final SoundEffect TELEPORT_LAND_SOUND = new SoundEffect(6820);
	private static final SoundEffect MAGE_THROW_SOUND = new SoundEffect(6730, 1, 24);
	private static final SoundEffect[] RANGE_SWING_SOUNDS = {new SoundEffect(6705, 1, 2), new SoundEffect(6787, 1, 21),
			new SoundEffect(6692, 1, 22), new SoundEffect(6774, 1, 60)};
	private static final SoundEffect[] MAGE_SWING_SOUNDS = {new SoundEffect(6731, 1, 2), new SoundEffect(6680, 1, 3),
			new SoundEffect(6813, 1, 11), new SoundEffect(6662, 1, 22), new SoundEffect(6846, 1, 80), new SoundEffect(6843, 1, 81),
			new SoundEffect(6802, 1, 83), new SoundEffect(6753, 1, 87), new SoundEffect(6685, 1, 88)};
	private static final SoundEffect[] TELEPORT_SWING_SOUNDS = {new SoundEffect(6717, 1, 11),
			new SoundEffect(6732, 1, 12), new SoundEffect(6759, 1, 13), new SoundEffect(6724, 1, 14)};
	private static final SoundEffect[] GROUND_ATTACK_CHARGE_SOUNDS = {new SoundEffect(6744, 1, 2), new SoundEffect(6828, 1, 3),
			new SoundEffect(6707, 1, 19), new SoundEffect(6796, 1, 29), new SoundEffect(6843, 1, 126), new SoundEffect(6753, 1, 137),
			new SoundEffect(6685, 1, 140), new SoundEffect(6807, 1, 164)};
	private static final SoundEffect GROUND_SMASH_SOUND = new SoundEffect(6817);
	private static final SoundEffect SOULSPLIT_SOUND = new SoundEffect(5237);
	private static final SoundEffect SHIELD_DOWN_SOUND = new SoundEffect(6805);
	private static final Projectile RANGE_PROJECTILE = new Projectile(2329, 32, 25, 26, 5, 34, 244, 0);
	private static final Projectile MAGE_PROJECTILE = new Projectile(2327, 100, 24, 24, 5, 30, 0, 0);
	private static final Projectile SOULSPLIT_PROJECTILE = new Projectile(2009, 30, 47, 0, 5, 50, 64, 0);
	private static final RSColour[] SHIELD_HUD_COLOURS = { new RSColour(2, 1, 4), new RSColour(7, 4, 13), new RSColour(14, 8, 25) };
	private static final int[][] TELEPORT_CIR = {{38, 33}, {24, 39}, {29, 25}, {36, 39}, {22, 33}, {35, 27}, {30, 38}, {23, 26}, {38, 33}, {24, 39}, {29, 25},
			{36, 39}, {22, 33}, {35, 27}, {30, 38}, {23, 26}, {38, 33}, {24, 39}, {29, 25} };
	private static final int[] TELEPORT_END_CIR = {36, 39};
	private static final int[] MIDDLE_CIR = { 29, 31 };
	private static final int PURPLE_GROUND_GFX_ID = 2330;
	private static final int WHITE_GROUND_GFX_ID = 2331;
	private final PhantomInstance instance;
	private final int originalId;
	private boolean meleeHit = true;
	private int phaseHealth = 100;
	private Runnable nextSpecial;
	private int specialIndex = 0;
	private boolean specialActive;
	private int attackCount = 4;
	private int previousLifePoints;
	private ShieldHitBar shieldHitBar;
	private int shieldDamageTicks = 6;
	private boolean playerRan;
	private boolean onlySalamander = true;
	private boolean takenAvoidableDamage;

	public PhantomMuspah(int id, Location tile, PhantomInstance instance) {
		super(id, tile, true);
		setMaxDistance(200);
		setForceAggressive(true);
		setDamageCap(100);
		this.originalId = id;
		this.instance = instance;
		possibleTargets.add(instance.getPlayer());
		instance.getPlayer().getHpHud().open(id, getMaxHitpoints());
	}

	@Override public void sendDeath() {
		if (id != MUSPAH_RANGE_FAST_ID) {
			setHitpoints(1);
			startLastPhase();
			return;
		}
		super.sendDeath();
	}


	@Override
	public void onFinish(final Entity source) {
		super.onFinish(source);
		if (source instanceof final Player player) {
			player.getCombatAchievements().checkKcTask("phantom muspah", 1, CAType.PHANTOM_MUSPAH_ADEPT);
			player.getCombatAchievements().checkKcTask("phantom muspah", 25, CAType.PHANTOM_MUSPAH_VETERAN);
			player.getCombatAchievements().checkKcTask("phantom muspah", 50, CAType.PHANTOM_MUSPAH_MASTER);
			final boolean surroundedBySpikes = instance.surroundedBySpikes(getLocation(), getSize() + 1);
			if (surroundedBySpikes) {
				player.getCombatAchievements().complete(CAType.SPACE_IS_TIGHT);
			}
			if (!takenAvoidableDamage) {
				player.getCombatAchievements().complete(CAType.WALK_STRAIGHT_PRAY_TRUE);
			}
			if (!playerRan) {
				player.getCombatAchievements().complete(CAType.CANT_ESCAPE);
			}
			if (onlySalamander) {
				player.getCombatAchievements().complete(CAType.MORE_THAN_JUST_A_RANGED_WEAPON);
			}
			if (!playerRan && !takenAvoidableDamage && !surroundedBySpikes) {
				player.getCombatAchievements().complete(CAType.PHANTOM_MUSPAH_MANIPULATOR);
			}
			if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - player.getBossTimer().getCurrentTracker()) < 180) {
				player.getCombatAchievements().complete(CAType.PHANTOM_MUSPAH_SPEED_TRIALIST);
			}
			if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - player.getBossTimer().getCurrentTracker()) < 120) {
				player.getCombatAchievements().complete(CAType.PHANTOM_MUSPAH_SPEED_CHASER);
			}
			if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - player.getBossTimer().getCurrentTracker()) < 90) {
				player.getCombatAchievements().complete(CAType.PHANTOM_MUSPAH_SPEED_RUNNER);
			}
			if (player.getTemporaryAttributes().containsKey(PhantomInstance.CA_TASK_INSTANCE_STARTED)) {
				int kc = player.getNumericTemporaryAttribute(PhantomInstance.CA_TASK_INSTANCE_KC_ATT).intValue() + 1;
				if (kc >= 10) {
					player.getCombatAchievements().complete(CAType.ESSENCE_FARMER);
				} else {
					player.incrementNumericTemporaryAttribute(PhantomInstance.CA_TASK_INSTANCE_KC_ATT, 1);
				}
			}
		}
		instance.muspahFinished();
	}

	@Override public void setUnprioritizedAnimation(Animation animation) {
		return;
	}

	@Override public void processNPC() {
		if (instance.getPlayer() != null && instance.getPlayer().isRun()) {
			playerRan = true;
		}
		if (shieldHitBar != null && --shieldDamageTicks <= 0) {
			shieldDamageTicks = 6;
			applyHit(new Hit(this, 2, HitType.SHIELD_DOWN));
		}
		if (!specialActive) {
			super.processNPC();
		}
		meleeHit = id == MUSPAH_MELEE_ID && (isFrozen() || !hasWalkSteps());
	}

	@Override protected void postHitProcess(Hit hit) {
		super.postHitProcess(hit);
		instance.getPlayer().getHpHud().updateValue(shieldHitBar != null ? shieldHitBar.getHitPoints() : getHitpoints());
		if ((id == MUSPAH_RANGE_ID || id == MUSPAH_MELEE_ID) && !specialActive) {
			phaseHealth -= hit.getDamage();
			if (phaseHealth <= 0) {
				setNextTransformation();
				return;
			}
			if (hitpoints <= 150) {
				specialIndex = 2;
				sendMiddleAttack();
			} else if (id == originalId && specialIndex == 0 && hitpoints <= 650) {
				specialIndex = 1;
				nextSpecial = originalId == MUSPAH_RANGE_ID ? this::sendTeleportSpecial : instance::sendMovingSpikes;
				combat.setCombatDelay(Math.min(combat.getCombatDelay(), 3));
			} else if (id != originalId && specialIndex == 1 && hitpoints <= 450) {
				specialIndex = 2;
				nextSpecial = originalId == MUSPAH_RANGE_ID ? instance::sendMovingSpikes : this::sendTeleportSpecial;
				combat.setCombatDelay(Math.min(combat.getCombatDelay(), 3));
			}
		}
	}

	private void setNextTransformation() {
		setTransformation(id == MUSPAH_RANGE_ID ? MUSPAH_MELEE_ID : MUSPAH_RANGE_ID);
		phaseHealth = 100;
		instance.spawnNextSpikes();
		combat.setCombatDelay(combatDefinitions.getAttackSpeed());
		setAnimation(TRANSFORM_ANIM);
		setGraphics(TRANSFORM_GFX);
		lock(1);
	}

	private void sendTeleportSpecial() {
		if (instance.getPlayers().size() < 1) {
			return;
		}
		startTeleport();
		WorldTasksManager.schedule(new WorldTask() {
			int ticks;
			@Override public void run() {
				if (instance.getMuspah() == null || instance.getMuspah().isFinished() || instance.getPlayers().size() < 1) {
					stop();
					return;
				}
				if (ticks < PhantomOrb.SPAWN_LOCATIONS.length && PhantomOrb.SPAWN_LOCATIONS[ticks] != null) {
					for (final Location tile : PhantomOrb.SPAWN_LOCATIONS[ticks]) {
						final Location spawnLoc = instance.getLocation(tile);
						if (instance.isSpikeTileBlocked(spawnLoc)) {
							continue;
						}
						final int directionIndx = (Direction.values.length - 1) - ((ticks + 7) % Direction.values.length);
						final PhantomOrb orb = new PhantomOrb(spawnLoc, instance, Direction.values[directionIndx]);
						orb.lock(1);
						orb.spawn();
					}
				}
				if (ticks == 0) {
					setTransformation(PhantomMuspah.MUSPAH_TELEPORT_ID);
				}
				if (ticks < 19) {
					setAnimation(TELEPORT_LAND_ANIM);
					setGraphics(TELEPORT_LAND_GFX);
					playSound(TELEPORT_LAND_SOUND);
					setLocation(instance.getLocationByIR(TELEPORT_CIR[ticks]));
				} else if (ticks == 19) {
					setLocation(instance.getLocationByIR(TELEPORT_END_CIR));
					setTransformation(PhantomMuspah.MUSPAH_RANGE_ID);
					setAnimation(TELEPORT_END_ANIM);
					setGraphics(TELEPORT_END_GFX);
					getCombat().setCombatDelay(3);
				} else {
					setForceAggressive(true);
					specialActive = false;
					instance.getPlayer().getPacketDispatcher().sendClientScript(7021, 255);
					stop();
				}
				ticks++;
			}
		}, 2, 0);
	}

	private void sendMiddleAttack() {
		if (instance.getPlayers().size() < 1) {
			return;
		}
		startTeleport();
		setCantInteract(true);
		WorldTasksManager.schedule(new WorldTask() {
			int ticks;
			@Override public void run() {
				if (instance.getMuspah() == null || instance.getMuspah().isFinished() || instance.getPlayers().size() < 1) {
					stop();
					return;
				}
				if (++ticks == 11 || id == MUSPAH_RANGE_FAST_ID) {
					setForceAggressive(true);
					if (id != MUSPAH_RANGE_FAST_ID) {
						if(instance.getPlayer() != null) {
							boolean hasBoon = instance.getPlayer().hasBoon(IgnoranceIsBliss.class);
							if(hasBoon)
								startLastPhase();
							else
								startShieldPhase();
						}
					}
					specialActive = false;
					getCombat().setTarget(instance.getPlayer());
					instance.getPlayer().getPacketDispatcher().sendClientScript(7021, 255);
					stop();
				} else if (ticks == 1) {
					setLocation(instance.getLocationByIR(MIDDLE_CIR));
					setAnimation(TELEPORT_END_ANIM);
					setGraphics(TELEPORT_END_GFX);
				} else if (ticks == 3) {
					setAnimation(GROUND_ATTACk_ANIM);
					setGraphics(GROUND_ATTACK_GFX);
					playSound(GROUND_ATTACK_CHARGE_SOUNDS);
				} else if (ticks == 8) {
					setCantInteract(false);
					setGraphics(GROUND_HIT_GFX);
					playSound(GROUND_SMASH_SOUND);
					instance.setSpikesBlockProjectiles(true);
					final Location middleTile = getMiddleLocation();
					if (middleTile.getTileDistance(instance.getPlayer().getLocation()) < 2) {
						instance.getPlayer().applyHit(new Hit(PhantomMuspah.this, Utils.random(40, 80), HitType.DEFAULT));
					}
					for (int radius = 2; radius < 15; radius++) {
						for (int x = -radius; x <= radius; x++) {
							for (int y = -radius; y <= radius; y++) {
								if (x != radius && x != -radius && y != radius && y != -radius) {
									continue;
								}
								final Location tile = middleTile.transform(x, y);
								if (World.isFloorFree(tile, 1) && !ProjectileUtils.isProjectileClipped(null, null, middleTile, tile, false)) {
									final int dx = Math.abs(tile.getX() - middleTile.getX());
									final int dy = Math.abs(tile.getY() - middleTile.getY());
									int id = WHITE_GROUND_GFX_ID;
									int delay = (dx + dy) * 3;
									if ((radius == 2 && dx != 0 && dy != 0) || (dx + dy >= 5 && dx + dy <= 8)) {
										delay += 5;
										id = PURPLE_GROUND_GFX_ID;
									}
									World.sendGraphics(new Graphics(id, delay, 0), tile);
									if (tile.equals(instance.getPlayer().getLocation())) {
										instance.getPlayer().applyHit(new Hit(PhantomMuspah.this, Utils.random(40, 80), HitType.DEFAULT));
										takenAvoidableDamage = true;
									}
								}
							}
						}
					}
					instance.setSpikesBlockProjectiles(false);
				}
			}
		}, 2, 0);
	}

	private void startTeleport() {
		setAnimation(TELEPORT_SWING_ANIM);
		setGraphics(TELEPORT_SWING_GFX);
		specialActive = true;
		setForceAggressive(false);
		getCombat().setTarget(null);
		playSound(TELEPORT_SWING_SOUNDS);
		instance.getPlayer().sendMessage(PhantomInstance.DARKNESS_MESSAGE);
		instance.getPlayer().getPacketDispatcher().sendClientScript(7021, 130);
	}

	@Override public void handleIngoingHit(Hit hit) {
		if (!HitType.SHIELD_DOWN.equals(hit.getHitType()) && !HitType.HEALED.equals(hit.getHitType())) {
			if (!(hit.getWeapon() instanceof Item) || !((Item) hit.getWeapon()).getName().toLowerCase().contains("salamander")) {
				onlySalamander = false;
			}
		}
		if (shieldHitBar != null) {
			if(hit.getAttribute("sapphire-proc") != null && hit.getAttribute("sapphire-proc") == "true") {
				hit.setHitType(HitType.SHIELD_DOWN);
			}
			if (!HitType.SHIELD_DOWN.equals(hit.getHitType()) && !HitType.HEALED.equals(hit.getHitType())) {
				hit.setDamage(0);
			} else if (HitType.SHIELD_DOWN.equals(hit.getHitType())) {
				hit.setDamage(shieldHitBar.damage(hit.getDamage()));
				if (shieldHitBar.getHitPoints() <= 0) {
					setHitpoints(previousLifePoints);
					setDamageCap(100);
					startLastPhase();
					return;
				} else {
					getHitBars().add(hitBar);
					getUpdateFlags().flag(UpdateFlag.HIT);
				}
			} else {
				previousLifePoints += hit.getDamage();
				if (previousLifePoints > getMaxHitpoints()) {
					previousLifePoints = getMaxHitpoints();
				}
			}
		}
		super.handleIngoingHit(hit);
	}

	private void startShieldPhase() {
		setDamageCap(300);
		previousLifePoints = getHitpoints();
		setTransformation(MUSPAH_SHIELD_ID);
		getCombat().setCombatDelay(getCombatDefinitions().getAttackSpeed());
		getHitBars().clear();
		getHitBars().add(new RemoveHitBar(hitBar.getType()));
		super.hitBar = shieldHitBar = new ShieldHitBar();
		getHitBars().add(shieldHitBar);
		getUpdateFlags().flag(UpdateFlag.HIT);
		instance.getPlayer().getHpHud().updateMaxValue(75);
		instance.getPlayer().getHpHud().updateValue(75);
		instance.getPlayer().getHpHud().sendColorChange(SHIELD_HUD_COLOURS[0], SHIELD_HUD_COLOURS[1], SHIELD_HUD_COLOURS[2]);
	}

	@Override protected void drop(Location tile) {
		final Player killer = getDropRecipient();
		if (killer == null) {
			return;
		}
		onDrop(killer);
		instance.getPlayer().getBossTimer().finishTracking("phantom muspah");
		final List<DropProcessor> processors = DropProcessorLoader.get(MUSPAH_RANGE_FAST_ID);
		if (processors != null) {
			for (final DropProcessor processor : processors) {
				processor.onDeath(this, killer);
			}
		}
		final NPCDrops.DropTable drops = NPCDrops.getTable(getId());
		if (drops == null) {
			return;
		}
		NPCDrops.rollTable(killer, drops, drop -> dropItem(killer, drop, tile));
		NPCDrops.rollTable(killer, drops, drop -> {
			if (!drop.isAlways()) {
				dropItem(killer, drop, tile, false);
			}
		});
		if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - killer.getBossTimer().getCurrentTracker()) <= 180
				&& !killer.containsItem(ItemId.CHARGED_ICE) && killer.getNumericAttribute("Muspah charged ice").intValue() < 1) {
			dropItem(killer, new Item(ItemId.CHARGED_ICE), tile, false);
		}
	}

	public void dropItem(final Player killer, final Drop drop, final Location location, final boolean uniques) {
		Item item = new Item(drop.getItemId(), Utils.random(drop.getMinAmount(), drop.getMaxAmount()));
		if (uniques) {
			final List<DropProcessor> processors = DropProcessorLoader.get(MUSPAH_RANGE_FAST_ID);
			if (processors != null) {
				final Item baseItem = item;
				for (final DropProcessor processor : processors) {
					if ((item = processor.drop(this, killer, drop, item)) == null) {
						return;
					}
					if (item != baseItem) break;
				}
			}
		}
		//do NOT reference 'drop' after this line, rely on 'item' only!
		dropItem(killer, item, location, drop.isAlways());
	}


	private void handleSoulSplit(final Hit hit) {
		if (shieldHitBar == null || isFinished() || instance.getPlayers().size() < 1) {
			return;
		}
		final int heal = hit.getDamage() / 2;
		if (heal > 0) {
			playSound(SOULSPLIT_SOUND);
			World.sendProjectile(instance.getPlayer(), this, SOULSPLIT_PROJECTILE);
			delayHit(0, this, new Hit(this, heal, HitType.HEALED));
		}
	}

	private void startLastPhase() {
		setTransformation(MUSPAH_RANGE_FAST_ID);
		instance.spawnNextSpikes();
		shieldHitBar = null;
		getHitBars().clear();
		getHitBars().add(new RemoveHitBar(hitBar.getType()));
		super.hitBar = new EntityHitBar(this);
		getHitBars().add(super.hitBar);
		getUpdateFlags().flag(UpdateFlag.HIT);
		instance.getPlayer().getHpHud().updateMaxValue(getMaxHitpoints());
		instance.getPlayer().getHpHud().updateValue(getHitpoints());
		instance.getPlayer().getHpHud().resetColors();
		playSound(SHIELD_DOWN_SOUND);
	}

	private void playSound(@NotNull final SoundEffect... sounds) {
		for (SoundEffect sound : sounds) {
			instance.getPlayer().getPacketDispatcher().sendSoundEffect(sound);
		}
	}

	@Override public float getXpModifier(Hit hit) {
		return 1.075F;
	}

	@Override
	public void setRespawnTask() {

	}

	@Override public boolean checkAggressivity() {
		return specialActive || super.checkAggressivity();
	}

	@Override public boolean isIntelligent() {
		return true;
	}

	@Override public List<Entity> getPossibleTargets(final EntityType type) {
		return possibleTargets;
	}

	@Override
	public void autoRetaliate(Entity source) {
		if (!specialActive) {
			super.autoRetaliate(source);
		}
	}

	@Override public void onDrop(@NotNull Player killer) {
		super.onDrop(killer);
	}

	@Override public double getMeleePrayerMultiplier() {
		return 0.7;
	}

	@Override public double getMagicPrayerMultiplier() {
		return 0.15;
	}

	@Override public double getRangedPrayerMultiplier() {
		return 0.1;
	}

	@Override public void finish() {
		super.finish();
		instance.getPlayer().getHpHud().close();
	}

	@Override public int attack(Entity target) {
		if (nextSpecial != null) {
			nextSpecial.run();
			nextSpecial = null;
			return getCombatDefinitions().getAttackSpeed();
		}
		if (id == MUSPAH_RANGE_FAST_ID && --attackCount <= 0) {
			attackCount = 4;
			instance.spawnNextSpikes();
		}
		switch(id) {
			case MUSPAH_MELEE_ID:
				setAnimation(MELEE_ANIM);
				playSound(MELEE_SOUND);
				final Hit hit = new Hit(this, meleeHit ? getRandomMaxHit(this, 34, MELEE, AttackType.STAB, target) : 0, HitType.MELEE);
				delayHit(0, target, hit);
				if (hit.getDamage() > 0) {
					takenAvoidableDamage = true;
				}
				break;
			case MUSPAH_RANGE_ID:
			case MUSPAH_SHIELD_ID:
			case MUSPAH_RANGE_FAST_ID:
				if (Utils.random(id == MUSPAH_RANGE_ID ? 4 : 3) == 0) {
					setAnimation(MAGE_ANIM);
					setGraphics(MAGE_SWING_GFX);
					playSound(MAGE_SWING_SOUNDS);
					WorldTasksManager.schedule(new WorldTask() {
						private int ticks;
						boolean applyCorruption;
						boolean missed;
						@Override public void run() {
							if (instance.getPlayer().isFinished() || !(instance.getPlayer().getArea() instanceof PhantomInstance) || instance.getPlayers().size() < 1) {
								stop();
								return;
							}
							if (++ticks == 1) {
								World.sendProjectile(PhantomMuspah.this, target, MAGE_PROJECTILE);
								missed = instance.getPlayer().getPrayerManager().isActive(Prayer.PROTECT_FROM_MAGIC);
								if (missed) {
									delayHit(1, target, new Hit(PhantomMuspah.this, 0, HitType.MAGIC));
								}
							} else if (ticks == 2) {
								playSound(MAGE_THROW_SOUND);
								if (!missed) {
									final Hit hit = new Hit(PhantomMuspah.this, getRandomMaxHit(PhantomMuspah.this, id == MUSPAH_SHIELD_ID ? 79 : 72, AttackType.MAGIC, target), HitType.MAGIC).onLand(PhantomMuspah.this::handleSoulSplit);
									delayHit(0, target, hit);
									if (hit.getDamage() > 0) {
										applyCorruption = true;
										takenAvoidableDamage = true;
									}
								}
							} else if (ticks == 3) {
								if (applyCorruption) {
									PhantomHandCorruptionKt.applyCorruptionEffect(instance.getPlayer()); //TODO make corruption core mechanic
								}
								stop();
							}
						}
					}, 1, 0);
				} else {
					setAnimation(RANGE_ANIM);
					playSound(RANGE_SWING_SOUNDS);
					target.setGraphics(RANGE_IMPACT_GFX);
					World.sendProjectile(this, target, RANGE_PROJECTILE);
					final Hit rangeHit = new Hit(this, getRandomMaxHit(this, id == MUSPAH_SHIELD_ID ? 72 : 61, RANGED, AttackType.RANGED, target), HitType.RANGED).onLand(PhantomMuspah.this::handleSoulSplit);
					delayHit(1, target, rangeHit);
					if (!instance.getPlayer().getPrayerManager().isActive(Prayer.PROTECT_FROM_MISSILES) && rangeHit.getDamage() > 0) {
						takenAvoidableDamage = true;
					}
				}
				break;
		}
		return getCombatDefinitions().getAttackSpeed();
	}

	public final ShieldHitBar getShieldHitBar() { return shieldHitBar; }

	void setTakenAvoidableDamage() { takenAvoidableDamage = true; }
}