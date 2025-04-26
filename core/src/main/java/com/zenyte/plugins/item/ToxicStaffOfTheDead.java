package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.model.item.degradableitems.DegradableItem;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.plugins.itemonitem.SerpentineHelmetChargingAction;

/**
 * @author Kris | 26/01/2019 16:24
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ToxicStaffOfTheDead extends ItemPlugin implements PairedItemOnItemPlugin {
    @Override
    public void handle() {
        bind("Uncharge", (player, item, container, slotId) -> {
            if (container.getFreeSlotsSize() <= 0 && !container.contains(12934, 1)) {
                player.sendMessage("You need some more free space to uncharge the staff.");
                return;
            }
            final Inventory inventory = player.getInventory();
            final int scales = (int) Math.floor(item.getCharges() * SerpentineHelmetChargingAction.SCALES_TO_CHARGES_RATIO);
            if (scales > 0) {
                inventory.addItem(new Item(12934, scales));
            }
            item.setCharges(0);
            item.setId(12902);
            container.refresh(slotId);
        });
        bind("Dismantle", (player, item, container, slotId) -> {
            final Inventory inventory = player.getInventory();
            if (inventory.getFreeSlots() < 1) {
                player.sendMessage("Not enough free space in your inventory.");
                return;
            }
            inventory.deleteItem(item);
            inventory.addItem(new Item(11791, 1));
            inventory.addItem(new Item(12932, 1));
        });
    }

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        final Item scales = from.getId() == 12934 ? from : to;
        final Item staff = from == scales ? to : from;
        int amount = (int) (scales.getAmount() * SerpentineHelmetChargingAction.CHARGES_TO_SCALES_RATIO);
        amount = Math.min(amount, DegradableItem.TOXIC_STAFF_OF_THE_DEAD.getMaximumCharges() - staff.getCharges());
        final int scalesRequired = (int) Math.ceil(amount * SerpentineHelmetChargingAction.SCALES_TO_CHARGES_RATIO);
        if (scalesRequired <= 0) {
            player.sendMessage("Your staff is already fully charged.");
            return;
        }
        final Inventory inventory = player.getInventory();
        inventory.deleteItem(new Item(scales.getId(), scalesRequired));
        staff.setCharges(staff.getCharges() + amount);
        if (staff.getId() == 12902) {
            staff.setId(12904);
        }
        inventory.refreshAll();
    }

    @Override
    public int[] getItems() {
        return new int[] {12902, 12904};
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {ItemPair.of(12904, 12934), ItemPair.of(12902, 12934)};
    }
}
