package com.zenyte.game.content.skills.magic.lecterns;

import com.zenyte.game.content.skills.magic.SpellState;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;

/**
 * @author Kris | 03/09/2019 08:06
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
final class TabletCreation extends Action {
    private static final Animation anim = new Animation(4067);
    static final int CLAY = 1761;
    static final int DARK_ESSENCE_BLOCK = 13446;

    TabletCreation(final LecternTablet tab, final int amount) {
        this.tab = tab;
        this.amount = amount;
    }

    private final LecternTablet tab;
    private int amount;

    @Override
    public boolean start() {
        return true;
    }

    @Override
    public boolean process() {
        return true;
    }

    @Override
    public int processWithDelay() {
        if (amount-- <= 0) {
            return -1;
        }
        final SpellState spell = new SpellState(player, tab.getLevel(), tab.getRunes());
        if (!spell.check(true)) {
            return -1;
        }
        spell.remove();
        player.setAnimation(anim);
        player.getSkills().addXp(SkillConstants.MAGIC, tab.getExperience());
        player.getInventory().addOrDrop(tab.getTab());
        return 5;
    }
}
