package com.zenyte.game.content.skills.hunter.actions;

import com.zenyte.game.content.skills.hunter.TrapState;
import com.zenyte.game.content.skills.hunter.object.HunterTrap;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author Kris | 29/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public abstract class BuiltHunterTrapAction extends Action {
    protected abstract HunterTrap getTrap();

    protected boolean preconditions(@NotNull final Player player, boolean checkSpace) {
        final HunterTrap trap = getTrap();
        final TrapState state = trap.getState();
        if (state == TrapState.INACTIVE || state == TrapState.REMOVED) {
            return false;
        }
        final Player owner = trap.getPlayer().get();
        if (!Objects.equals(owner, player)) {
            player.sendMessage("This isn't your trap.");
            return false;
        }
        if (!checkSpace) {
            return true;
        }
        return player.getInventory().checkSpace();
    }

    protected boolean isProcessing() {
        return getTrap().getState() == TrapState.PROCESSING;
    }

    protected boolean isCollapsed() {
        return getTrap().getState() == TrapState.COLLAPSED;
    }
}
