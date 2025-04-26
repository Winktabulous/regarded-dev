package com.zenyte.game.content.minigame.barrows;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.packet.PacketDispatcher;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 21/10/2018 10:07
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BarrowsPuzzleInterface extends Interface {
    @Override
    protected void attach() {
        put(3, "Option 0");
        put(5, "Option 1");
        put(7, "Option 2");
        put(13, "Answer option 0");
        put(15, "Answer option 1");
        put(17, "Answer option 2");
        put(12, "Select option 0");
        put(14, "Select option 1");
        put(16, "Select option 2");
    }

    @Override
    public void open(Player player) {
        if (!player.inArea("Barrows chambers")) {
            throw new RuntimeException("Player " + player + " attempting to open barrows puzzle outside of chambers.");
        }
        final BarrowsPuzzle puzzle = player.getBarrows().getPuzzle();
        player.getInterfaceHandler().sendInterface(getInterface());
        final PacketDispatcher dispatcher = player.getPacketDispatcher();
        for (int i = 0; i < BarrowsPuzzle.OPTIONS_SIZE; i++) {
            dispatcher.sendComponentModel(getInterface(), getComponent("Option " + i), puzzle.getSequenceModel(i));
            dispatcher.sendComponentModel(getInterface(), getComponent("Answer option " + i), puzzle.getModel(i));
        }
    }

    @Override
    protected void build() {
        bind("Select option 0", player -> player.getBarrows().getPuzzle().select(0));
        bind("Select option 1", player -> player.getBarrows().getPuzzle().select(1));
        bind("Select option 2", player -> player.getBarrows().getPuzzle().select(2));
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.BARROWS_PUZZLE;
    }
}
