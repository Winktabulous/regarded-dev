package com.zenyte.game.content.skills.magic.resources;

import com.zenyte.game.content.skills.magic.Magic;
import com.zenyte.game.content.skills.magic.RuneContainer;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;

/**
 * @author Kris | 7. juuli 2018 : 13:57:32
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class BryophytasResource implements RuneResource {
	@Override
	public RuneContainer getContainer() {
		return RuneContainer.EQUIPMENT;
	}

	@Override
	public int getAmountOf(final Player player, final int runeId, final int amountRequired) {
		final Item weapon = player.getWeapon();
		if (weapon == null || weapon.getId() != 22370) {
			return 0;
		}
		final int charges = weapon.getCharges();
		return Math.min(charges, amountRequired);
	}

	@Override
	public void consume(final Player player, final int runeId, final int amount) {
		if (Utils.percentage(15)) {
			return;
		}
		final Item weapon = player.getWeapon();
		if (weapon == null || weapon.getId() != 22370) {
			return;
		}
		player.getChargesManager().removeCharges(weapon, amount, player.getEquipment().getContainer(), EquipmentSlot.WEAPON.getSlot());
	}

	@Override
	public int[] getAffectedRunes() {
		return new int[] {Magic.NATURE_RUNE};
	}
}
