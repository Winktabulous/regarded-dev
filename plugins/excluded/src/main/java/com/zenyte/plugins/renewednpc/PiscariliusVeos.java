package com.zenyte.plugins.renewednpc;

import com.zenyte.game.content.sailing.Sailing;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 25/11/2018 19:56
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PiscariliusVeos extends NPCPlugin {
    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                npc("Hello again, Starrify! What can I do for you?");
                options(TITLE, new DialogueOption("Where am I exactly?", () -> setKey(100000)), new DialogueOption("Can you take me somewhere?", () -> setKey(200000)));
                player(100000, "Where am I exactly?");
                npc("This is the port of " + Colour.RED + "Piscarilius" + Colour.END + " house. Home to Kourend's finest fisherman but beware of the thieves!");
                player("Can you tell me about the other houses?");
                npc("Certainly. To the west is the house of " + Colour.RED + "Arceuus" + Colour.END + ", where they study magic at Kourend's Grand Library.");
                npc("Further west is " + Colour.RED + "Lovakengj" + Colour.END + " house, where you'll find our best miners and smithers working to arm the soldiers of the " + Colour.RED + "Shayzien" + Colour.END + " house to the south east.");
                npc("Finally, to the south you'll find " + Colour.RED + "Hosidius" + Colour.END + " house where they like to focus on agriculture to provide food for all of Great Kourend.");
                player("Thanks.");

                player(200000, "Can you take me somewhere?");
                npc("Where would you like to go?");
                options(TITLE, new DialogueOption("Travel to Port Sarim.", () -> setKey(110000)), new DialogueOption("Travel to Land's End.", () -> setKey(120000)), new DialogueOption("Stay where you are.", () -> setKey(130000)));
                player(110000, "I'd like to travel to Port Sarim, please.");
                npc("As you wish, I hope you don't get seasick. It is a long voyage.").executeAction(() -> Sailing.sailZeah(player, "Port Sarim"));
                player(120000, "I'd like to travel to Land's End, please.");
                npc("As you wish.").executeAction(() -> Sailing.sailZeah(player, "Land's End"));
                player(130000, "Actually I'd like to stay here.");
                npc("As you wish.");
            }
        }));
        bind("Travel", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                options(TITLE, new DialogueOption("Travel to Port Sarim.", () -> Sailing.sailZeah(player, "Port Sarim")), new DialogueOption("Travel to Land's End.", () -> Sailing.sailZeah(player, "Land's End")), new DialogueOption("Stay where you are.", () -> setKey(130000)));
                player(130000, "Actually I'd like to stay here.");
                npc("As you wish.");
            }
        }));
    }

    @Override
    public int[] getNPCs() {
        return new int[] {
                2850
        };
    }
}
