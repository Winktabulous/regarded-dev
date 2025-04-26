package com.zenyte.game.content.boss.dagannothkings;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.ItemRetrievalService;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import mgi.utilities.StringFormatUtil;

/**
 * @author Kris | 19/06/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class HagavikNPC extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                npc("Hello there adventurer!");
                options(new DialogueOption("Who are you?", key(100)), new DialogueOption("What are you doing here?", key(200)), new DialogueOption("What is this crack in the floor?", key(300)), new DialogueOption("Nothing.", key(400)));
                player(100, "Who are you?");
                npc("I'm Hagavik, Askeladden's brother.").executeAction(key(2));
                player(200, "What are you doing here?");
                npc("I collect the stuff from down the hole when adventurers fall and offer to sell the items back to them if they want.");
                npc("It's how I make my living.").executeAction(key(2));
                player(300, "What is this crack in the floor?");
                npc("It's a surface crack into the lair of the Dagannoth kings. Down below are some nasty creatures.");
                npc("People can make an instance of the lair using the crack, if they so prefer. It'll cost them " + StringFormatUtil.format(DagannothKingsInstanceConstants.COST) + " coins to do so.");
                npc("However if they die within that instance, I'll be collecting their items. If they want them back, they have to pay a hefty price of 100,000 coins to me.").executeAction(key(2));
                player(400, "Nothing.");
            }
        }));
        bind("Collect", (player, npc) -> {
            if (player.getRetrievalService().getType() != ItemRetrievalService.RetrievalServiceType.HAGAVIK || player.getRetrievalService().getContainer().isEmpty()) {
                player.getDialogueManager().start(new Dialogue(player, npc) {

                    @Override
                    public void buildDialogue() {
                        npc("There's nothing to collect at this time.");
                    }
                });
                return;
            }
            GameInterface.ITEM_RETRIEVAL_SERVICE.open(player);
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.HAGAVIK };
    }
}
