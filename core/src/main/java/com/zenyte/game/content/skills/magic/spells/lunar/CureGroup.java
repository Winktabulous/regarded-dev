package com.zenyte.game.content.skills.magic.spells.lunar;

import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.magic.spells.DefaultSpell;
import com.zenyte.game.model.ui.testinterfaces.advancedsettings.SettingVariables;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.Toxins.ToxinType;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;

import java.util.List;

/**
 * @author Kris | 16. veebr 2018 : 20:55.41
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class CureGroup implements DefaultSpell {
	private static final Animation ANIM = new Animation(4411);
	private static final Graphics GFX = new Graphics(736, 0, 92);
	private static final SoundEffect castSound = new SoundEffect(2886, 5, 67);
	private static final SoundEffect areaSound = new SoundEffect(2889, 5, 51);

	@Override
	public int getDelay() {
		return 3500;
	}

	@Override
	public boolean spellEffect(final Player player, final int optionId, final String option) {
		if (!hasDefenceRequirement(player)) {
			return false;
		}
		int count = 0;
		final List<Player> characters = player.findCharacters(1, Player.class, p2 -> p2 != player && !p2.isDead() && p2.getVarManager().getBitValue(SettingVariables.ACCEPT_AID_VARBIT_ID) == 1 && p2.getDuel() == null && (p2.getToxins().isPoisoned() || p2.getToxins().isVenomed()));
		for (int i = characters.size() - 1; i >= 0; i--) {
			final Player p2 = characters.get(i);
			p2.setGraphics(GFX);
			World.sendSoundEffect(p2, areaSound);
			p2.getToxins().cureToxin(ToxinType.POISON);
			p2.sendMessage("You have been cured of all illnesses!");
			count++;
		}
		if (count > 0) {
			player.sendMessage("The spell affected " + count + " nearby people.");
			this.addXp(player, 74);
			player.setAnimation(ANIM);
			World.sendSoundEffect(player, castSound);
			return true;
		}
		player.sendMessage("There are no players around you whom you can cure.");
		return false;
	}

	@Override
	public Spellbook getSpellbook() {
		return Spellbook.LUNAR;
	}
}
