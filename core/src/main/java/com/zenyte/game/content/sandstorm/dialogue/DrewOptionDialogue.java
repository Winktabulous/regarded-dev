package com.zenyte.game.content.sandstorm.dialogue;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import org.jetbrains.annotations.NotNull;

/**
 * @author Chris
 * @since August 20 2020
 */
public class DrewOptionDialogue extends Dialogue {
    public DrewOptionDialogue(@NotNull final Player player, @NotNull final NPC npc) {
        super(player, npc);
    }

    @Override
    public void buildDialogue() {
        options(TITLE,
                new DialogueOption("Tell me about yourself.", () -> player.getDialogueManager().start(new DrewIntroductionDialogue(player, npc))),
                new DialogueOption("Deposit buckets.", () -> player.getDialogueManager().start(new DrewDepositDialogue(player, npc))),
                new DialogueOption("Withdraw buckets of sand.", () -> player.getDialogueManager().start(new DrewClaimDialogue(player, npc))),
                new DialogueOption("Check my buckets and sand.", () -> player.getDialogueManager().start(new DrewCheckDialogue(player, npc))),
                new DialogueOption("Nothing.")
        );
    }
}
