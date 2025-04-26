package com.zenyte.game.content.event.halloween2019;

import com.zenyte.game.GameConstants;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.Expression;

/**
 * @author Kris | 01/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CarolPlugin extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                if (HalloweenUtils.isCompleted(player)) {
                    npc("Thank you for helping me again, " + player.getName() + ".");
                    return;
                }
                player("Are you okay?");
                npc("Far from it im afraid. My son, Shilop, were going to go trick or treating around " + GameConstants.SERVER_NAME + ".", Expression.DISTRESSED);
                npc("I took my eyes off of him for one moment and he was gone, a scream was out in the distance, it must have been him.", Expression.DISTRESSED);
                plain("*Carol continues to sob*");
                npc("This was his first halloween. Im worried something had happened to him, Will you help me find him?", Expression.DISTRESSED);
                options(TITLE, new DialogueOption("Of course!", key(100)), new DialogueOption("I have this thing...", key(10)));
                player(100, "Sure, I’ll help. I'm an adventurer after all.");
                npc("Oh, Thank you stranger.");
                player("The name's " + player.getName() + ".");
                player("So, where did the scream come from?");
                npc("It sounded like it was coming from Barbarian Village.").executeAction(() -> HalloweenUtils.setStage(player, HalloweenUtils.SPOKEN_TO_CAROL));
                player("I’ll check it out and see what I find.");
                npc("Thank you, " + player.getName() + ".");
                player(10, "I have this thing I need to attend to right now. I'll be back later.");
                npc("B-b-but wait! I'll reward you!");
                player("Reward me?");
                npc("I have some halloween-themed clothing I was going give to Diango, but they're yours if you'll just help me.", Expression.DISTRESSED);
                options("Help Carol?", new DialogueOption("You're very persuasive.", key(200)), new DialogueOption("I'm sorry, I have to go.", key(300)));
                player(200, "You're very persuasive, you know that?<br>I'll help you out.").executeAction(() -> setKey(101));
                player(300, "I'm sorry, I have to go.");
            }
        }));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.CAROL };
    }
}
