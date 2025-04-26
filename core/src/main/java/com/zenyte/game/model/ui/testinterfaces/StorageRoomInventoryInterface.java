package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import it.unimi.dsi.fastutil.ints.IntList;

/**
 * @author Kris | 03/07/2022
 */
public class StorageRoomInventoryInterface extends Interface {

    @Override
    protected void attach() {
        put(0, "Items");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(getInterface());
        player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Items"), 0, 27,
                AccessMask.CLICK_OP1, AccessMask.CLICK_OP10, AccessMask.DRAG_DEPTH1, AccessMask.DRAG_TARGETABLE);
        player.getPacketDispatcher().sendClientScript(149, 44171264, 93, 4, 7, 1, -1, "Store<col=ff9040>", "", "", "", "");
    }

    @Override
    protected void build() {
        bind("Items", (player, slotId, itemId, option) -> {
            if (player.getVarManager().getBitValue(StorageRoomInterface.DEPOSIT_TYPE_VARBIT) == 0) {
                final Item item = player.getInventory().getContainer().get(slotId);
                if (item == null) return;
                if (player.getStorageRoom().getContainer().getAmountOf(item.getId()) > 0) {
                    player.sendMessage("You already have that item in your storage unit.");
                    return;
                }

                final var categoryItems = StorageRoomInterface.itemSetsByCategory.get(StorageRoomInterface.getType(player).getEnumId());

                if (categoryItems == null) {
                    return;
                }

                final IntList items = categoryItems.get(item.getId());
                if (items == null) {
                    player.sendMessage("This item does not belong here.");
                    return;
                }

                var storageContainer = player.getStorageRoom().getContainer();
                var type  = (StorageType) player.getTemporaryAttributes().get("storage_room_type");

                player.getInventory().getContainer().withdraw(player, player.getStorageRoom().getContainer(), slotId, item.getAmount());
            } else {
                final Item itemSelected = player.getInventory().getContainer().get(slotId);
                if (itemSelected == null) return;

                final var categoryItems = StorageRoomInterface.itemSetsByCategory.get(StorageRoomInterface.getType(player).getEnumId());
                if (categoryItems == null) return;

                final IntList items = categoryItems.get(itemSelected.getId());
                if (items == null) return;
                for (int itemInCategoryId : items) {
                    if (player.getStorageRoom().getContainer().getAmountOf(itemInCategoryId) > 0) {
                        continue;
                    }
                    final int slotInInventory = player.getInventory().getContainer().getSlotOf(itemInCategoryId);
                    if (slotInInventory == -1) continue;
                    final Item item = player.getInventory().getItem(slotInInventory);
                    player.getInventory().getContainer().withdraw(player, player.getStorageRoom().getContainer(), slotInInventory, item.getAmount());
                }
            }
            player.getPacketDispatcher().sendUpdateItemContainer(32768 + ContainerType.STORAGE_ROOM.getId(), -1, 0, player.getStorageRoom().getContainer());
            player.getInventory().refreshAll();
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.STORAGE_ROOM_INV;
    }
}
