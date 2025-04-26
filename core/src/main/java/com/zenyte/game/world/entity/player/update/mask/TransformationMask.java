package com.zenyte.game.world.entity.player.update.mask;

import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.update.UpdateMask;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Kris | 7. mai 2018 : 17:21:16
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class TransformationMask extends UpdateMask {

	@Override
	public UpdateFlag getFlag() {
		return UpdateFlag.TRANSFORMATION;
	}

	@Override
	public void writeNPC(final RSBuffer buffer, final Player player, final NPC npc) {
		buffer.writeShort128(npc.getNextTransformation());
	}

}
