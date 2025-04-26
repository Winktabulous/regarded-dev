package com.zenyte.game.content.chambersofxeric.greatolm;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.Chunk;

/**
 * @author Kris | 18. jaan 2018 : 0:22.11
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * The fire wall npc that Olm spawns during the {@link com.zenyte.game.content.chambersofxeric.greatolm.scripts.FireWall} attack.
 * Players must douse the fire wall using any water spell, {@link com.zenyte.game.content.skills.magic.spells.lunar.Humidify} included.
 */
public final class FireWallNPC extends NPC {
	public FireWallNPC(final Location tile) {
		super(NpcId.FIRE, tile, Direction.SOUTH, 5);
	}

	@Override
	public boolean canAttack(final Player source) {
		return false;
	}

	public boolean isAttackable() {
		return false;
	}

	@Override
	public boolean addWalkStep(final int nextX, final int nextY, final int lastX, final int lastY, final boolean check) {
		return false;
	}

	@Override
	public void unclip() {
		final int size = getSize();
		final int x = getX();
		final int y = getY();
		final int z = getPlane();
		int hash;
		int lastHash = -1;
		Chunk chunk = null;
		for (int x1 = x; x1 < (x + size); x1++) {
			for (int y1 = y; y1 < (y + size); y1++) {
				if ((hash = Chunk.getChunkHash(x1 >> 3, y1 >> 3, z)) != lastHash) {
					chunk = World.getChunk(lastHash = hash);
				}
				assert chunk != null;
				final WorldObject object = World.getObjectWithType(Location.hash(x1, y1, z), 10);
				if (object != null && object.getId() == 0) {
					World.removeObject(object);
				}
			}
		}
	}

	@Override
	public void clip() {
		if (isFinished()) {
			return;
		}
		final int size = getSize();
		final int x = getX();
		final int y = getY();
		final int z = getPlane();
		for (int x1 = x; x1 < (x + size); x1++) {
			for (int y1 = y; y1 < (y + size); y1++) {
				World.spawnObject(new WorldObject(0, 10, 0, x1, y1, z));
			}
		}
	}
}
