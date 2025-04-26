package com.zenyte.game.content.pyramidplunder.npc;

import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

public class SnakeCharmer extends NPCPlugin {
    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                player("Wow a snake charmer. Can I have a go? Can I have a go? Please?");
                plain("The snake charmer fails to acknowledge you, he seems too deep into the music to notice you.");
            }
        }));
    }

    @Override
    public int[] getNPCs() {
        return new int[]{NpcId.ALI_THE_SNAKE_CHARMER};
    }
}
