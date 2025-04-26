package com.zenyte.game.content.event.christmas2019;

import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.Expression;

/**
 * @author Kris | 24/12/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class AdvertiserPartygoer extends NPCPlugin {
    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                if (AChristmasWarble.progressedAtLeast(player, AChristmasWarble.ChristmasWarbleProgress.EVENT_COMPLETE)) {
                    npc("Thank you for saving Christmas, " + player.getName() + ".", Expression.HIGH_REV_HAPPY);
                } else {
                    npc("This Christmas is a disaster! Could you come and help the Queen of Snow with her Christmas feast?" +
                            " Hop in the cupboard if you're willing to help us.", Expression.HIGH_REV_SAD);
                }
            }
        }));
    }

    @Override
    public int[] getNPCs() {
        return new int[] {
                ChristmasConstants.PARTYGOER_EVENT_ADVERTISER
        };
    }
}
