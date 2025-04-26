package com.zenyte.plugins.itemonnpc;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.impl.slayer.Gargoyle;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 11. mai 2018 : 01:03:37
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class GargoyleNPCOAction implements ItemOnNPCAction {

	private static final Animation ANIM = new Animation(401);
	
	@Override
	public void handleItemOnNPCAction(final Player player, final Item item, final int slot, final NPC npc) {
		player.setAnimation(ANIM);
		player.lock(1);
		if (npc.getHitpoints() <= 9) {
			npc.getTemporaryAttributes().put("used_rock_hammer", true);
			npc.sendDeath();
		} else {
			player.sendMessage("The gargoyle isn't weak enough to be affected by the rock hammer.");
		}
	}

	@Override
	public Object[] getItems() {
		return new Object[] { Gargoyle.ROCK_HAMMER.getId(), Gargoyle.GRANITE_HAMMER.getId(), 21754 };
	}

	@Override
	public Object[] getObjects() {
		return new Object[] { "Gargoyle", "Marble gargoyle" };
	}

}
