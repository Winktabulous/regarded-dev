package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.VarManager;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import it.unimi.dsi.fastutil.ints.*;
import mgi.types.config.enums.EnumDefinitions;
import mgi.types.config.enums.IntEnum;
import mgi.types.config.enums.StringEnum;
import mgi.types.config.items.ItemDefinitions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Kris | 03/07/2022
 */
public class StorageRoomInterface extends Interface {

    private static final Map<Integer, Map<Integer, IntList>> items = new HashMap<>();
    public static final Map<Integer, IntSet> allItemsByCategory = new HashMap<>();
    public static final Map<Integer, Map<Integer, IntList>> itemSetsByCategory = new HashMap<>();
    private static final Set<Integer> allItems = new HashSet<>();
    public static final int DEPOSIT_TYPE_VARBIT = 10581;

    static {
        VarManager.appendPersistentVarbit(DEPOSIT_TYPE_VARBIT);
        IntEnum variants = EnumDefinitions.getIntEnum(3077);
        IntEnum alternates = EnumDefinitions.getIntEnum(3303);
        StringEnum stringEnum = EnumDefinitions.getStringEnum(3301);
        for (int k : stringEnum.getValues().keySet()) {
            Map<Integer, IntList> subItems = new HashMap<>();
            Map<Integer, IntList> categorizedSubItems = new HashMap<>();
            items.put(k, subItems);
            itemSetsByCategory.put(k, categorizedSubItems);
            final IntSet itemsInCategory = new IntOpenHashSet();
            allItemsByCategory.put(k, itemsInCategory);
            IntEnum intEnum = EnumDefinitions.getIntEnum(k);
            intEnum.getValues().forEach((slot, v) -> {
                final int valuesEnum = variants.getValue(v).orElse(-1);
                final IntCollection items = valuesEnum == -1 ? IntList.of(v) : EnumDefinitions.getIntEnum(valuesEnum).getValues().values();
                final IntList categoryItems = new IntArrayList(items);
                for (int id : items) {
                    final int alternate = alternates.getValue(id).orElse(-1);
                    if (alternate != -1) {
                        categoryItems.add(alternate);
                    }
                }
                int offset = slot * 39;
                subItems.put(offset, categoryItems);
                offset += 4;
                for (int itemId : items) {
                    itemsInCategory.add(itemId);
                    allItems.add(itemId);
                    final int alternate = alternates.getValue(itemId).orElse(-1);
                    if (alternate != -1) {
                        allItems.add(alternate);
                        categorizedSubItems.put(alternate, categoryItems);
                        subItems.put(offset++, IntList.of(alternate));
                    }
                    categorizedSubItems.put(itemId, categoryItems);
                    subItems.put(offset++, IntList.of(itemId));
                }
            });
        }
    }

    @Override
    protected void attach() {
        put(4, "Items");
        put(8, "Deposit mode");
        put(10, "Deposit inventory");
    }

    @Override
    public void open(Player player) {
        final StorageType type = getType(player);
        player.getVarManager().sendVarInstant(262, -1);
        player.getVarManager().sendVarInstant(261, 25);
        player.getInterfaceHandler().sendInterface(getInterface());
        player.getStorageRoom().getContainer().setFullUpdate(true);
        player.getPacketDispatcher().sendUpdateItemContainer(32768 + ContainerType.STORAGE_ROOM.getId(), -1, 0, player.getStorageRoom().getContainer());
        player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Items"), 0, 3311, AccessMask.CLICK_OP1, AccessMask.CLICK_OP10);
        player.getPacketDispatcher().sendClientScript(3532, type.getEnumId(), 1, 1);
    }

    @Override
    protected void build() {
        bind("Items", (player, slotId, itemId, option) -> {
            final StorageType type = getType(player);
            final IntList categoryItems = items.get(type.getEnumId()).get(slotId);
            if (option == 10) {
                final int item = categoryItems.getInt(0);
                player.sendMessage(ItemDefinitions.get(item).getExamine());
                return;
            }
            if (option == 1) {
                // Take out full set...
                if (itemId <= -1) {
                    for (int i = 0; i < categoryItems.size(); i++) {
                        if (!player.getInventory().checkSpace(1)) {
                            return;
                        }
                        var result = player.getStorageRoom().getContainer().remove(new Item(categoryItems.getInt(i)));

                        if (result.getSucceededAmount() <= 0) continue;
                        var item = new Item(result.getItem().getId(), result.getSucceededAmount());
                        player.getInventory().addItem(item);
                        player.getStorageRoom().getContainer().setFullUpdate(true);
                        player.getPacketDispatcher().sendUpdateItemContainer(32768 + ContainerType.STORAGE_ROOM.getId(), -1, 0, player.getStorageRoom().getContainer());
                    }
                } else {
                    int slotForItem = player.getStorageRoom().getContainer().getSlotOf(itemId);
                    if (slotForItem <= -1) return;

                    Item item = player.getStorageRoom().getContainer().get(slotForItem);
                    if (item == null) return;

                    player.getStorageRoom().getContainer().withdraw(player, player.getInventory().getContainer(), slotForItem, item.getAmount());
                    player.getPacketDispatcher().sendUpdateItemContainer(32768 + ContainerType.STORAGE_ROOM.getId(), -1, 0, player.getStorageRoom().getContainer());
                    player.getInventory().refreshAll();
                }

            }
        });
        bind("Deposit mode", (player) -> player.getVarManager().flipBit(DEPOSIT_TYPE_VARBIT));
        bind("Deposit inventory", (player) -> {
            for (int i = 0; i < 28; i++) {
                final Item item = player.getInventory().getItem(i);
                if (item == null) continue;
                final var categoryItems = StorageRoomInterface.itemSetsByCategory.get(StorageRoomInterface.getType(player).getEnumId());
                if (categoryItems == null) continue;
                final IntList items = categoryItems.get(item.getId());
                if (items == null) continue;
                if (player.getStorageRoom().getContainer().getAmountOf(item.getId()) > 0) continue;

                player.getInventory().getContainer().withdraw(player, player.getStorageRoom().getContainer(), i, item.getAmount());
            }
            player.getPacketDispatcher().sendUpdateItemContainer(32768 + ContainerType.STORAGE_ROOM.getId(), -1, 0, player.getStorageRoom().getContainer());
            player.getInventory().refreshAll();
        });
    }

    public static StorageType getType(Player player) {
        return (StorageType) player.getTemporaryAttributes().get("storage_room_type");
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.STORAGE_ROOM;
    }
}
