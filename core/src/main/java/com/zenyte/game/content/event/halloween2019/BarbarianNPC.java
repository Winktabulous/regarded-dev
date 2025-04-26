package com.zenyte.game.content.event.halloween2019;

import com.zenyte.ContentConstants;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.Expression;

/**
 * @author Kris | 01/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BarbarianNPC extends NPCPlugin {
    @Override
    public void handle() {
        if (!ContentConstants.HALLOWEEN) {
            return;
        }
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                npc("Did you hear that scream!?", Expression.ANXIOUS);
                player("What scream?");
                npc("There was a loud scream coming from one of the huts in the village!");
            }
        }));
    }

    @Override
    public int[] getNPCs() {
        return new int[] {
                3055, 3056, 3057, 3058, 3059, 3060, 3061, 3062, 3064, 3065, 3066, 3067, 3068, 3069, 3070, 3071, 3072, 3096, 3102
        };
    }
}
