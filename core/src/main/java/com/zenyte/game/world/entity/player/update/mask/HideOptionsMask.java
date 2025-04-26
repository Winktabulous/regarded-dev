package com.zenyte.game.world.entity.player.update.mask;

import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.update.UpdateMask;
import com.zenyte.net.io.RSBuffer;

public class HideOptionsMask extends UpdateMask {

	@Override
	public void writeNPC(RSBuffer buffer, Player player, NPC npc) {
		buffer.writeByte(npc.getOptionMask());
	}

	@Override
	public UpdateFlag getFlag() {
		return UpdateFlag.HIDE_OPTIONS;
	}

}
