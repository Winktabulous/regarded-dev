package com.zenyte.game.world.entity.player.update.mask;

import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.masks.UpdateFlags;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.update.UpdateMask;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Kris | 11/06/2022
 */
public final class CombatLevelChangeMask extends UpdateMask {

	@Override
	public UpdateFlag getFlag() {
		return UpdateFlag.COMBAT_LEVEL_CHANGE;
	}

	@Override
	public boolean apply(Player player, Entity entity, UpdateFlags flags, boolean added) {
		return super.apply(player, entity, flags, added) || (entity instanceof NPC && ((NPC) entity).getCombatLevelChange() != -1);
	}

	@Override
	public void writePlayer(final RSBuffer buffer, final Player player, final Player processedPlayer) {
		throw new IllegalStateException("Players do not support combat level overrides through this mask.");
	}

	@Override
	public void writeNPC(final RSBuffer buffer, final Player player, final NPC npc) {
		final int combatLevelChange = npc.getCombatLevelChange();
		buffer.writeInt(combatLevelChange);
	}

}
