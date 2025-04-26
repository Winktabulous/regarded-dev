package com.zenyte.plugins.renewednpc;

import com.zenyte.game.content.sailing.Sailing;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 26/11/2018 19:33
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CaptainMagoro extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                npc("Hello! What can I do for you?");
                options(TITLE, new DialogueOption("Where am I exactly?", () -> setKey(100000)), new DialogueOption("Can you take me to Piscarilius House please?", () -> setKey(200000)), new DialogueOption("Can you take me to Port Sarim please?", () -> setKey(300000)), new DialogueOption("Nothing.", () -> setKey(400000)));
                player(100000, "Where am I exactly?");
                npc("This is the port of " + Colour.RED + "Land's End" + Colour.END + "!");
                player("Can you tell me about the local area?");
                npc("Certainly. Just North-East of here is " + Colour.RED + "Great Kourend" + Colour.END + ".");
                npc("The kingdom is divided into five districts, you'll find the " + Colour.RED + "Shayzien" + Colour.END + " and " + Colour.RED + "Lovakengj" + Colour.END + " houses to the North, and the remaining houses to the North-East.");
                player("Thanks.");
                player(200000, "I'd like to travel to Piscarilius House, please.");
                npc("As you wish.").executeAction(() -> Sailing.sailZeah(player, "Piscarilius House"));
                player(300000, "I'd like to travel to Port Sarim, please.");
                npc("As you wish, I hope you don't get seasick, it is a long voyage.").executeAction(() -> Sailing.sailZeah(player, "Port Sarim"));
                player(400000, "Nothing");
            }
        }));
        bind("Port Sarim", (player, npc) -> Sailing.sailZeah(player, "Port Sarim"));
        bind("Port Piscarilius", (player, npc) -> Sailing.sailZeah(player, "Piscarilius House"));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.CAPTAIN_MAGORO };
    }
}
