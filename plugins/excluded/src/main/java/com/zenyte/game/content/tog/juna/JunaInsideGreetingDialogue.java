package com.zenyte.game.content.tog.juna;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import org.jetbrains.annotations.NotNull;

/**
 * @author Chris
 * @since September 14 2020
 */
public class JunaInsideGreetingDialogue extends Dialogue {
    public JunaInsideGreetingDialogue(@NotNull final Player player, final int npcId) {
        super(player, npcId);
    }

    @Override
    public void buildDialogue() {
        npc("I will not permit you to be in the cave for long. Collect as many tears as you can")
                .executeAction(() -> player.getDialogueManager().start(new JunaInsideOptionDialogue(player, npcId)));
    }
}
