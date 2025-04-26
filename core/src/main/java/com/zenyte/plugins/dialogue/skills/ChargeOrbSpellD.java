package com.zenyte.plugins.dialogue.skills;

import com.zenyte.game.content.skills.magic.actions.ChargeOrbSpell;
import com.zenyte.game.content.skills.magic.actions.ChargeOrbSpell.ChargeOrbSpellData;
import com.zenyte.game.content.skills.magic.spells.MagicSpell;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.SkillDialogue;

/**
 * @author Tommeh | 9 jan. 2018 : 20:07:31
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ChargeOrbSpellD extends SkillDialogue {

	private final ChargeOrbSpellData data;
	private final MagicSpell spell;

	public ChargeOrbSpellD(final Player player, final ChargeOrbSpellData data, final MagicSpell spell) {
		super(player, "How many many would you like to charge?", data.getProduct());
		this.data = data;
		this.spell = spell;
	}

	@Override
	public void run(final int slotId, final int amount) {
		if (data == null) {
			return;
		}
		player.getActionManager().setAction(new ChargeOrbSpell(data, amount, spell));
	}

}
