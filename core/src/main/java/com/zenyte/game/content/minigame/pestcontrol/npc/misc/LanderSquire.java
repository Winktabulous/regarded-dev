package com.zenyte.game.content.minigame.pestcontrol.npc.misc;

import com.zenyte.game.content.sailing.Sailing;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 27/11/2018 11:24
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class LanderSquire extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                npc("Hi, how can I help you?");
                options(TITLE, new DialogueOption("I'd like to go back to Port Sarim please.", () -> setKey(5)), new DialogueOption("I'm fine thanks.", () -> setKey(10)));
                player(5, "I'd like to go back to Port Sarim please.");
                npc("Ok, but please come back soon and help us.").executeAction(() -> Sailing.sail(player, "the Void Knight outpost", "Port Sarim"));
                player(10, "I'm fine thanks.");
            }
        }));
        bind("Travel", (player, npc) -> Sailing.sail(player, "the Void Knight outpost", "Port Sarim"));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.SQUIRE_1769 };
    }
}
