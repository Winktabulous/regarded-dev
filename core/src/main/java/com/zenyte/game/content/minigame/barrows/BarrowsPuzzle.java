package com.zenyte.game.content.minigame.barrows;

import com.zenyte.game.GameInterface;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.ints.IntArrayList;

import java.util.Collections;

/**
 * @author Kris | 30/11/2018 12:50
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
class BarrowsPuzzle {

    static final int OPTIONS_SIZE = 3;

    BarrowsPuzzle(final Player player) {
        this.player = player;
        this.options = new IntArrayList(OPTIONS_SIZE);
    }

    private transient final Player player;
    private BarrowsPuzzleType type;
    private int correctSlot;
    private final IntArrayList options;

    void reset() {
        options.clear();
        type = Utils.getRandomElement(BarrowsPuzzleType.values);
        options.addAll(IntArrayList.wrap(type.options));
        Collections.shuffle(options);
        correctSlot = options.indexOf(type.answer);
    }

    void select(int slot) {
        if (slot == correctSlot) {
            player.sendMessage("You hear the doors' locking mechanism grind open.");
            player.getBarrows().setPuzzleSolved(true);
        } else {
            player.sendMessage("You hear the doors' locking mechanism shut down.");
            player.getBarrows().shiftCorner();
            player.getBarrows().shiftDoorways();
            player.getBarrows().refreshDoors();
        }
        player.getInterfaceHandler().closeInterface(GameInterface.BARROWS_PUZZLE);
    }

    int getModel(final int slot) {
        return options.getInt(slot);
    }

    int getSequenceModel(final int slot) {
        return type.sequence[slot];
    }


}
