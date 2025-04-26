package com.zenyte.game.content.skills.magic;

import com.zenyte.game.content.skills.magic.spells.MagicSpell;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.area.plugins.TempPlayerStatePlugin;
import com.zenyte.utils.TextUtils;

import java.util.Map;

/**
 * @author Kris | 15. juuli 2018 : 21:02:04
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */

public enum Spellbook implements TempPlayerStatePlugin.State {

	NORMAL(Magic.regularSpells),
	ANCIENT(Magic.ancientSpells),
	LUNAR(Magic.lunarSpells),
	ARCEUUS(Magic.arceuusSpells);

	public static final Spellbook[] VALUES = values();

    private final Map<String, MagicSpell> spellCollection;

    Spellbook(Map<String, MagicSpell> spellCollection) {
        this.spellCollection = spellCollection;
    }

    public void refresh(Player player) {
        player.getVarManager().sendBit(4070, ordinal());
    }

	public static final Spellbook getSpellbook(final int ordinal) {
		if (ordinal < 0 || ordinal >= VALUES.length) {
			return NORMAL;
        }
        return VALUES[ordinal];
    }

    @Override
    public String toString() {
        return TextUtils.capitalizeFirstCharacter(name().toLowerCase());
    }

    public Map<String, MagicSpell> getSpellCollection() {
        return spellCollection;
    }

    @Override
    public TempPlayerStatePlugin.StateType tempType() {
        return TempPlayerStatePlugin.StateType.SPELLBOOK;
    }
}
