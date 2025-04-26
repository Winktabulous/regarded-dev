package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.model.item.enums.ImbueableItem;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.RequestResult;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.ItemChat;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import mgi.types.config.items.ItemDefinitions;

/**
 * @author Tommeh | 17-2-2019 | 23:41
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ImbueTokenOnItemAction implements ItemOnItemAction {
    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        boolean fromIsImbueScroll = isImbueScroll(from);
        final Item imbueScroll = fromIsImbueScroll ? from : to;
        final Item item = fromIsImbueScroll ? to : from;
        final ImbueableItem imbueable = ImbueableItem.get(from.getId()) == null ? ImbueableItem.get(to.getId()) : ImbueableItem.get(from.getId());
        if (imbueable == null) {
            player.getDialogueManager().start(new ItemChat(player, imbueScroll, "You can't imbue this item. Try something " +
                    "else."));
            return;
        }
        final Inventory inventory = player.getInventory();
        final String name = ItemDefinitions.get(imbueable.getNormal()).getName();
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                options(Dialogue.TITLE, "Imbue item: "+name, "Cancel").onOptionOne(() -> {
                    final int charges = item.getCharges();
                    final Item imbued = new Item(imbueable.getImbued(), 1, charges);
                    if (inventory.deleteItems(
                            new Item(imbueScroll.getId(), 1),
                            new Item(imbueable.getNormal(), 1)
                    ).getResult() == RequestResult.SUCCESS) {
                        inventory.addItem(imbued);
                        player.sendDeveloperMessage("Successfully imbued item.");
                    } else {
                        player.sendDeveloperMessage("Failed to imbue item.");
                    }
                });
            }
        });
    }

    private static boolean isImbueScroll(Item from) {
        return from.getId() == ItemId.IMBUE_SCROLL || from.getId() == ItemId.SCROLL_OF_IMBUING;
    }

    @Override
    public int[] getItems() {
        final IntArrayList list = new IntArrayList();
        for (final ImbueableItem item : ImbueableItem.values) {
            list.add(item.getNormal());
        }
        list.add(ItemId.IMBUE_SCROLL);
        list.add(ItemId.SCROLL_OF_IMBUING);
        return list.toArray(new int[list.size()]);
    }
}
