package com.zenyte.game.content.event.christmas2019;

import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.Expression;

/**
 * @author Kris | 24/12/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class GeneralWartface extends NPCPlugin {
    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                if (AChristmasWarble.progressedAtLeast(player, AChristmasWarble.ChristmasWarbleProgress.GHOST_OF_CHRISTMAS_PRESENT)) {
                    npc("Stoopid human free me?", Expression.HIGH_REV_SHOCKED);
                } else {
                    npc("..brr.. this Christmas a disaster! brr..", Expression.HIGH_REV_SAD);
                }
            }
        }));
    }

    @Override
    public int[] getNPCs() {
        return new int[] {
                ChristmasConstants.WARTFACE_NPC_ID
        };
    }
}