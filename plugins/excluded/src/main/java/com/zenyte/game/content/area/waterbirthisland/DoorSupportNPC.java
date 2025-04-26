package com.zenyte.game.content.area.waterbirthisland;

import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 21. march 2018 : 22:01.10
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class DoorSupportNPC extends NPC implements Spawnable {
	private static final Location NORTHERN_TILE = new Location(2545, 10145, 0);
	private static final Location SOUTHERN_TILE = new Location(2545, 10141, 0);
	private static final Location MIDDLE_TILE = new Location(2543, 10143, 0);

	public DoorSupportNPC(final int id, final Location tile, final Direction facing, final int radius) {
		super(id, tile, facing, radius);
		if (tile == null) {
			return;
		}
		hash = tile.getPositionHash();
		this.id = id;
		World.getRegion(getLocation().getRegionId(), true);
		object = World.getObjectWithType(getLocation(), 10);
	}

	private int hash;
	private int id;
	private WorldObject object;

	@Override
	public boolean checkProjectileClip(final Player player, boolean melee) {
		return !player.getLocation().withinDistance(getLocation(), 5);
	}

	@Override
	protected boolean isMovableEntity() {
		return false;
	}

	@Override
	public boolean isMovementRestricted() {
		return true;//prevents combat
	}

	@Override
	public void applyHit(final Hit hit) {
		hit.setDamage(1);
		super.applyHit(hit);
	}

	@Override
	public boolean canAttack(final Player source) {
		if (hash == NORTHERN_TILE.getPositionHash()) {
			if (source.getLocation().getY() >= NORTHERN_TILE.getY()) {
				source.sendMessage("You\'re unable to damage the door from this side.");
				return false;
			}
		} else if (hash == SOUTHERN_TILE.getPositionHash()) {
			if (source.getLocation().getY() <= SOUTHERN_TILE.getY()) {
				source.sendMessage("You\'re unable to damage the door from this side.");
				return false;
			}
		} else if (hash == MIDDLE_TILE.getPositionHash()) {
			if (source.getLocation().getX() <= MIDDLE_TILE.getX()) {
				source.sendMessage("You\'re unable to damage the door from this side.");
				return false;
			}
		}
		return true;
	}

	@Override
	public void sendDeath() {
		final Player source = getMostDamagePlayerCheckIronman();
		onDeath(source);
		WorldTasksManager.schedule(new WorldTask() {
			private int loop;
			@Override
			public void run() {
				if (loop == 0) {
					setTransformation(id + 1);
				} else if (loop == 2) {
					setRespawnTask();
					reset();
					World.removeObject(object);
					setTransformation(id + 2);
					stop();
					return;
				}
				loop++;
			}
		}, 0, 1);
	}

	@Override
	public boolean isAttackableNPC() {
		return getDefinitions().containsOption("Destroy");
	}

	@Override
	public void setRespawnTask() {
		WorldTasksManager.schedule(() -> {
			World.spawnObject(object);
			setTransformation(id);
		}, 30);
	}

	@Override
	public boolean validate(final int id, final String name) {
		return id >= 2250 && id <= 2256;
	}
}
