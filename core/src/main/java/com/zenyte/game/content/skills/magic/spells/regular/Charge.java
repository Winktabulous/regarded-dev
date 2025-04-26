package com.zenyte.game.content.skills.magic.spells.regular;

import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.magic.spells.DefaultSpell;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.variables.TickVariable;

/**
 * @author Kris | 8. jaan 2018 : 2:11.57
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class Charge implements DefaultSpell {

	private static final Animation ANIM = new Animation(811);
	private static final Graphics GFX = new Graphics(111, 0, 100);

	@Override
	public int getDelay() {
		return 1000;
	}

	@Override
	public boolean spellEffect(final Player player, final int optionId, final String option) {
		if (player.getNumericTemporaryAttribute("ChargeSpellUseDelay").longValue() > Utils.currentTimeMillis()) {
			player.sendMessage("You can't recast that yet, your current Charge is too strong.");
			return false;
		}
		player.getVariables().schedule(600, TickVariable.CHARGE);
		player.getTemporaryAttributes().put("ChargeSpellUseDelay", Utils.currentTimeMillis() + 60000);
		this.addXp(player, 180);
		player.setAnimation(ANIM);
		player.setGraphics(GFX);
		player.sendMessage("<col=ef1020>You feel charged with magic power.</col>");
		return true;
	}
	
	@Override
	public Spellbook getSpellbook() {
		return Spellbook.NORMAL;
	}

}
