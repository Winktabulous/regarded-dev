package com.zenyte.game.content.event.halloween2019;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Emote;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 03/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class RestlessGhostPlugin extends NPCPlugin {
    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                if (player.getEquipment().getId(EquipmentSlot.AMULET) != ItemId.GHOSTSPEAK_AMULET) {
                    player("I need to pass.");
                    npc("Boooo.");
                    player("What?");
                    npc("Boooooooo!").executeAction(() -> Emote.play(player, "Think"));
                    plain("I can't understand a word he's saying!<br>Perhaps there's something around here that would help me understand him.");
                    return;
                }
                npc("You made it through!?");
                player("What? Oh... The maze.");
                player("That's nothing compared to half of the challenges I've faced!");
                player("Could you excuse me? I need to get into the shed.");
                npc("I will not. This is MY shed!");
                player("But you're dead! Just step aside!");
                npc("No. I can't leave!");
                player("How come?");
                npc("I need to find my remains to finally be able to get peace.");
                player("Your remains?");
                npc("My bones.");
                npc("Jonas has taken my bones and hidden them somewhere within the maze.");
                player("I suppose I could go and look for your remains if it'll help me get into the shed.");
                npc("I'll need you to find them and bury them in my grave over there. It's the only way I'll find peace.");
                player("Alright, seems easy enough. I'll go find your bones.");
            }
        }));
    }

    @Override
    public int[] getNPCs() {
        return new int[] {
                //HalloweenNPC.GHOST.getRepackedNPC()
        };
    }
}
