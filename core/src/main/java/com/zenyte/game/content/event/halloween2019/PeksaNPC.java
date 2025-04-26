package com.zenyte.game.content.event.halloween2019;

import com.zenyte.ContentConstants;
import com.zenyte.game.GameConstants;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 01/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PeksaNPC extends NPCPlugin {
    @Override
    public void handle() {
        if (!ContentConstants.HALLOWEEN) {
            return;
        }
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                if (HalloweenUtils.getStage(player) != HalloweenUtils.SPOKEN_TO_CAROL) {
                    npc("So you came for high quality helmets after all.");
                    player("What have you got to offer?").executeAction(() -> player.openShop("Helmet Shop"));
                    return;
                }
                npc("So you came for high quality helmets after all.");
                player("Not exactly... Have you seen a boy run through here?");
                npc("Was he with a shifty fellow? Had a weird warrior outfit on, quite the frightening looking fellow.");
                player("Where did they go?");
                npc("Over to the big hut over yonder to the next village.").executeAction(() -> {
                    npc.setFaceEntity(null);
                    npc.setFaceLocation(new Location(3108, 3363, 0));
                    HalloweenUtils.setStage(player, HalloweenUtils.SPOKEN_TO_PEKSA);
                });
                plain("*Peksa Points over to Draynor Manor*").executeAction(() -> npc.setFaceEntity(player));
                player("How long ago?");
                npc("Moments ago, you just missed them.");
                player("Thanks, Peksa.");
                npc("Next time you’re around, come buy a helmet. Best quality in all of " + GameConstants.SERVER_NAME + ".");
                player("I’ll keep that in mind.");
            }
        }));
    }

    @Override
    public int[] getNPCs() {
        return new int[] {
                524
        };
    }
}
