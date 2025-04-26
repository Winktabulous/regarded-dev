package com.zenyte.game.content.tombsofamascut.npc;

import com.near_reality.game.world.entity.TargetSwitchCause;
import com.zenyte.game.content.tombsofamascut.encounter.ZebakEncounter;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NPCCombat;
import com.zenyte.game.world.entity.pathfinding.Flags;
import com.zenyte.game.world.entity.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Savions.
 */
public class CrondisJug extends NPC {

	public static HashMap<CrondisJug, Player> backupJugReference = new HashMap<>();

	public static void removeAll(Player player) {
		List<Map.Entry<CrondisJug, Player>> entries = backupJugReference.entrySet().stream().filter(it -> it.getValue() == player).toList();
		for(Map.Entry<CrondisJug, Player> entry: entries) {
			entry.getKey().finish();
		}
	}

	private static final Projectile SPREAD_PROJECTILE = new Projectile(2193, 7, 0, 10, 20, 10, 0, 0);
	private static final Graphics POISON_REMOVE_GFX = new Graphics(95);
	private static final Graphics BREAK_GFX = new Graphics(2192);
	private static final Graphics SPLASH_GFX = new Graphics(68);
	private static final Animation PLAYER_MOVE_ANIM = new Animation(834);
	private final ZebakEncounter zebakEncounter;
	private boolean addHitBar = false;
	private Direction moveDirection;
	private int breakJug;

	public CrondisJug(int id, Location tile, ZebakEncounter zebakEncounter) {
		super(id, tile, Direction.randomDirection(), 0);
		this.zebakEncounter = zebakEncounter;
		this.combat = new NPCCombat(this) {
			@Override
			public void setTarget(final Entity target, TargetSwitchCause cause) { }
			@Override
			public void forceTarget(final Entity target) { }
		};
		backupJugReference.put(this, zebakEncounter.getParty().getLeader());
	}



	@Override public void processHit(Hit hit) {
		if (hit.getSource() instanceof Zebak) {
			addHitBar = true;
			super.processHit(hit);
		} else {
			sendDeath();
		}
	}

	@Override public boolean isForceAttackable() { return true; }

	@Override public boolean canAttack(Player source) {
		if (source.getAppearance().getRenderAnimation() != null && source.getAppearance().getRenderAnimation().getWalk() == 772) {
			source.sendMessage("You cannot initiate combat while swimming!");
			return false;
		}
		return true;
	}

	@Override public void sendDeath() {
		World.sendGraphics(BREAK_GFX, getLocation());
		final int spreadRange = zebakEncounter.isUpsetStomach() ? 1 : 2;
		final ArrayList<Location> clearLocations = new ArrayList<>();
		for (int x = -spreadRange; x <= spreadRange; x++) {
			for (int y = -spreadRange; y <= spreadRange; y++) {
				final Location tile = getLocation().transform(x, y);
				if (zebakEncounter.onPoison(tile)) {
					clearLocations.add(tile);
					World.sendProjectile(getLocation(), tile, SPREAD_PROJECTILE);
				}
			}
		}
		if (clearLocations.size() > 0) {
			WorldTasksManager.schedule(zebakEncounter.addRunningTask(() -> {
				clearLocations.forEach(loc -> {
					zebakEncounter.removePoison(loc);
					World.sendGraphics(POISON_REMOVE_GFX, loc);
				});
			}), 0);
		}
		finish();
	}



	@Override public void processNPC() {
		if (moveDirection != null) {
			if (breakJug == 1) {
				sendDeath();
			} else if (breakJug == 2) {
				World.sendGraphics(SPLASH_GFX, getLocation());
				finish();
			}
			final Location nextLoc = getLocation().transform(moveDirection.getOffsetX(), moveDirection.getOffsetY());
			addWalkSteps(nextLoc.getX(), nextLoc.getY(), -1, false);
			if (World.getObjectWithId(nextLoc, Zebak.BOULDER_BLOCK_ID) != null) {
				breakJug = 1;
			} else if ((World.getMask(getLocation()) & Flags.FLOOR_DECORATION) != 0) {
				breakJug = 2;
			}
		}
	}

	public void moveJug(final Player player, boolean push) {
		if (id != Zebak.JUG_ID) {
			return;
		}
		moveJug(push ? Direction.getDirection(player.getLocation(), getLocation()) : Direction.getDirection(getLocation(), player.getLocation()));
		player.setAnimation(PLAYER_MOVE_ANIM);
	}

	public void moveJug(final Direction direction) {
		moveDirection = direction;
		if (id != Zebak.JUG_ID + 1) {
			setTransformation(Zebak.JUG_ID + 1);
		}
	}

	@Override public void finish() {
		super.finish();
		zebakEncounter.removeJug(this);
		backupJugReference.remove(this);
	}

	@Override public boolean isEntityClipped() { return false; }

	@Override public void setRespawnTask() {}

	@Override public void setTarget(Entity target, TargetSwitchCause cause) {}

	@Override public void setFaceEntity(Entity entity) {}
}
