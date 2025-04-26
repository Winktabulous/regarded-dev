package com.zenyte.plugins.renewednpc;

import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 15/04/2019 22:14
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MawnisBurowgar extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            if (TreasureTrail.talk(player, npc)) {
                return;
            }
            player.getDialogueManager().start(new Dialogue(player, npc) {

                @Override
                public void buildDialogue() {
                    npc("Greetings, " + player.getName() + ".");
                    player("Hello.");
                    npc("What may I help you with today?");
                    options(TITLE, new DialogueOption("Could you sell me your helmet?", key(10)), new DialogueOption("Nothing.", key(200)));
                    player(200, "Nothing, I'm fine.");
                    player(10, "Could you sell me your helmet?");
                    npc("Certainly. That'll cost you 50,000 coins.");
                    if (!player.getInventory().containsItem(995, 50000)) {
                        player("I'm afraid I haven't got that much.");
                        npc("Well I'm not giving it away.");
                    } else {
                        options("Purchase a helm of neitiznot for 50,000 coins?", new DialogueOption("Purchase it.", key(50)), new DialogueOption("No.", key(100)));
                        player(50, "I'll take it.").executeAction(() -> {
                            player.getInventory().ifDeleteItem(new Item(995, 50000), () -> {
                                player.getInventory().addOrDrop(new Item(10828));
                                doubleItem(51, new Item(995, 50000), new Item(10828), "You purchase a helm of neitiznot for 50,000 coins.");
                                setKey(51);
                            });
                        });
                        doubleItem(new Item(995, 50000), new Item(10828), "You need 50,000 coins to do that.");
                        player(100, "On second thought, I think I'm fine.");
                    }
                }
            });
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { 2979, NpcId.MAWNIS_BUROWGAR };
    }
}
