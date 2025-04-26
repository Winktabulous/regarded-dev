package com.zenyte.game.world.entity.player.update.mask;

import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.update.UpdateMask;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Kris | 7. mai 2018 : 17:13:15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class ForceChatMask extends UpdateMask {

	@Override
	public UpdateFlag getFlag() {
		return UpdateFlag.FORCED_CHAT;
	}

	@Override
	public void writePlayer(final RSBuffer buffer, final Player player, final Player processedPlayer) {
		final ForceTalk talk = processedPlayer.getForceTalk();
		buffer.writeString(talk == null ? "" : talk.getText());
	}

	@Override
	public void writeNPC(final RSBuffer buffer, final Player player, final NPC npc) {
		final ForceTalk talk = npc.getForceTalk();
		buffer.writeString(talk == null ? "" : talk.getText());
	}

}
