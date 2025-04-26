package com.zenyte.game.content.skills.magic.spells.lunar;

import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.magic.spells.DefaultSpell;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.variables.TickVariable;

/**
 * @author Kris | 17. veebr 2018 : 17:21.42
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class MagicImbue implements DefaultSpell {

	private static final Animation ANIM = new Animation(722);
	private static final Graphics GFX = new Graphics(141, 0, 92);
	private static final SoundEffect sound = new SoundEffect(2888);
	
	@Override
	public int getDelay() {
		return 2000;
	}

	@Override
	public boolean spellEffect(final Player player, final int optionId, final String option) {
		if (player.getVariables().getTime(TickVariable.MAGIC_IMBUE) > 0) {
			player.sendMessage("You've already cast a magic imbue!");
			return false;
		}
		player.sendMessage("You are charged to combine runes!");
		player.getVarManager().sendBit(5438, 1);
		player.getVariables().schedule(20, TickVariable.MAGIC_IMBUE);
		player.setAnimation(ANIM);
		player.sendSound(sound);
		this.addXp(player, 86);
		player.setGraphics(GFX);
		return true;
	}
	
	@Override
	public Spellbook getSpellbook() {
		return Spellbook.LUNAR;
	}

}
