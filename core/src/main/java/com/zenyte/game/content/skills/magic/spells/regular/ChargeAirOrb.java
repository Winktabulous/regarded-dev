package com.zenyte.game.content.skills.magic.spells.regular;

import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.magic.actions.ChargeOrbSpell.ChargeOrbSpellData;
import com.zenyte.game.content.skills.magic.spells.ObjectSpell;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.skills.ChargeOrbSpellD;

/**
 * @author Kris | 8. juuli 2018 : 20:25:21
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class ChargeAirOrb implements ObjectSpell {

	@Override
	public int getDelay() {
		return 1000;
	}

	@Override
	public boolean spellEffect(final Player player, final WorldObject object) {
        if (!object.getName().equals("Obelisk of Air")) {
            player.sendMessage("Nothing interesting happens.");
            return false;
        }
		player.getDialogueManager().start(new ChargeOrbSpellD(player, ChargeOrbSpellData.CHARGE_AIR_ORB, this));
		return false;
	}
	
	@Override
	public Spellbook getSpellbook() {
		return Spellbook.NORMAL;
	}

}
