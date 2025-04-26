package com.zenyte.game.content.boss.wildernessbosses.vetion;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.player.Player;


/**
 * @author Andys1814
 */
public final class SkeletalHellhound extends NPC implements Spawnable {
	public SkeletalHellhound(final int id, final Location tile, final Direction facing, final int radius) {
		super(id, tile, facing, radius);
	}

	@Override
	public boolean isEntityClipped() {
		return true;
	}

	@Override
	public void setRespawnTask() {
	}

	@Override
	protected void onFinish(final Entity source) {
		reset();
		finish();
		if (!spawned) {
			setRespawnTask();
		}
		if (source != null) {
			if (source instanceof Player) {
				final Player player = (Player) source;
				sendNotifications(player);
			}
		}
	}

	@Override
	public boolean validate(final int id, final String name) {
		return id == 6613 || id == 6614;
	}

	@Override
	public boolean isForceAttackable() {
		return true;
	}
}
