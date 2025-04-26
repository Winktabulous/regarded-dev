package com.zenyte.game.content.skills.magic.spells.lunar;

import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.magic.spells.DefaultSpell;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 16. veebr 2018 : 16:32.40
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class NPCContact implements DefaultSpell {

    private static final Animation animation = new Animation(4413);
    private static final Graphics graphics = new Graphics(728, 0, 92);
    private static final SoundEffect sound = new SoundEffect(3618);

	@Override
	public int getDelay() {
		return 6000;
	}

	@Override
	public boolean spellEffect(final Player player, final int optionId, final String option) {
		//this.addXp(player, 63);
		player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 75);
		return false;
	}

	@Override
	public Spellbook getSpellbook() {
		return Spellbook.LUNAR;
	}

	@Override
	public String getSpellName() {
		return "npc contact";
	}

}
