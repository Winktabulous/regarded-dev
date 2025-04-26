package com.zenyte.game.world.entity.player.update;

import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.masks.UpdateFlags;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Kris | 7. mai 2018 : 16:30:00
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public abstract class UpdateMask {

	public abstract UpdateFlag getFlag();
	
	public boolean apply(Player player, final Entity entity, final UpdateFlags flags, final boolean added) {
		return flags.get(getFlag());
	}
	
	public void writePlayer(final RSBuffer buffer, final Player player, final Player processedPlayer) {
		
	}
	
	public void writeNPC(final RSBuffer buffer, final Player player, final NPC npc) {
		
	}
	
}
