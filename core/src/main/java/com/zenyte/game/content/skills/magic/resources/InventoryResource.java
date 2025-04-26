package com.zenyte.game.content.skills.magic.resources;

import com.zenyte.game.content.skills.magic.RuneContainer;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 7. juuli 2018 : 01:35:22
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class InventoryResource implements RuneResource {

	@Override
	public RuneContainer getContainer() {
		return RuneContainer.INVENTORY;
	}

	@Override
	public int getAmountOf(final Player player, final int runeId, final int amountRequired) {
		return Math.min(player.getInventory().getAmountOf(runeId), amountRequired);
	}

	@Override
	public void consume(final Player player, final int runeId, final int amount) {
		player.getInventory().deleteItem(runeId, amount);
	}

	@Override
	public int[] getAffectedRunes() {
		return null;
	}

}
