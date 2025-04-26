package com.zenyte.game.content.treasuretrails.interfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.treasuretrails.clues.PuzzleBox;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;

import java.util.Optional;

/**
 * @author Kris | 20/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PuzzleBoxInterface extends Interface {
    @Override
    protected void attach() {
        put(4, "Puzzle piece");
    }

    @Override
    public void open(Player player) {
        final PuzzleBox puzzleBox = player.getPuzzleBox();
        assert puzzleBox.containsPuzzle() : "A puzzle has not yet been constructed.";
        player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Puzzle piece"), 0, 25, AccessMask.CLICK_OP1);
        player.getInterfaceHandler().sendInterface(this);
        puzzleBox.fullRefresh();
    }

    @Override
    public void close(final Player player, final Optional<GameInterface> replacement) {
        final PuzzleBox puzzleBox = player.getPuzzleBox();
        assert puzzleBox.containsPuzzle() : "A puzzle has not yet been constructed.";
        puzzleBox.checkCompletion();
    }

    @Override
    protected void build() {
        bind("Puzzle piece", (player, slotId, itemId, option) -> player.getPuzzleBox().shiftPuzzle(slotId));
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.PUZZLE_BOX;
    }
}
