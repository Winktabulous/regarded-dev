package com.near_reality.game.content.boss.nex;

import com.near_reality.game.content.boss.nex.npc.BloodReaver;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.Player;

public final class NexBloodReaver extends BloodReaver {

	public static Projectile soulSplitProjectile = new Projectile(2009, 30, 30, 0, 6, 60, 0, 0);
	public static final int ID = NpcId.BLOOD_REAVER_11294;

	private final NexNPC nex;

	public NexBloodReaver(Location tile, Direction facing, int radius, NexNPC nex) {
		super(ID, tile, facing, radius);
		this.nex = nex;
		this.spawned = true;
	}

	public int healNex() {
		if (nex == null || isDead() || isFinished())
			return 0;
		sendDeath();
		soulSplitProjectile.build(this, nex);
		return getHitpoints();
	}

	/**
	 * Override this to ignore protective items.
	 */
	@Override
	public boolean isAcceptableTarget(Entity target) {
		return target instanceof Player;
	}
}
