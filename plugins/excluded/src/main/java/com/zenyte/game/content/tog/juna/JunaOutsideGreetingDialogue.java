package com.zenyte.game.content.tog.juna;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import org.jetbrains.annotations.NotNull;

/**
 * @author Chris
 * @since September 07 2020
 */
public class JunaOutsideGreetingDialogue extends Dialogue {
    public JunaOutsideGreetingDialogue(@NotNull final Player player, final int npcId) {
        super(player, npcId);
    }

    @Override
    public void buildDialogue() {
        npc("Tell me... why are you here...").executeAction(() -> player.getDialogueManager().start(new JunaOutsideOptionDialogue(player, npcId)));
    }
}
