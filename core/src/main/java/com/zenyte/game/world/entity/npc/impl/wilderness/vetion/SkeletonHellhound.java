package com.zenyte.game.world.entity.npc.impl.wilderness.vetion;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CAType;


/**
 * @author Tommeh | 10 feb. 2018 : 18:18:31
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class SkeletonHellhound extends NPC implements Spawnable {
	public SkeletonHellhound(final int id, final Location tile, final Direction facing, final int radius) {
		super(id, tile, facing, radius);
	}

	@Override
	public boolean isEntityClipped() {
		return false;
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
}
