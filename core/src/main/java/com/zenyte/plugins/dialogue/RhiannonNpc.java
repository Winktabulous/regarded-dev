package com.zenyte.plugins.dialogue;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

public class RhiannonNpc extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-To", (player, npc) -> player.getDialogueManager().start(new RhiannonD(player, npc)));
    }

    @Override
    public int[] getNPCs() {
        return new int[] {NpcId.RHIANNON};
    }

    private static final class RhiannonD extends Dialogue {

        public RhiannonD(Player player, NPC npc) {
            super(player, npc);
        }

        @Override
        public void buildDialogue() {
            player("Hello.");
            npc("Oh, hello adventurer. Can I help you with anything?");
            options(new DialogueOption("What is this place?", key(100)), new DialogueOption("What can I do here?", key(200)));

            player(100, "What is this place?");
            npc("This is the prison of Zalcano");
            player("Zalcano?");
            npc("That's right. She's a powerful demon, one bearing an ancient curse.");
            player("A curse? That doesn't sound good.");
            npc("Don't worry, the curse only affects her. Because of it, her entire body has been turned to stone.");
            player("I see. So why is she here?");
            npc("She came to our lands long ago, before the curse had fully taken hold. We imprisoned her here for study, so that we might learn more about her affliction.");
            npc("If you want to learn more, you should be able to find a book in the Grand Library about her.");
            player("How insightful, thanks you.");

            player(200, "What can I do here?");
            npc("Well we could always use some help in keeping Zalcano under control.");
            player("How would I do that?");
            npc("Zalcano is formed completely from stone. Because of this, normal weapons won't work on her.");
            npc("That said, when she's vulnerable, you can still do some damage to her if you use a pickaxe.");
            player("When she's vulnerable? How do I make her vulnerable?");
            npc("The ore found within these caverns can be rather volatile, especially when heated. We've found that when imbued, this ore can damage her armour.");
            npc("You'll want to head on in and mine the ore from the rocks around the prison. Once you have some, you can heat it using the furnace and then imbue it using the altar.");
            npc("Once you've got some imbued ore, just give it a throw her way and she'll definitely feel it.");
            player("Oh... wow alright. Thank you.");
        }

    }
}