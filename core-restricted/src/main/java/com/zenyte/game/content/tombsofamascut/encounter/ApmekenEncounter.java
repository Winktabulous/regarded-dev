package com.zenyte.game.content.tombsofamascut.encounter;

import com.zenyte.game.content.tombsofamascut.npc.BaboonNPC;
import com.zenyte.game.content.tombsofamascut.npc.TOANPC;
import com.zenyte.game.content.tombsofamascut.raid.EncounterStage;
import com.zenyte.game.content.tombsofamascut.raid.EncounterType;
import com.zenyte.game.content.tombsofamascut.raid.TOARaidArea;
import com.zenyte.game.content.tombsofamascut.raid.TOARaidParty;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.packet.out.MapAnim;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.*;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Skills;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;

import java.util.*;

/**
 * @author Savions.
 */
public class ApmekenEncounter extends TOARaidArea implements CycleProcessPlugin {

	public static final String SIGHT_PLAYER_ATTRIBUTE = "apmeken_sight_player";
	private static final Location[] BABOON_SPAWN_LOCATIONS = {new Location(3800, 5288), new Location(3808, 5288), new Location(3816, 5288), new Location(3816, 5272), new Location(3808, 5272), new Location(3800, 5272)};
	public static final Location[] VENT_LOCATIONS = {new Location(3800, 5284), new Location(3816, 5284), new Location(3816, 5276), new Location(3800, 5276)};
	public static final Location[] ROOF_SKULL_LOCATIONS = {new Location(3804, 5288), new Location(3812, 5288), new Location(3812, 5272), new Location(3804, 5272)};
	private static final Location[] ROOF_LOCATIONS = {new Location(3803, 5289), new Location(3811, 5289), new Location(3811, 5269), new Location(3803, 5269)};
	private static final Location BARRIER_BASE_LOCATION = new Location(3820, 5279);
	private static final SoundEffect CYCLE_SOUND = new SoundEffect(6574);
	private static final SoundEffect JOB_COMPLETE_SOUND = new SoundEffect(2655);
	private static final SoundEffect ROOF_REPAIR_SOUND = new SoundEffect(938);
	private static final SoundEffect[] ROOF_COLLAPSE_SOUNDS = {new SoundEffect(6573), new SoundEffect(6583)};
	private static final SoundEffect VENT_SOUND = new SoundEffect(6569, 10, 5);
	private static final SoundEffect POUR_SOUND = new SoundEffect(2401);
	private static final SoundEffect CORRUPTION_SOUND = new SoundEffect(6570);
	private static final Graphics SIGHT_GRANTED_GFX = new Graphics(2132, 0, 180);
	private static final Graphics ROOF_COLLAPSE_GFX = new Graphics(2139);
	private static final Graphics SIGHT_LOST_GFX = new Graphics(2133, 0, 180);
	private static final Graphics VENTS_GFX = new Graphics(2138, 5, 0);
	private static final Animation ROOF_REPAIR_ANIMATION = new Animation(3676);
	private static final Animation POUR_ANIMATION = new Animation(2295);
	private static final Tinting CORRUPTION_FAILURE_TINTING = new Tinting(0, 6, 36, 112, 0, 50);
	private static final Tinting RESET_TINTING = new Tinting(-1, -1, -1, 0, 0, 0);
	private static final int SENSE_NORTHERN_SKULL_GFX_ID = 2134;
	private static final int SENSE_SOUTHERN_SKULL_GFX_ID = 2135;
	private static final int ROOF_DAMAGED_OBJECT_ID = 45495;
	private static final int ROOF_REGULAR_OBJECT_ID = 45494;
	public static final int VENOM_POOL_OBJECT_ID = 45493;
	public static final int BABOON_MELEE_ID = 11709;
	public static final int BABOON_RANGE_ID = 11710;
	public static final int BABOON_MAGE_ID = 11711;
	public static final int BABOON_STRONGER_MELEE_ID = 11712;
	public static final int BABOON_STRONGER_RANGE_ID = 11713;
	public static final int BABOON_STRONGER_MAGE_ID = 11714;
	public static final int BABOON_SHAMAN_ID = 11715;
	public static final int BABOON_VOLATILE_ID = 11716;
	public static final int BABOON_CURSED_ID = 11717;
	public static final int BABOON_THRALL_ID = 11718;
	private static final int[] BABOON_SPECIAL_IDS = {BABOON_SHAMAN_ID, BABOON_VOLATILE_ID, BABOON_CURSED_ID};
	private static final int[][] WAVE_BABOONS = {{BABOON_MELEE_ID, BABOON_MELEE_ID}, {BABOON_RANGE_ID, BABOON_MAGE_ID}, {BABOON_MELEE_ID, BABOON_MELEE_ID, BABOON_SHAMAN_ID}, {BABOON_MELEE_ID, BABOON_MELEE_ID, -1}, {BABOON_RANGE_ID, BABOON_RANGE_ID, BABOON_VOLATILE_ID}, {BABOON_MAGE_ID, BABOON_MAGE_ID, BABOON_CURSED_ID}, {BABOON_STRONGER_RANGE_ID, BABOON_STRONGER_RANGE_ID, -1, -1, -1}, {BABOON_STRONGER_MAGE_ID, BABOON_STRONGER_MAGE_ID, -1, -1, -1}, {BABOON_STRONGER_MELEE_ID, BABOON_STRONGER_MELEE_ID, -1, -1, -1}, {BABOON_STRONGER_MELEE_ID, BABOON_STRONGER_RANGE_ID, -1, -1, -1}, {BABOON_SHAMAN_ID, BABOON_SHAMAN_ID, BABOON_VOLATILE_ID, BABOON_VOLATILE_ID, BABOON_VOLATILE_ID}};
	private static final int[] EXTRA_COMBAT_BABOONS = {BABOON_MELEE_ID, BABOON_MAGE_ID, BABOON_MELEE_ID, BABOON_MAGE_ID, BABOON_MELEE_ID, BABOON_RANGE_ID, BABOON_STRONGER_MELEE_ID, BABOON_STRONGER_MAGE_ID, BABOON_STRONGER_RANGE_ID, BABOON_STRONGER_MAGE_ID, BABOON_CURSED_ID};
	private int wave;
	private int waveDelay = 6;
	private int criticalNpcCount;
	private List<TOANPC> spawnedBaboons = new ArrayList<>();
	private Player sightPlayer;
	private int cycleTicks = 2;
	private int cycleAmount;
	private int specialTicks = 50;
	private int specialIndex = Utils.random(2);
	private int requiredActions;
	private boolean usedSpecial;
	private boolean switchPlayer;
	private boolean[] actionsDone = new boolean[4];
	private List<WorldObject> venomPools = new ArrayList<>();
	private int roofTicks;

	public ApmekenEncounter(AllocatedArea allocatedArea, int copiedChunkX, int copiedChunkY, TOARaidParty party, EncounterType encounterType) {
		super(allocatedArea, copiedChunkX, copiedChunkY, party, encounterType);
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
	}

	@Override public void onRoomEnd() {
		for (Player p : getChallengePlayers()) {
			if (p.getToxins().isVenomed()) {
				p.getToxins().cureToxin(Toxins.ToxinType.VENOM);
			}
		}
		if (sightPlayer != null) {
			sightPlayer.getTemporaryAttributes().remove(SIGHT_PLAYER_ATTRIBUTE);
		}
		removeVenomPools();
		final Location barrierLoc = getLocation(BARRIER_BASE_LOCATION);
		for (int y = 0; y < 2; y++) {
			World.removeObject(new WorldObject(45135, 10, 1, barrierLoc.transform(0, y)));
		}
		for (Player p : players) {
			if (p != null) {
				p.getInventory().deleteItem(ItemId.NEUTRALISING_POTION, Integer.MAX_VALUE);
				p.getInventory().deleteItem(ItemId.HAMMER, Integer.MAX_VALUE);
			}
		}
	}

	@Override public void onRoomReset() {
		wave = 0;
		waveDelay = 5;
		criticalNpcCount = 0;
		spawnedBaboons.removeIf(baboon -> {
			if (!baboon.isDying() && !baboon.isFinished()) {
				baboon.finish();
			}
			return true;
		});
		sightPlayer = null;
		cycleTicks = 0;
		cycleAmount = 0;
		specialTicks = 0;
		switchPlayer = false;
		requiredActions = 0;
		Arrays.fill(actionsDone, false);
		removeVenomPools();
		roofTicks = 0;
		resetRoofs();
		if (sightPlayer != null) {
			sightPlayer.getTemporaryAttributes().remove(SIGHT_PLAYER_ATTRIBUTE);
		}
	}

	@Override public void process() {
		if (EncounterStage.STARTED.equals(stage)) {
			final Player[] players = getChallengePlayers();
			if (waveDelay > 0 && --waveDelay <= 0) {
				spawnWave();
			}
			if (cycleTicks > 0 && --cycleTicks == 0) {
				if (!usedSpecial && (sightPlayer == null || sightPlayer.isDying() || sightPlayer.isFinished() || !insideChallengeArea(sightPlayer)
					|| (players.length > 1 && switchPlayer))) {
					switchPlayerSight();
				} else {
					if (!usedSpecial) {
						for (Player p : players) {
							p.sendSound(CYCLE_SOUND);
							if (!p.getUsername().equals(sightPlayer.getUsername())) {
								p.sendMessage("<col=0000b2>You sense an issue somewhere in the room.");
							}
						}
						requiredActions = Math.min(4, players.length);
						Arrays.fill(actionsDone, false);
						if (specialIndex == 2 && players.length < 2) {
							specialIndex = Utils.random(1);
						}
						switch (specialIndex) {
							case 0 -> {
								sightPlayer.sendMessage("<col=6800bf>You sense some strange fumes coming from holes in the floor.");
								for (int i = 0; i < VENT_LOCATIONS.length; i++) {
									final Location location = getLocation(VENT_LOCATIONS[i]);
									sightPlayer.sendZoneUpdate(location.getX(), location.getY(), new MapAnim(location, new Graphics(i < 2 ? SENSE_NORTHERN_SKULL_GFX_ID  : SENSE_SOUTHERN_SKULL_GFX_ID, 0, 46)));
								}
							}
							case 1 -> {
								sightPlayer.sendMessage("<col=6800bf>You sense an issue with the roof supports.");
								for (int i = 0; i < ROOF_SKULL_LOCATIONS.length; i++) {
									final Location location = getLocation(ROOF_SKULL_LOCATIONS[i]);
									sightPlayer.sendZoneUpdate(location.getX(), location.getY(), new MapAnim(location, new Graphics(i < 2 ? SENSE_NORTHERN_SKULL_GFX_ID  : SENSE_SOUTHERN_SKULL_GFX_ID, 0, 96)));
								}
							}
							case 2 -> {
								// Temp disable this attack cuz it crashes players
//								sightPlayer.sendMessage("<col=a53fff>You sense Amascut's corruption beginning to take hold.");
//								final Tinting corruptionTinting = new Tinting(0, 6, 24, 112, 0, 600);
//								corruptionTinting.setViewers(sightPlayer);
//								final Graphics sightGraphics = new Graphics(2137, 0, 180);
//								sightGraphics.setViewers(sightPlayer);
//								for (Player p : players) {
//									if (!p.getUsername().equals(sightPlayer.getUsername())) {
//										p.getTemporaryAttributes().put("apmeken_corruption_effect", Boolean.TRUE);
//										p.setTinting(corruptionTinting);
//										p.setGraphics(sightGraphics);
//									}
//								}

							}
						}
						cycleTicks = 30;
						usedSpecial = true;
					} else {
						int completedActions = 0;
						for (int i = 0; i < actionsDone.length; i++) {
							if (actionsDone[i]) {
								completedActions++;
							}
						}
						final boolean completedSpecial = completedActions >= requiredActions;
						switch (specialIndex) {
							case 0 -> {
								if (!completedSpecial) {
									triggerVents(new int[] {0, 1, 2, 3}, players);
								} else {
									sendCompletion(players, "<col=0000b2>Apmeken's Sight guides you into neutralising some dangerous fumes.");
								}
							}
							case 1 -> {
								if (!completedSpecial) {
									triggerRoofsCollapse(new int[] {0, 1, 2, 3}, players);
								} else {
									sendCompletion(players, "<col=0000b2>Apmeken's Sight guides you into repairing the roof supports.");
								}
							}
							case 2 -> {
								boolean completed = true;
								for (Player p : players) {
									if (p.getTemporaryAttributes().containsKey("apmeken_corruption_effect")) {
										completed = false;
										break;
									}
								}
								if (!completed && players.length > 1) {
									for (Player p : players) {
										p.sendMessage("<col=ff3045>Your group is overwhelmed by Amascut's corruption!");
										p.setTinting(CORRUPTION_FAILURE_TINTING);
										p.sendSound(CORRUPTION_SOUND);
									}
								} else {
									sendCompletion(players, "<col=3366ff>Apmeken's Sight guides your group into overcoming Amascut's corruption.");
								}
							}
						}

						if (++cycleAmount == (players.length == 2 ? 3 : 2)) {
							switchPlayer = true;
							cycleTicks = 30;
						} else {
							specialTicks -= 1;
							specialTicks = Math.max(30, specialTicks);
							cycleTicks = specialTicks;
						}
						specialIndex = Utils.random(2);
						usedSpecial = false;
					}
				}
			}
			venomPools.forEach(pool -> {
				for (Player p : players) {
					if (p != null) {
						if (p.getLocation().equals(pool.getLocation())) {
							p.applyHit(new Hit(p, (int) (party.getDamageMultiplier() * 6) + Utils.random(1), HitType.VENOM));
							p.getToxins().applyToxin(Toxins.ToxinType.VENOM, 14);
						}
					}
				}
			});
			if (roofTicks > 0 && --roofTicks <= 0) {
				resetRoofs();
			}
		}
	}

	private void sendCompletion(Player[] players, String message) {
		for (Player p : players) {
			p.sendMessage(message);
			sightPlayer.sendSound(JOB_COMPLETE_SOUND);
		}
	}

	public void spawnWave() {
		if (wave >= 11) {
			completeRoom();
			return;
		}
		final List<Integer> indices = new ArrayList<>();
		for (int i = 0; i < BABOON_SPAWN_LOCATIONS.length; i++) {
			indices.add(i);
		}
		Collections.shuffle(indices);
		final boolean extraBaboon = getStartTeamSize() > 1;
		for (int index = 0; index < WAVE_BABOONS[wave].length + (extraBaboon ? 1 : 0); index++) {
			final int id = index == WAVE_BABOONS[wave].length ? EXTRA_COMBAT_BABOONS[wave] : WAVE_BABOONS[wave][index];
			final int locationIndex = indices.get(index);
			final BaboonNPC baboonNPC = new BaboonNPC(id == -1 ? BABOON_SPECIAL_IDS[Utils.random(BABOON_SPECIAL_IDS.length - 1)] : id,
					getLocation(BABOON_SPAWN_LOCATIONS[locationIndex]), locationIndex < 3 ? Direction.SOUTH : Direction.NORTH, this);
			spawnedBaboons.add(baboonNPC);
			World.spawnNPC(baboonNPC);
			criticalNpcCount++;
		}
		wave++;
	}

	public void addThrall(BaboonNPC npc) {
		spawnedBaboons.add(npc);
	}

	public void onDeath(BaboonNPC npc) {
		spawnedBaboons.remove(npc);
		if ((wave < 11 && npc.getId() != BABOON_THRALL_ID && --criticalNpcCount == getRequiredCriticalNpcCount())
				|| (wave >= 11 && spawnedBaboons.isEmpty())) {
			waveDelay = 4;
		}
	}

	private int getRequiredCriticalNpcCount() {
		if (wave <= 3) {
			return 0;
		} else if (wave <= 9) {
			return 1;
		} else {
			return 2;
		}
	}

	private void switchPlayerSight() {
		if (sightPlayer != null && !sightPlayer.isDying() && !sightPlayer.isFinished() && insideChallengeArea(sightPlayer)) {
			sightPlayer.sendSound(CYCLE_SOUND);
			sightPlayer.sendMessage("<col=3366ff>You no longer have Apmeken's Sight.");
			sightPlayer.getTemporaryAttributes().remove(SIGHT_PLAYER_ATTRIBUTE);
			sightPlayer.setGraphics(SIGHT_LOST_GFX);
		}
		final Player[] players = getChallengePlayers(p -> sightPlayer == null || !p.getUsername().equals(sightPlayer.getUsername()));
		if (players.length > 0) {
			sightPlayer = players[players.length == 1 ? 0 : Utils.random(players.length - 1)];
			sightPlayer.getTemporaryAttributes().put(SIGHT_PLAYER_ATTRIBUTE, Boolean.TRUE);
			sightPlayer.setGraphics(SIGHT_GRANTED_GFX);
		}
		getPlayers().stream().filter(Objects::nonNull).forEach(p -> p.getTOAManager().refreshHudStates());
		for (Player p : players) {
			p.sendSound(CYCLE_SOUND);
			p.sendMessage("<col=3366ff>" + (p.getUsername().equals(sightPlayer.getUsername()) ? "You have" : (sightPlayer.getName() + " has")) + " been granted Apmeken's Sight.");
		}
		cycleTicks = 30;
		switchPlayer = false;
	}

	private void removeVenomPools() {
		venomPools.removeIf(pool -> {
			World.removeObject(pool);
			return true;
		});
	}

	public void attemptPlacePool(Location location) {
		if (venomPools.stream().anyMatch(pool -> location.equals(pool.getLocation()))) {
			return;
		}
		final WorldObject venomPool = new WorldObject(VENOM_POOL_OBJECT_ID, 10, Utils.random(3), location);
		venomPools.add(venomPool);
		World.spawnTemporaryObject(venomPool, 29, () -> venomPools.remove(venomPool));
	}

	private void resetRoofs() {
		for (int i = 0; i < ROOF_LOCATIONS.length; i++) {
			World.spawnObject(new WorldObject(ROOF_REGULAR_OBJECT_ID, 10, 3, getLocation(ROOF_LOCATIONS[i])));
		}
	}

	private void triggerVents(int[] indices, Player... players) {
		for (Player p : players) {
			if (p != null) {
				p.sendSound(VENT_SOUND);
				p.sendMessage(indices.length == 1 ? "<col=ff3045>Some toxic fumes errupt out of the hole! There was clearly nothing to neutralise." : "<col=ff3045>The fumes filling the room suddenly ignite!");
				p.applyHit(new Hit((int) (party.getDamageMultiplier() * 22), HitType.DEFAULT));
			}
		}
		roofTicks = 4;
		for (int i : indices) {
			World.sendGraphics(VENTS_GFX, VENT_LOCATIONS[i]);
		}
	}

	private void triggerRoofsCollapse(int[] indices, Player... players) {
		for (Player p : players) {
			if (p != null) {
				p.setGraphics(ROOF_COLLAPSE_GFX);
				p.sendMessage(indices.length == 1 ? "<col=ff3045>Some debris falls on you! The roof support clearly didn't need repairing." : "<col=ff3045>Damaged roof supports cause some debris to fall on you!");
				p.applyHit(new Hit((int) (party.getDamageMultiplier() * 22), HitType.DEFAULT));
				for (SoundEffect sound : ROOF_COLLAPSE_SOUNDS) {
					p.sendSound(sound);
				}
			}
		}
		roofTicks = 4;
		for (int i : indices) {
			World.spawnObject(new WorldObject(ROOF_DAMAGED_OBJECT_ID, 10, 3, getLocation(ROOF_LOCATIONS[i])));
		}
	}

	public void repairRoof(Player player, WorldObject object) {
		int index = -1;
		for (int i = 0; i < ROOF_LOCATIONS.length; i++) {
			if (getLocation(ROOF_LOCATIONS[i]).equals(object.getLocation())) {
				index = i;
				break;
			}
		}
		if (index != -1) {
			if (!player.getInventory().containsItem(ItemId.HAMMER)) {
				player.sendMessage("You need a hammer to repair this roof support.");
			} else if (!usedSpecial || specialIndex != 1) {
				triggerRoofsCollapse(new int[] {index}, player);
			} else if (actionsDone[index]) {
				player.sendMessage("This roof support has already been repaired.");
			} else {
				player.sendMessage("<col=229628>You repair the damaged roof support.");
				//player.getSkills().addXp(Skills.CONSTRUCTION, 10);
				player.setAnimation(ROOF_REPAIR_ANIMATION);
				player.sendSound(ROOF_REPAIR_SOUND);
				actionsDone[index] = true;
			}
		}
	}

	public void pourPotion(Player player) {
		int index = -1;
		for (int i = 0; i < VENT_LOCATIONS.length; i++) {
			if (getLocation(VENT_LOCATIONS[i]).equals(player.getLocation())) {
				index = i;
				break;
			}
		}
		player.setAnimation(POUR_ANIMATION);
		player.sendSound(POUR_SOUND);
		player.getInventory().deleteItem(ItemId.NEUTRALISING_POTION, 1);
		if (index == -1) {
			boolean neutralizedPlayers = false;
			for (Player p : getChallengePlayers()) {
				if (!player.getUsername().equals(p.getUsername()) && player.getLocation().getTileDistance(p.getLocation()) < 1) {
					if (neutralizeCorruption(player, p)) {
						neutralizedPlayers = true;
					}
				}
			}
			if (!neutralizedPlayers) {
				player.sendMessage("You pour the potion on the floor, but nothing happens.");
			}
		} else if (!usedSpecial || specialIndex != 0) {
			triggerVents(new int[] {index}, player);
		} else if (actionsDone[index]) {
			player.sendMessage("This vent has already been neutralised.");
		} else {
			player.sendMessage("<col=06600c>You neutralise the fumes coming from the hole.");
			actionsDone[index] = true;
		}
	}

	public void usePotionOn(Player player, Player target) {
		player.setAnimation(POUR_ANIMATION);
		player.sendSound(POUR_SOUND);
		player.getInventory().deleteItem(ItemId.NEUTRALISING_POTION, 1);
		if (!usedSpecial || specialIndex != 2) {
			player.sendMessage("<col=ff3045>As there was nothing to neutralise, you hurt yourself in process.");
			player.applyHit(new Hit((int) (party.getDamageMultiplier() * 22), HitType.DEFAULT));
		} else if (!neutralizeCorruption(player, target)) {
			player.sendMessage("<col=229628>" + target.getUsername() + "'s corruption has already been neutralised.");
		}
	}

	private boolean neutralizeCorruption(Player player, Player target) {
		if (!target.getTemporaryAttributes().containsKey("apmeken_corruption_effect")) {
			return false;
		}
		player.sendMessage("<col=229628>You neutralise " + target.getUsername() + "'s corruption.");
		target.getTemporaryAttributes().remove("apmeken_corruption_effect");
		target.sendMessage("<col=229628>" + player.getUsername() + " has neutralised your corruption.");
		target.setTinting(RESET_TINTING);
		return true;
	}
}
