package com.zenyte.game.content.minigame.pestcontrol.npc;

import com.zenyte.game.content.minigame.pestcontrol.PestControlInstance;
import com.zenyte.game.content.minigame.pestcontrol.PestNPC;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity._Location;
import com.zenyte.game.world.entity.pathfinding.Flags;
import com.zenyte.game.world.region.Chunk;

/**
 * @author Kris | 27. juuni 2018 : 18:30:48
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class BrawlerNPC extends PestNPC {
	public BrawlerNPC(final PestControlInstance instance, final PestPortalNPC portal, final int id, final Location tile) {
		super(instance, portal, id, tile);
		instance.getBrawlers().add(this);
		forceAggressive = true;
	}

	@Override
	public void finish() {
		super.finish();
		instance.getBrawlers().remove(this);
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
				World.getRegion(_Location.getRegionId(x1, y1), true).removeFlag(z, x1 & 63, y1 & 63, Flags.OCCUPIED_BLOCK_NPC | Flags.OCCUPIED_BLOCK_PLAYER | Flags.OCCUPIED_PROJECTILE_BLOCK_NPC | Flags.OCCUPIED_PROJECTILE_BLOCK_PLAYER);
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
				World.getRegion(_Location.getRegionId(x1, y1), true).addFlag(z, x1 & 63, y1 & 63, Flags.OCCUPIED_BLOCK_NPC | Flags.OCCUPIED_BLOCK_PLAYER | Flags.OCCUPIED_PROJECTILE_BLOCK_NPC | Flags.OCCUPIED_PROJECTILE_BLOCK_PLAYER);
			}
		}
	}
}
