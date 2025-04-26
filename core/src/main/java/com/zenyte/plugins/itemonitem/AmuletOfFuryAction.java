package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.ItemChat;

public final class AmuletOfFuryAction implements ItemOnItemAction {

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {

        if(from.getId() == ItemId.AMULET_OF_FURY || from.getId() == ItemId.BLOOD_SHARD &&
                to.getId() == ItemId.AMULET_OF_FURY || to.getId() == ItemId.BLOOD_SHARD) {

            Item item = new Item(ItemId.AMULET_OF_BLOOD_FURY);
            item.setCharges(10_000);
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    item(item, "If you create an Amulet of blood fury you will be able to revert it, but you will not get your Blood shard back.");
                    options("Create an <col=00080>" + item.getName() + "</col>?", "Yes, I'm " +
                            "sure.", "No.").onOptionOne(() -> {
                        player.getDialogueManager().start(new ItemChat(player, item, "You fuse the Blood shard with your Amulet of fury, to create an Amulet of blood fury."));
                        player.getInventory().deleteItem(to);
                        player.getInventory().deleteItem(from);
                        player.getInventory().addItem(item);
                        setKey(5);
                    });
                    item(5, item, "You fuse your Amulet of fury with your Blood shard creating a <col=00080>" + item.getName() + "</col>.");
                }
            });
        }
    }

    @Override
    public int[] getItems() {
        return new int[]{ ItemId.AMULET_OF_FURY, ItemId.BLOOD_SHARD };
    }
}
