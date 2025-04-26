package com.zenyte.game.content.tombsofamascut.encounter;

import com.zenyte.game.content.tombsofamascut.npc.CrondisCrocodile;
import com.zenyte.game.content.tombsofamascut.npc.PalmOfResourcefulness;
import com.zenyte.game.content.tombsofamascut.raid.EncounterStage;
import com.zenyte.game.content.tombsofamascut.raid.EncounterType;
import com.zenyte.game.content.tombsofamascut.raid.TOARaidArea;
import com.zenyte.game.content.tombsofamascut.raid.TOARaidParty;
import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.RSColour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Savions.
 */
public class CrondisPuzzleEncounter extends TOARaidArea implements CycleProcessPlugin {

	public static final int CONTAINER_ITEM_ID = 27295;
	public static final int WATERFALL_OBJECT_ID = 45398;
	public static final int PALM_NPC_ID = 11700;
	private static final int STATUE_SPEAR_OBJECT_ID = 45415;
	private static final int STATUE_OBJECT_ID = 45414;
	private static final int ACID_OBJECT_ID = 45403;
	private static final int ACID_GFX_ID = 2129;
	private static final SoundEffect CONTAINER_FILL_SOUND = new SoundEffect(6522, 1, 10);
	private static final SoundEffect CONTAINER_DECLINE_SOUND = new SoundEffect(6524, 1, 10);
	private static final SoundEffect SPILL_SOUND = new SoundEffect(2401, 1, 10);
	private static final SoundEffect PALM_TREE_WATERING_SOUND = new SoundEffect(6534, 1, 15);
	private static final Animation CONTAINER_FILL_ANIM = new Animation(827);
	private static final Location[] CONTAINER_DROP_LOCATIONS = {new Location(3934, 5273), new Location(3938, 5287)};
	private static final Location[] STATUE_SOUTH_LOCATIONS = {new Location(3943, 5255), new Location(3929, 5255)};
	private static final Location[] STATUE_NORTH_LOCATIONS = {new Location(3929, 5304), new Location(3943, 5304)};
	private static final Location[] WATERFALL_SOUTH_LOCATIONS = {new Location(3926, 5250), new Location(3940, 5250)};
	private static final Location[] WATERFALL_NORTH_LOCATIONS = {new Location(3926, 5306), new Location(3940, 5306)};
	private static final Location[] ACID_BASE_SOUTH_LOCATIONS = {new Location(3941, 5257), new Location(3927, 5257)};
	private static final Location[] ACID_BASE_NORTH_LOCATIONS = {new Location(3941, 5303), new Location(3927, 5303)};
	private static final Location[] STATUE_SPEAR_LOCATIONS = {new Location(3925, 5293), new Location(3939, 5293),
			new Location(3925, 5258), new Location(3939, 5258)};
	private static final Location[] CROCODILE_SPAWN_LOCATIONS = {new Location(3925, 5285), new Location(3946, 5285),
			new Location(3925, 5274), new Location(3946, 5274)};
	private static final int[][] STATUE_SPEAR_DELAYS = {{0, 2, 4, 6, 8, 0, 2, 4, 6, 8}, {0, 3, 6, 9, 2, 0, 3, 6, 9, 2},
			{2, 1, 1, 0, 0, 2, 2, 1, 1, 0}, {0, 1, 2, 3, 4, 4, 3, 2, 1, 0}};
	private static final Location PALM_SPAWN_LOC = new Location(3934, 5278);
	private static final Location END_BARRIER_LOCATION = new Location(3922, 5279);
	private static final Animation STATUE_END_ANIM = new Animation(9562);
	private static final Animation STATUE_MOUTH_ANIM = new Animation(9563);
	private static final Animation STATUE_SPEAR_ANIM = new Animation(9565);
	private static final RSColour[] PALM_HUD_COLOURS = { new RSColour(0, 4, 4), new RSColour(0, 19, 15), new RSColour(0, 26, 21)};
	private PalmOfResourcefulness palmNpc;
	private int acidProcessTicks = 0;
	private int spearProcessTicks = 0;
	private int crocodileTicks = 50;
	private final ArrayList<AcidTrail> acidTrails = new ArrayList<>();
	private final ArrayList<CrondisCrocodile> crocodiles = new ArrayList<>();

	public CrondisPuzzleEncounter(AllocatedArea allocatedArea, int copiedChunkX, int copiedChunkY, TOARaidParty party, EncounterType encounterType) {
		super(allocatedArea, copiedChunkX, copiedChunkY, party, encounterType);
	}

	@Override public void constructed() {
		super.constructed();
		Arrays.stream(CONTAINER_DROP_LOCATIONS).forEach(loc -> World.spawnFloorItem(new Item(CONTAINER_ITEM_ID, 1), getLocation(loc), null, -1, Integer.MAX_VALUE));
		Arrays.stream(STATUE_SOUTH_LOCATIONS).forEach(loc -> World.spawnObject(new WorldObject(STATUE_OBJECT_ID, 10, 0, getLocation(loc))));
		Arrays.stream(STATUE_NORTH_LOCATIONS).forEach(loc -> World.spawnObject(new WorldObject(STATUE_OBJECT_ID, 10, 2, getLocation(loc))));
		spawnPalm();
		World.spawnObject(new WorldObject(32740, 10, 0, getLocation(PALM_SPAWN_LOC)));
	}

	@Override public void enter(Player player) {
		super.enter(player);
		for (int animId = 9618; animId <= 9646; animId++) {
			player.getPacketDispatcher().sendClientScript(1846, animId);
		}
		player.getPacketDispatcher().sendClientScript(1846, 9532);
		player.getPacketDispatcher().sendClientScript(1846, 9533);
		player.getPacketDispatcher().sendClientScript(1846, 9534);
		player.getPacketDispatcher().sendClientScript(1846, 9541);
	}

	public void handleWaterfall(Player player, WorldObject object) {
		if (EncounterStage.COMPLETED.equals(stage)) {
			player.sendMessage("You don't need to do that right now.");
			return;
		}
		if (object.getId() != CrondisPuzzleEncounter.WATERFALL_OBJECT_ID) {
			player.sendMessage("It's empty.");
			player.sendSound(CONTAINER_DECLINE_SOUND);
		} else {
			final Item container = player.getInventory().getAny(CrondisPuzzleEncounter.CONTAINER_ITEM_ID);
			if (container == null) {
				player.sendMessage("You don't have anything to fill.");
			} else if (container.getCharges() == 100) {
				player.sendMessage("Your container is full.");
			} else {
				player.sendMessage("You fill your container.");
				player.lock(1);
				player.sendSound(CONTAINER_FILL_SOUND);
				player.setAnimation(CONTAINER_FILL_ANIM);
				player.getVariables().setRunEnergy(player.getVariables().getRunEnergy() + 20);
				container.setCharges(100);
				final int respawnTicks = 128 - (teamSize - 1) * 18;
				World.spawnTemporaryObject(new WorldObject(object.getId() + 1, object.getType(), object.getRotation(),
						object.getLocation()), respawnTicks, addRunningTask(() -> World.spawnObject(object)));
			}
		}
	}

	private void spawnPalm() {
		if (palmNpc != null) {
			palmNpc.finish();
		}
		palmNpc = new PalmOfResourcefulness(PALM_NPC_ID, getLocation(PALM_SPAWN_LOC), this);
		palmNpc.spawn();
	}

	private void spawnWaterfalls() {
		Arrays.stream(WATERFALL_SOUTH_LOCATIONS).forEach(loc -> World.spawnObject(new WorldObject(WATERFALL_OBJECT_ID, 10, 0, getLocation(loc))));
		Arrays.stream(WATERFALL_NORTH_LOCATIONS).forEach(loc -> World.spawnObject(new WorldObject(WATERFALL_OBJECT_ID, 10, 2, getLocation(loc))));
	}

	@Override public void process() {
		if (EncounterStage.STARTED.equals(stage)) {
			acidTrails.removeIf(AcidTrail::process);
			players.stream().filter(p -> !p.isDying() && insideChallengeArea(p)).forEach(p -> {
				acidTrails.forEach(acid -> acid.check(p));
			});
			if (--acidProcessTicks <= 0) {
				acidProcessTicks = 5;
				spawnAcidTrails(ACID_BASE_NORTH_LOCATIONS, false);
				spawnAcidTrails(ACID_BASE_SOUTH_LOCATIONS, true);
			}
			handleSpears();
			if (++spearProcessTicks >= 10) {
				spearProcessTicks = 0;
			}
			if (crocodileTicks-- <= 0) {
				crocodileTicks = 50;
				if (crocodiles.size() < 8) {
					for (int i = 0; i < Math.min(Math.ceil((float) teamSize / 2), CROCODILE_SPAWN_LOCATIONS.length); i++) {
						final CrondisCrocodile crondisCrocodile = new CrondisCrocodile(getLocation(CROCODILE_SPAWN_LOCATIONS[i]), palmNpc, this);
						crondisCrocodile.spawn();
						crocodiles.add(crondisCrocodile);
					}
				}
			}
		}
	}

	private void spawnAcidTrails(Location[] baseLocations, final boolean moveNorth) {
		Arrays.stream(baseLocations).forEach(loc -> {
			final List<Integer> deltas = new ArrayList<>(IntStream.range(0, 5).boxed().toList());
			Collections.shuffle(deltas);
			for (int i = 0; i < Utils.random(2, 3); i++) {
				final AcidTrail acidTrail = new AcidTrail(getLocation(loc.transform(deltas.get(i), 0)), moveNorth);
				World.spawnTemporaryObject(new WorldObject(ACID_OBJECT_ID, 10, 0, acidTrail.baseLocation), 3);
				WorldTasksManager.schedule(addRunningTask(acidTrail::spawnTrail), 2);
			}
		});
	}

	private void handleSpears() {
		final List<Location> activeSpearLocations = new ArrayList<>();
		for (int i = 0; i < STATUE_SPEAR_LOCATIONS.length; i++) {
			handleSpearColumn(i, false, activeSpearLocations);
			handleSpearColumn(i, true, activeSpearLocations);
		}
		players.stream().filter(p -> insideChallengeArea(p) && !p.isDying()).forEach(p -> {
			for (Location loc : activeSpearLocations) {
				if (p.getY() == loc.getY() && p.getX() >= loc.getX() && p.getX() <= loc.getX() + 2) {
					long lastHit = (long) p.getTemporaryAttributes().getOrDefault("toa_acid_spike_hit", 0L);
					if (lastHit <= WorldThread.getCurrentCycle()) {
						final int base = (int) Math.floor(6 * party.getDamageMultiplier());
						spillWater(p);
						p.applyHit(new Hit(p, Utils.random(base, base + 8), HitType.DEFAULT));
						p.getTemporaryAttributes().put("toa_acid_spike_hit", WorldThread.getCurrentCycle() + 3);
						applyDebuffs(p);
					}
					break;
				}
			}
		});
	}

	private void handleSpearColumn(int index, boolean east, List<Location> activeSpearLocations) {
		for (int y = 0; y < 5; y++) {
			final int delay = STATUE_SPEAR_DELAYS[index][y + (east ? 5 : 0)];
			final Location instanceLocation = getLocation(STATUE_SPEAR_LOCATIONS[index]);
			if (delay == spearProcessTicks) {
				World.sendObjectAnimation(STATUE_SPEAR_OBJECT_ID, 10, east ? 0 : 2, instanceLocation.transform(east ? 7 : 0, y * 2 + (east ? 1 : 0)), STATUE_MOUTH_ANIM);
			} else if ((delay + 3) % 10 == spearProcessTicks) {
				World.sendObjectAnimation(STATUE_SPEAR_OBJECT_ID + 1, 10, east ? 0 : 2, instanceLocation.transform(east ? 4 : 2, y * 2 + (east ? 1 : 0)), STATUE_SPEAR_ANIM);
			} else if ((delay + 4) % 10 == spearProcessTicks || ((delay + 5) % 10 == spearProcessTicks)) {
				activeSpearLocations.add(instanceLocation.transform(east ? 4 : 2, y * 2 + (east ? 1 : 0)));
			} else if ((delay + 6) % 10 == spearProcessTicks) {
				World.sendObjectAnimation(STATUE_SPEAR_OBJECT_ID + 1, 10, east ? 0 : 2, instanceLocation.transform(east ? 4 : 2, y * 2 + (east ? 1 : 0)), STATUE_END_ANIM);
				activeSpearLocations.add(instanceLocation.transform(east ? 4 : 2, y * 2 + (east ? 1 : 0)));
			} else if ((delay + 7) % 10 == spearProcessTicks) {
				World.sendObjectAnimation(STATUE_SPEAR_OBJECT_ID, 10, east ? 0 : 2, instanceLocation.transform(east ? 7 : 0, y * 2 + (east ? 1 : 0)), STATUE_END_ANIM);
			}
		}
	}

	private void clearStatueSpikes() {
		for (int i = 0; i < STATUE_SPEAR_LOCATIONS.length; i++) {
			clearSpearColumn(i, false);
			clearSpearColumn(i, true);
		}
	}

	private void clearSpearColumn(int index, boolean east) {
		for (int y = 0; y < 5; y++) {
			final Location instanceLocation = getLocation(STATUE_SPEAR_LOCATIONS[index]);
			World.sendObjectAnimation(STATUE_SPEAR_OBJECT_ID + 1, 10, east ? 0 : 2, instanceLocation.transform(east ? 4 : 2, y * 2 + (east ? 1 : 0)), STATUE_END_ANIM);
			World.sendObjectAnimation(STATUE_SPEAR_OBJECT_ID, 10, east ? 0 : 2, instanceLocation.transform(east ? 7 : 0, y * 2 + (east ? 1 : 0)), STATUE_END_ANIM);
		}
	}

	public void spillWater(Player player) {
		final Item container = player.getInventory().getAny(CONTAINER_ITEM_ID);
		if (container != null) {
			final int charges = container.getCharges();
			if (charges > 0) {
				int reduce = (int) Math.ceil((double) charges / 2);
				player.sendMessage("Water spills out of your container.");
				player.sendSound(SPILL_SOUND);
				container.setCharges(Math.max(charges - reduce, 0));
			}
		}
	}

	public void waterPalm(Player player) {
		if (palmNpc == null) {
			return;
		}
		final Item container = player.getInventory().getAny(CONTAINER_ITEM_ID);
		if (container == null || container.getCharges() < 1) {
			player.sendMessage("You have nothing to water the palm with.");
		} else {
			final int charges = container.getCharges();
			String amountString = "lot";
			if (charges > 25 && charges <= 50) {
				amountString = "reasonable amount";
			} else if (charges <= 25) {
				amountString = "small amount";
			}
			player.sendMessage("You empty a " + amountString + " of water onto the palm.");
			player.setAnimation(CONTAINER_FILL_ANIM);
			player.lock(1);
			player.sendSound(PALM_TREE_WATERING_SOUND);
			container.setCharges(0);
			palmNpc.applyHit(new Hit(player, charges, HitType.SHIELD_CHARGE));
		}
	}

	@Override public void leave(Player player, boolean logout) {
		super.leave(player, logout);
		player.getHpHud().close();
	}

	@Override public void onRoomStart() {
		palmNpc.getCombatDefinitions().setHitpoints(teamSize * 200);
		palmNpc.setHitpoints(palmNpc.getMaxHitpoints());
		players.forEach(p -> {
			p.getHpHud().open(11700, palmNpc.getMaxHitpoints());
			p.getHpHud().updateValue(0);
			p.getHpHud().sendColorChange(PALM_HUD_COLOURS[0], PALM_HUD_COLOURS[1], PALM_HUD_COLOURS[2]);
		});
	}

	@Override public void onRoomEnd() {
		spawnWaterfalls();
		clearStatueSpikes();
		acidTrails.clear();
		for (CrondisCrocodile croc : crocodiles) {
			if (croc != null && !croc.isFinished()) {
				croc.remove();
			}
		}
		crocodiles.clear();
		players.forEach(p -> {
			p.getInventory().deleteItem(CONTAINER_ITEM_ID, 28);
			p.getHpHud().close();
		});
		Arrays.stream(CONTAINER_DROP_LOCATIONS).forEach(loc -> World.destroyFloorItem(new Item(CONTAINER_ITEM_ID, 1), getLocation(loc)));
		for (int y = 0; y < 3; y++) {
			World.removeObject(new WorldObject(45135, 10, 3, getLocation(END_BARRIER_LOCATION).transform(0, y)));
		}
	}

	@Override public void onRoomReset() {
		spawnPalm();
		spawnWaterfalls();
		clearStatueSpikes();
		acidTrails.clear();
		for (CrondisCrocodile croc : crocodiles) {
			if (croc != null && !croc.isFinished()) {
				croc.remove();
			}
		}
		crocodiles.clear();
		players.forEach(p -> p.getInventory().deleteItem(CONTAINER_ITEM_ID, 28));
		acidProcessTicks = 0;
		spearProcessTicks = 0;
		crocodileTicks = 50;
	}

	private void applyDebuffs(final Player player) {
		final int defLevel = player.getSkills().getLevel(SkillConstants.DEFENCE);
		player.getSkills().setLevel(SkillConstants.DEFENCE, Math.max(0, defLevel - 3));
		final int agilityLevel = player.getSkills().getLevel(SkillConstants.AGILITY);
		player.getSkills().setLevel(SkillConstants.AGILITY, Math.max(0, agilityLevel - 3));
	}

	public void finishCrocodile(CrondisCrocodile crondisCrocodile) {
		crocodiles.remove(crondisCrocodile);
	}

	private class AcidTrail {

		private static final int START_TICKS = 6;
		final Location baseLocation;
		final boolean moveNorth;
		int ticks;

		AcidTrail(Location baseLocation, boolean moveNorth) {
			this.baseLocation = baseLocation;
			this.moveNorth = moveNorth;
			ticks = START_TICKS;
		}

		void spawnTrail() {
			acidTrails.add(this);
			for (int y = 1; y <= 10; y++) {
				World.sendGraphics(new Graphics(ACID_GFX_ID, 10 * y, 0), baseLocation.transform(0, moveNorth ? y : -y));
			}
		}

		boolean process() {
			return --ticks <= 0;
		}

		void check(final Player p) {
			if (p.getX() != baseLocation.getX()) {
				return;
			}
			if (moveNorth ? (p.getY() > baseLocation.getY() && p.getY() <= baseLocation.getY() + 10) : (p.getY() < baseLocation.getY() && p.getY() >= baseLocation.getY() - 10)) {
				final int delta = Math.abs(p.getY() - baseLocation.getY()) - 1;
				final int sub = 1 + delta / 3;
				if (ticks < START_TICKS - sub && ticks >= START_TICKS - sub - 2) {
					long lastHit = (long) p.getTemporaryAttributes().getOrDefault("toa_acid_trail_hit", 0L);
					if (lastHit <= WorldThread.getCurrentCycle()) {
						final int base = (int) Math.floor(5 * party.getDamageMultiplier());
						spillWater(p);
						p.applyHit(new Hit(p, Utils.random(base, base + 8), HitType.DEFAULT));
						p.getTemporaryAttributes().put("toa_acid_trail_hit", WorldThread.getCurrentCycle() + 2);
						applyDebuffs(p);
					}
				}
			}
		}
	}
}
