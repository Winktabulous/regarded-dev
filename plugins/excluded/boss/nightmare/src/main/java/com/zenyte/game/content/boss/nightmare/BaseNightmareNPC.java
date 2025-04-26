package com.zenyte.game.content.boss.nightmare;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.boss.nightmare.area.NightmareBossArea;
import com.zenyte.game.content.chambersofxeric.greatolm.OlmRoom;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.content.skills.prayer.PrayerManager;
import com.zenyte.game.content.xamphur.XamphurBoost;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.task.TickTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.util.ZoneBorders;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.*;
import com.zenyte.game.world.entity.masks.*;
import com.zenyte.game.world.entity.npc.CombatScriptsHandler;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NPCCombat;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.combatdefs.AggressionType;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.npc.combatdefs.ImmunityType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.entity.player.privilege.GameMode;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.utils.TimeUnit;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import kotlin.Pair;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static com.zenyte.game.content.boss.nightmare.NightmareDropExt.dropForPlayer;

public abstract class BaseNightmareNPC extends NPC implements CombatScript {

	public static final Location CENTER = new Location(3870, 9949, 3);
	public static final Location ARENA_START = new Location(3863, 9941, 3);
	public static final Location ARENA_END = new Location(3881, 9961, 3);

	private static final Projectile RANGED_PROJ = new Projectile(1766, 100, 30, 100, 15, 60, 128, 0);
	private static final Projectile MAGIC_PROJ = new Projectile(1764, 100, 30, 70, 15, 60, 128, 0);
	private static final Projectile BERRIES_PROJ = new Projectile(1783, 10, 10, 0, 15, 30, 0, 5);
	private static final Projectile HUSK_PROJ = new Projectile(1781, 110, 30, 65, 15, 50, 128, 0);
	private static final Projectile TOTEM_PROJ = new Projectile(1768, 150, 50, 10, 1, 100, 100, 0);
	private static final Projectile PARASITE_PROJ = new Projectile(1770, 100, 20, 65, 15, 50, 128, 0);

	private static final Graphics MAGE_END_GFX = new Graphics(1765, 0, 96);
	public static final Graphics SHADOW_ATK_GROUND_GFX = new Graphics(1767);
	private static final Graphics EXPLODE_GFX = new Graphics(1769);
	private static final Graphics PARASITE_SPAWN = new Graphics(1779);
	private static final Graphics PARASITE_SPAWN_WEAKEN = new Graphics(1780);
	public static final Graphics PLAYER_EXPLODE_GFX = new Graphics(1782);

	private static final Animation MELEE_ATK = new Animation(8594);
	private static final Animation MAGIC_ATK = new Animation(8595);
	private static final Animation RANGED_ATK = new Animation(8596);
	private static final Animation CHARGE_ANIM = new Animation(8597);
	public static final Animation SHADOW_ATK = new Animation(8598);
	private static final Animation CURSE_ATK = new Animation(8599);
	private static final Animation FLOWER_ATK = new Animation(8601);
	private static final Animation START_EXPLORE = new Animation(8604);
	private static final Animation HUSK_ATK = new Animation(8605);
	private static final Animation PARASITE_ATK = new Animation(8606);
	private static final Animation JUMP_IN = new Animation(8607);
	private static final Animation SINK_IN = new Animation(8608);
	private static final Animation JUMP_UP = new Animation(8609);
	private static final Animation JUMP_UP_EXPLODING = new Animation(8610);
	private static final Animation AWAKE_ANIM = new Animation(8611);
	private static final Animation SLEEP_ANIM = new Animation(8612);
	private static final Animation BLOSSOMS_SPAWN = new Animation(8617);
	private static final Animation BLOSSOMS_OPEN = new Animation(8619);
	private static final Animation BLOSSOMS_DESPAWN = new Animation(8621);
	private static final Animation BERRIES_SPAWN = new Animation(8623);
	private static final Animation BERRIES_OPEN = new Animation(8625);
	private static final Animation BERRIES_DESPAWN = new Animation(8627);
	private static final Animation SPORES_ANIM = new Animation(8630);
	private static final Animation SPORES_BURST = new Animation(8632);

	private static final SoundEffect MAGIC_SOUND = new SoundEffect(4286, 15, 10);
	private static final SoundEffect MELEE_SOUND1 = new SoundEffect(4245, 15, 10);
	private static final SoundEffect MELEE_SOUND2 = new SoundEffect(4282, 15, 37);
	private static final SoundEffect RANGED_SOUND1 = new SoundEffect(4246, 15, 10);
	private static final SoundEffect RANGED_SOUND2 = new SoundEffect(4251, 15, 37);
	private static final SoundEffect RANGED_SOUND3 = new SoundEffect(4221, 15, 51);
	private static final SoundEffect RANGED_SOUND4 = new SoundEffect(4326, 15, 72);
	private static final SoundEffect CURSE_SOUND = new SoundEffect(4331);
	private static final SoundEffect CURSE_END_SOUND = new SoundEffect(4260);

	private final List<NPC> spawns = new ArrayList<>();
	private int playersOnStart;
	private int baseShield;
	private int totemBaseCharge;
	private NightmareFlowerAttackData activeQuadrant;
	private boolean totemAttack;
	public int absorbed;
	private int specialAttackCounter;
	private long killStartTime;
	private int attackCounter;
	private NightmareTotem totemNE;
	private NightmareTotem totemNW;
	private NightmareTotem totemSE;
	private NightmareTotem totemSW;
	private int sleepwalkerCount;
	private boolean crushHour = true;
	private boolean prayerDisabled;
	private boolean dreamlandExpress = true;

	public BaseNightmareNPC(int id, Location location) {
		super(id, location, Direction.SOUTH, 0);
		this.spawned = true;
		this.hitBar = new EntityHitBar(this) {
			@Override
			public int getType() {
				return 13;
			}
		};
		this.maxDistance = 64;
		this.combat = new NPCCombat(this) {

			@Override
			public boolean isMelee() {
				return false;
			}

			@Override
			public int combatAttack() {
				if (target == null) {
					return 0;
				}
				final boolean melee = isMelee();
				if (npc.isProjectileClipped(target, melee)) {
					return 0;
				}
				int distance = melee || npc.isForceFollowClose() ? 0 : npc.getAttackDistance();
				if (outOfRange(target, distance, target.getSize(), melee)) {
					return 0;
				}
				addAttackedByDelay(target);
				return CombatScriptsHandler.specialAttack(npc, target);
			}

		};
	}

	public void initTotems(NightmareTotem totemNE, NightmareTotem totemNW, NightmareTotem totemSE, NightmareTotem totemSW) {
		this.totemNE = totemNE;
		this.totemNW = totemNW;
		this.totemSE = totemSE;
		this.totemSW = totemSW;
	}

	public int chargeAttack() {
		disableAggression();
		NightmareChargeData chargeData = Utils.random(NightmareChargeData.values);
		Location npcStart = locationTransform(chargeData.getNpcStart());
		Location npcEnd = locationTransform(chargeData.getNpcEnd());

		lock();
		jump(npcStart);
		WorldTasksManager.schedule(() -> {
			setFaceLocation(npcEnd);
			WorldTasksManager.schedule(() -> {
				setAnimation(CHARGE_ANIM);
				setForceMovement(new ForceMovement(npcStart, 0, npcEnd, 60, OlmRoom.getMovementDirection(npcStart, npcEnd)));
				WorldTasksManager.schedule(() -> {
					setAnimation(null);
					setLocation(npcEnd);
					WorldTasksManager.schedule(() -> {
						enableAggression();
						unlock();
					});
				}, 1);

				ZoneBorders zoneBorders = new ZoneBorders(locationTransform(chargeData.getCheckStart()), locationTransform(chargeData.getCheckEnd()));
				forEachPlayers(player -> {
					if (!zoneBorders.insideBorder(player)) {
						return;
					}

					player.sendMessage(Colour.RED.wrap("The Nightmare surges across the room, damaging you as she does!"));
					player.applyHit(new Hit(this, Utils.random(60), HitType.REGULAR));
				});
			}, 1);
		}, 3);
		return 6;
	}

	public int sporesAttack(Runnable afterUnlock) {
		setAnimation(CURSE_ATK);
		lock();

		NightmareSpores nightmareSpores = Utils.random(NightmareSpores.values);

		Location base = getBase();

		List<WorldObject> sporesList = new ArrayList<>();

		for (int i = 0, length = nightmareSpores.getLocations().length; i < length; i++) {
			Location sporeLocation = nightmareSpores.getLocations()[i];
			WorldObject gameObject = new WorldObject(ObjectId.SPORE, 10, 0, base.transform(sporeLocation.getX(), sporeLocation.getY(), location.getPlane()));
			World.spawnObject(gameObject);
			sporesList.add(gameObject);
		}

		final int length = sporesList.size();
		TickTask pulse = new TickTask() {
			@Override
			public void run() {
				if (ticks == 1) {
					for (int i = 0; i < length; i++) {
						WorldObject sporesObject = sporesList.get(i);
						World.sendObjectAnimation(sporesObject, SPORES_ANIM);
					}

					forEachPlayers(player -> player.sendMessage("<col=E00A19>The Nightmare summons some infectious spores!"));
				} else if (ticks == 3) {
					unlock();
					if (afterUnlock != null) {
						afterUnlock.run();
					}
				} else if (ticks == 4) {
					List<WorldObject> sporesListNew = new ArrayList<>(length);

					for (int i = 0; i < length; i++) {
						WorldObject sporesObject = sporesList.get(i);
						WorldObject newSporesObject = sporesObject.transform(ObjectId.SPORE_37739);
						World.replaceObject(sporesObject, newSporesObject);

						sporesListNew.add(newSporesObject);
					}

					sporesList.clear();
					sporesList.addAll(sporesListNew);
				} else if (ticks > 4) {
					if (ticks == 5 + 24) {
						for (int i = 0; i < sporesList.size(); i++) {
							WorldObject sporesObject = sporesList.get(i);
							World.sendObjectAnimation(sporesObject, SPORES_BURST);

							forEachPlayers(player -> {
								int distance = sporesObject.getLocation().getTileDistance(player.getLocation());
								if (distance > 1) {
									return;
								}

								drowsyPlayer(player);
							});
						}
					} else if (ticks == 6 + 24 || totemAttack) {
						for (int i = 0; i < sporesList.size(); i++) {
							WorldObject sporesObject = sporesList.get(i);
							World.removeObject(sporesObject);
						}

						sporesList.clear();
						stop();
						return;
					} else {
						Iterator<WorldObject> iterator = sporesList.iterator();
						while (iterator.hasNext()) {
							WorldObject sporesObject = iterator.next();
							AtomicBoolean popped = new AtomicBoolean(false);

							forEachPlayers(player -> {
								int distance = sporesObject.getLocation().getTileDistance(player.getLocation());
								if (distance > 1) {
									return;
								}

								if (!popped.get()) {
									World.sendObjectAnimation(sporesObject, SPORES_BURST);
									WorldTasksManager.schedule(() -> World.removeObject(sporesObject));

									popped.set(true);
									iterator.remove();
								}

								drowsyPlayer(player);
							});
						}
					}
				}

				ticks++;
			}

		};

		WorldTasksManager.schedule(pulse, 0, 0);
		return getCombatDefinitions().getAttackSpeed();
	}

	public static void handleDrops(BaseNightmareNPC npc, int playersOnStart) {
		boolean phosanis = npc instanceof PhosanisNightmareNPC;
		boolean boostActive = World.hasBoost(XamphurBoost.NIGHTMARE);

		Map<Player, Integer> damageMap = new HashMap<>();
		for (Map.Entry<Pair<String, GameMode>, ObjectArrayList<ReceivedDamage>> entry : npc.getReceivedDamage().entrySet()) {
			Player player = World.getPlayer(entry.getKey().getFirst()).orElse(null);
			if (player == null || !phosanis && !player.inArea(NightmareBossArea.class)) {
				continue;
			}

			int damage = 0;
			for (ReceivedDamage pair : entry.getValue()) {
				damage += pair.getDamage();
			}

			damageMap.put(player, damage);
		}

		for (Map.Entry<Player, Integer> entry : damageMap.entrySet()) {
			dropForPlayer(entry.getKey(), npc, playersOnStart,  boostActive, phosanis);
		}
	}

	public static int rateForPet(int players) {
		switch (players) {
			case 1:
				return 399;
			case 2:
				return 799;
			case 3:
				return 1199;
			case 4:
				return 1599;
			default:
				return 1999;
		}
	}

	public static final int LITTE_NIGHTMARE_RATE_PHOSANIS = 999;


	private void drowsyPlayer(Player player) {
		if (player.getBooleanTemporaryAttribute("nightmare_drowsy")) {
			return;
		}

		player.sendMessage("<col=E00A19>The Nightmare's spores have infected you, making you feel drowsy.");
		player.setRun(false);
		player.putBooleanTemporaryAttribute("nightmare_drowsy", true);
		player.putBooleanTemporaryAttribute("nightmare_drowsy_phosanis", this instanceof PhosanisNightmareNPC);
		WorldTasksManager.schedule(new TickTask() {
			@Override
			public void run() {
				if (player.isDead() || player.isNulled() || player.isFinished()) {
					player.getTemporaryAttributes().remove("nightmare_drowsy");
					player.getTemporaryAttributes().remove("nightmare_drowsy_phosanis");
					stop();
					return;
				}

				ticks++;
				switch (ticks) {
					case 2:
					case 7:
					case 13:
					case 19:
					case 25:
						player.setForceTalk("*yawn*");
						if (player.getBooleanTemporaryAttribute("nightmare_drowsy_phosanis")) {
							player.getPrayerManager().drainPrayerPoints(6);
						}
						break;
				}
				if (ticks >= 25) {
					player.getTemporaryAttributes().remove("nightmare_drowsy");
					player.getTemporaryAttributes().remove("nightmare_drowsy_phosanis");
					player.sendMessage("<col=229628>The Nightmare's infection has wore off.");
					stop();
				}
			}
		}, 0, 0);
		//TODO make it contagious
	}

	public int curseAttack() {
		lock(4);
		setAnimation(CURSE_ATK);

		forEachPlayers(player -> player.getPacketDispatcher().sendClientScript(10673));
		WorldTasksManager.schedule(() -> forEachPlayers(player -> curseAttackPlayer(player)), 1);

		return getCombatDefinitions().getAttackSpeed();
	}

	public static void curseAttackPlayer(Player player) {
		player.sendMessage("<col=a53fff>The Nightmare has cursed you, shuffling your prayers!");
		player.putBooleanTemporaryAttribute("nightmare_curse", true);
		player.getPacketDispatcher().sendSoundEffect(CURSE_SOUND);

		WorldTasksManager.schedule(() -> {
			player.getPacketDispatcher().sendSoundEffect(CURSE_END_SOUND);
			player.getTemporaryAttributes().remove("nightmare_curse");
			player.sendMessage("<col=229628>You feel the effects of the Nightmare's curse wear off.");
			for (Prayer prayerType : protectionPrayers) {
				if (player.getPrayerManager().isActive(prayerType)) {
					player.getPrayerManager().activatePrayer(PrayerManager.cursePrayerTypeReverse(prayerType));
					break;
				}
			}
		}, 34);

		for (Prayer prayerType : protectionPrayers) {
			if (player.getPrayerManager().isActive(prayerType)) {
				player.getPrayerManager().activatePrayer(cursePrayerType(prayerType));
				break;
			}
		}
	}

	public static final Prayer[] protectionPrayers = { Prayer.PROTECT_FROM_MAGIC, Prayer.PROTECT_FROM_MISSILES, Prayer.PROTECT_FROM_MELEE };

	public static Prayer cursePrayerType(Prayer input) {
		switch (input) {
			case PROTECT_FROM_MAGIC:
				return Prayer.PROTECT_FROM_MISSILES;
			case PROTECT_FROM_MISSILES:
				return Prayer.PROTECT_FROM_MELEE;
			case PROTECT_FROM_MELEE:
				return Prayer.PROTECT_FROM_MAGIC;
			default:
				return input;
		}
	}

	public int parasiteAttack() {
		setAnimation(PARASITE_ATK);
		lock(4);

		List<Player> players = new ArrayList<>(getAreaPlayers());
		Collections.shuffle(players);

		int length = Math.max(1, Utils.randomNoPlus(players.size()));
		for (int i = 0; i < length; i++) {
			Player player = players.get(i);
			int ticks = World.sendProjectile(this, player, PARASITE_PROJ);

			WorldTasksManager.schedule(() -> {
				player.sendMessage("<col=ff289d>The Nightmare has impregnated you with a deadly parasite!");
				player.performDefenceAnimation(this);
				player.getVarManager().sendBit(10151, 1);
				WorldTasksManager.schedule(() -> {
					if (!getAreaPlayers().contains(player) || isFinished()) {
						return;
					}

					boolean weakened = player.getVarManager().getBitValue(10151) == 0;
					player.getVarManager().sendBit(10151, 0);
					Graphics graphics;
					int id;

					if (weakened) {
						graphics = PARASITE_SPAWN_WEAKEN;
						id = weakenParasiteId();
						player.applyHit(new Hit(this, Utils.random(5, 6), HitType.REGULAR));
					} else {
						graphics = PARASITE_SPAWN;
						id = parasiteId();
						player.applyHit(new Hit(this, Utils.random(50, 60), HitType.REGULAR));
						player.sendMessage("<col=E00A19>The parasite bursts out of you, fully grown!");
					}

					NightmareParasiteNPC parasite = new NightmareParasiteNPC(id, getHuskLocation(player.getLocation())[0], player, this, totemNE, totemNW, totemSE, totemSW);
					parasite.spawn();

					player.setGraphics(graphics);
					spawns.add(parasite);
				}, 19);
			}, ticks);
		}

		return getCombatDefinitions().getAttackSpeed();
	}

	public int flowerPowerAttack(Runnable afterUnlock) {
		jumpCenter();
		lock();

		WorldTasksManager.schedule(() -> {
			setAnimation(FLOWER_ATK);

			activeQuadrant = Utils.random(NightmareFlowerAttackData.values);
			ZoneBorders origArea = activeQuadrant.getSafeArea();
			ZoneBorders safeArea = new ZoneBorders(locationTransform(new Location(origArea.getSouthWestX(), origArea.getSouthWestY())), locationTransform(new Location(origArea.getNorthEastX(), origArea.getNorthEastY())), origArea.getPlane());
			Location[] berries = activeQuadrant.getBerries();
			Location[] blossoms = activeQuadrant.getBlossoms();
			int berriesLength = berries.length;
			int blossomsLength = blossoms.length;
			List<WorldObject> berriesList = new ArrayList<>(berriesLength);
			List<WorldObject> blossomsList = new ArrayList<>(blossomsLength);

			TickTask tickTask = new TickTask() {
				@Override
				public void run() {
					if (ticks == 0) {
						for (int i = 0; i < berriesLength; i++) {
							WorldObject berriesObject = new WorldObject(ObjectId.NIGHTMARE_BERRIES, 10, 0, locationTransform(berries[i]));
							World.spawnObject(berriesObject);
							berriesList.add(berriesObject);
						}

						for (int i = 0; i < blossomsLength; i++) {
							WorldObject blossomsObject = new WorldObject(ObjectId.NIGHTMARE_BLOSSOM, 10, 0, locationTransform(blossoms[i]));
							World.spawnObject(blossomsObject);
							blossomsList.add(blossomsObject);
						}
					} else if (ticks == 1) {
						for (int i = 0; i < berriesLength; i++) {
							WorldObject berriesObject = berriesList.get(i);
							World.sendObjectAnimation(berriesObject, BERRIES_SPAWN);
						}

						for (int i = 0; i < blossomsLength; i++) {
							WorldObject blossomsObject = blossomsList.get(i);
							World.sendObjectAnimation(blossomsObject, BLOSSOMS_SPAWN);
						}
					} else if (ticks == 2) {
						List<WorldObject> berriesListNew = new ArrayList<>(berriesLength);
						List<WorldObject> blossomsListNew = new ArrayList<>(blossomsLength);

						for (int i = 0; i < berriesLength; i++) {
							WorldObject berriesObject = berriesList.get(i);
							WorldObject newBerriesObject = berriesObject.transform(ObjectId.NIGHTMARE_BERRIES_37741);
							World.replaceObject(berriesObject, newBerriesObject);

							berriesListNew.add(newBerriesObject);
						}

						for (int i = 0; i < blossomsLength; i++) {
							WorldObject blossomsObject = blossomsList.get(i);
							WorldObject newBlossomsObject = blossomsObject.transform(ObjectId.NIGHTMARE_BLOSSOM_37744);
							World.replaceObject(blossomsObject, newBlossomsObject);

							blossomsListNew.add(newBlossomsObject);
						}

						forEachPlayers(player -> player.sendMessage("<col=E00A19>The Nightmare splits the area into segments!"));

						berriesList.clear();
						berriesList.addAll(berriesListNew);
						blossomsList.clear();
						blossomsList.addAll(blossomsListNew);
					} else if (ticks == 7) {
						unlock();
						if (afterUnlock != null) {
							afterUnlock.run();
						}

						for (int i = 0; i < berriesLength; i++) {
							WorldObject berriesObject = berriesList.get(i);
							World.sendObjectAnimation(berriesObject, BERRIES_OPEN);
						}

						for (int i = 0; i < blossomsLength; i++) {
							WorldObject blossomsObject = blossomsList.get(i);
							World.sendObjectAnimation(blossomsObject, BLOSSOMS_OPEN);
						}
					} else if (ticks == 8) {
						List<WorldObject> berriesListNew = new ArrayList<>(berriesLength);
						List<WorldObject> blossomsListNew = new ArrayList<>(blossomsLength);

						for (int i = 0; i < berriesLength; i++) {
							WorldObject berriesObject = berriesList.get(i);
							WorldObject newBerriesObject = berriesObject.transform(ObjectId.NIGHTMARE_BERRIES_37742);
							World.replaceObject(berriesObject, newBerriesObject);

							berriesListNew.add(newBerriesObject);
						}

						for (int i = 0; i < blossomsLength; i++) {
							WorldObject blossomsObject = blossomsList.get(i);
							WorldObject newBlossomsObject = blossomsObject.transform(ObjectId.NIGHTMARE_BLOSSOM_37745);
							World.replaceObject(blossomsObject, newBlossomsObject);

							blossomsListNew.add(newBlossomsObject);
						}

						berriesList.clear();
						berriesList.addAll(berriesListNew);
						blossomsList.clear();
						blossomsList.addAll(blossomsListNew);
					} else if (ticks > 8) {
						if (ticks == 8 + 22) {
							for (int i = 0; i < berriesLength; i++) {
								WorldObject berriesObject = berriesList.get(i);
								World.sendObjectAnimation(berriesObject, BERRIES_DESPAWN);
							}

							for (int i = 0; i < blossomsLength; i++) {
								WorldObject blossomsObject = blossomsList.get(i);
								World.sendObjectAnimation(blossomsObject, BLOSSOMS_DESPAWN);
							}
						} else if (ticks == 9 + 22 || totemAttack) {
							for (int i = 0; i < berriesLength; i++) {
								WorldObject berriesObject = berriesList.get(i);
								World.removeObject(berriesObject);
							}

							for (int i = 0; i < blossomsLength; i++) {
								WorldObject blossomsObject = blossomsList.get(i);
								World.removeObject(blossomsObject);
							}

							berriesList.clear();
							blossomsList.clear();
							activeQuadrant = null;
							stop();
							return;
						} else {
							forEachPlayers(player -> {
								if (safeArea == null || safeArea.insideBorder(player)) return;

								World.sendProjectile(Utils.random(berries), player, BERRIES_PROJ);
								Hit hit = new Hit(Utils.random(4, 6), HitType.REGULAR);
								player.applyHit(hit);
								if (isShieldNpc() && getHitpoints() < getMaxHitpoints()) {
									applyHit(new Hit(hit.getDamage(), HitType.HEALED));
								}
							});
						}
					}

					ticks++;
				}
			};

			WorldTasksManager.schedule(tickTask, 0, 0);
		}, 4);
		return 8;
	}

	public void jump(Location location) {
		setAnimation(JUMP_IN);
		WorldTasksManager.schedule(() -> {
			setLocation(location);
			setAnimation(JUMP_UP);
		}, 1);
	}

	public void jumpCenter() {
		jump(locationTransform(CENTER));
	}

	public int husksAttack() {
		setAnimation(HUSK_ATK);
		lock(4);

		var players = new ArrayList<>(getAreaPlayers());
		Collections.shuffle(players);

		int playersSize = players.size();
		int husksMax = Utils.interpolate(1, 15, 1, 80, Math.min(playersSize, 80));
		int husksAmount = Utils.random(1, Math.min(husksMax, playersSize));

		for (int i = 0; i < husksAmount; i++) {
			var player = players.get(i);
			if (player.isFrozen()) continue;

			int delay = World.sendProjectile(this, player, HUSK_PROJ);
			WorldTasksManager.schedule(() -> {
				// if the player is dead or dying, then don't proceed
				if (player.isDying() || player.isDead() || player.isFinished()) return;
				// if for some reason they are past the dead or dying, and spawned at home
				// we want to make sure if the target is not in the area, don't proceed
				if (!getAreaPlayers().contains(player)) return;

				var huskLocations = getHuskLocation(player.getLocation());
				var magicHusk = new NightmareHuskMagicNPC(getMageHuskId(), huskLocations[0], player, this);
					magicHusk.spawn();

				var rangedHusk = new NightmareHuskRangedNPC(getRangedHuskId(), huskLocations[1], player, this);
					rangedHusk.spawn();

				magicHusk.oppositeHusk = rangedHusk;
				rangedHusk.oppositeHusk = magicHusk;

				player.freeze(100_000);
				player.sendMessage("<col=ff289d>The Nightmare puts you in a strange trance, preventing you from moving!");
				spawns.add(magicHusk);
				spawns.add(rangedHusk);
			}, delay);
		}

		return 6;
	}

	private Location[] getHuskLocation(Location base) {
		Location[] locations = new Location[2];
		int pos = 0;

		for (Direction direction : Direction.cardinalDirections) {
			Location spawn = base.transform(direction);
			if (World.isFloorFree(spawn)) {
				locations[pos++] = spawn;
				if (pos == 2) {
					return locations;
				}
			}
		}

		for (Direction direction : Direction.intercardinalDirections) {
			Location spawn = base.transform(direction);
			if (World.isFloorFree(spawn)) {
				locations[pos++] = spawn;
				if (pos == 2) {
					return locations;
				}
			}
		}

		return locations;
	}

	public int graspingClawAttack() {
		setAnimation(SHADOW_ATK);
		lock(7);

		Location arenaStart = locationTransform(ARENA_START);
		Location arenaEnd = locationTransform(ARENA_END);

		int x = arenaEnd.getX() - arenaStart.getX();
		int y = arenaEnd.getY() - arenaStart.getY();
		IntSet coords = new IntOpenHashSet();

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
			if (distance < 3) {
				player.applyHit(new Hit(this, Utils.random(50), HitType.REGULAR));
			} else {
				int offX = player.getLocation().getX() - arenaStart.getX();
				int offY = player.getLocation().getY() - arenaStart.getY();
				int bitPack = offX << 16 | offY;
				if (coords.contains(bitPack)) {
					player.applyHit(new Hit(this, Utils.random(50), HitType.REGULAR));
				}
			}
		}), 3);
		return 10;
	}

	public void mageRangeAttack(AttackType type) {
		Projectile projectile;
		if (type == AttackType.MAGIC) {
			projectile = MAGIC_PROJ;
			setAnimation(MAGIC_ATK);
			World.sendSoundEffect(getLocation(), MAGIC_SOUND);
		} else {
			projectile = RANGED_PROJ;
			setAnimation(RANGED_ATK);
			World.sendSoundEffect(getLocation(), RANGED_SOUND1);
			World.sendSoundEffect(getLocation(), RANGED_SOUND2);
			World.sendSoundEffect(getLocation(), RANGED_SOUND3);
			World.sendSoundEffect(getLocation(), RANGED_SOUND4);
		}

		lock(3);
		forEachPlayers(player -> {
			int delay = World.sendProjectile(this, player, projectile);
			WorldTasksManager.schedule(() -> hitPlayer(player, type), delay);
		});
	}

	public void meleeAttack(Entity victim) {
		setAnimation(MELEE_ATK);
		Location hitLocation = victim.getLocation().copy();
		lock(2);
		World.sendSoundEffect(getLocation(), MELEE_SOUND1);
		World.sendSoundEffect(getLocation(), MELEE_SOUND2);
		WorldTasksManager.schedule(() -> forEachPlayers(player -> {
			Location location = player.getLocation();
			if (!location.withinDistance(hitLocation, 1)) {
				return;
			}

			hitPlayer(player, AttackType.MELEE);
		}), 2);
	}

	private void hitPlayer(Player player, AttackType style) {
		int hitInt;
		if (style == AttackType.MELEE) {
			hitInt = Utils.random(meleeMaxHit());
		} else {
			hitInt = getRandomMaxHit(this, rangedMageMaxHit(), style, player);
		}

		if (hitInt > 0) {
			PrayerManager prayerManager = player.getPrayerManager();
			boolean protMelee = prayerManager.isActive(Prayer.PROTECT_FROM_MELEE);
			boolean protRanged = prayerManager.isActive(Prayer.PROTECT_FROM_MISSILES);
			boolean protMagic = prayerManager.isActive(Prayer.PROTECT_FROM_MAGIC);
			if (style == AttackType.MELEE && protMelee || style == AttackType.RANGED && protRanged || style == AttackType.MAGIC && protMagic) {
				hitInt *= protectionPrayerMod();
			} else if (protMelee || protRanged || protMagic) {
				hitInt *= 1.2;
			}
		}

		Hit hit = new Hit(this, hitInt, style.getHitType());
		if (style.isMagic()) {
			hit.onLand(hit1 -> player.setGraphics(MAGE_END_GFX));
		}
		player.scheduleHit(this, hit, -1);
	}

	public abstract void init(int playersOnStart);

	public void awaken() {
		setAnimation(AWAKE_ANIM);
		playersOnStart = Math.min(getAreaPlayers().size(), 80);
		init(playersOnStart);
		getReceivedDamage().clear();
		lock();

		WorldTasksManager.schedule(() -> {
			killStartTime = System.currentTimeMillis();
			forEachPlayers(player -> player.getHpHud().open(getAwakeShieldP1Id(), getHitpoints()));
			setTransformation(getAwakeShieldP1Id());
			WorldTasksManager.schedule(() -> {
				unlock();
				combat.setCombatDelay(4);
			}, 3);
		}, 3);
	}

	@Override
	public void setTransformation(int id) {
		super.setTransformation(id);

		NightmarePhase phase = getPhase();
		if (isShieldNpc()) {
			setHitpoints(baseShield);
			enableAggression();
			hitBar = new EntityHitBar(this) {
				@Override
				public int getType() {
					return 13;
				}
			};
			getHitBars().add(removeHpBar);
			forEachPlayers(player -> {
				player.getPacketDispatcher().sendClientScript(45, 303 << 16 | 12, 9760);
				player.getPacketDispatcher().sendClientScript(45, 303 << 16 | 13, 31846);
				player.getPacketDispatcher().sendClientScript(45, 303 << 16 | 14, 53932);
				player.getHpHud().updateValue(hitpoints);
				player.getHpHud().updateMaxValue(getMaxHitpoints());
				if (phase.getNumber() != NightmarePhase.NightmarePhaseNumber.FIRST) {
					player.sendMessage("<col=E00A19>The Nightmare restores her shield.");
				}
			});
		} else {
			if (phase != null) {
				setHitpoints(phase.getHp());
				enableAggression();
			}
			hitBar = new EntityHitBar(this) {
				@Override
				public int getType() {
					return 21;
				}
			};
			getHitBars().add(removeShieldBar);
			forEachPlayers(player -> {
				player.getPacketDispatcher().sendClientScript(45, 303 << 16 | 12, 13369344);
				player.getPacketDispatcher().sendClientScript(45, 303 << 16 | 13, 38144);
				player.getPacketDispatcher().sendClientScript(45, 303 << 16 | 14, 52224);
				player.getHpHud().updateValue(hitpoints);
				player.getHpHud().updateMaxValue(getMaxHitpoints());
			});
		}

		if (phase != null) {
			lobbyNpcRun(npc -> npc.setTransformation(phase.getLobbyId()));
		}
	}

	@Override
	public void sendDeath() {
		NightmarePhase phase = getPhase();
		if (phase == null) {
			return;
		}

		int nextId = phase.getNextAwakeId();
		if (getId() == nextId) {
			return;
		}

		enableTotems();
		setTransformation(nextId);
		forEachPlayers(player -> GameInterface.NIGHTMARE_TOTEMS.open(player));
	}

	@Override
	public int getMaxHitpoints() {
		if (isShieldNpc()) {
			return baseShield;
		} else {
			return baseMaxHitpoints();
		}
	}

	public abstract int baseMaxHitpoints();

	public void enableAggression() {
		getCombatDefinitions().setAggressionType(null);
		setForceAggressive(true);
	}

	public void disableAggression() {
		getCombatDefinitions().setAggressionType(AggressionType.ALWAYS_AGGRESSIVE);
		setForceAggressive(false);
		getCombat().reset();
		setFaceEntity(null);
	}

	@Override
	public boolean triggersAutoRetaliate() {
		return isForceAggressive() && isAttackable();
	}

	@Override
	public float getXpModifier(Hit hit) {
		final Entity source = hit.getSource();
		if (isAwakeNpc() && source != this) {
			hit.setDamage(0);
			if (source instanceof Player player) {
				player.sendMessage("Your attacks have no effect on the Nightmare.");
			}

			return 0;
		}

		return super.getXpModifier(hit);
	}

	@Override
	public void applyHit(Hit hit) {
		if (hit.getDamage() > 0 && hit.getSource() instanceof Player player) {
			if (activeQuadrant != null && !activeQuadrant.getSafeArea().insideBorder(player)) {
				hit.setHitType(HitType.HEALED);
			}
			if (isShieldNpc()) {
				hit.setHitType(HitType.SHIELD);
			}
		}

		super.applyHit(hit);
	}

	@Override
	public boolean isTolerable() {
		return false;
	}

	@Override
	public void finish() {
		super.finish();

		clearSpawns();
	}

	public void clearSpawns() {
		int length = spawns.size();
		if (length == 0) {
			return;
		}

		for (int i = 0; i < length; i++) {
			NPC npc = spawns.get(i);
			npc.finish();
		}

		spawns.clear();
	}


	public void enableTotems() {
		configureTotem(totemNE, totemBaseCharge, NightmareTotemNE.CHARGE);
		configureTotem(totemNW, totemBaseCharge, NightmareTotemNW.CHARGE);
		configureTotem(totemSE, totemBaseCharge, NightmareTotemSE.CHARGE);
		configureTotem(totemSW, totemBaseCharge, NightmareTotemSW.CHARGE);
	}

	public void disableTotems() {
		configureTotem(totemNE, 0, NightmareTotemNE.REGULAR);
		configureTotem(totemNW, 0, NightmareTotemNW.REGULAR);
		configureTotem(totemSE, 0, NightmareTotemSE.REGULAR);
		configureTotem(totemSW, 0, NightmareTotemSW.REGULAR);
	}

	private void configureTotem(NightmareTotem totem, int hp, int newId) {
		totem.setMaxCharge(Math.max(1, hp));
		totem.setHitpoints(hp);
		totem.setTransformation(newId);
	}

	public void updateTotemHud() {
		int swMax = totemSW.getMaxHitpoints();
		int swLp = swMax - totemSW.getHitpoints();
		int seMax = totemSE.getMaxHitpoints();
		int seLp = seMax - totemSE.getHitpoints();
		int nwMax = totemNW.getMaxHitpoints();
		int nwLp = nwMax - totemNW.getHitpoints();
		int neMax = totemNE.getMaxHitpoints();
		int neLp = neMax - totemNE.getHitpoints();

		Object[] arguments = {413 << 16 | 20, 413 << 16 | 21, swMax, swLp, 413 << 16 | 26, 413 << 16 | 27, seMax, seLp, 413 << 16 | 8, 413 << 16 | 9, nwMax, nwLp, 413 << 16 | 14, 413 << 16 | 15, neMax, neLp, 0};
		forEachPlayers(player -> player.getPacketDispatcher().sendClientScript(3315, arguments));
	}

	public void totemAttack() {
		if (totemAttack || getAreaPlayers().isEmpty()) {
			return;
		}

		if (totemSW.getHitpoints() > 0 || totemSE.getHitpoints() > 0 || totemNW.getHitpoints() > 0 || totemNE.getHitpoints() > 0) {
			return;
		}

		forEachPlayers(player -> player.sendMessage("<col=E00A19>All four totems are fully charged."));

		totemAttack = true;
		totemProjectile(totemSW);
		totemProjectile(totemSE);
		totemProjectile(totemNW);
		totemProjectile(totemNE);

		WorldTasksManager.schedule(() -> {
			disableAggression();
			lock();
			setAnimation(Animation.STOP);

			WorldTasksManager.schedule(() -> {
				NightmarePhase phase = getPhase();
				setGraphics(EXPLODE_GFX);
				applyHit(new Hit(this, phase.getTotemDmg(), HitType.REGULAR));

				boolean dead = phase.getNextAwakeShieldId() == -1;
				if (dead) {
					death(true);
				} else {
					explodeAttack(phase);
					disableTotems();
				}

				forEachPlayers(player -> {
					player.getInterfaceHandler().closeInterface(InterfacePosition.NIGHTMARE_TOTEMS_POS);
					if (dead) {
						player.getHpHud().close();
					}
				});
			}, 1);
		}, 1);
	}

	private void explodeAttack(NightmarePhase phase) {
		absorbed = 0;
		WorldTasksManager.schedule(() -> {
			setAnimation(JUMP_IN);
			WorldTasksManager.schedule(() -> {
				setTransformation(getExplodingId());
				setLocation(locationTransform(CENTER));
				setAnimation(JUMP_UP_EXPLODING);
				disableAggression();

				WorldTasksManager.schedule(() -> {
					forEachPlayers(player -> player.sendMessage("<col=E00A19>The Nightmare begins to charge up a devastating attack."));

					for (int i = 0; i < sleepwalkerCount; i++) {
						spawnSleepwalker(phase, locationTransform(getSleepwalkerLocation(i)));
					}

					WorldTasksManager.schedule(() -> {
						int damage = getSleepwalkerDamage();
						setAnimation(START_EXPLORE);
						WorldTasksManager.schedule(() -> {
							forEachPlayers(player -> {
								player.setGraphics(PLAYER_EXPLODE_GFX);
								player.scheduleHit(this, new Hit(this, Math.min(player.getHitpoints(), damage), HitType.REGULAR), 0);
							});
							WorldTasksManager.schedule(() -> {
								totemAttack = false;
								setTransformation(phase.getNextAwakeShieldId());
								WorldTasksManager.schedule(() -> unlock());
							}, 5);
						}, 2);
					}, 10);
				}, 2);
			}, 1);
		}, 2);
	}

	public void totemProjectile(NPC npc) {
		World.sendProjectile(npc, this, TOTEM_PROJ);
	}

	public void death(boolean death) {
		totemAttack = false;
		disableTotems();
		clearSpawns();
		setTransformation(getSleepingId());
		setAnimation(SLEEP_ANIM);
		if (!death) {
			return;
		}

		disableAggression();
		WorldTasksManager.schedule(() -> {
			lobbyNpcRun(NightmareLobbyNPC::stall);

			long killTime = System.currentTimeMillis() - killStartTime;
			updateKillStat(killTime);

			final String name = getTrackingName();
			forEachPlayers(player -> {
				player.getVarManager().sendBit(10239, 1);
				player.getNotificationSettings().increaseKill(name);
				player.getNotificationSettings().sendBossKillCountNotification(name);
				player.getBossTimer().inform(name, killTime);
				player.getSlayer().checkAssignment(this);

				if (name.equalsIgnoreCase("the nightmare")) {
					player.getCombatAchievements().checkKcTask(name, 1, CAType.NIGHTMARE_ADEPT);
					player.getCombatAchievements().checkKcTask(name, 25, CAType.NIGHTMARE_VETERAN);
					player.getCombatAchievements().checkKcTask(name, 50, CAType.NIGHTMARE_MASTER);

					if (playersOnStart == 1) {
						player.getCombatAchievements().complete(CAType.SLEEP_TIGHT);
						long minutes = TimeUnit.MILLISECONDS.toMinutes(killTime);
						if (minutes < 27) {//Originally 23, we making it 27
							player.getCombatAchievements().complete(CAType.NIGHTMARE__SOLO__SPEED_TRIALIST);
						}
						if (minutes < 22) {//Originally 19, we making it 22
							player.getCombatAchievements().complete(CAType.NIGHTMARE__SOLO__SPEED_CHASER);
						}
						if (minutes < 19) {//Originally 16, we making it 19
							player.getCombatAchievements().complete(CAType.NIGHTMARE__SOLO__SPEED_RUNNER);
						}
					} else if (playersOnStart == 5) {
						if (TimeUnit.MILLISECONDS.toMinutes(killTime) < 5) {//Originally 5, we making it 7
							player.getCombatAchievements().complete(CAType.NIGHTMARE__5_SCALE__SPEED_TRIALIST);
						}

						if (TimeUnit.MILLISECONDS.toSeconds(killTime) < 330) {//Originally 4, we making it 5:30
							player.getCombatAchievements().complete(CAType.NIGHTMARE__5_SCALE__SPEED_CHASER);
						}
					}
				} else {
					player.getCombatAchievements().checkKcTask(name, 1, CAType.PHOSANIS_VETERAN);
					player.getCombatAchievements().checkKcTask(name, 5, CAType.PHOSANIS_MASTER);
					player.getCombatAchievements().checkKcTask(name, 25, CAType.PHOSANIS_GRANDMASTER);

					if (crushHour) {
						player.getCombatAchievements().complete(CAType.CRUSH_HOUR);
					}

					if (TimeUnit.MILLISECONDS.toMinutes(killTime) < 10) {//Originally 9, we making it 10
						player.getCombatAchievements().complete(CAType.PHOSANIS_SPEEDCHASER);
					}

					if (!prayerDisabled) {
						player.getCombatAchievements().complete(CAType.I_WOULD_SIMPLY_REACT);
					}

					if (dreamlandExpress) {
						player.getCombatAchievements().complete(CAType.DREAMLAND_EXPRESS);
					}
				}
			});
			handleDrops(this, playersOnStart);
		}, 5);
	}

	private static final HitBar removeShieldBar = new HitBar() {
		@Override
		public int getType() {
			return 13;
		}

		@Override
		public int getPercentage() {
			return 0;
		}

		@Override
		public int interpolateTime() {
			return 32767;
		}

	};

	private static final HitBar removeHpBar = new HitBar() {
		@Override
		public int getType() {
			return 21;
		}

		@Override
		public int getPercentage() {
			return 0;
		}

		@Override
		public int interpolateTime() {
			return 32767;
		}

	};

	@Override
	public void processHit(Hit hit) {
		super.processHit(hit);

		forEachPlayers(player -> player.getHpHud().updateValue(hitpoints));
	}

	@Override
	public List<Entity> getPossibleTargets(EntityType type) {
		if (!possibleTargets.isEmpty()) {
			possibleTargets.clear();
		}

		Set<Player> initialPlayers = getAreaPlayers();
		if (initialPlayers.isEmpty()) {
			return possibleTargets;
		}

		//for (Player player : initialPlayers) {
		//	System.out.println(PlayerCombat.getTargetDefenceRoll(player, player, AttackType.CRUSH)+":"+player.getUsername());
		//}

		List<Player> players = new ArrayList<>(initialPlayers);
		players.sort(Comparator.comparingInt(o -> PlayerCombat.getTargetDefenceRoll(o, o, AttackType.CRUSH)));
		possibleTargets.add(players.get(players.size() - 1));
		//System.out.println(players.get(players.size() - 1).getUsername());

		return possibleTargets;
	}

	@Override
	public int getPossibleTargetsDefaultRadius() {
		return 64;
	}

	@Override
	public boolean isEntityClipped() {
		return false;
	}

	@Override
	public double getMagicPrayerMultiplier() {
		return 1.0;
	}

	@Override
	public double getRangedPrayerMultiplier() {
		return 1.0;
	}

	@Override
	public double getMeleePrayerMultiplier() {
		return 1.0;
	}

	@Override
	public boolean isFreezeable() {
		return false;
	}

	@Override
	public boolean isAttackable() {
		return id != getExplodingId() && id != getSleepingId();
	}

	@Override
	public boolean checkAggressivity() {
		if (!isAttackable()) {
			return true;
		}

		return super.checkAggressivity();
	}

	@Override
	public void setFaceEntity(Entity entity) {
		if (!isAttackable()) {
			entity = null;
		}

		super.setFaceEntity(entity);
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		super.handleIngoingHit(hit);
		hit.setExecuteIfLocked();
	}

	public abstract int getSleepingId();

	public abstract int getAwakeShieldP1Id();

	public abstract int getExplodingId();

	public abstract boolean isShieldNpc();

	public abstract boolean isAwakeNpc();

	public abstract NightmarePhase getPhase();

	public void forEachPlayers(Consumer<Player> consumer) {
		for (Player player : getAreaPlayers()) {
			if (player == null || player.isHidden()) {
				continue;
			}

			consumer.accept(player);
		}
	}

	public abstract Set<Player> getAreaPlayers();

	public void setBaseShield(int baseShield) {
		this.baseShield = baseShield;
	}

	public void setTotemBaseCharge(int totemBaseCharge) {
		this.totemBaseCharge = totemBaseCharge;
	}

	public void setSleepwalkerCount(int sleepwalkerCount) {
		this.sleepwalkerCount = sleepwalkerCount;
	}

	public int getSleepwalkerCount() {
		return sleepwalkerCount;
	}

	public abstract int meleeMaxHit();

	public abstract int rangedMageMaxHit();

	public abstract double protectionPrayerMod();

	public void increaseAttackCounter() {
		this.attackCounter++;
	}

	public void setAttackCounter(int attackCounter) {
		this.attackCounter = attackCounter;
	}

	public int getAttackCounter() {
		return attackCounter;
	}

	public int getSpecialAttackCounter() {
		return specialAttackCounter;
	}

	public void setSpecialAttackCounter(int specialAttackCounter) {
		this.specialAttackCounter = specialAttackCounter;
	}

	public abstract Location locationTransform(Location location);

	public abstract void lobbyNpcRun(Consumer<NightmareLobbyNPC> consumer);

	@Override
	public void performDefenceAnimation(Entity attacker) {
		/* empty */
	}

	public abstract Location getBase();

	public abstract int getMageHuskId();

	public abstract int getRangedHuskId();

	public abstract Location getSleepwalkerLocation(int index);

	public abstract int getSleepwalkerDamage();

	public abstract int getSleepwalkerId();

	public void increaseAbsorbed() {
		absorbed++;
	}

	@Override
	public boolean isCycleHealable() {
		return false;
	}

	public NightmareSleepwalker spawnSleepwalker(NightmarePhase phase, Location location) {
		NightmareSleepwalker sleepwalker = new NightmareSleepwalker(getSleepwalkerId(), location, this, phase);
		sleepwalker.spawn();
		spawns.add(sleepwalker);
		return sleepwalker;
	}

	public abstract String getTrackingName();

	public abstract void updateKillStat(long duration);

	public boolean isTotemAttack() {
		return totemAttack;
	}

	@Override
	protected void updateCombatDefinitions() {
		super.updateCombatDefinitions();
		combatDefinitions.setImmunityTypes(EnumSet.of(ImmunityType.VENOM, ImmunityType.POISON));
	}

	public abstract int parasiteId();

	public abstract int weakenParasiteId();

	public void setCrushHour(boolean crushHour) {
		this.crushHour = crushHour;
	}

	public void setPrayerDisabled(boolean prayerDisabled) {
		this.prayerDisabled = prayerDisabled;
	}

	public void setDreamlandExpress(boolean dreamlandExpress) {
		this.dreamlandExpress = dreamlandExpress;
	}

}