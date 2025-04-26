package com.zenyte.game.content.boss.nightmare;

import com.zenyte.game.content.boss.nightmare.area.PhosaniInstance;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.content.skills.prayer.PrayerManager;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.CollisionUtil;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CAType;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

import java.util.*;
import java.util.function.Consumer;

public class PhosanisNightmareNPC extends BaseNightmareNPC {

	public static final int SLEEPING = NpcId.PHOSANIS_NIGHTMARE_9423;
	public static final int AWAKE_SHIELD_P1 = NpcId.PHOSANIS_NIGHTMARE_9416;
	public static final int AWAKE_SHIELD_P2 = NpcId.PHOSANIS_NIGHTMARE_9417;
	public static final int AWAKE_SHIELD_P3 = NpcId.PHOSANIS_NIGHTMARE_9418;
	public static final int AWAKE_SHIELD_P4 = NpcId.PHOSANIS_NIGHTMARE_11153;
	public static final int AWAKE_SHIELD_P5 = NpcId.PHOSANIS_NIGHTMARE_11154;
	public static final int AWAKE_P1 = NpcId.PHOSANIS_NIGHTMARE_9419;
	public static final int AWAKE_P2 = NpcId.PHOSANIS_NIGHTMARE_9420;
	public static final int AWAKE_P3 = NpcId.PHOSANIS_NIGHTMARE_9421;
	public static final int AWAKE_P4 = NpcId.PHOSANIS_NIGHTMARE_11155;
	public static final int EXPLODING = NpcId.PHOSANIS_NIGHTMARE_9422;

	private static final Animation MULTI_CLAWS_START = new Animation(9102);
	private static final Animation MULTI_CLAWS = new Animation(9103);
	private static final Animation MULTI_CLAWS_END = new Animation(9104);

	public static final NightmarePhase[] phases = {
			new NightmarePhase(NightmarePhase.NightmarePhaseNumber.FIRST, 3200, AWAKE_P1, AWAKE_SHIELD_P2, 800),
			new NightmarePhase(NightmarePhase.NightmarePhaseNumber.SECOND, 2400, AWAKE_P2, AWAKE_SHIELD_P3, 800),
			new NightmarePhase(NightmarePhase.NightmarePhaseNumber.THIRD, 1600, AWAKE_P3, AWAKE_SHIELD_P4, 800),
			new NightmarePhase(NightmarePhase.NightmarePhaseNumber.FOURTH, 800, AWAKE_P4, AWAKE_SHIELD_P5, 799),
			new NightmarePhase(NightmarePhase.NightmarePhaseNumber.FIFTH, 150, AWAKE_SHIELD_P5, -1, -1),
	};



	private final Set<Player> player = new HashSet<>();
	private NightmareAttack[] attacksRotation;
	private int rotationPos;
	private int spawnSpecialPos = Utils.random(1);
	private final PhosaniInstance instance;
	private NightmareAttack forceAttack;
	private int sleepwalkerSpawnTick;

	public PhosanisNightmareNPC(Player player, PhosaniInstance instance, Location location) {
		super(SLEEPING, location);
		this.instance = instance;
		this.player.add(player);
	}

	@Override
	public void setTransformation(int id) {
		super.setTransformation(id);

		if (isShieldNpc()) {
			generatePhaseRotation();
			setSleepwalkerCount(getSleepwalkerCount() + 1);
		} else if (id == AWAKE_SHIELD_P5) {
			applyHit(new Hit(149, HitType.HEALED));
		}
	}

	@Override
	public void death(boolean death) {
		super.death(death);

		if (death) {
			forEachPlayers(player -> player.getHpHud().close());
		}
	}

	@Override
	public void sendDeath() {
		NightmarePhase phase = getPhase();
		if (phase != null && phase.getNumber() == NightmarePhase.NightmarePhaseNumber.FIFTH) {
			death(true);
			return;
		}

		super.sendDeath();
	}

	public void generatePhaseRotation() {
		List<NightmareAttack> attacks = new ArrayList<>(NightmareAttack.SPECIALS);
		Collections.shuffle(attacks);

		List<NightmareAttack> rotationAttacks = new ArrayList<>(3);
		for (int i = 0; i < 2; i++) {
			rotationAttacks.add(attacks.get(i));
		}
		rotationAttacks.add(NightmareAttack.SPAWNS[spawnSpecialPos % 2]);
		spawnSpecialPos++;

		Collections.shuffle(rotationAttacks);

		attacksRotation = rotationAttacks.toArray(new NightmareAttack[0]);
		rotationPos = 0;
	}

	@Override
	public int getSleepingId() {
		return SLEEPING;
	}

	@Override
	public int getAwakeShieldP1Id() {
		return AWAKE_SHIELD_P1;
	}

	@Override
	public int getExplodingId() {
		return EXPLODING;
	}

	public boolean isShieldNpc() {
		return switch (getId()) {
			case AWAKE_SHIELD_P1, AWAKE_SHIELD_P2, AWAKE_SHIELD_P3, AWAKE_SHIELD_P4 -> true;
			default -> false;
		};
	}

	public boolean isAwakeNpc() {
		return switch (getId()) {
			case AWAKE_P1, AWAKE_P2, AWAKE_P3, AWAKE_P4, EXPLODING -> true;
			default -> false;
		};
	}

	public NightmarePhase getPhase() {
		return switch (getId()) {
			case AWAKE_SHIELD_P1, AWAKE_P1 -> phases[0];
			case AWAKE_SHIELD_P2, AWAKE_P2 -> phases[1];
			case AWAKE_SHIELD_P3, AWAKE_P3 -> phases[2];
			case AWAKE_SHIELD_P4, AWAKE_P4 -> phases[3];
			case AWAKE_SHIELD_P5 -> phases[4];
			default -> null;
		};
	}

	@Override
	public Set<Player> getAreaPlayers() {
		return player;
	}

	@Override
	public int meleeMaxHit() {
		return 63;
	}

	@Override
	public int rangedMageMaxHit() {
		return 60;
	}

	@Override
	public double protectionPrayerMod() {
		return 0.0;
	}

	@Override
	public void init(int playersOnStart) {
		setBaseShield(400);
		setTotemBaseCharge(75);
		setSleepwalkerCount(0);
	}

	@Override
	public void processNPC() {
		super.processNPC();

		NightmarePhase phase = getPhase();
		if (phase != null && phase.getNumber() == NightmarePhase.NightmarePhaseNumber.FIFTH) {
			if (sleepwalkerSpawnTick > 0) {
				sleepwalkerSpawnTick--;
			}

			if (sleepwalkerSpawnTick == 0) {
				sleepwalkerSpawnTick = 8;
				Location sleepwalkerLocation = locationTransform(getSleepwalkerLocation(getSleepwalkerCount() % 4));
				NightmareSleepwalker sleepwalker = spawnSleepwalker(phase, sleepwalkerLocation);
				sleepwalker.lock(7);
				setSleepwalkerCount(getSleepwalkerCount() + 1);
			}
		}
	}

	@Override
	public void increaseAbsorbed() {
		super.increaseAbsorbed();

		NightmarePhase phase = getPhase();
		if (phase != null && phase.getNumber() == NightmarePhase.NightmarePhaseNumber.FIFTH) {
			setDreamlandExpress(false);
			forEachPlayers(player -> {
				player.setGraphics(PLAYER_EXPLODE_GFX);
				player.scheduleHit(this, new Hit(this, 15, HitType.REGULAR), 0);
			});
		}
	}

	@Override
	public int attack(Entity target) {
		if (!isAttackable()) return combatDefinitions.getAttackSpeed();

		NightmarePhase phase = getPhase();
		if (phase != null && phase.getNumber() == NightmarePhase.NightmarePhaseNumber.FIFTH) {
			multiGraspingClawAttack(target);
			return Integer.MAX_VALUE;
		}

		if (forceAttack != null) {
			int delay = handleAttack(target, forceAttack);
			forceAttack = null;
			return delay;
		}

		if (Utils.randomBoolean(5)) {
			if (Utils.randomBoolean(2)) {
				return graspingClawAttack(target);
			}

			return handleAttack(target, attacksRotation[rotationPos++ % 3]);
		}

		if (!combat.outOfRange(target, 0, target.getSize(), true) && !CollisionUtil.collides(getX(), getY(), getSize(), target.getX(), target.getY(), target.getSize()) && Utils.randomBoolean(5)) {
			meleeAttack(target);
			turnOffPrayer(target, Prayer.PROTECT_FROM_MELEE, phase);
		} else {
			if (Utils.randomBoolean()) {
				mageRangeAttack(AttackType.MAGIC);
				turnOffPrayer(target, Prayer.PROTECT_FROM_MAGIC, phase);
			} else {
				mageRangeAttack(AttackType.RANGED);
				turnOffPrayer(target, Prayer.PROTECT_FROM_MISSILES, phase);
			}
		}

		return getCombatDefinitions().getAttackSpeed();
	}

	private void turnOffPrayer(Entity target, Prayer prayer, NightmarePhase phase) {
		if (phase != null && phase.getNumber() != NightmarePhase.NightmarePhaseNumber.FOURTH) {
			return;
		}

		PrayerManager prayerManager = ((Player) target).getPrayerManager();
		if (prayerManager.isActive(prayer)) {
			prayerManager.deactivatePrayer(prayer);
			setPrayerDisabled(true);
		}
	}

	private int handleAttack(Entity target, NightmareAttack attack) {
		switch (attack) {
			case MULTI_CLAWS_5:
				multiGraspingClawAttack(target, 5);
				return 0;
			case MULTI_CLAWS_6:
				multiGraspingClawAttack(target, 6);
				return 0;
			case FLOWERS:
				return flowerPowerAttack(() -> {
					combat.setCombatDelay(2);
					forceAttack = NightmareAttack.MULTI_CLAWS_5;
				});
			case HUSKS:
				return husksAttack();
			case PARASITE:
				return parasiteAttack();
			case CURSE:
				return curseAttack();
			case CHARGE:
				return chargeAttack();
			case SPORES:
				return sporesAttack(() -> {
					combat.setCombatDelay(3);
					forceAttack = NightmareAttack.MULTI_CLAWS_6;
				});
			default:
				return 0;
		}
	}

	private void multiGraspingClawAttack(Entity target) {
		lock();

		setFaceEntity(target);
		setAnimation(MULTI_CLAWS_START);
		grasphingClawMechanics(target);
		WorldTasksManager.schedule(() -> setAnimation(MULTI_CLAWS), 3);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				if (isDead() || isFinished() || isTotemAttack()) {
					stop();
					return;
				}

				setAnimation(MULTI_CLAWS);
				grasphingClawMechanics(target);
			}
		}, 5, 4);
	}

	private void multiGraspingClawAttack(Entity target, int attempts) {
		lock();

		setFaceEntity(target);
		setAnimation(MULTI_CLAWS_START);
		grasphingClawMechanics(target);
		WorldTasksManager.schedule(() -> setAnimation(MULTI_CLAWS), 3);
		WorldTasksManager.schedule(new WorldTask() {
			int attempt = 1;

			@Override
			public void run() {
				if (isDead() || isFinished() || isTotemAttack()) {
					stop();
					return;
				}

				if (attempt == attempts) {
					setAnimation(MULTI_CLAWS_END);
					WorldTasksManager.schedule(() -> unlock(), 3);
					stop();
					return;
				}

				setAnimation(MULTI_CLAWS);
				grasphingClawMechanics(target);
				attempt++;
			}
		}, 5, 4);
	}

	private int graspingClawAttack(Entity target) {
		setAnimation(SHADOW_ATK);
		lock(7);
		grasphingClawMechanics(target);

		return 10;
	}

	private void grasphingClawMechanics(Entity target) {
		IntSet coords = new IntOpenHashSet();

		Location arenaStart = locationTransform(ARENA_START);
		Location arenaEnd = locationTransform(ARENA_END);

		List<Location> playerShadows = new ArrayList<>();
		for (int x = -2; x <= 2; x++) {
			for (int y = -2; y <= 2; y++) {
				if (x == 0 && y == 0) continue;
				Location targetLocation = target.getLocation().transform(x, y);
				playerShadows.add(new Location(targetLocation.getX() - arenaStart.getX(), targetLocation.getY() - arenaStart.getY()));
			}
		}

		Collections.shuffle(playerShadows);
		playerShadows.add(0, new Location(target.getLocation().getX() - arenaStart.getX(), target.getLocation().getY() - arenaStart.getY()));

		int loops = Utils.random(15, 20);
		for (int i = 0; i < loops; i++) {
			Location location = playerShadows.get(i);
			int bitPack = location.getX() << 16 | location.getY();

			final Location shadowLocation = arenaStart.transform(location.getX(), location.getY());
			if (!World.isSquareFree(shadowLocation, 1)) {
				continue;
			}

			int distance = getMiddleLocation().getTileDistance(shadowLocation);
			if (distance < 3) {
				continue;
			}

			coords.add(bitPack);
			World.sendGraphics(SHADOW_ATK_GROUND_GFX, shadowLocation);
		}

		int x = arenaEnd.getX() - arenaStart.getX();
		int y = arenaEnd.getY() - arenaStart.getY();

		for (int i = 0; i < 40; i++) {
			int randomX = Utils.randomNoPlus(x);
			int randomY = Utils.randomNoPlus(y);
			int bitPack = randomX << 16 | randomY;
			if (coords.contains(bitPack)) {
				continue;
			}

			final Location shadowLocation = arenaStart.transform(randomX, randomY);
			if (!World.isSquareFree(shadowLocation, 1)) {
				continue;
			}

			int distance = getMiddleLocation().getTileDistance(shadowLocation);
			if (distance < 3) {
				continue;
			}

			coords.add(bitPack);
			World.sendGraphics(SHADOW_ATK_GROUND_GFX, shadowLocation);
		}

		WorldTasksManager.schedule(() -> forEachPlayers(player -> {
			if (player.isFrozen()) {
				return;
			}

			int distance = getMiddleLocation().getTileDistance(player.getLocation());
			if (distance < 2) {
				player.applyHit(new Hit(this, Utils.random(65), HitType.REGULAR));
			} else {
				int offX = player.getLocation().getX() - arenaStart.getX();
				int offY = player.getLocation().getY() - arenaStart.getY();
				int bitPack = offX << 16 | offY;
				if (coords.contains(bitPack)) {
					player.applyHit(new Hit(this, Utils.random(65), HitType.REGULAR));
				}
			}
		}), 3);
	}

	@Override
	public Location locationTransform(Location location) {
		return instance.getLocation(location);
	}

	@Override
	public void lobbyNpcRun(Consumer<NightmareLobbyNPC> consumer) {
		/* empty */
	}

	@Override
	public int baseMaxHitpoints() {
		NightmarePhase phase = getPhase();
		if (phase != null && phase.getNumber() == NightmarePhase.NightmarePhaseNumber.FIFTH) {
			return phase.getHp();
		}

		return 3200;
	}

	@Override
	public Location getBase() {
		return instance.getBaseLocation(0, 0);
	}

	@Override
	public int getMageHuskId() {
		return NightmareHuskMagicNPC.ID_PHOSANIS;
	}

	@Override
	public int getRangedHuskId() {
		return NightmareHuskRangedNPC.ID_PHOSANIS;
	}

	@Override
	public Location getSleepwalkerLocation(int index) {
		//TODO these aren't final locations, map all of them later on.
		switch (index) {
			case 0:
				return new Location(3863, 9957, 3);
			case 1:
				return new Location(3866, 9942, 3);
			case 2:
				return new Location(3881, 9945, 3);
			default:
				return new Location(3878, 9960, 3);
		}
	}

	@Override
	public int getSleepwalkerDamage() {
		return (int) (5.0 + 144.0 * ((double) absorbed / (double) getSleepwalkerCount()));
	}

	@Override
	public int getSleepwalkerId() {
		return NpcId.SLEEPWALKER_9470;
	}

	@Override
	public String getTrackingName() {
		return "Phosani's Nightmare";
	}

	@Override
	public void updateKillStat(long duration) {
		NightmareGlobal.updatePhosanisKillStatistics(duration);
		instance.checkWakePulse(20);
		instance.addInstanceKc();
		if (instance.getInstanceKc() >= 3) {//Original 5
			forEachPlayers(player1 -> player1.getCombatAchievements().complete(CAType.CANT_WAKE_UP));
		}
	}

	@Override
	public int parasiteId() {
		return NightmareParasiteNPC.ID_PHOSANIS;
	}

	@Override
	public int weakenParasiteId() {
		return NightmareParasiteNPC.ID_WEAKEN_PHOSANIS;
	}

}
