package com.zenyte.game.world.entity.player.update.mask;

import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.masks.UpdateFlags;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.update.UpdateMask;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Kris | 7. mai 2018 : 16:56:01
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class MovementMask extends UpdateMask {

	@Override
	public UpdateFlag getFlag() {
		return UpdateFlag.MOVEMENT_TYPE;
	}
	
	@Override
	public boolean apply(Player player, final Entity entity, final UpdateFlags flags, final boolean added) {
		return flags.get(getFlag()) || added;
	}

	@Override
	public void writePlayer(final RSBuffer buffer, final Player player, final Player processedPlayer) {
		buffer.writeByte128(processedPlayer.getLastMovementType());
	}

}
