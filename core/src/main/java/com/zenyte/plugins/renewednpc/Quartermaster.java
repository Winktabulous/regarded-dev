package com.zenyte.plugins.renewednpc;

import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 21/05/2019 22:06
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Quartermaster extends NPCPlugin {
    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            player.getDialogueManager().start(new Dialogue(player, npc) {
                @Override
                public void buildDialogue() {
                    npc("Hello. What can I do for you today?");
                    options(TITLE, new DialogueOption("Do you have anything for sale?", key(5)), new DialogueOption("Nothing.", key(50)));
                    player(5, "Do you have anything for sale?");
                    npc("Of course! I have a wide variety of halberds in store. Please, have a look!").executeAction(() -> player.openShop("Quartermaster's Stores"));
                    player(50, "Nothing.");
                }
            });
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] {
                3438
        };
    }
}
