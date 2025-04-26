package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 16/04/2019 16:57
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SkillsTabInterface extends Interface {

    private static final int[] SKILL_BUTTON_VARPS = new int[] { 0, 1, 2, 5, 3, 7, 4, 12, 22, 6, 8, 9, 10, 11, 19, 20, 23, 13, 14, 15, 16, 17, 18, 21 };

    @Override
    protected DefaultClickHandler getDefaultHandler() {
        return (player, componentId, slotId, itemId, optionId) -> {
            if (optionId == 1) { // if player is on mobile and clicking skill to see current exp
                return;
            }
            if (player.isLocked()) {
                return;
            }
            if (player.isUnderCombat()) {
                player.sendMessage("You can't do this while in combat.");
                return;
            }
            player.getSkills().sendSkillMenu(SKILL_BUTTON_VARPS[componentId], 0);
        };
    }

    @Override
    protected void attach() {

    }

    @Override
    public void open(final Player player) {
        player.getInterfaceHandler().sendInterface(this);
    }

    @Override
    protected void build() {

    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.SKILLS_TAB;
    }
}
