package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 09/09/2019 14:58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class VorkathsHeadOnCape implements PairedItemOnItemPlugin {
    @Override
    public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
        final Item head = from.getId() == ItemId.VORKATHS_HEAD_21907 ? from : to;
        final Item cape = from == head ? to : from;
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                if (cape.getNumericAttribute("vorkath head effect").intValue() != 0) {
                    plain("Your " + cape.getName().toLowerCase() + " is already imbued with a vorkath's head.");
                    return;
                }
                doubleItem(head, cape, "Imbuing the cape with the Vorkath's head will consume the head and make the " +
                        "cape unstackable. Losing the cape means losing the effect. Do you wish to continue?");
                options("Imbue the cape?", new DialogueOption("Yes", () -> {
                    final Inventory inventory = player.getInventory();
                    if (inventory.getItem(fromSlot) != from || inventory.getItem(toSlot) != to) {
                        return;
                    }
                    inventory.deleteItem(head);
                    cape.setAttribute("vorkath head effect", 1);
                }), new DialogueOption("No"));
            }
        });
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {ItemPair.of(ItemId.VORKATHS_HEAD_21907, ItemId.RANGING_CAPE), ItemPair.of(ItemId.VORKATHS_HEAD_21907, ItemId.RANGING_CAPET), ItemPair.of(ItemId.VORKATHS_HEAD_21907, ItemId.MAX_CAPE_13342)};
    }
}

