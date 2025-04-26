package com.zenyte.game.content.tombsofamascut.encounter;

import com.zenyte.game.content.tombsofamascut.InvocationType;
import com.zenyte.game.content.tombsofamascut.npc.CrondisJug;
import com.zenyte.game.content.tombsofamascut.npc.Zebak;
import com.zenyte.game.content.tombsofamascut.raid.*;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.Toxins;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin;
import com.zenyte.game.world.region.area.plugins.FullMovementPlugin;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Savions.
 */
public class ZebakEncounter extends TOARaidArea implements FullMovementPlugin, CycleProcessPlugin {

	public static final int ZEBAK_ID = 11730;
	private static final int BLEED_FLOOR_ID = 652;
	private static final int POISON_OBJECT_ID = 45570;
	private static final Projectile POISON_SPREAD_PROJECTILE = new Projectile(2194, 0, 0, 0, 20, 30, 10, 0);
	private Zebak zebak;
	private boolean checkPoisonObjects;
	private final boolean upsetStomach;
	private final List<Location> activePoisonTiles = new ArrayList<>();
	private final List<WorldObject> poisonObjects = new ArrayList<WorldObject>();
	private final List<CrondisJug> jugs = new ArrayList<>();


	public ZebakEncounter(AllocatedArea allocatedArea, int copiedChunkX, int copiedChunkY, TOARaidParty party, EncounterType encounterType) {
		super(allocatedArea, copiedChunkX, copiedChunkY, party, encounterType);
		upsetStomach = party.getPartySettings().isActive(InvocationType.UPSET_STOMACH);
	}

	@Override public void constructed() {
		super.constructed();
		spawnZebak();
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

	private void spawnZebak() {
		if (zebak != null && !zebak.isFinished()) {
			zebak.finish();
		}
		zebak = new Zebak(this, party.getBossLevels()[TOAPathType.CRONDIS.ordinal()]);
		zebak.spawn();
	}

	@Override public void onRoomStart() {
		zebak.setMaxHealth();
		players.forEach(p -> p.getHpHud().open(ZEBAK_ID, zebak.getMaxHitpoints()));
	}

	@Override public void onRoomEnd() {
		players.forEach(p -> p.getHpHud().close());
		reset();
		party.getPathsCompleted().add(TOAPathType.CRONDIS);
	}

	@Override public void onRoomReset() {
		stopRunningTasks();
		spawnZebak();
		reset();
		players.forEach(p -> p.getHpHud().close());
	}

	private void reset() {
		checkPoisonObjects = false;
		activePoisonTiles.clear();
		for(WorldObject object: poisonObjects) {
			World.removeObject(object);
		}
		try {
			for(CrondisJug object: jugs) {
				if (!object.isDying() && !object.isFinished()) {
					object.finish();
				}
			}
		} catch (Exception io) {
			CrondisJug.removeAll(party.getLeader());
		}
		poisonObjects.clear();
		jugs.clear();
	}

	@Override public boolean processMovement(Player player, int x, int y) {
		if (player.getLocation().getX() != x || player.getLocation().getY() != y) {
			long bleedDelay = (long) player.getTemporaryAttributes().getOrDefault("toa_zebak_bleed", 0L);
			if (bleedDelay > WorldThread.getCurrentCycle()) {
				final int base = zebak.getMaxHit(5);
				player.applyHit(new Hit(zebak, Utils.random(base, base + 7), HitType.DEFAULT));
				if (World.getObjectWithType(player.getLocation(), 22) == null) {
					World.spawnTemporaryObject(new WorldObject(BLEED_FLOOR_ID + Utils.random(2), 22, 0, new Location(player.getLocation())), 10);
				}
			}
		}
		return true;
	}

	@Override public void process() {
		final Player[] players = getChallengePlayers();
		if (zebak != null && !zebak.isDying() && !zebak.isFinished() && EncounterStage.STARTED.equals(stage) && players != null && players.length > 0) {
			Arrays.stream(players).forEach(p -> {
				if (onActivePoison(p.getLocation())) {
					final int base = zebak.getMaxHit(10);
					zebak.delayHit(-1, p, new Hit(zebak, Utils.random(base, base + 7), HitType.POISON).onLand(hit -> {
						p.getToxins().applyToxin(Toxins.ToxinType.POISON, 2, zebak);
					}));
				}
			});
			if (checkPoisonObjects) {
				poisonObjects.forEach(object -> {
					if (!activePoisonTiles.contains(object.getLocation())) {
						activePoisonTiles.add(object.getLocation());
					}
				});
				checkPoisonObjects = false;
			}
		}
	}

	public void addPoison(Location location, boolean spread, boolean guaranteed) {
		if (onPoison(location)) {
			return;
		}
		checkPoisonObjects = true;
		final WorldObject object = new WorldObject(POISON_OBJECT_ID + Utils.random(5), 10, Utils.random(3), location);
		World.spawnObject(object);
		poisonObjects.add(object);
		final ArrayList<Location> spreadTiles = new ArrayList<>();
		if (spread) {
			final int spreadRange = upsetStomach ? 2 : 1;
			for (int x = -spreadRange; x <= spreadRange; x++) {
				for (int y = -spreadRange; y <= spreadRange; y++) {
					if (x == 0 && y == 0) {
						continue;
					}
					final Location attemptTile = location.transform(x, y);
					if ((!guaranteed || y != 0) && Utils.random(2) != 0) {
						continue;
					}
					if (World.getObjectWithType(attemptTile, 10) != null || !World.isFloorFree(attemptTile, 1) || activePoisonTiles.contains(attemptTile)) {
						continue;
					}
					World.sendProjectile(location, attemptTile, POISON_SPREAD_PROJECTILE);
					spreadTiles.add(attemptTile);
				}
			}
			if (spreadTiles.size() > 0) {
				WorldTasksManager.schedule(addRunningTask(() -> {
					if (zebak != null && !zebak.isDying() && !zebak.isFinished() || EncounterStage.STARTED.equals(stage)) {
						spreadTiles.forEach(loc -> addPoison(loc, false, false));
					}
				}), 0);
			}
		}
	}

	public void addJug(CrondisJug crondisJug) {
		if (!jugs.contains(crondisJug)) {
			jugs.add(crondisJug);
		}
	}

	public void removeJug(CrondisJug crondisJug) {
		jugs.remove(crondisJug);
	}

	public List<CrondisJug> getJugs() { return jugs; }

	public boolean onPoison(Location location) {
		for (WorldObject poison : poisonObjects) {
			if (poison != null && location.equals(poison.getLocation())) {
				return true;
			}
		}
		return false;
	}

	public void removePoison(Location location) {
		activePoisonTiles.remove(location);
		poisonObjects.removeIf(poison -> {
			if (poison != null && location.equals(poison.getLocation())) {
				World.removeObject(poison);
				return true;
			}
			return false;
		});
	}

	@Override public void leave(Player player, boolean logout) {
		super.leave(player, logout);
		player.getHpHud().close();
	}

	@Override public Player[] getChallengePlayers() {
		return players.stream().filter(p -> p != null && !p.isDying() &&
				(insideChallengeArea(p) || p.getAppearance().getRenderAnimation() != null && p.getAppearance().getRenderAnimation().getWalk() == 772)).toArray(Player[]::new);
	}

	public boolean onActivePoison(Location location) { return activePoisonTiles.contains(location); }

	public boolean isUpsetStomach() { return upsetStomach; }
}
