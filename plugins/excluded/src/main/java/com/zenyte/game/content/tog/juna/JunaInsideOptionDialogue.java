package com.zenyte.game.content.tog.juna;

import com.zenyte.game.content.tog.TearsOfGuthixCaveArea;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import org.jetbrains.annotations.NotNull;

/**
 * @author Chris
 * @since September 14 2020
 */
public class JunaInsideOptionDialogue extends Dialogue {
    private static final int GET_OUT_KEY = 5;

    public JunaInsideOptionDialogue(@NotNull final Player player, final int npcId) {
        super(player, npcId);
    }

    @Override
    public void buildDialogue() {
        options(TITLE,
                new DialogueOption("Okay."),
                new DialogueOption("I want to get out now.", key(GET_OUT_KEY))
        );
        player(GET_OUT_KEY, "I want to get out now.").executeAction(() -> TearsOfGuthixCaveArea.remove(player));
    }
}
