package com.zenyte.game.content.sandstorm;

import com.zenyte.game.content.sandstorm.dialogue.DrewCheckDialogue;
import com.zenyte.game.content.sandstorm.dialogue.DrewClaimDialogue;
import com.zenyte.game.content.sandstorm.dialogue.DrewDepositDialogue;
import com.zenyte.game.content.sandstorm.dialogue.DrewGreetingDialogue;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;

/**
 * @author Chris
 * @since August 20 2020
 */
public class Drew extends NPCPlugin {
    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new DrewGreetingDialogue(player, npc)));
        bind("Deposit buckets", (player, npc) -> player.getDialogueManager().start(new DrewDepositDialogue(player, npc)));
        bind("Claim Sand", (player, npc) -> player.getDialogueManager().start(new DrewClaimDialogue(player, npc)));
        bind("Check", (player, npc) -> player.getDialogueManager().start(new DrewCheckDialogue(player, npc)));
    }

    @Override
    public int[] getNPCs() {
        return new int[]{NpcId.DREW};
    }
}
