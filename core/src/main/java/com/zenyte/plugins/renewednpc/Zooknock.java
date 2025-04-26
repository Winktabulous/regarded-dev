package com.zenyte.plugins.renewednpc;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.DoubleItemChat;
import com.zenyte.plugins.dialogue.ItemChat;

/**
 * @author Kris | 24/04/2019 23:23
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Zooknock extends NPCPlugin implements ItemOnNPCAction {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                npc("Hey, you came to save me!");
                npc("Are you interested in making greegrees? " + "I'll do it for 100k coins and a monkey talisman. " + "Show me the talisman if you're interested.");
            }
        }));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.ZOOKNOCK };
    }

    @Override
    public void handleItemOnNPCAction(final Player player, final Item item, final int slot, final NPC npc) {
        player.getDialogueManager().start(new Dialogue(player) {

            @Override
            public void buildDialogue() {
                options(TITLE, new DialogueOption("Ninja monkey greegree", () -> purchase(player, 4024)), new DialogueOption("Gorilla greegree", () -> purchase(player, 4026)), new DialogueOption("Bearded gorilla greegree", () -> purchase(player, 4027)), new DialogueOption("Ancient gorilla greegree", () -> purchase(player, 4028)), new DialogueOption("See more options", key(5)));
                options(5, TITLE, new DialogueOption("Zombie greegree", () -> purchase(player, 4029)), new DialogueOption("Karamjan greegree", () -> purchase(player, 4031)), new DialogueOption("Kruk greegree", () -> purchase(player, 19525)), new DialogueOption("Go back", key(1)));
            }

            private final void purchase(final Player player, final int itemId) {
                player.getDialogueManager().finish();
                if (!player.getInventory().containsItem(995, 100000) || !player.getInventory().containsItem(4023, 1)) {
                    player.getDialogueManager().start(new DoubleItemChat(player, new Item(995, 100000), new Item(4023), "You need 100,000 coins and a monkey talisman to purchase a " + "greegree."));
                    return;
                }
                player.getInventory().deleteItemsIfContains(new Item[] { new Item(995, 100000), new Item(4023) }, () -> {
                    player.getInventory().addOrDrop(new Item(itemId));
                    player.getDialogueManager().start(new ItemChat(player, new Item(itemId), "Zooknock hands you a greegree."));
                });
            }
        });
    }

    @Override
    public Object[] getItems() {
        return new Object[] { 4023 };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 1442 };
    }
}
