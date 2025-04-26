package com.zenyte.plugins.renewednpc;

import com.zenyte.game.content.sailing.Sailing;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 25/11/2018 16:42
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PortVeos extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                player("Hello Veos.");
                npc("Hello there. What can I do for you?");
                options(TITLE, new DialogueOption("Can you take me somewhere?", () -> setKey(100000)), new DialogueOption("Tell me more about Great Kourend.", () -> setKey(200000)), new DialogueOption("Tell me more about Zeah.", () -> setKey(300000)), new DialogueOption("Nothing.", () -> setKey(400000)));
                player(100000, "Can you take me somewhere?");
                npc("Where would you like to go?");
                options(TITLE, new DialogueOption("Travel to Piscarilius House.", () -> setKey(110000)), new DialogueOption("Travel to Land's End.", () -> setKey(120000)), new DialogueOption("Stay where you are.", () -> setKey(130000)));
                player(110000, "I'd like to travel to Piscarilius House, please.");
                npc("As you wish, I hope you don't get seasick, it is a long voyage.").executeAction(() -> Sailing.sailZeah(player, "Piscarilius House"));
                player(120000, "I'd like to travel to Land's End, please.");
                npc("As you wish, I hope you don't get seasick, it is a long voyage.").executeAction(() -> Sailing.sailZeah(player, "Land's End"));
                player(130000, "Actually I'd like to stay here.");
                npc("As you wish.");
                player(200000, "Can you tell me more about Great Kourend?");
                npc("Great Kourend is a magnificent kingdom comprising of five districts called Houses. The houses are ruled by Lords of Arceuus, Lovakengj, Shayzien, Piscarilius and Hosidius.");
                npc("Since our last King died 20 years ago the kingdom has been ruled by an elected council.");
                options(TITLE, new DialogueOption("That's great, can you take me there please?", () -> setKey(210000)), new DialogueOption("Tell me more about Zeah.", () -> setKey(300000)), new DialogueOption("Good bye.", () -> setKey(230000)));
                player(210000, "That's great, can you take me there please?");
                npc("Certainly, which port would you like to travel to?");
                options(TITLE, new DialogueOption("Travel to Piscarilius House.", () -> setKey(110000)), new DialogueOption("Travel to Land's End.", () -> setKey(120000)), new DialogueOption("Stay where you are.", () -> setKey(130000)));
                player(230000, "Good bye.");
                player(300000, "Tell me more about Zeah.");
                npc("Zeah is a huge continent far to the west of here full of new adventures and hidden treasures. Sadly, the kingdom of Great Kourend is besieged by lizard creatures and it is not possible to get our of the");
                npc("kingdom just yet.");
                options(TITLE, new DialogueOption("That's great, can you take me there please?", () -> setKey(210000)), new DialogueOption("Tell me more about Great Kourend.", () -> setKey(200000)), new DialogueOption("Good bye.", () -> setKey(230000)));
                player(400000, "Nothing.");
            }
        }));
        bind("Port Sarim", (player, npc) -> Sailing.sailZeah(player, "Port Sarim"));
        bind("Land's End", (player, npc) -> Sailing.sailZeah(player, "Land's End"));
        bind("Port Piscarilius", (player, npc) -> Sailing.sailZeah(player, "Piscarilius House"));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.VEOS_10726, NpcId.VEOS_8630 };
    }
}
