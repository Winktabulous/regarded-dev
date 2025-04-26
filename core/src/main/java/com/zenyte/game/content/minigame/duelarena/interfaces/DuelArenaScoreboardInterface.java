package com.zenyte.game.content.minigame.duelarena.interfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.minigame.duelarena.Duel;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 30-11-2018 | 18:06
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class DuelArenaScoreboardInterface extends Interface {

    private static final int START_HISTORY_INDEX = 71;

    @Override
    protected void attach() {
        put(16, "Duels Won");
        put(17, "Duels Lost");
        put(20, "Scoreboard");
    }

    @Override
    public void open(Player player) {
        int index = START_HISTORY_INDEX + 1;
        player.getInterfaceHandler().sendInterface(getInterface());
        for (final String entry : Duel.getDuelHistory()) {
            player.getPacketDispatcher().sendComponentText(getInterface(), --index, entry);
        }
        while (--index >= 0) {
            player.getPacketDispatcher().sendComponentText(getInterface(), index, "");
        }
        player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Duels Won"), "My Wins: " + player.getNumericAttribute("DuelsWon").intValue());
        player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Duels Lost"), "My Losses: " + player.getNumericAttribute("DuelsLost").intValue());
        player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Scoreboard"), "Scoreboard");
    }

    @Override
    protected void build() {
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.DUEL_SCOREBOARD;
    }
}
