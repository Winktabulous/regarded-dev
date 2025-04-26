package com.zenyte.game.content.tombsofamascut.encounter;

import com.zenyte.game.content.tombsofamascut.npc.Kephri;
import com.zenyte.game.content.tombsofamascut.raid.*;
import com.zenyte.game.util.RSColour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Savions.
 */
public class KephriEncounter extends TOARaidArea implements CycleProcessPlugin {

	public static final int KEPHRI_ID = 11719;
	private static final int DUNG_ID = 45504;
	private static final Animation PLAYER_DUNG_MOVE_ANIM = new Animation(1114);
	private static final RSColour[] HUD_COLOURS = { new RSColour(0, 4, 4), new RSColour(0, 19, 15), new RSColour(0, 26, 21)};
	private Kephri kephri;
	private List<WorldObject> dungObjects = new ArrayList<>();

	public KephriEncounter(AllocatedArea allocatedArea, int copiedChunkX, int copiedChunkY, TOARaidParty party, EncounterType encounterType) {
		super(allocatedArea, copiedChunkX, copiedChunkY, party, encounterType);
	}

	@Override public void enter(Player player) {
		super.enter(player);
		for (int animId = 9529; animId <= 9609; animId++) {
			player.getPacketDispatcher().sendClientScript(1846, animId);
		}
	}

	@Override public void constructed() {
		super.constructed();
		spawnKephri();
	}

	@Override public void process() {
		final Player[] players = getChallengePlayers();
		if (kephri != null && !kephri.isDying() && !kephri.isFinished() && EncounterStage.STARTED.equals(stage) && players != null && players.length > 0) {
			for (WorldObject object : dungObjects) {
				if (object != null) {
					for (Player p : players) {
						if (p != null && p.getLocation().equals(object.getLocation())) {
							damagePlayerByDung(p);
						}
					}
					for (NPC npc : kephri.getAgileScarabs()) {
						if (npc != null && npc.getLocation().equals(object.getLocation())) {
							npc.applyHit(new Hit(kephri, 10_000, HitType.DEFAULT));
						}
					}
				}
			}
		}
	}

	private void damagePlayerByDung(final Player player) {
		Location nextLocation = null;
		radiusLoop : for (int radius = 1; radius <= 10; radius++) {
			for (int x = -radius; x <= radius; x++) {
				for (int y = -radius; y <= radius; y++) {
					if (x != -radius && x != radius && y != -radius && y != radius) {
						continue;
					}
					final Location attemptLocation = player.getLocation().transform(x, y);
					if (World.isFloorFree(attemptLocation, 1) && World.getObjectWithType(attemptLocation, 10) == null && !onDungObject(attemptLocation)) {
						nextLocation = attemptLocation;
						break radiusLoop;
					}
				}
			}
		}
		player.applyHit(new Hit(kephri, kephri.getMaxHit(3), HitType.DEFAULT));
		if (nextLocation != null) {
			player.stopAll();
			player.setForceTalk("Ouch!");
			player.lock(1);
			player.setAnimation(PLAYER_DUNG_MOVE_ANIM);
			player.autoForceMovement(player.getLocation(), nextLocation, 0, 29);
		}
	}

	private void spawnKephri() {
		if (kephri != null && !kephri.isFinished()) {
			kephri.finish();
		}
		kephri = new Kephri(this, party.getBossLevels()[TOAPathType.SCABARAS.ordinal()]);
		kephri.spawn();
	}

	@Override public void onRoomStart() {
		kephri.setMaxHealth();
		players.forEach(p -> {
			p.getHpHud().open(KEPHRI_ID, kephri.getMaxHitpoints());
			p.getHpHud().sendColorChange(HUD_COLOURS[0], HUD_COLOURS[1], HUD_COLOURS[2]);
		});
	}

	@Override public void onRoomEnd() {
		players.forEach(p -> p.getHpHud().close());
		reset();
		party.getPathsCompleted().add(TOAPathType.SCABARAS);
	}

	@Override public void onRoomReset() {
		spawnKephri();
		reset();
		players.forEach(p -> p.getHpHud().close());
	}

	@Override public void leave(Player player, boolean logout) {
		super.leave(player, logout);
		player.getHpHud().close();
	}

	private void reset() {
		dungObjects.removeIf(object -> {
			World.removeObject(object);
			return true;
		});
	}

	public void addDungObject(final Location location) {
		if (!onDungObject(location)) {
			final WorldObject object = new WorldObject(DUNG_ID, 10, Utils.random(3), location);
			World.spawnObject(object);
			dungObjects.add(object);
		}
	}

	public boolean onDungObject(final Location location) {
		for (WorldObject dung : dungObjects) {
			if (dung != null && location.equals(dung.getLocation())) {
				return true;
			}
		}
		return false;
	}

	public Kephri getKephri() { return kephri; }
}