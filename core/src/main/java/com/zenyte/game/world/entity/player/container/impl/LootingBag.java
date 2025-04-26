package com.zenyte.game.world.entity.player.container.impl;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import java.util.Optional;

/**
 * @author Tommeh | 27-2-2019 | 16:20
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class LootingBag {
    private final transient Player player;
    private final Container container;
    private boolean open;

    public LootingBag(final Player player) {
        this.player = player;
        container = new Container(ContainerPolicy.NORMAL, ContainerType.LOOTING_BAG, Optional.of(player));
    }

    public void initialize(final LootingBag lootingBag) {
        if (lootingBag == null) {
            return;
        }
        if (lootingBag.container != null) {
            container.setContainer(lootingBag.container);
        }
        open = lootingBag.open;
    }

    public void refresh() {
        container.setFullUpdate(true);
        container.refresh(player);
    }

    public void deposit(final int slotId, final int amount) {
        container.deposit(player, player.getInventory().getContainer(), slotId, amount);
        player.getInventory().refreshAll();
    }

    public long getTotalValue() {
        int value = 0;
        for (final Int2ObjectMap.Entry<Item> entry : container.getItems().int2ObjectEntrySet()) {
            if (entry == null || entry.getValue() == null) {
                continue;
            }
            value += entry.getValue().getSellPrice() * entry.getValue().getAmount();
        }
        return value;
    }

    public static boolean hasBag(final Player player) {
        return player.containsAny(ItemId.LOOTING_BAG, ItemId.LOOTING_BAG_22586);
    }

    public static boolean isBag(final int id) {
        return id == ItemId.LOOTING_BAG || id == ItemId.LOOTING_BAG_22586;
    }

    public Item getItem(final int slot) {
        return container.get(slot);
    }

    public boolean isEmpty() {
        return container.isEmpty();
    }

    public boolean isFull() {
        return container.getSize() == container.getContainerSize();
    }

    public void clear() {
        container.clear();
    }

    public Container getContainer() {
        return container;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean canHold(Item item) {
        if (!isFull())
            return true;
        if (item.isStackable()) {
            final long amountInInventory = container.getAmountOf(item.getId());
            if (amountInInventory > 0) {
                return (amountInInventory + item.getAmount()) <= Integer.MAX_VALUE;
            } else
                return false;
        }
        return false;
    }
}
