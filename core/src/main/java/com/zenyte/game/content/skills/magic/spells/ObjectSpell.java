package com.zenyte.game.content.skills.magic.spells;

import com.zenyte.game.content.skills.magic.SpellDefinitions;
import com.zenyte.game.content.skills.magic.SpellState;
import com.zenyte.game.content.skills.magic.spells.lunar.SpellbookSwap;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 9. jaan 2018 : 17:51.23
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public interface ObjectSpell extends MagicSpell {
    boolean spellEffect(final Player player, final WorldObject object);

    default void execute(final Player player, final WorldObject object) {
        if (!canCast(player)) {
            player.sendMessage("You cannot cast that spell on this spellbook.");
            return;
        }
        if (!canUse(player)) {
            return;
        }
        final SpellDefinitions definitions = SpellDefinitions.SPELLS.get(getSpellName());
        if (definitions == null) {
            return;
        }
        final int level = definitions.getLevel();
        final Item[] runes = definitions.getRunes();
        final long spellDelay = player.getNumericTemporaryAttribute("spellDelay").longValue();
        if (spellDelay > Utils.currentTimeMillis()) {
            return;
        }
        if (player.isLocked()) {
            return;
        }
        final SpellState state = new SpellState(player, level, runes);
        if (!state.check()) {
            return;
        }
        player.getInterfaceHandler().closeInterfaces();
        if (spellEffect(player, object)) {
            player.setLunarDelay(getDelay());
            state.remove();
            SpellbookSwap.checkSpellbook(player);
        }
    }
}
