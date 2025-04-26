package com.zenyte.game.content.event.christmas2019;

import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.Expression;

/**
 * @author Kris | 24/12/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class TinyThom extends NPCPlugin {
    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                if (AChristmasWarble.progressedAtLeast(player, AChristmasWarble.ChristmasWarbleProgress.GHOST_OF_CHRISTMAS_PRESENT)) {
                    npc("Thank you for freeing me, " + player.getName() + ".", Expression.HIGH_REV_NORMAL);
                } else {
                    npc("This Christmas is a disater! Where are the presents?", Expression.HIGH_REV_NORMAL);
                }
            }
        }));
    }

    @Override
    public int[] getNPCs() {
        return new int[] {
                ChristmasConstants.TINY_THOM_NPC_ID
        };
    }
}
