package com.zenyte.plugins.dialogue.skills;

import com.zenyte.game.content.skills.smithing.BarbarianSmithing;
import com.zenyte.game.content.skills.smithing.BarbarianWeapon;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.SkillDialogue;

/**
 * @author Tommeh | 31 jul. 2018 | 00:14:51
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class BarbarianSmithingD extends SkillDialogue {
	
	private final BarbarianWeapon weapon;

	public BarbarianSmithingD(final Player player, BarbarianWeapon weapon) {
		super(player, weapon.getSpear(), weapon.getHasta());
		this.weapon = weapon;
	}

	@Override
	public void run(final int slotId, final int amount) {
		player.getActionManager().setAction(new BarbarianSmithing(weapon, slotId, amount));
	}
}
