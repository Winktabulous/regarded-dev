package com.near_reality.game.content.boss.nex;

import com.zenyte.game.content.chambersofxeric.greatolm.OlmRoom;
import com.zenyte.game.content.chambersofxeric.greatolm.scripts.Lightning;
import com.zenyte.game.content.skills.magic.spells.arceuus.DeathChargeKt;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.task.TickTask;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.*;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.*;
import com.zenyte.game.world.entity.masks.*;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.npc.combatdefs.StatDefinitions;
import com.zenyte.game.world.entity.npc.combatdefs.StatType;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessorLoader;
import com.zenyte.game.world.entity.player.Bonuses;
import com.zenyte.game.world.entity.player.MovementLock;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.CharacterLoop;
import com.zenyte.utils.TimeUnit;
import mgi.types.config.npcs.NPCDefinitions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class NexNPC extends NPC implements CombatScript, Spawnable {

	public static final Location MIDDLE_LOCATION = new Location(2925, 5203);
	public static final Location NEX_BASE = new Location(2924, 5202);
	public static final ZoneBorders PRISON_ZONE = new ZoneBorders(2910, 5188, 2940, 5218);

	private static final Projectile wrathProjectile = new Projectile(2012, 30, 0, 0, 0, 60, 64, 0);
	public static final Projectile iceProjectile = new Projectile(2004, 30, 30, 45, 6, 60, 64, 0);
	public static final Projectile bloodProjectile = new Projectile(2002, 30, 0, 45, 6, 64, 64, 0);
	public static final Projectile shadowProjectile = new Projectile(1999, 30, 30, 45, 6, 60, 64, 0);
	public static final Projectile zaros = new Projectile(2007, 30, 30, 45, 6, 60, 64, 0);
	public static final Projectile soulsplitProjectile = new Projectile(2009, 30, 50, 0, 0, 50, 64, 0);
	private static final Projectile turmoilProjectile = new Projectile(2010, 30, 30, 45, 0, 60, 64, 0);
	private static final Projectile ICE_PROJECTILE_DATA = new Projectile(2010, 30, 30, 30, 6, 120, 0, 0);
	public static final Projectile smokeProjectile = new Projectile(1997, 30, 30, 45, 6, 60, 64, 0);
	public static final Animation spawnAnim = new Animation(9182);
	public static final Animation minionPower = new Animation(9189);
	public static final Animation spellAttack = new Animation(9188);
	public static final Animation groundSmash = new Animation(9186);
	public static final Animation meleeAnim = new Animation(9180);
	public static final Graphics turmoilGfx = new Graphics(2011);
	public static final int ID = NpcId.NEX;
	public static final int NO_ATK = NpcId.NEX_11279;
	public static final int SOULSPLIT = NpcId.NEX_11280;
	public static final int DEFLECT_MELEE = NpcId.NEX_11281;
	public static final int WRATH = NpcId.NEX_11282;

	private long spawnTime;
	private NexPhase phase;
	private NexStage stage;
	private FumusNPC fumus;
	private UmbraNPC umbra;
	private CruorNPC cruor;
	private GlaciesNPC glacies;
	private int specialAttackCount;
	private WorldTask darknessPulse;
	private List<NexBloodReaver> reavers = new ArrayList<>();
	private WorldTask siphonPulse;
	private List<Player> playersInPrison = new ArrayList<>();
	private WorldTask icePrisonPulse;
	private int stuckTicks;
	private int attacks;
	private int totalAttacks;
	private boolean smokeDrag;
	private boolean nexSurvivors = true;
	private boolean hitByIceSpells;
	private boolean healedBySiphon;
	private boolean hitByShadowsSmash;
	private boolean hitBySmokeDash;

	private Location nexBase;
	private ZoneBorders prisonZone;
	private NexGodwarsInstance instance;
	private Location middleLocation;
	private int startPlayers;

	protected NexNPC(int id, Location tile, Direction facing, int radius) {
		super(id, tile, facing, radius);
		this.radius = 0;
		setForceAggressive(true);
		setMaxDistance(64);
		setDeathDelay(3);
		hitBar = new NexHitBar(this);
		spawned = true;
		setIntelligent(true);
		setRun(true);
	}

	public void init(NexGodwarsInstance instance) {
		this.instance = instance;
		if (instance != null) {
			nexBase = instance.getLocation(NEX_BASE);
			prisonZone = new ZoneBorders(instance.getX(PRISON_ZONE.getSouthWestX()), instance.getY(PRISON_ZONE.getSouthWestY()), instance.getX(PRISON_ZONE.getNorthEastX()), instance.getY(PRISON_ZONE.getNorthEastY()));
			middleLocation = instance.getLocation(MIDDLE_LOCATION);
			startPlayers = instance.playersInsideInt();
		} else {
			nexBase = NEX_BASE;
			prisonZone = PRISON_ZONE;
			middleLocation = MIDDLE_LOCATION;
			startPlayers = AncientChamberArea.Companion.countPlayersInPrison();
		}
	}

	@Override
	public boolean validate(int id, String name) {
		switch (id) {
			case ID, NO_ATK, SOULSPLIT, DEFLECT_MELEE, WRATH:
				return true;
			default:
				return false;
		}
	}

	@Override
	public void processNPC() {
		super.processNPC();
		if (isDead() || isFinished()) {
			return;
		}
		int players = instance != null ? instance.playersInsideInt() : AncientChamberArea.Companion.countPlayersInPrison();
		if (players == 0) {
			finish();
			return;
		}
		checkPhaseSwitch();
		processShadowEmbrace();
		checkStuck();
		smokeDrag();
		if (stage != null && nextAttackReady()) {
			switch (stage) {
				case SPAWN:
					handleSpawn();
					break;
				case SMOKE_CHOKE:
					chokeAttack();
					break;
				case SMOKE_DASH:
					smokeDash();
					break;
				case SHADOW_SWITCH:
					shadowSwitch();
					break;
				case SHADOW_SMASH:
					shadowSmash();
					break;
				case SHADOW_EMBRACE:
					shadowEmbrace();
					break;
				case BLOOD_SWITCH:
					bloodSwitch();
					break;
				case BLOOD_SIPHON:
					bloodSiphon();
					break;
				case BLOOD_SACRIFICE:
					bloodSacrifice();
					break;
				case ICE_SWITCH:
					iceSwitch();
					break;
				case ICE_CONTAINMENT:
					iceContaiment();
					break;
				case ICE_PRISON:
					icePrison();
					break;
				case ZAROS_SWITCH:
					zarosSwitch();
					break;
			}
		}
	}

	private void checkStuck() {
		if (stage == null || !stage.isAutoAttack() || !getCombatDefinitions().isMelee()) {
			return;
		}

		stuckTicks++;
		if (stuckTicks >= getAttackSpeed() * 3) {
			stuckTicks = 0;
			dashTo(nexBase);
		}
	}

	private void dashTo(Location location) {
		setAnimation(new Animation(9187));
		setForceMovement(new ForceMovement(getLocation(), 0, location, 30, OlmRoom.getMovementDirection(getLocation(), location)));
		WorldTasksManager.schedule(() -> {
			setAnimation(null);
			setLocation(location);
		});
		swapStyles(false);
		setNextAttack(2);
		lock(2);
	}

	private void zarosSwitch() {
		stage = null;
		lock();
		setNextAttack(3);
		switchPhase(NexPhase.ZAROS);
		scheduleHit(this, new Hit(500, HitType.HEALED), 0);
		setAnimation(new Animation(9179));
		setGraphics(new Graphics(2016));
		switchStage(NexStage.ZAROS_AA);
		WorldTasksManager.schedule(() -> {
			setTransformation(SOULSPLIT);
			unlock();
		}, 3);
	}

	private void iceSwitch() {
		stage = null;
		lock();
		setNextAttack(5);
		switchPhase(NexPhase.ICE);
		WorldTasksManager.schedule(() -> {
			unlock();
			if (Utils.randomBoolean()) {
				switchStage(NexStage.ICE_CONTAINMENT);
			} else {
				specialAttackCount = 1;
				switchStage(NexStage.ICE_PRISON);
			}
		}, 5);
	}

	private void bloodSwitch() {
		stage = null;
		lock();
		setNextAttack(5);
		switchPhase(NexPhase.BLOOD);
		WorldTasksManager.schedule(() -> {
			unlock();
			if (Utils.randomBoolean()) {
				switchStage(NexStage.BLOOD_SACRIFICE);
			} else {
				specialAttackCount = 1;
				switchStage(NexStage.BLOOD_SIPHON);
			}
		}, 5);
	}

	private void shadowSwitch() {
		stage = null;
		lock();
		setNextAttack(5);
		switchPhase(NexPhase.SHADOW);
		WorldTasksManager.schedule(() -> {
			unlock();
			if (Utils.randomBoolean()) {
				switchStage(NexStage.SHADOW_EMBRACE);
			} else {
				switchStage(NexStage.SHADOW_SMASH);
			}
		}, 5);
	}

	private void icePrison() {
		Player randomPlayer = randomPlayerInPrison();
		if (randomPlayer != null) {
			stage = null;
			setNextAttack(8);
			setAnimation(spellAttack);
			say("Die now, in a prison of ice!");
			specialAttackCount++;

			Projectile icePrisonProjectile = new Projectile(2006, 30, 30, 45, 0, 60, 64, 1);
			int ticks = icePrisonProjectile.build(this, randomPlayer);
			WorldTasksManager.schedule(() -> {
				randomPlayer.stopAll();
				randomPlayer.lock();
				randomPlayer.addMovementLock(new MovementLock(System.currentTimeMillis() + TimeUnit.TICKS.toMillis(10), null, () -> randomPlayer.sendSound(Lightning.walkFailSound)));

				playersInPrison.clear();
				Location prisonLocation = randomPlayer.getLocation().copy();
				int x1 = prisonLocation.getX() - 1, x2 = prisonLocation.getX() + randomPlayer.getSize();
				int y1 = prisonLocation.getY() - 1, y2 = prisonLocation.getY() + randomPlayer.getSize();
				spawnStalagmites(randomPlayer, true, new ZoneBorders(x1, y1, x2, y2));

				forEachPlayerInPrison(player -> {
					if (player.getLocation().matches(prisonLocation)) {
						player.sendMessage(Colour.RS_PINK.wrap("You've been trapped in an ice prison!"));
						player.lock();
						player.stopAll();
						Lightning.deactivateOverheadProtectionPrayers(player, player.getPrayerManager(), true);
						playersInPrison.add(player);
					}
				});

				icePrisonPulse = new WorldTask() {
					boolean hitPlayers = true;

					@Override
					public void run() {
						if (hitPlayers) {
							for (Player player : playersInPrison) {
								player.unlock();
								player.applyHit(new Hit(Utils.random(75), HitType.REGULAR));
								hitByIceSpells = true;
							}
						}
					}

					@Override
					public void stop() {
						hitPlayers = false;
						WorldTask.super.stop();
					}
				};
				WorldTasksManager.schedule(icePrisonPulse, 8);
			}, ticks);
		}

		switchStage(NexStage.ICE_AA);
	}

	private Player randomPlayerInPrison() {
		return Utils.random(getPlayersInPrison());
	}

	private void iceContaiment() {
		stage = null;
		setNextAttack(getAttackSpeed());
		setAnimation(groundSmash);
		say("Contain this!");
		specialAttackCount++;
		WorldTasksManager.schedule(() -> {
			int x1 = getX() - 1, x2 = getX() + getSize();
			int y1 = getY() - 1, y2 = getY() + getSize();
			ZoneBorders zoneBorders = new ZoneBorders(x1, y1, x2, y2);
			spawnStalagmites(this, false, zoneBorders);

			forEachPlayerInPrison(player -> {
				if (zoneBorders.insideBorder(player)) {
					player.setAnimation(new Animation(1114));
					Lightning.deactivateOverheadProtectionPrayers(player, player.getPrayerManager(), true);
					player.scheduleHit(this, new Hit(Utils.random(60), HitType.DEFAULT), 1);
					player.stun(10);
					player.addMovementLock(new MovementLock(System.currentTimeMillis() + TimeUnit.TICKS.toMillis(10), null, () -> player.sendSound(Lightning.walkFailSound)));
					hitByIceSpells = true;
				}
			});
		}, 5);

		switchStage(NexStage.ICE_AA);
	}

	private void spawnStalagmites(Entity entity, boolean breakable, ZoneBorders borders) {
		int id = breakable ? 42944 : 42943;
		int x1 = borders.getSouthWestX(), x2 = borders.getNorthEastX();
		int y1 = borders.getSouthWestY(), y2 = borders.getNorthEastY();
		int z = entity.getPlane();
		for (int y = y1; y <= y2; y++) {
			spawnStalagmite(id, z, x1, y, entity, breakable);
			spawnStalagmite(id, z, x2, y, entity, breakable);
		}
		for (int x = x1; x <= x2; x++) {
			spawnStalagmite(id, z, x, y1, entity, breakable);
			spawnStalagmite(id, z, x, y2, entity, breakable);
		}
	}

	private void spawnStalagmite(int id, int plane, int x, int y, Entity entity, boolean breakable) {
		if (!World.isSquareFree(x, y, plane, 1)) {
			return;
		}

		Location location = new Location(x, y, plane);
		if (breakable) {
			forEachPlayerInPrison(player -> {
				Location playerLocation = player.getLocation();
				if (playerLocation.matches(location)) {
					Location pushOffLocation = location.transform(Direction.getDirection(entity.getLocation(), location));
					if (World.isSquareFree(pushOffLocation.getX(), pushOffLocation.getY(), plane, 1)) {//TODO maybe make player fly to next available tile? good enough for now lol
						player.setAnimation(new Animation(1157));
						player.setForceMovement(new ForceMovement(player.getLocation(), 80, pushOffLocation, 80, OlmRoom.getMovementDirection(player.getLocation(), pushOffLocation)));
					}
				}
			});
		}

		World.spawnTemporaryObject(new WorldObject(id, 10, 0, location), 10);
	}

	private void bloodSacrifice() {
		Player player = randomPlayerInPrison();
		if (player != null) {
			specialAttackCount++;
			setNextAttack(8);
			say("I demand a blood sacrifice!");
			int duration = 7;
			player.setTinting(new Tinting(0, 6, 28, 112, 0, duration * 30));
			player.sendMessage(Colour.RED.wrap("Nex has marked you for a blood sacrifice! RUN!"));
			WorldTasksManager.schedule(() -> {
				Location playerLocation = player.getLocation().copy();
				int distance = playerLocation.getTileDistance(getLocation());
				if (distance < 7) {
					int damage = Utils.random(50);
					int heal = damage;
					player.applyHit(new Hit(damage, HitType.DEFAULT));
					player.getPrayerManager().drainPrayerPoints(player.getPrayerManager().getPrayerPoints() / 3);

					List<Player> nearbyPlayers = CharacterLoop.find(playerLocation, 1, Player.class, p2 -> !p2.isDead() && !p2.isFinished());
					for (Player p2 : nearbyPlayers) {
						if (p2 == null || player.equals(p2)) continue;
						int damage2 = Utils.random(12);
						heal += damage2;
						p2.getPrayerManager().drainPrayerPoints(player.getPrayerManager().getPrayerPoints() / 3);
					}

					scheduleHit(this, new Hit(heal, HitType.HEALED), 0);
				} else {
					player.sendMessage(Colour.RS_GREEN.wrap("You managed to escape from Nex's blood sacrifice."));
				}
			}, duration);
		}

		switchStage(NexStage.BLOOD_AA);
	}

	private void openHud() {
		forEachPlayerInPrison(player -> {
			player.getHpHud().open(ID, getMaxHitpoints());
			player.getBossTimer().startTracking("Nex");
		});

	}

	private void bloodSiphon() {
		stage = null;
		setNextAttack(10);
		say("A siphon will solve this!");
		setAnimation(new Animation(9183));
		setGraphics(new Graphics(2015));
		specialAttackCount++;

		AtomicInteger hpHealed = new AtomicInteger();
		forEachReaver(reaver -> hpHealed.addAndGet(reaver.healNex()));
		int heal = hpHealed.get();
		if (heal > 0) {
			scheduleHit(this, new Hit(heal, HitType.HEALED), 0);
		}

		reavers.clear();
		int reaverCount = Utils.interpolate(1, 8, 1, 20, getPlayersInPrison().size());
		for (int i = 0; i < reaverCount; i++) {
			NexBloodReaver reaver = (NexBloodReaver) World.spawnNPC(new NexBloodReaver(getDistantLocation(getLocation(), NPCDefinitions.get(NexBloodReaver.ID).getSize()), Direction.SOUTH, 5, this));
			reavers.add(reaver);
		}

		lock();
		siphonPulse = () -> {
			siphonPulse = null;
			unlock();
		};
		WorldTasksManager.schedule(siphonPulse, 8);

		switchStage(NexStage.BLOOD_AA);
	}

	private Location getDistantLocation(final Location middle, final int size) {
		int count = 50;
		loop:
		while (true) {
			if (count-- <= 0) {
				return middle;
			}
			final Location tile = new Location(Utils.random(-8, 8) + middle.getX(), Utils.random(-8, 8) + middle.getY(), middle.getPlane());
			if (!prisonZone.insideBorder(tile)) {
				continue;
			}
			for (int x = tile.getX(), endX = tile.getX() + size; x < endX; x++) {
				for (int y = tile.getY(), endY = tile.getY() + size; y < endY; y++) {
					if (World.getMask(tile.getPlane(), x, y) != 0) {
						continue loop;
					}
				}
			}
			return tile;
		}
	}

	public void forEachReaver(Consumer<NexBloodReaver> consumer) {
		int length = reavers.size();
		if (length <= 0) {
			return;
		}

		for (int i = 0; i < length; i++) {
			consumer.accept(reavers.get(i));
		}
	}

	private void processShadowEmbrace() {
		if (darknessPulse == null) {
			return;
		}

		Location nexLocation = getMiddleLocation().copy();
		forEachPlayerInPrison(player -> {
			int distance = player.getLocation().getTileDistance(nexLocation);
			int newTrans;
			if (distance <= 2) {
				newTrans = 100;
				final int warningTicks = player.getNumericTemporaryAttribute("nex_darkness_warning_ticks").intValue();
				if (distance <= 1) {
					newTrans = 50;
					if (warningTicks == 0) {
						player.sendMessage(Colour.RS_PINK.wrap("Darkness begins to surround Nex."));
					}
					//only increment warning ticks when directly next to her
					player.addTemporaryAttribute("nex_darkness_warning_ticks", warningTicks + 1);
				}

				//if warning has been triggered and player was around her too long, do damage when *near* her every tick
				if (warningTicks >= 5) {
					if (player.getNumericTemporaryAttribute("nex_darkness_warn").intValue() == 0) {
						player.sendMessage(Colour.RS_PINK.wrap("The darkness starts to engulf you!"));
						player.addTemporaryAttribute("nex_darkness_warn", 1);
					}
					player.applyHit(new Hit(Utils.random(8), HitType.DEFAULT));
				}
			} else if (distance <= 4) {
				newTrans = 150;
			} else {
				newTrans = 200;
			}

			player.getPacketDispatcher().sendClientScript(5313, newTrans);
		});
	}

	private void shadowEmbrace() {
		setNextAttack(getAttackSpeed());
		switchStage(NexStage.SHADOW_AA);

		setAnimation(new Animation(9182));
		say("Embrace darkness!");
		if (darknessPulse != null) {
			darknessPulse.stop();
		}

		darknessPulse = new WorldTask() {
			@Override
			public void run() {
				stopShadowEmbrace();
			}
		};
		WorldTasksManager.schedule(darknessPulse, 30);
	}

	private void shadowSmash() {
		setNextAttack(7);
		switchStage(NexStage.SHADOW_AA);
		setAnimation(groundSmash);

		say("Fear the shadow!");
		forEachPlayerInPrison(player -> {
			Location location = player.getLocation().copy();
			WorldObject object = new WorldObject(42942, 10, 0, location);
			World.spawnObject(object);
			WorldTasksManager.schedule(() -> {
				if (player.getLocation().matches(location)) {
					player.applyHit(new Hit(Utils.random(50), HitType.REGULAR));
					hitByShadowsSmash = true;
				}
				World.sendGraphics(new Graphics(383), location);
				World.removeObject(object);
			}, 3);
		});
	}

	private void chokeAttack() {
		setNextAttack(getAttackSpeed());
		switchStage(NexStage.SMOKE_AA);

		Player targetPlayer = null;
		int playerMagicBonus = Integer.MAX_VALUE;
		final List<Player> players = getPlayersInPrison();
		for (Player player : players) {
			if (player == null) continue;
			int thisBonus = player.getBonuses().getBonus(Bonuses.Bonus.DEF_MAGIC);
			if (thisBonus < playerMagicBonus) {
				targetPlayer = player;
				playerMagicBonus = thisBonus;
			}
		}

		if (targetPlayer == null) {
			return;
		}

		say("Let the virus flow through you!");
		setAnimation(spellAttack);
		setFaceEntity(targetPlayer);
		NexPlayerExtKt.startChoking(targetPlayer, false);
		specialAttackCount++;
	}

	private void smokeDash() {
		specialAttackCount++;
		stage = null;
		endCombatPulse();
		int nextAttackTicks = 14;
		setNextAttack(nextAttackTicks);
		lock(nextAttackTicks);
		say("There is...");
		NexDashData data = Utils.random(NexDashData.values);
		final Location startLocation;
		final ZoneBorders hitBorders;
		if (instance != null) {
			startLocation = instance.getLocation(data.getStartLocation());
			hitBorders = new ZoneBorders(instance.getX(data.getHitZone().getSouthWestX()), instance.getY(data.getHitZone().getSouthWestY()), instance.getX(data.getHitZone().getNorthEastX()), instance.getY(data.getHitZone().getNorthEastY()));
		} else {
			startLocation = data.getStartLocation();
			hitBorders = data.getHitZone();
		}

		int forceMoveDirection = OlmRoom.getMovementDirection(startLocation, nexBase);
		Direction direction = ForceMovement.direction(startLocation, nexBase);

		WorldTasksManager.schedule(new TickTask() {

			@Override
			public void run() {
				switch (ticks) {
					case 2:
						setFaceLocation(startLocation);
						break;
					case 3:
						setAnimation(new Animation(9178));
						break;
					case 6:
						setLocation(startLocation);
						setFaceLocation(nexBase);
						break;
					case 8:
						blockIncomingHits(4);
						setForceMovement(new ForceMovement(startLocation, 10, nexBase, 80, forceMoveDirection));
						say("NO ESCAPE!");
						forEachPlayerInPrison(player -> {
							if (player.getActionManager().getAction() instanceof PlayerCombat) {
								final PlayerCombat combat = (PlayerCombat) player.getActionManager().getAction();
								if (combat.getTarget() == NexNPC.this) {
									player.cancelCombat();
								}
							}
						});
						break;
					case 9:
						for (Player player : getPlayersInPrison()) {
							if (player != null && hitBorders.insideBorder(player)) {
								hitBySmokeDash = true;
								player.setAnimation(new Animation(1157));
								Location dest = nexBase.transform(direction, 3);
								player.setForceMovement(new ForceMovement(player.getLocation(), 30, dest, 80, forceMoveDirection));
								WorldTasksManager.schedule(() -> {
									player.sendMessage("<col=ff289d>Nex dashes into you and knocks you out of the way!</col>");
									player.applyHit(new Hit(Utils.random(50), HitType.REGULAR));
									player.setLocation(dest);
								});
							}
						}
						break;
					case 12:
						setLocation(nexBase);
						unlock();
						setAnimation(Animation.STOP);
						switchStage(NexStage.SMOKE_AA);
						stop();
						return;
				}

				ticks++;
			}
		}, 0, 0);
	}

	private void endCombatPulse() {
		getCombat().reset();
	}

	private List<Player> getPlayersInPrison() {
		return CharacterLoop.find(middleLocation, 15, Player.class, player -> !player.isDead());
	}

	public void forEachPlayerInPrison(Consumer<Player> consumer) {
		for (Player player : getPlayersInPrison()) {
			if (player != null) {
				consumer.accept(player);
			}
		}
	}

	private boolean nextAttackReady() {
		return getCombat().getCombatDelay() <= 0;
	}

	private void handleSpawn() {
		stage = null;
		spawnTime = System.currentTimeMillis();
		openHud();
		lock();
		WorldTasksManager.schedule(new TickTask() {

			@Override
			public void run() {
				if (isFinished()) {
					stop();
					return;
				}
				switch (ticks) {
					case 0 -> {
						say("AT LAST!");
						setAnimation(spawnAnim);
					}
					case 5 -> {
						Location spawnLocation = NexPhase.SMOKE.getLocation();
						if (instance != null) {
							spawnLocation = instance.getLocation(spawnLocation);
						}
						fumus = new FumusNPC(spawnLocation, NexPhase.SMOKE.getSpawnDirection(), NexNPC.this);
						spawnMinion(fumus);
					}
					case 10 -> {
						Location spawnLocation = NexPhase.SHADOW.getLocation();
						if (instance != null) {
							spawnLocation = instance.getLocation(spawnLocation);
						}
						umbra = new UmbraNPC(spawnLocation, NexPhase.SHADOW.getSpawnDirection(), NexNPC.this);
						spawnMinion(umbra);
					}
					case 15 -> {
						Location spawnLocation = NexPhase.BLOOD.getLocation();
						if (instance != null) {
							spawnLocation = instance.getLocation(spawnLocation);
						}
						cruor = new CruorNPC(spawnLocation, NexPhase.BLOOD.getSpawnDirection(), NexNPC.this);
						spawnMinion(cruor);
					}
					case 20 -> {
						Location spawnLocation = NexPhase.ICE.getLocation();
						if (instance != null) {
							spawnLocation = instance.getLocation(spawnLocation);
						}
						glacies = new GlaciesNPC(spawnLocation, NexPhase.ICE.getSpawnDirection(), NexNPC.this);
						spawnMinion(glacies);
					}
					case 25 -> {
						switchPhase(NexPhase.SMOKE);
						faceDirection(getSpawnDirection());
						setTransformation(ID);
					}
					case 30 -> {
						unlock();
						switchStage(NexStage.SMOKE_CHOKE);
						stop();
						return;
					}
				}

				ticks++;
			}
		}, 0, 0);
	}

	private void spawnMinion(NexMinion npc) {
		say(npc.getName() + "!");
		setAnimation(minionPower);
		setFaceLocation(npc.getLocation());
		takePowerFrom(npc);
		npc.spawn();
	}

	private void takePowerFrom(NPC npc) {
		npc.setAnimation(NexMinion.attackAnimation);
		ICE_PROJECTILE_DATA.build(npc, this);
	}

	private void checkPhaseSwitch() {
		if (phase == null) {
			return;
		}

		NexMinion minion = findPhaseNpc(phase);
		if (minion != null && minion.isImmune()) {
			int hp = getHitpoints();
			int maxHp = getMaxHitpoints();
			int percent = hp * 100 / maxHp;
			if (percent <= phase.getNextPhasePercent()) {
				say(minion.getName() + ", don't fail me!");
				minion.removeImmunity();
				minion.enableAggression();
			}
		}
	}

	private NexMinion findPhaseNpc(NexPhase phase) {
		switch (phase) {
			case SMOKE:
				return fumus;
			case SHADOW:
				return umbra;
			case BLOOD:
				return cruor;
			case ICE:
				return glacies;
			default:
				return null;
		}
	}

	public void switchPhase(NexPhase phase) {
		this.phase = phase;
		specialAttackCount = 0;
		say(phase.getPhaseStartText());
		NexMinion npc = findPhaseNpc(phase);
		if (npc != null) {
			takePowerFrom(npc);
		}
	}

	public void switchStage(NexStage stage) {
		if (this.stage != null && this.stage.isPriority() && stage != null && !stage.isPriority()) {
			return;
		}

		this.stage = stage;
	}

	public int getAttackSpeed() {
		return getCombatDefinitions().getAttackSpeed();
	}

	public void setNextAttack(int ticks) {
		getCombat().setCombatDelay(ticks);
	}

	@Override
	public int attack(Entity t) {
		if (!(t instanceof Player)) {
			return 0;
		}

		Player target = (Player) t;
		if (stage == null) {
			return getAttackSpeed();
		}

		boolean switchStyles = false;
		if (stage.isAutoAttack()) {
			//TODO prob dont need this anymore zenyte handles?
			/*if (melee && !getProperties().getCombatPulse().canInteract().canAttack()) {
				return getAttackSpeed();
			} else if (!CombatStyle.RANGE.canAttack(this, victim)) {
				return getAttackSpeed();
			}*/

			switchStyles = Utils.randomBoolean(5);
		}

		stuckTicks = 0;
		switch (stage) {
			case SMOKE_AA -> smokeAuto(target);
			case SHADOW_AA -> shadowAuto(target);
			case BLOOD_AA -> bloodAuto(target);
			case ICE_AA -> iceAuto(target);
			case ZAROS_AA -> zarosAuto(target);
		}

		if (switchStyles) {
			swapStyles(!getCombatDefinitions().isMelee());
		}

		return getAttackSpeed();
	}

	private void increaseAttack() {
		attacks++;
		totalAttacks++;
		//TODO target switching proper 1 instead dropping combat
		//if (totalAttacks > 0 && totalAttacks % 3 == 0) {
		//	getCombat().removeTarget();
		//}
	}

	private void handleTurmoil(Player entity) {
		if (phase != NexPhase.ZAROS || !Utils.randomBoolean(4)) {
			return;
		}

		int ticks = turmoilProjectile.build(this, entity);
		WorldTasksManager.schedule(() -> {
			entity.setGraphics(turmoilGfx);
			entity.getSkills().drainSkill(SkillConstants.ATTACK, 2);
			entity.getSkills().drainSkill(SkillConstants.STRENGTH, 2);
			entity.getSkills().drainSkill(SkillConstants.DEFENCE, 2);
			entity.getSkills().drainSkill(SkillConstants.RANGED, 2);
			entity.getSkills().drainSkill(SkillConstants.MAGIC, 2);

			final StatDefinitions statDefinitions = combatDefinitions.getStatDefinitions();
			for (final StatType statType : StatType.levelTypes) {
				final int currentLevel = statDefinitions.get(statType);
				statDefinitions.set(statType, currentLevel + 2);
			}
		}, ticks);
	}

	private void handleSoulSplit(Player entity, int damage) {
		if (getId() != SOULSPLIT) {
			return;
		}

		int ticks = soulsplitProjectile.build(entity, this);
		scheduleHit(this, new Hit(damage, HitType.HEALED), ticks);
	}

	private void meleeAuto(Player victim) {
		setAnimation(meleeAnim);
		final Hit hit = melee(victim, 30);
		victim.scheduleHit(this, hit, -1);
		handleSoulSplit(victim, hit.getDamage());
		handleTurmoil(victim);
	}

	private void zarosAuto(Player victim) {
		setNextAttack(getAttackSpeed());

		if (getCombatDefinitions().isMelee()) {
			meleeAuto(victim);
		} else {
			setAnimation(minionPower);
			forEachPlayerInPrison(player -> {
				int ticks = zaros.build(this, player);
				WorldTasksManager.schedule(() -> player.scheduleHit(this, magic(player, 40).onLand(hit -> {
					int prayerDrain = 5;
					if (player.getShield() != null && player.getShield().getId() == ItemId.SPECTRAL_SPIRIT_SHIELD) {
						prayerDrain /= 2;
					}
					player.getPrayerManager().drainPrayerPoints(prayerDrain);
					handleSoulSplit(player, hit.getDamage());
					Graphics graphic;
					if (hit.getDamage() <= 0) {
						graphic = new Graphics(85, 0, 124);
					} else {
						graphic = new Graphics(2008, 0, 96);
					}
					player.setGraphics(graphic);
				}), 0), ticks);
			});
		}

		increaseAttack();
		if (attacks >= 5) {
			attacks = 0;
			if (getId() == ID) {
				setTransformation(SOULSPLIT);
			} else if (getId() == SOULSPLIT) {
				setTransformation(DEFLECT_MELEE);
			} else if (getId() == DEFLECT_MELEE) {
				setTransformation(ID);
			}
		}

		switchStage(NexStage.ZAROS_AA);
	}

	private void iceAuto(Player victim) {
		setNextAttack(getAttackSpeed());

		if (getCombatDefinitions().isMelee()) {
			meleeAuto(victim);
		} else {
			setAnimation(minionPower);
			forEachPlayerInPrison(player -> {
				int ticks = iceProjectile.build(this, player);
				WorldTasksManager.schedule(() -> player.scheduleHit(this, magic(player, 33).onLand(hit -> {
					int prayerDrain = hit.getDamage();
					if (player.getShield() != null && player.getShield().getId() == ItemId.SPECTRAL_SPIRIT_SHIELD) {
						prayerDrain /= 3;
					} else {
						prayerDrain /= 2;
					}
					player.getPrayerManager().drainPrayerPoints(prayerDrain);

					Graphics graphic;
					if (hit.getDamage() <= 0) {
						graphic = new Graphics(85, 0, 124);
					} else {
						graphic = new Graphics(2005);
					}
					player.setGraphics(graphic);

					if (!player.getPrayerManager().isActive(Prayer.PROTECT_FROM_MAGIC)) {
						player.freeze(8);
					}
				}), 0), ticks);
			});
		}

		increaseAttack();
		if (attacks >= 5) {
			attacks = 0;
			switch (specialAttackCount % 2) {
				case 0:
					switchStage(NexStage.ICE_CONTAINMENT);
					break;
				case 1:
					switchStage(NexStage.ICE_PRISON);
					break;
			}
			return;
		}

		switchStage(NexStage.ICE_AA);
	}

	private void bloodAuto(Player victim) {
		setNextAttack(getAttackSpeed());

		if (getCombatDefinitions().isMelee()) {
			meleeAuto(victim);
		} else {
			setAnimation(minionPower);
			int ticks = bloodProjectile.build(this, victim);

			WorldTasksManager.schedule(() -> {
				List<Player> list = CharacterLoop.find(victim.getLocation(), 1, Player.class, player -> !player.isDead() && !player.isFinished());
				int size = Math.min(list.size(), 9);
				for (int i = 0; i < size; i++) {
					Player player = list.get(i);
					player.scheduleHit(this, magic(player, 33).onLand(hit -> {
						int damage = hit.getDamage();

						int prayerDrain = 5;
						if (player.getShield() != null && player.getShield().getId() == ItemId.SPECTRAL_SPIRIT_SHIELD) {
							prayerDrain /= 2;
						}
						player.getPrayerManager().drainPrayerPoints(prayerDrain);

						int heal = damage / 4;
						if (heal > 0) {
							scheduleHit(this, new Hit(heal, HitType.HEALED), 0);
						}

						Graphics graphic;
						if (damage <= 0) {
							graphic = new Graphics(85, 0, 124);
						} else {
							graphic = new Graphics(2003);
						}
						player.setGraphics(graphic);
					}), 0);
				}
			}, ticks);
		}

		increaseAttack();
		if (attacks >= 5) {
			attacks = 0;
			switch (specialAttackCount % 2) {
				case 0:
					switchStage(NexStage.BLOOD_SACRIFICE);
					break;
				case 1:
					switchStage(NexStage.BLOOD_SIPHON);
					break;
			}
			return;
		}

		switchStage(NexStage.BLOOD_AA);
	}

	private void shadowAuto(Player victim) {
		setNextAttack(getAttackSpeed());

		if (getCombatDefinitions().isMelee()) {
			meleeAuto(victim);
		} else {
			setAnimation(minionPower);
			forEachPlayerInPrison(player -> {
				int ticks = shadowProjectile.build(this, player);
				WorldTasksManager.schedule(() -> {
					int distance = getMiddleLocation().getTileDistance(player.getLocation());
					int maxHit = Utils.interpolate(60, 2, 1, 10, Math.max(Math.min(distance, 10), 1));
					player.scheduleHit(this, ranged(player, maxHit), -1);
				}, ticks);
			});
		}

		increaseAttack();
		if (attacks >= 5) {
			attacks = 0;
			if (Utils.randomBoolean()) {
				switchStage(NexStage.SHADOW_EMBRACE);
			} else {
				switchStage(NexStage.SHADOW_SMASH);
			}
			return;
		}

		switchStage(NexStage.SHADOW_AA);
	}

	private void smokeAuto(Player victim) {
		setNextAttack(getAttackSpeed());

		if (getCombatDefinitions().isMelee()) {
			meleeAuto(victim);
		} else {
			setAnimation(minionPower);
			forEachPlayerInPrison(player -> {
				int ticks = smokeProjectile.build(this, player);
				WorldTasksManager.schedule(() -> {
					player.scheduleHit(this, magic(player, 33).onLand(hit -> {
						player.getToxins().applyToxin(Toxins.ToxinType.POISON, 8, this);
						Graphics graphic;
						if (hit.getDamage() <= 0) {
							graphic = new Graphics(85, 0, 124);
						} else {
							graphic = new Graphics(1998, 0, 50);
						}
						player.setGraphics(graphic);
					}), 0);
				}, ticks);
			});
		}

		increaseAttack();
		if (attacks >= 5) {
			attacks = 0;
			switch (specialAttackCount % 2) {
				case 0 -> switchStage(NexStage.SMOKE_CHOKE);
				case 1 -> switchStage(NexStage.SMOKE_DASH);
			}
			return;
		}

		switchStage(NexStage.SMOKE_AA);
	}

	private void swapStyles(boolean melee) {
		if (!melee) {
			getCombatDefinitions().getAttackDefinitions().setType(AttackType.MAGIC);
		} else {
			getCombatDefinitions().getAttackDefinitions().setType(AttackType.MELEE);
		}
		updateCombatDistance();
	}

	private void updateCombatDistance() {
		if (getCombatDefinitions().isMelee()) {
			setAttackDistance(1);
		} else {
			setAttackDistance(14);
		}
	}

	@Override
	public boolean isTolerable() {
		return false;
	}

	public void drag() {
		smokeDrag = true;
	}

	public void smokeDrag() {
		if (!smokeDrag) {
			return;
		}

		smokeDrag = false;
		List<Player> players = CharacterLoop.find(middleLocation, 15, Player.class, player -> !player.isDead() && !player.isFinished() && ProjectileUtils.isProjectileClipped(null, null, getMiddleLocation(), player, false));
		Player targetPlayer = Utils.random(players);
		if (targetPlayer == null) {
			return;
		}

		targetPlayer.lock(1);
		targetPlayer.setAnimation(new Animation(7208));
		Lightning.deactivateOverheadProtectionPrayers(targetPlayer, targetPlayer.getPrayerManager(), true);

		int forceMoveDirection = OlmRoom.getMovementDirection(getMiddleLocation(), nexBase);
		Direction direction = ForceMovement.direction(getMiddleLocation(), nexBase);
		Location dest = getMiddleLocation().transform(direction, 2);
		targetPlayer.setForceMovement(new ForceMovement(targetPlayer.getLocation(), 30, dest, 80, forceMoveDirection));
		WorldTasksManager.schedule(() -> {
			targetPlayer.setLocation(dest);
			targetPlayer.setAnimation(null);
		});
	}

	public void stopShadowEmbrace() {
		if (darknessPulse != null) {
			darknessPulse.stop();
			forEachPlayerInPrison(player -> player.getPacketDispatcher().sendClientScript(5313, 255));
			darknessPulse = null;
		}
	}

	public void clearReavers() {
		forEachReaver(reaver -> reaver.sendDeath());
		reavers.clear();
	}

	@Override
	public void finish() {
		super.finish();

		despawnMinion(fumus);
		despawnMinion(umbra);
		despawnMinion(cruor);
		despawnMinion(glacies);
		clearReavers();
	}

	private void despawnMinion(NPC npc) {
		if (npc != null) {
			npc.finish();
		}
	}

	public void say(String line) {
		setForceTalk(line);
		String lineToSay = "Nex: " + Colour.BLUE.wrap(line);
		forEachPlayerInPrison(player -> player.sendMessage(lineToSay));
	}

	@Override
	public void sendDeath() {
		super.sendDeath();

		NexModule.updateKillStatistics(spawnTime);
		forEachPlayerInPrison(player -> {
			player.sendMessage("The MVP for this fight was: "+getMostDamagePlayer().getName());
			player.getHpHud().close();
			player.getSlayer().checkAssignment(this);
		});
		say("Taste my wrath!");
		setTransformation(WRATH);
		WorldTasksManager.schedule(() -> {
			Location center = getMiddleLocation().copy();
			World.sendGraphics(new Graphics(2013), center);

			int x1 = center.getX() - 3, x2 = center.getX() + 3;
			int y1 = center.getY() - 3, y2 = center.getY() + 3;
			for (int y = y1; y <= y2; y++) {
				wrathGraphic(center, new Location(x1, y));
				wrathGraphic(center, new Location(x2, y));
			}
			for (int x = x1; x <= x2; x++) {
				wrathGraphic(center, new Location(x, y1));
				wrathGraphic(center, new Location(x, y2));
			}

			final ZoneBorders deathZone = new ZoneBorders(x1, y1, x2, y2);
			WorldTasksManager.schedule(() -> {
				forEachPlayerInPrison(player -> {
					if (deathZone.insideBorder(player)) {
						player.scheduleHit(this, new Hit(Utils.random(40), HitType.REGULAR), 4);
					}
				});
			});
		}, 2);
	}

	@Override
	protected void drop(Location tile) {
		final AtomicBoolean handledDrop = new AtomicBoolean(false);
		forEachPlayerInPrison(player -> {
			onDrop(player);
			if (!handledDrop.get()) {
				final List<DropProcessor> processors = DropProcessorLoader.get(NpcId.NEX);
				if (processors != null) {
					for (final DropProcessor processor : processors) {
						processor.onDeath(this, player);
						handledDrop.set(true);
					}
				}
			}
		});
	}

	@Override
	protected void onDeath(Entity source) {
		try {
			setTimeOfDeath(WorldThread.WORLD_CYCLE);
			resetWalkSteps();
			combat.removeTarget();
			setAnimation(null);
			if (source instanceof Player player) {
				DeathChargeKt.invokeDeathChargeEffect(player);
			}
		} catch (Exception e) {
			getLogger().error("", e);
		}
	}

	private void wrathGraphic(Location center, Location location) {
		if (!World.isSquareFree(location, 1)) {
			return;
		}

		int ticks = wrathProjectile.build(center, location);
		WorldTasksManager.schedule(() -> World.sendGraphics(new Graphics(2014), location), ticks);
	}

	@Override
	public void postHitProcess(Hit hit) {
		super.postHitProcess(hit);

		forEachPlayerInPrison(player -> player.getHpHud().updateValue(getHitpoints()));
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		super.handleIngoingHit(hit);

		if (phase != null) {
			hit.setExecuteIfLocked();

			Entity attacker = hit.getSource();
			if (phase == NexPhase.BLOOD && siphonPulse != null) {
				hit.setHitType(HitType.HEALED);
				healedBySiphon = true;
				return;
			}

			NexMinion minion = findPhaseNpc(phase);
			if (minion != null && !minion.isImmune()) {
				hit.setDamage(0);
				if (attacker instanceof Player) {
					((Player) attacker).sendMessage(Colour.RED.wrap("Nex is currently immune to your attacks."));
				}
			}

			if (phase == NexPhase.ZAROS) {
				if (getId() == DEFLECT_MELEE && hit.getHitType() == HitType.MELEE) {
					final int finalHit = hit.getDamage() / 2;
					hit.setDamage(0);
					if (finalHit > 0) {
						attacker.scheduleHit(this, new Hit(finalHit, HitType.REGULAR), 0);
					}
				}
			} else {
				int phaseHp = getMaxHitpoints() * phase.getNextPhasePercent() / 100;
				int lp = getHitpoints();
				int hitDamage = hit.getDamage();
				if (hitDamage > 0 && lp - hitDamage < phaseHp) {
					if (lp > phaseHp) {
						hit.setDamage(lp - phaseHp);
					} else {
						hit.setDamage(0);
					}
				}
			}
		} else {
			hit.setDamage(0);
		}
	}

	@Override
	protected void onFinish(final Entity source) {
		drop(getMiddleLocation());
		reset();
		finish();
		if (instance != null) {
			instance.tryStartSpawnTimer();
		} else {
			NexModule.INSTANCE.tryStartSpawnTimer();
		}

		AtomicBoolean playerInfectedWithCough = new AtomicBoolean();
		for (Player player : getPlayersInPrison()) {
			if (player != null && NexPlayerExtKt.getNexChokesCount(player) > 0) {
				playerInfectedWithCough.set(true);
				break;
			}
		}

		forEachPlayerInPrison(player -> {
			sendNotifications(player);
			player.getBossTimer().finishTracking("Nex");

			player.getCombatAchievements().checkKcTask("nex", 1, CAType.NEX_VETERAN);
			player.getCombatAchievements().checkKcTask("nex", 25, CAType.NEX_MASTER);
			if (nexSurvivors) {
				player.getCombatAchievements().complete(CAType.NEX_SURVIVORS);
			}
			if (!hitByIceSpells) {
				player.getCombatAchievements().complete(CAType.CONTAIN_THIS);
			}
			if (!healedBySiphon) {
				player.getCombatAchievements().complete(CAType.A_SIPHON_WILL_SOLVE_THIS);
			}
			if (!hitByShadowsSmash) {
				player.getCombatAchievements().complete(CAType.SHADOWS_MOVE);
			}
			if (!hitBySmokeDash) {
				player.getCombatAchievements().complete(CAType.THERE_IS_NO_ESCAPE);
			}
			if (startPlayers <= 3) {
				player.getCombatAchievements().complete(CAType.NEX_TRIO);
			}
			if (startPlayers <= 2) {
				player.getCombatAchievements().complete(CAType.NEX_DUO);
			}
			if (playerInfectedWithCough.get()) {
				player.getCombatAchievements().complete(CAType.I_SHOULD_SEE_A_DOCTOR);
			}
		});
	}

	@Override
	public double getMagicPrayerMultiplier() {
		return 0.5;
	}

	@Override
	public double getRangedPrayerMultiplier() {
		return 0.5;
	}

	@Override
	public double getMeleePrayerMultiplier() {
		return 0.5;
	}

	public NexPhase getPhase() {
		return phase;
	}

	public void breakIcePrison() {
		if (icePrisonPulse != null) {
			for (Player player : playersInPrison) {
				player.unlock();
				player.sendMessage(Colour.RS_GREEN.wrap("You've been freed from the ice prison!"));
			}

			icePrisonPulse.stop();
			icePrisonPulse = null;
		}
	}

	private static final class NexHitBar extends EntityHitBar {
		NexHitBar(final Entity entity) {
			super(entity);
		}

		@Override
		public int getType() {
			return 22;
		}

		@Override
		public int getMultiplier() {
			return 160;
		}
	}

	public void playerDied() {
		nexSurvivors = false;
	}

}
