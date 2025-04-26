package com.zenyte.game.world.entity.player.update.mask;

import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.masks.UpdateFlags;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.update.UpdateMask;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Kris | 7. mai 2018 : 17:09:32
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class NametagMask extends UpdateMask {
	@Override
	public UpdateFlag getFlag() {
		return UpdateFlag.NAMETAG;
	}

	@Override
	public boolean apply(Player player, final Entity entity, final UpdateFlags flags, final boolean added) {
		return flags.get(getFlag()) || (((Player) entity).getNametags() != null && added);
	}

	@Override
	public void writePlayer(final RSBuffer buffer, final Player player, final Player processedPlayer) {
		final String[] nametags = processedPlayer.getNametags();
		if (nametags == null) {
			for (int i = 0; i < 3; i++) {
				buffer.writeString("");
			}
			return;
		}
		for (int i = 0; i < 3; i++) {
			final String nametag = nametags[i];
			buffer.writeString(nametag == null ? "" : nametag);
		}
	}
}
