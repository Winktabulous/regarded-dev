package com.zenyte.game.content.tombsofamascut.npc;

import com.near_reality.game.world.entity.TargetSwitchCause;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.content.tombsofamascut.InvocationType;
import com.zenyte.game.content.tombsofamascut.encounter.ZebakEncounter;
import com.zenyte.game.content.tombsofamascut.raid.EncounterStage;
import com.zenyte.game.content.tombsofamascut.raid.TOAPathType;
import com.zenyte.game.model.CameraShakeType;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.*;
import com.zenyte.game.world.entity.masks.*;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.pathfinding.Flags;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
import com.zenyte.game.world.object.WorldObject;

import java.util.*;

/**
 * @author Savions.
 */
public class Zebak extends TOANPC implements CombatScript {

	public static final int JUG_ID = 11735;
	private static final int ZEBAK_TAIL_ID = 11731;
	private static final int BOULDER_ID = 11737;
	private static final int WAVE_ID = 11738;
	private static final int WATER_CROCODILE_ID = 11741;
	private static final int BLOOD_CLOUD_NPC_ID = 11742;
	private static final int PROJECTILE_NPC_ID = 11744;
	public static final int BOULDER_BLOCK_ID = 43876;
	private static final int FLOOR_ROAR_GFX_ID = 2184;
	private static final Location SPAWN_LOCATION = new Location(3918, 5404);
	private static final Location TAIL_SPAWN_LOCATION = new Location(3909, 5403);
	private static final Location PROJECTILE_NPC_LOCATION = new Location(3930, 5405);
	private static final Location GROUND_MIN_LOCATION = new Location(3926, 5398);
	private static final Location GROUND_MAX_LOCATION = new Location(3942, 5418);
	private static final Location BOULDER_MIN_LOCATION = new Location(3927, 5400);
	private static final Location BOULDER_MAX_LOCATION = new Location(3937, 5414);
	private static final Location ZEBAK_MIDDLE_LOC = new Location(3926, 5408);
	private static final Location BASE_WAVE_SOUTH_LOC = new Location(3923, 5397);
	private static final Location BASE_WAVE_NORTH_LOC = new Location(3923, 5419);
	private static final Location[] BLOOD_SPELL_GFX_LOCATIONS = {new Location(3924, 5406), new Location(3925, 5410)};
	private static final Location[] BLOOD_CLOUD_LOCATIONS = {new Location(3931, 5413), new Location(3934, 5401)};
	private static final Projectile POISON_SHOOT_PROJECTILE = new Projectile(1555, 62, 0, 30, 35, 120, 100, 0);
	private static final Projectile BOULDER_PROJECTILE = new Projectile(2172, 62, 0, 30, 35, 120, 100, 0);
	private static final Projectile JUG_PROJECTILE = new Projectile(2173, 62, 0, 30, 35, 120, 100, 0);
	private static final Projectile POISON_BOULDER_PROJECTILE = new Projectile(2194, 62, 0, 30, 35, 120, 100, 0);
	private static final Animation SHOOT_ANIM = new Animation(9624);
	private static final Animation TAIL_SHOOT_ANIM = new Animation(9625);
	private static final Animation MELEE_ANIM = new Animation(9621);
	private static final Animation MELEE_TAIL_ANIM = new Animation(9620);
	private static final Animation DEATH_ANIM = new Animation(9634);
	private static final Animation TAIL_DEATH_ANIM = new Animation(9635);
	private static final Animation JUGS_SHOOT_ANIM = new Animation(9624);
	private static final Animation PLAYER_BOULDER_HIT_ANIM = new Animation(1114);
	private static final Animation SCREAM_ANIM = new Animation(9628);
	private static final Animation TAIL_SCREAM_ANIM = new Animation(9629);
	private static final Animation CALL_WAVE_ANIM = new Animation(9630);
	private static final Animation TAIL_CALL_WAVE_ANIM = new Animation(9631);
	private static final Animation PLAYER_PUSHED_ANIM = new Animation(4177);
	private static final SoundEffect MAGE_SHOOT_SOUND = new SoundEffect(5823);
	private static final SoundEffect RANGE_SHOOT_SOUND = new SoundEffect(5819);
	private static final SoundEffect FINAL_PHASE_SOUND = new SoundEffect(3405);
	private static final SoundEffect BLOOD_BARRAGE_IMPACT_SOUND = new SoundEffect(102);
	private static final SoundEffect JUGS_SHOOT_SOUND = new SoundEffect(5908, 15);
	private static final SoundEffect BASE_POISON_LAND_SOUND = new SoundEffect(5909, 15);
	private static final SoundEffect BOULDER_LAND_SOUND = new SoundEffect(5913, 5);
	private static final SoundEffect PLAYER_PUSHED_SOUND = new SoundEffect(5888, 1, 1);
	private static final SoundEffect RUMBLING_SOUND = new SoundEffect(1678);
	private static final SoundEffect WAVE_HIT_SOUND = new SoundEffect(5868);
	private static final SoundEffect[] SCREAM_SOUNDS = {new SoundEffect(5860, 1, 16), new SoundEffect(5836, 1, 20), new SoundEffect(5845, 1, 32),
			new SoundEffect(5904, 1, 69), new SoundEffect(5838, 1, 70), new SoundEffect(5851, 1, 79), new SoundEffect(5863, 1, 260)};
	private static final SoundEffect[] WAVES_LAND_SOUNDS = {new SoundEffect(5882), new SoundEffect(5843, 1, 200), new SoundEffect(5852, 1, 200),
			new SoundEffect(5852, 1, 530), new SoundEffect(5852, 1, 920)};
	private static final Graphics MAGE_BREAK_GFX = new Graphics(2186, 0, 750);
	private static final Graphics MAGE_IMPACT_GFX = new Graphics(131, 90, 90);
	private static final Graphics RANGE_BREAK_GFX = new Graphics(2185, 0, 750);
	private static final Graphics RANGE_IMPACT_GFX = new Graphics(1103, 90, 90);
	private static final Graphics BLOOD_SPELL_IMPACT_GFX = new Graphics(377);
	private static final Graphics ROCKS_FALLING_DOWN_GFX = new Graphics(2195);
	private static final Graphics BIG_SPLASH_GFX = new Graphics(68, 200, 0);
	private static final RenderAnimation WATER_RENDER_ANIMATION = new RenderAnimation(773, 772, 772, 772, 772, 772, 772);
	private static final int MAGE_PROJECTILE_ID = 2176;
	private static final int MAGE_SPLIT_PROJECTILE_ID = 2181;
	private static final int RANGE_PROJECTILE_ID = 2178;
	private static final int RANGE_SPLIT_PROJECTILE_ID = 2187;
	private static final int MAGE_SPLIT_SOUND_ID = 5878;
	private static final int RANGE_SPLIT_SOUND_ID = 5896;
	private static final int IMPACT_SOUND_ID = 5884;
	private static final Location PROJECTILE_START_LOC = new Location(3925, 5408);
	private static final Location PROJECTILE_BASE_LOC = new Location(3933, 5408);
	private static final Location[] CROCODILE_SPAWN_LOCS = {new Location(3919, 5403), new Location(3921, 5418), new Location(3934, 5423), new Location(3936, 5394),
			new Location(3936, 5395), new Location(3938, 5420), new Location(3948, 5408), new Location(3948, 5408)};
	private final ZebakEncounter zebakEncounter;
	private final ZebakStationaryEntity zebakTail;
	private final WaterCrocodile[] waterCrocodiles = new WaterCrocodile[CROCODILE_SPAWN_LOCS.length];
	private final ZebakStationaryEntity zebakProjectileNpc;
	private BoulderNpc[] boulders;
	private final boolean spreadBarrage;
	private final boolean multipleClouds;
	private final ArrayList<BloodCloud> bloodClouds = new ArrayList<>();
	private final ArrayList<WaveNPC> waves = new ArrayList<>();
	private int attackSpeed;
	private int attackCycleTicks = 7;
	private int bloodSpellTicks = -1;
	private boolean useBarrageSpell = Utils.random(1) == 0;
	private boolean cloudsFromSouth = Utils.random(1) == 0;
	private boolean usingMage;
	private boolean jugAttack = Utils.random(1) == 0;
	private boolean wavesFromSouth = Utils.random(1) == 0;
	private boolean lastPhase;
	private int specialAmount;
	private boolean usingSpecial = false;
	private final Queue<Runnable> specials = new ArrayDeque<Runnable>();

	public Zebak(ZebakEncounter zebakEncounter, int level) {
		super(ZebakEncounter.ZEBAK_ID, zebakEncounter.getLocation(SPAWN_LOCATION), Direction.EAST, 0, zebakEncounter, level);
		this.zebakEncounter = zebakEncounter;
		zebakTail = new ZebakStationaryEntity(ZEBAK_TAIL_ID, zebakEncounter.getLocation(TAIL_SPAWN_LOCATION), Direction.EAST);
		zebakTail.spawn();
		zebakProjectileNpc = new ZebakStationaryEntity(PROJECTILE_NPC_ID, zebakEncounter.getLocation(PROJECTILE_NPC_LOCATION), Direction.SOUTH);
		zebakProjectileNpc.spawn();
		for (int i = 0; i < waterCrocodiles.length; i++) {
			waterCrocodiles[i] = new WaterCrocodile(WATER_CROCODILE_ID, zebakEncounter.getLocation(CROCODILE_SPAWN_LOCS[i]), Direction.SOUTH, this);
			waterCrocodiles[i].spawn();
		}
		setAggressionDistance(48);
		setAttackDistance(48);
		attackSpeed = 7 - Math.min(2, level / 2);
		if (zebakEncounter.getParty().getPartySettings().isActive(InvocationType.NOT_JUST_A_HEAD)) {
			bloodSpellTicks = attackSpeed * 6 - 1;
		}
		spreadBarrage = zebakEncounter.getParty().getPartySettings().isActive(InvocationType.ARTERIAL_SPRAY);
		multipleClouds = zebakEncounter.getParty().getPartySettings().isActive(InvocationType.BLOOD_THINNERS);
		usingMage = Utils.random(1) == 0;
	}

	@Override public boolean setHitpoints(int amount) {
		final boolean set = super.setHitpoints(amount);
		if (zebakEncounter != null && EncounterStage.STARTED.equals(zebakEncounter.getStage())) {
			zebakEncounter.getPlayers().forEach(p -> p.getHpHud().updateValue(hitpoints));
		}
		if (combatDefinitions != null && !lastPhase) {
			if ((hitpoints <= getMaxHitpoints() * 0.85 && specialAmount == 0) || (hitpoints <= getMaxHitpoints() * 0.7 && specialAmount == 1)
					|| (hitpoints <= getMaxHitpoints() * 0.55 && specialAmount == 2) || (hitpoints <= getMaxHitpoints() * 0.4 && specialAmount == 3)) {
				specials.add(() -> {
					if (jugAttack) {
						shootJugs();
					} else {
						landWaves();
					}
				});
				jugAttack = !jugAttack;
				specialAmount++;
			}
			if (hitpoints <= getMaxHitpoints() * 0.25) {
				lastPhase = true;
				attackSpeed = Math.max(2, attackSpeed - 3);
				if (zebakEncounter != null) {
					final Player[] players = zebakEncounter.getChallengePlayers();
					if (players != null) {
						sendSound(players, FINAL_PHASE_SOUND);
					}
				}
			}
		}
		return set;
	}

	@Override public void setRespawnTask() {}

	@Override public void sendDeath() {
		final Player source = getMostDamagePlayerCheckIronman();
		onDeath(source);
	}

	@Override public void finish() {
		super.finish();
		zebakTail.finish();
		zebakProjectileNpc.finish();
		bloodClouds.stream().filter(bloodCloud -> !bloodCloud.isFinished() && !bloodCloud.isDying()).forEach(NPC::finish);
		finishMobs();
		if (boulders != null) {
			for (ZebakStationaryEntity boulder : boulders) {
				if (boulder != null && !boulder.isDying() && !boulder.isFinished()) {
					boulder.finish();
				}
			}
		}
	}

	@Override public void processNPC() {
		super.processNPC();
		if (!isDying() && !isFinished() && EncounterStage.STARTED.equals(zebakEncounter.getStage())) {
			final Player[] players = zebakEncounter.getChallengePlayers();
			if (players != null && players.length > 0) {
				if (!usingSpecial && bloodSpellTicks != -1 && --bloodSpellTicks == 0) {
					bloodSpellTicks = attackSpeed * (lastPhase ? 8 : 6) - 1;
					sendBloodSpell();
				}
				if (--attackCycleTicks <= 0) {
					attackCycleTicks = attackSpeed;
					if (!lastPhase && !usingSpecial && !specials.isEmpty()) {
						specials.poll().run();
					} else {
						handleNormalCombat(players);
					}
				}
			}
		}
	}

	private void sendBloodSpell() {
		Arrays.stream(BLOOD_SPELL_GFX_LOCATIONS).forEach(loc -> World.sendGraphics(BLOOD_SPELL_IMPACT_GFX, zebakEncounter.getLocation(loc)));
		WorldTasksManager.schedule(zebakEncounter.addRunningTask(() -> {
			final Player[] players = zebakEncounter.getChallengePlayers();
			if (players != null && players.length > 0) {
				if (useBarrageSpell) {
					int totalHeal = 0;
					for (Player player : players) {
						final int base = (int) Math.floor(7 * zebakEncounter.getParty().getDamageMultiplier());
						final int damage = Utils.random(base, base + 7);
						if (!player.getPrayerManager().isActive(Prayer.PROTECT_FROM_MAGIC)) {
							totalHeal += damage * 0.66;
						}
						delayHit(0, player, new Hit(Zebak.this, damage, HitType.MAGIC));
						player.setGraphics(BLOOD_SPELL_IMPACT_GFX);
						for (Player p : players) {
							if (!player.getUsername().equals(p.getUsername())
									&& player.getLocation().getTileDistance(p.getLocation()) <= (spreadBarrage ? 2 : 1)) {
								if (!p.getPrayerManager().isActive(Prayer.PROTECT_FROM_MAGIC)) {
									totalHeal += damage * 0.66;
								}
								delayHit(0, p, new Hit(Zebak.this, damage, HitType.MAGIC));
							}
						}
					}
					if (totalHeal > 0) {
						applyHit(new Hit(Zebak.this, totalHeal, HitType.HEALED));
					}
					sendSound(players, BLOOD_BARRAGE_IMPACT_SOUND);
				} else {
					final Location baseLocation = zebakEncounter.getLocation(BLOOD_CLOUD_LOCATIONS[cloudsFromSouth ? 1 : 0]);
					if (multipleClouds) {
						for (int i = 0; i < 3; i++) {
							final BloodCloud bloodCloud = new BloodCloud(BLOOD_CLOUD_NPC_ID + 1, baseLocation.transform(i, cloudsFromSouth ? (i / 2) : -(i / 2)), this);
							bloodCloud.spawn();
							bloodClouds.add(bloodCloud);
						}
					} else {
						final BloodCloud bloodCloud = new BloodCloud(BLOOD_CLOUD_NPC_ID, baseLocation, this);
						bloodCloud.spawn();
						bloodClouds.add(bloodCloud);
					}
					cloudsFromSouth = !cloudsFromSouth;
				}
				useBarrageSpell = !useBarrageSpell;
			}
		}), 2);
	}

	private void handleNormalCombat(final Player[] players) {
		if (Utils.random(2) == 0) {
			usingMage = !usingMage;
		}
		ArrayList<Player> meleeTargets = new ArrayList<>();
		for (Player player : players) {
			if (isWithinMeleeDistance(this, player)) {
				meleeTargets.add(player);
			}
		}
		if (meleeTargets.size() > 0 && Utils.random(2) == 0) {
			setAnimation(MELEE_ANIM);
			setAnimation(MELEE_TAIL_ANIM);
			WorldTasksManager.schedule(zebakEncounter.addRunningTask(() -> {
				meleeTargets.stream().filter(p -> !p.isDying() && zebakEncounter.insideChallengeArea(p)).forEach(p -> {
					delayHit(this, 0, p, new Hit(this, getRandomMaxHit(this, getMaxHit(34), AttackType.SLASH, p), HitType.MELEE));
					if (Utils.random(3) == 0) {
						p.sendMessage("<col=ff3045>Zebak's fangs tear into your flesh, causing you to bleed.</col>");
						p.getTemporaryAttributes().put("toa_zebak_bleed", WorldThread.getCurrentCycle() + 10);
					}
				});
			}), 1);
		} else if (usingMage) {
			shootNormalAttack(players, MAGE_SHOOT_SOUND, MAGE_SPLIT_SOUND_ID, MAGE_PROJECTILE_ID, MAGE_BREAK_GFX, MAGE_IMPACT_GFX, MAGE_SPLIT_PROJECTILE_ID, true);
		} else {
			shootNormalAttack(players, RANGE_SHOOT_SOUND, RANGE_SPLIT_SOUND_ID, RANGE_PROJECTILE_ID, RANGE_BREAK_GFX, RANGE_IMPACT_GFX, RANGE_SPLIT_PROJECTILE_ID, false);
		}
	}

	private void shootJugs() {
		setAnimation(JUGS_SHOOT_ANIM);
		attackSpeed = 10;
		usingSpecial = true;
		zebakTail.setAnimation(new Animation(-1));
		WorldTasksManager.schedule(zebakEncounter.addRunningTask(new WorldTask() {
			int ticks = -1;
			final List<Location> poisonLocations = new ArrayList<>();
			List<Location> boulderLocations;
			List<Location> jugsLocations;
			List<Location> poisonBaseLocations;
			final List<Location> poisonBoulderLocations = new ArrayList<>();
			@Override public void run() {
				if (isDying() || isFinished() || EncounterStage.COMPLETED.equals(zebakEncounter.getStage())) {
					stop();
					return;
				}
				if (++ticks == 0) {
					World.sendSoundEffect(zebakEncounter.getLocation(ZEBAK_MIDDLE_LOC), JUGS_SHOOT_SOUND);
					boulderLocations = getBoulderLocations();
					if (boulderLocations != null) {
						jugsLocations = getJugsSolveLocations(boulderLocations);
						if (jugsLocations != null) {
							poisonBaseLocations = getPoisonBaseLocations(6, boulderLocations);
							final Location shootLocation = zebakEncounter.getLocation(PROJECTILE_START_LOC);
							poisonBaseLocations.forEach(loc -> World.sendProjectile(shootLocation, loc, POISON_SHOOT_PROJECTILE));
							if (boulderLocations != null) {
								boulderLocations.forEach(loc -> {
									final Location boulderPoisonTile = loc.transform(2, 0);
									if (!poisonBaseLocations.contains(boulderPoisonTile)) {
										poisonBaseLocations.add(boulderPoisonTile);
										poisonBoulderLocations.add(boulderPoisonTile);
										World.sendProjectile(shootLocation, boulderPoisonTile, POISON_BOULDER_PROJECTILE);
									}
								});
							}
							boulderLocations.forEach(loc -> World.sendProjectile(shootLocation, loc, BOULDER_PROJECTILE));
							jugsLocations.forEach(loc -> World.sendProjectile(shootLocation, loc, JUG_PROJECTILE));
							poisonLocations.addAll(poisonBaseLocations);
						} else {
							attackCycleTicks = attackSpeed;
							stop();
						}
					} else {
						attackCycleTicks = attackSpeed;
						stop();
					}
				} else if (ticks == 5) {
					final Player[] players = zebakEncounter.getChallengePlayers();
					if (players != null && players.length > 0) {
						boulders = new BoulderNpc[boulderLocations.size()];
						for (int i = 0; i < boulders.length; i++) {
							boulders[i] = new BoulderNpc(BOULDER_ID, boulderLocations.get(i), Direction.SOUTH);
							boulders[i].spawn();
							World.sendSoundEffect(boulders[i].getLocation(), BOULDER_LAND_SOUND);
						}
						Arrays.stream(players).forEach(p -> {
							for (Location boulderLocation : boulderLocations) {
								if (boulderLocation.equals(p.getLocation())) {
									delayHit(0, p, new Hit(Zebak.this, Utils.random(2, 5), HitType.DEFAULT));
									movePlayer(p, boulderLocation);
									break;
								}
							}
						});
						poisonBoulderLocations.forEach(loc -> zebakEncounter.addPoison(loc, true, true));
						spawnBasePoison(poisonBaseLocations);
						spawnJugs(jugsLocations, players);
					}
				} else if (ticks == 33) {
					attackCycleTicks = 11;
					setAnimation(SCREAM_ANIM);
					zebakTail.setAnimation(TAIL_SCREAM_ANIM);
					final Player[] players = zebakEncounter.getChallengePlayers();
					if (players != null) {
						sendSound(players, SCREAM_SOUNDS);
					}
				} else if (ticks == 36 || ticks == 38 || ticks == 40) {
					final Location minLocation = zebakEncounter.getLocation(GROUND_MIN_LOCATION);
					final Location maxLocation = zebakEncounter.getLocation(GROUND_MAX_LOCATION);
					final Location shootLocation = zebakEncounter.getLocation(ZEBAK_MIDDLE_LOC);
					for (int x = minLocation.getX(); x <= maxLocation.getX(); x++) {
						yLoop : for (int y = minLocation.getY(); y <= maxLocation.getY(); y++) {
							final Location location = new Location(x, y);
							if (boulders != null) {
								for (BoulderNpc boulder : boulders) {
									if (boulder != null && location.getY() == boulder.getLocation().getY() &&
											location.getX() > boulder.getLocation().getX() && location.getX() <= boulder.getLocation().getX() + 3) {
										continue yLoop;
									}
								}
							}
							if ((World.getMask(location) & Flags.FLOOR_DECORATION) == 0 && World.getObjectWithType(location, 10) == null) {

								World.sendGraphics(new Graphics(FLOOR_ROAR_GFX_ID, 0, 1 + location.getTileDistance(shootLocation)), location);
							}
						}
					}
					if (boulders != null) {
						for (BoulderNpc boulder : boulders) {
							if (boulder != null) {
								delayHit(0, boulder, new Hit(Zebak.this, 50, HitType.DEFAULT));
							}
						}
					}
					final Player[] players = zebakEncounter.getChallengePlayers();
					if (players != null) {
						playerLoop: for (Player player : players) {
							if (boulders != null) {
								for (BoulderNpc boulder : boulders) {
									if (boulder != null) {
										if (player.getLocation().getX() >= boulder.getX() && player.getLocation().getX() <= boulder.getX() + 3 &&
												player.getLocation().getY() == boulder.getY()) {
											continue playerLoop;
										}
									}
								}
							}
							pushPlayer(player, Direction.EAST, 20, true);
						}
					}
					zebakEncounter.getJugs().forEach(jug -> delayHit(0, jug, new Hit(Zebak.this, 5, HitType.DEFAULT)));
				} else if (ticks == 49) {
					boulders = null;
					usingSpecial = false;
					stop();
				}
			}
		}), 0, 0);
	}

	private void pushPlayer(Player player, Direction direction, int baseDamage, boolean scream) {
		Location baseLoc = player.getLocation();
		boolean insideWater = false;
		for (int i = 0; i < (scream ? 2 : 4); i++) {

			if (World.checkWalkStep(baseLoc.getPlane(), baseLoc.getX(), baseLoc.getY(), direction.getMovementDirection(), 1, false, false)) {
				baseLoc = baseLoc.transform(direction.getOffsetX(), direction.getOffsetY());
			} else {
				if (!scream) {
					final Location waterLocation = baseLoc.transform(direction.getOffsetX() * (5 - i), direction.getOffsetY() * (5 - i));
					if (World.isFloorFree(waterLocation, 1)) {
						baseLoc = waterLocation;
						insideWater = true;
						break;
					}
				}
				break;
			}
		}
		if (!player.getLocation().equals(baseLoc)) {
			player.setLocation(baseLoc);
		} else {
			player.resetWalkSteps();
		}
		player.faceDirection(Direction.getOppositeDirection(direction));
		player.setAnimation(PLAYER_PUSHED_ANIM);
		player.sendSound(scream ? PLAYER_PUSHED_SOUND : WAVE_HIT_SOUND);
		final int damage = getMaxHit(baseDamage);
		delayHit(0, player, new Hit(this, Utils.random(damage, damage + 10), HitType.DEFAULT));
		if (insideWater) {
			player.getAppearance().setRenderAnimation(WATER_RENDER_ANIMATION);
			player.setRunSilent(true);

		}
	}

	private void movePlayer(Player player, Location location) {
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				if (x == 0 && y == 0) {
					continue;
				}
				final Location nextLoc = location.transform(x, y);
				if (World.checkWalkStep(player.getLocation().getPlane(), player.getLocation().getX(), player.getLocation().getY(),
						Direction.getDirection(x, y).getMovementDirection(), 1, false, false) && World.getObjectWithType(nextLoc, 10) == null) {
					player.setAnimation(PLAYER_BOULDER_HIT_ANIM);
					player.lock(1);
					player.autoForceMovement(player.getLocation(), nextLoc, 0, 30);
					return;
				}
			}
		}
	}

	private void landWaves() {
		setAnimation(JUGS_SHOOT_ANIM);
		zebakTail.setAnimation(new Animation(-1));
		attackCycleTicks = 16;
		usingSpecial = true;
		WorldTasksManager.schedule(zebakEncounter.addRunningTask(new WorldTask() {

			final List<Location> poisonLocations = new ArrayList<>();
			List<Location> poisonBaseLocations;
			List<Location> jugsLocations;
			int waveSkipX = -1;
			int ticks = -1;
			@Override public void run() {
				if (isDying() || isFinished() || EncounterStage.COMPLETED.equals(zebakEncounter.getStage())) {
					stop();
					return;
				}
				if (++ticks == 0) {
					jugsLocations = getRandomJugLocations(Utils.random(6, 8));
					poisonBaseLocations = getPoisonBaseLocations(16, null);
					final Location shootLocation = zebakEncounter.getLocation(PROJECTILE_START_LOC);
					poisonBaseLocations.forEach(loc -> World.sendProjectile(shootLocation, loc, POISON_SHOOT_PROJECTILE));
					jugsLocations.forEach(loc -> World.sendProjectile(shootLocation, loc, JUG_PROJECTILE));
					poisonLocations.addAll(poisonBaseLocations);
				} else if (ticks == 4) {
					setAnimation(CALL_WAVE_ANIM);
					zebakTail.setAnimation(TAIL_CALL_WAVE_ANIM);
				} else if (ticks == 5) {
					final Player[] players = zebakEncounter.getChallengePlayers();
					if (players != null && players.length > 0) {
						spawnBasePoison(poisonBaseLocations);
						spawnJugs(jugsLocations, players);
					} else {
						stop();
					}
				} else if (ticks == 6) {
					final Player[] players = zebakEncounter.getChallengePlayers();
					if (players != null && players.length > 0) {
						sendSound(players, WAVES_LAND_SOUNDS);
						final Location baseLoc = zebakEncounter.getLocation(wavesFromSouth ? BASE_WAVE_SOUTH_LOC : BASE_WAVE_NORTH_LOC);
						for (int x = 0; x < 7; x++) {
							final Location loc = baseLoc.transform(x * 3, 0);
							World.sendGraphics(BIG_SPLASH_GFX, loc);
							World.sendGraphics(ROCKS_FALLING_DOWN_GFX, loc);
						}
						for (Player player : players) {
							player.getPacketDispatcher().resetCamera();
							player.getPacketDispatcher().sendCameraShake(CameraShakeType.LEFT_AND_RIGHT, 7, 0, 0);
							player.getPacketDispatcher().sendCameraShake(CameraShakeType.UP_AND_DOWN, 6, 0, 0);
							player.getPacketDispatcher().sendCameraShake(CameraShakeType.FRONT_AND_BACK, 7, 0, 0);
							player.sendSound(RUMBLING_SOUND);
						}
					} else {
						stop();
					}
				} else if (ticks == 8) {
					final Player[] players = zebakEncounter.getChallengePlayers();
					if (players != null && players.length > 0) {
						for (Player player : players) {
							player.getPacketDispatcher().resetCamera();
						}
					} else {
						stop();
					}
				} else if (ticks == 13 || ticks == 20 || ticks == 27) {
					final Location baseLoc = zebakEncounter.getLocation(wavesFromSouth ? BASE_WAVE_SOUTH_LOC : BASE_WAVE_NORTH_LOC);
					final int currentWaveSkipX = waveSkipX  == -1 ? Utils.random(12) : 12 - waveSkipX;
					final int openHoles = 3 - Math.min(2, zebakEncounter.getParty().getBossLevels()[TOAPathType.CRONDIS.ordinal()] / 2);
					waveLoop : for (int x = 0; x < 21; x++) {
						if (x >= 4) {
							for (int hole = 0; hole < openHoles; hole++) {
								if (x - 4 == currentWaveSkipX + (currentWaveSkipX < 6 ? hole : -hole)) {
									continue waveLoop;
								}
							}
						}
						final Location waveLoc = baseLoc.transform(x, 0);
						final WaveNPC waveNPC = new WaveNPC(waveLoc, wavesFromSouth ? Direction.NORTH : Direction.SOUTH, Zebak.this);
						waveNPC.spawn();
						waves.add(waveNPC);
					}
					waveSkipX = waveSkipX == -1 ? currentWaveSkipX : -1;
				} else if (ticks == 47) {
					usingSpecial = false;
					wavesFromSouth = !wavesFromSouth;
					stop();
				}
			}
		}), 0, 0);
	}

	private void spawnBasePoison(List<Location> poisonBaseLocations) {
		poisonBaseLocations.forEach(loc -> {
			World.sendSoundEffect(loc, BASE_POISON_LAND_SOUND);
			zebakEncounter.addPoison(loc, true, false);
		});
	}

	private void spawnJugs(List<Location> jugsLocations, Player[] players) {
		jugsLocations.forEach(loc -> {
			final CrondisJug jug = new CrondisJug(JUG_ID, loc, zebakEncounter);
			jug.spawn();
			zebakEncounter.addJug(jug);
			World.sendGraphics(new Graphics(FLOOR_ROAR_GFX_ID), loc);
			Arrays.stream(players).forEach(p -> {
				if (loc.equals(p.getLocation())) {
					delayHit(0, p, new Hit(Zebak.this, Utils.random(2, 5), HitType.DEFAULT));
				}
			});
		});
	}

	@Override public void performDefenceAnimation(Entity attacker) {

	}

	private List<Location> getFreeTiles(Location minLocation, Location maxLocation, List<Location> excludes) {
		final ArrayList<Location> possibleLocations = new ArrayList<>();
		final Location minLoc = zebakEncounter.getLocation(minLocation);
		final Location maxLoc = zebakEncounter.getLocation(maxLocation);
		for (int x = minLoc.getX(); x <= maxLoc.getX(); x++) {
			for (int y = minLoc.getY(); y <= maxLoc.getY(); y++) {
				final Location location = new Location(x, y);
				if (excludes != null && excludes.contains(location)) {
					continue;
				}
				if (World.getObjectWithType(location, 10) == null && World.isFloorFree(location, 1) && !zebakEncounter.onPoison(location)) {
					possibleLocations.add(location);
				}
			}
		}
		Collections.shuffle(possibleLocations);
		return possibleLocations;
	}

	private List<Location> getBoulderLocations() {
		final List<Location> freeTiles = getFreeTiles(BOULDER_MIN_LOCATION, BOULDER_MAX_LOCATION, null);
		if (freeTiles.size() < 1) {
			return null;
		}
		final List<Location> possibleBoulderTiles = new ArrayList<>();
		final Location baseLocation = freeTiles.get(0);
		for (int y = -6; y <= 6; y++) {
			final List<Location> possibleXLocations = new ArrayList<>();
			for (int x = -3; x <= 3; x++) {
				final Location location = baseLocation.transform(x, y);
				if (freeTiles.contains(location) && freeTiles.contains(location.transform(1, 0)) && !possibleBoulderTiles.contains(location)) {
					possibleXLocations.add(location);
				}
			}
			Collections.shuffle(possibleXLocations);
			if (possibleXLocations.size() > 0) {
				possibleBoulderTiles.add(possibleXLocations.get(0));
			}
		}
		Collections.shuffle(possibleBoulderTiles);
		final int boulderAmounts = zebakEncounter.getStartTeamSize() > 1 ? 2 : 3;
		return possibleBoulderTiles.size() < 1 ? null : possibleBoulderTiles.subList(0, Math.min(possibleBoulderTiles.size(), boulderAmounts));
	}

	private List<Location> getJugsSolveLocations(List<Location> boulderLocations) {
		final Location minLoc = zebakEncounter.getLocation(GROUND_MIN_LOCATION);
		final Location maxLoc = zebakEncounter.getLocation(GROUND_MIN_LOCATION);
		final List<Location> freeTiles = getFreeTiles(GROUND_MIN_LOCATION, GROUND_MAX_LOCATION, boulderLocations);
		final List<Location> possibleJugLocations = new ArrayList<>();
		final List<Location> badTiles = new ArrayList<>();
		for (Location tile : freeTiles) {
			if (tile.getX() == minLoc.getX() || tile.getY() == minLoc.getY() || tile.getX() == maxLoc.getX() || tile.getY() == maxLoc.getY()) {
				continue;
			}
			Collections.shuffle(boulderLocations);
			for (Location boulder : boulderLocations) {
				final int deltaX = boulder.getX() - tile.getX();
				final int deltaY = boulder.getY() - tile.getY();
				final int diffX = Math.abs(deltaX);
				final int diffY = Math.abs(deltaY);
				if ((diffX == 0 || deltaX > 0) && diffY == 0) {
					continue;
				}
				if (diffY < 2 && deltaX > -4 && deltaX < 0) {
					continue;
				}
				if (diffX > 10 || diffY > 10) {
					continue;
				}
				if (Math.abs(diffY - diffX) < 2 || (deltaX >= -2 && deltaX <= 0) || (diffY <= 1)) {
					possibleJugLocations.add(tile);
					break;
				}
			}
			if (!possibleJugLocations.contains(tile)) {
				badTiles.add(tile);
			}
		}
		if (possibleJugLocations.size() < boulderLocations.size()) {
			return null;
		}
		final int jugAmount = Utils.random(6, 8);
		final List<Location> jugLocations = new ArrayList<>(possibleJugLocations.subList(0, Math.min(boulderLocations.size(), possibleJugLocations.size())));
		jugLocations.addAll(badTiles.subList(0, Math.min(jugAmount - jugLocations.size(), jugLocations.size())));
		return jugLocations;
	}

	private List<Location> getRandomJugLocations(int amount) {
		final List<Location> freeTiles = getFreeTiles(GROUND_MIN_LOCATION, GROUND_MAX_LOCATION, null);
		return freeTiles.subList(0, Math.min(freeTiles.size(), amount));
	}

	private List<Location> getPoisonBaseLocations(int amount, List<Location> boulders) {
		final List<Location> excludes = new ArrayList<>();
		if (boulders != null) {
			excludes.addAll(boulders);
		}
		final List<Location> freeTiles = getFreeTiles(GROUND_MIN_LOCATION, GROUND_MAX_LOCATION, excludes);
		return freeTiles.subList(0, Math.min(freeTiles.size(), amount));
	};

	private void shootNormalAttack(Player[] players, SoundEffect soundEffect, int splitSoundId, int firstProjectileId, Graphics breakGfx, Graphics impactGfx, int secondProjectileId, boolean mage) {
		setAnimation(SHOOT_ANIM);
		zebakTail.setAnimation(TAIL_SHOOT_ANIM);
		sendSound(players, soundEffect, new SoundEffect(splitSoundId, 1, 120));
		final Location baseLoc = zebakEncounter.getLocation(PROJECTILE_BASE_LOC);
		World.sendProjectile(zebakEncounter.getLocation(PROJECTILE_START_LOC), baseLoc, new Projectile(firstProjectileId, 50, 175, 60, 30, 60, 0, 0));
		WorldTasksManager.schedule(zebakEncounter.addRunningTask(() -> {
			if (isDying() || isFinished()) {
				return;
			}
			final Player[] updatedPlayers = zebakEncounter.getChallengePlayers();
			if (updatedPlayers != null && updatedPlayers.length > 0) {
				zebakProjectileNpc.setGraphics(breakGfx);
				sendSound(updatedPlayers, new SoundEffect(IMPACT_SOUND_ID, 1, 90));
				for (Player player : updatedPlayers) {
					World.sendProjectile(baseLoc, player, new Projectile(secondProjectileId, 175, 22, 0, 1, 90, 0, 0));
					player.setGraphics(impactGfx);
				}
				WorldTasksManager.schedule(zebakEncounter.addRunningTask(() -> {
					final Player[] updatedPlayers1 = zebakEncounter.getChallengePlayers();
					for (Player player : updatedPlayers1) {
						player.scheduleHit(Zebak.this, new Hit(Zebak.this, getRandomMaxHit(Zebak.this, getMaxHit(32), mage ? AttackType.MAGIC : AttackType.RANGED, player), mage ? HitType.MAGIC : HitType.RANGED),-1);
					}
				}), 2);
			}
		}), 3);
	}

	private void sendSound(Player[] players, SoundEffect... soundEffects) {
		for (Player player : players) {
			for (SoundEffect effect : soundEffects) {
				player.sendSound(effect);
			}
		}
	}

	@Override protected void onDeath(Entity source) {
		zebakEncounter.completeRoom();
		setAnimation(DEATH_ANIM);
		zebakTail.setAnimation(TAIL_DEATH_ANIM);
		bloodClouds.stream().filter(bloodCloud -> !bloodCloud.isFinished() && !bloodCloud.isDying()).forEach(NPC::sendDeath);
		finishMobs();
		if (boulders != null) {
			for (ZebakStationaryEntity boulder : boulders) {
				if (boulder != null && !boulder.isDying() && !boulder.isFinished()) {
					boulder.sendDeath();
				}
			}
		}
		WorldTasksManager.schedule(() -> {
			if (EncounterStage.COMPLETED.equals(zebakEncounter.getStage()) && !isFinished() && !zebakTail.isFinished()) {
				setTransformation(id + 3);
				zebakTail.setTransformation(zebakTail.getId() + 3);
			}
			zebakEncounter.spawnTeleportNPC();
		}, 3);
	}

	private void finishMobs() {
		waves.stream().filter(wave -> !wave.isFinished() && !wave.isDying()).forEach(NPC::finish);
		Arrays.stream(waterCrocodiles).forEach(croc -> {
			if (!croc.isDying() && !croc.isFinished()) {
				croc.finish();
			}
		});
	}

	@Override public boolean canAttack(Player source) {
		if (source.getAppearance().getRenderAnimation() != null && source.getAppearance().getRenderAnimation().getWalk() == 772) {
			source.sendMessage("You cannot initiate combat while swimming!");
			return false;
		}
		return super.canAttack(source);
	}


	@Override public void setTarget(Entity target, TargetSwitchCause cause) {}

	@Override public void setFaceEntity(Entity entity) {}

	@Override public void setFacedInteractableEntity(InteractableEntity facedInteractableEntity) {}

	@Override public void setFaceLocation(Location tile) {}

	@Override
	public boolean addWalkStep(final int nextX, final int nextY, final int lastX, final int lastY, final boolean check) { return false; }

	@Override public int attack(Entity target) { return 0; }

	@Override public float getPointMultiplier() {
		return 1.5F;
	}

	static class ZebakStationaryEntity extends NPC {

		public ZebakStationaryEntity(int id, Location location, Direction direction) {
			super(id, location, direction, 0);
		}

		@Override public void setRespawnTask() {

		}

		@Override public void setTarget(Entity target, TargetSwitchCause cause) {}

		@Override public void setFaceEntity(Entity entity) {}

		@Override public void setFacedInteractableEntity(InteractableEntity facedInteractableEntity) {}

		@Override public void setFaceLocation(Location tile) {}

		@Override public boolean isEntityClipped() { return false; }

		@Override public boolean isValidAnimation(int animID) { return true; }

		@Override
		public boolean addWalkStep(final int nextX, final int nextY, final int lastX, final int lastY, final boolean check) { return false; }
	}

	static class BloodCloud extends NPC {

		private final Zebak zebak;
		private Player target;
		private int switchTargetTicks;
		private int initialDelayTicks = 4;

		public BloodCloud(int id, Location tile, Zebak zebak) {
			super(id, tile, Direction.WEST, 64);
			this.zebak = zebak;
			switchTargetTicks = Utils.random(10, 20);
		}

		@Override public void setRespawnTask() {

		}

		@Override public void processNPC() {
			if (!isDying() && !zebak.isDying() && !zebak.isFinished()) {
				if (initialDelayTicks > 0) {
					initialDelayTicks--;
				}
				checkTarget();
			}
		}

		private void checkTarget() {
			switchTargetTicks = Math.max(0, switchTargetTicks - 1);
			boolean switchTarget = switchTargetTicks == 0 || target == null || target.isDying() || !zebak.zebakEncounter.insideChallengeArea(target);
			if (switchTargetTicks == 0) {
				switchTargetTicks = Utils.random(10, 20);
			}
			final Player[] players = zebak.zebakEncounter.getChallengePlayers();
			if (switchTarget) {
				if (players != null && players.length > 0) {
					final List<Player> playerList = new ArrayList<>(List.of(players));
					Collections.shuffle(playerList);
					Player newTarget = target;
					int distance = 64;
					for (Player player : playerList) {
						if (player != null && (target == null || !player.getUsername().equals(target.getUsername()))) {
							final int pDistance = player.getLocation().getTileDistance(getLocation());
							if (pDistance < distance) {
								distance = pDistance;
								newTarget = player;
							}
						}
					}
					if (newTarget != null) {
						this.target = newTarget;
					}
				}
			}
			if (target != null && !CombatUtilities.isWithinMeleeDistance(this, target)) {
				addWalkStepsInteract(target.getLocation().getX(), target.getLocation().getY(), 64, target.getSize(), true);
			}
			if (initialDelayTicks <= 0) {
				boolean heal = false;
				if (players != null) {
					for (Player player : players) {
						if (CombatUtilities.isWithinMeleeDistance(this, player)) {
							heal = true;
							player.applyHit(new Hit(this, 2, HitType.DEFAULT));
							applyHit(new Hit(this, 2, HitType.HEALED));
						}
					}
				}
				if (!heal) {
					zebak.delayHit(0, this, new Hit(zebak, 2, HitType.DEFAULT));
				}
			}
		}

		@Override public void setTarget(Entity target, TargetSwitchCause cause) {}

		@Override protected void onFinish(Entity source) {
			super.onFinish(source);
			zebak.bloodClouds.remove(this);
		}

		@Override public boolean isEntityClipped() { return false; }
	}

	static class BoulderNpc extends ZebakStationaryEntity {

		private WorldObject blockingObject;

		public BoulderNpc(int id, Location location, Direction direction) {
			super(id, location, direction);
		}

		@Override
		public NPC spawn() {
			final NPC npc = super.spawn();
			blockingObject = new WorldObject(BOULDER_BLOCK_ID, 10, 0, getLocation());
			World.spawnObject(blockingObject);
			return npc;
		}

		@Override
		public void onFinish(final Entity source) {
			super.onFinish(source);
			World.removeObject(blockingObject);
		}

		@Override public boolean isValidAnimation(int animID) {
			return true;
		}

		@Override public boolean isEntityClipped() { return true; }

		@Override
		public boolean isCycleHealable() { return false; }
	}

	static class WaveNPC extends NPC {

		private final Direction moveDirection;
		private final Zebak zebak;
		private int tiles = 23;

		public WaveNPC(Location location, Direction direction, Zebak zebak) {
			super(WAVE_ID, location, direction, 0);
			this.moveDirection = direction;
			this.zebak = zebak;
		}

		@Override public void processNPC() {
			if (!isDying() && !zebak.isDying() && !zebak.isFinished()) {
				if (--tiles == 0) {
					finish();
				} else {
					final Player[] players = zebak.zebakEncounter.getChallengePlayers();
					if (players != null) {
						for (Player player : players) {
							if (getLocation().equals(player.getLocation())) {
								zebak.pushPlayer(player, moveDirection, 8, false);
							}
						}
						final Location nextLoc = getLocation().transform(moveDirection.getOffsetX(), moveDirection.getOffsetY());
						final Location locAfter = nextLoc.transform(moveDirection.getOffsetX(), moveDirection.getOffsetY());
						zebak.zebakEncounter.getJugs().forEach(jug -> {
							if (locAfter.equals(jug.getLocation())) {
								jug.moveJug(moveDirection);
							}
						});
						if (zebak.zebakEncounter.onPoison(getLocation()) && Utils.random(3) == 0) {
							zebak.zebakEncounter.removePoison(getLocation());
						}
						boolean killedCloud = false;
						for (BloodCloud cloud : zebak.bloodClouds) {
							if (!cloud.isDying() && !cloud.isFinished() && getLocation().equals(cloud.getLocation())) {
								cloud.finish();
								killedCloud = true;
							}
						}
						if (killedCloud) {
							setTransformation(WAVE_ID + 1);
						}
						addWalkSteps(nextLoc.getX(), nextLoc.getY(), 1, false);

					}
				}
			}
		}



		@Override public void setRespawnTask() {

		}

		@Override public void setTarget(Entity target, TargetSwitchCause cause) {}

		@Override public void setFaceEntity(Entity entity) {}

		@Override public void setFacedInteractableEntity(InteractableEntity facedInteractableEntity) {}

		@Override public boolean isEntityClipped() { return false; }

		@Override protected void onFinish(Entity source) {
			super.onFinish(source);
			zebak.waves.remove(this);
		}
	}

	static class WaterCrocodile extends NPC {

		private final Zebak zebak;
		private int damageTicks = 2;

		public WaterCrocodile(int id, Location tile, Direction facing, final Zebak zebak) {
			super(id, tile, facing, 64);
			this.zebak = zebak;
		}

		@Override public void processNPC() {
			if (!zebak.isDying() && !zebak.isFinished() && EncounterStage.STARTED.equals(zebak.zebakEncounter.getStage())) {
				final Player[] players = zebak.zebakEncounter.getChallengePlayers();
				if (players != null && players.length > 0) {
					Player target = null;
					int distance = 16;
					for (Player player : players) {
						if (player.getAppearance().getRenderAnimation() != null && player.getAppearance().getRenderAnimation().getWalk() == 772) {
							final int newDistance = player.getLocation().getTileDistance(getLocation());
							if (newDistance < distance) {
								distance = newDistance;
								target = player;
							}
						}
					}
					if (target != null) {
						addWalkStepsInteract(target.getLocation().getX(), target.getLocation().getY(), 64, target.getSize(), true);
					}
					if (--damageTicks == 0) {
						damageTicks = 2;
						if (target != null && CombatUtilities.isWithinMeleeDistance(this, target)) {
							zebak.delayHit(0, target, new Hit(this, Utils.random(0, 3), HitType.DEFAULT));
						}
					}
				}
			}
			super.processNPC();
		}

		@Override public void setRespawnTask() {

		}

		@Override public boolean isEntityClipped() { return false; }
	}
}
