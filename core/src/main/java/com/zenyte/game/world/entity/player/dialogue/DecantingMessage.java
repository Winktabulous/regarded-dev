package com.zenyte.game.world.entity.player.dialogue;

import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.var.VarCollection;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tommeh | 16-3-2019 | 17:20
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class DecantingMessage implements Message {

    private final Map<Integer, Runnable> actions = new HashMap<>();

    public void onClick(final Player player, final int option) {
        final Runnable action = actions.get(option);
        if (action == null) {
            return;
        }
        action.run();
    }

    public DecantingMessage onDose(final int dose, final Runnable runnable) {
        actions.put(dose, runnable);
        return this;
    }

    @Override
    public void display(Player player) {
        VarCollection.DIALOGUE_FULL_SIZE.send(player, 0);
        player.getInterfaceHandler().sendInterface(InterfacePosition.DIALOGUE, 582);
    }

    public DecantingMessage() {
    }

}
