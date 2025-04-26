package com.zenyte.game.content.skills.magic.resources;

import com.zenyte.game.content.skills.magic.Magic;
import com.zenyte.game.content.skills.magic.RuneContainer;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.item.TomeOfFire;

/**
 * @author Kris | 7. juuli 2018 : 13:40:50
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class TomeOfFireResource implements RuneResource {
	@Override
	public RuneContainer getContainer() {
		return RuneContainer.EQUIPMENT;
	}

	@Override
	public int getAmountOf(final Player player, final int runeId, final int amountRequired) {
		final Item shield = player.getShield();
		if (shield == null || shield.getId() != TomeOfFire.TOME_OF_FIRE) {
			return 0;
		}
		if (shield.getCharges() == 0) {
			return 0;
		}
		return amountRequired;
	}

	@Override
	public void consume(final Player player, final int runeId, final int amount) {
	}

	@Override
	public int[] getAffectedRunes() {
		return new int[] {Magic.FIRE_RUNE};
	}
}
