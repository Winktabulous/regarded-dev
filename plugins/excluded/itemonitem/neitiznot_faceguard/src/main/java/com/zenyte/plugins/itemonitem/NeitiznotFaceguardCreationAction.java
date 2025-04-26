package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.ItemChat;

/**
 * @author Joe
 */
public final class NeitiznotFaceguardCreationAction implements ItemOnItemAction {
    private static final int faceguardItemId = ItemId.NEITIZNOT_FACEGUARD;
    private final int helmOfNeitiznotItemId = ItemId.HELM_OF_NEITIZNOT;

    @Override
    public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
        final Item helmOfNeitiznot = from.getId() == helmOfNeitiznotItemId ? from : to;
        final Item basiliskJaw = helmOfNeitiznot == from ? to : from;

        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                options("Attach the jaw to the helmet?", "Yes.", "No.").onOptionOne(() -> {
                    final Inventory inventory = player.getInventory();
                    inventory.deleteItem(fromSlot, from);
                    inventory.deleteItem(toSlot, to);
                    Item faceguard = new Item(faceguardItemId);
                    inventory.addItem(faceguard);
                    player.getDialogueManager().start(new ItemChat(player, faceguard, "You attach the Basilisk Jaw to the Helm of Neitiznot."));
                });
            }
        });
    }

    @Override
    public int[] getItems() {
        return null;
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        int basiliskJawItemId = ItemId.BASILISK_JAW;
        return new ItemPair[]{new ItemPair(helmOfNeitiznotItemId, basiliskJawItemId)};
    }
}
