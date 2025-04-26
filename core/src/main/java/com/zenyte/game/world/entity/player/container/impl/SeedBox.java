package com.zenyte.game.world.entity.player.container.impl;

import com.zenyte.game.content.skills.farming.FarmingProduct;
import com.zenyte.game.content.skills.farming.Seedling;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.Optional;

/**
 * @author Tommeh | 2-1-2019 | 20:57
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class SeedBox {
    private final transient Player player;
    private final Container container;
    private boolean open;

    public SeedBox(final Player player) {
        this.player = player;
        container = new Container(ContainerPolicy.NORMAL, ContainerType.SEED_BOX, Optional.of(player));
    }

    public void initialize(final SeedBox seedBox) {
        if (seedBox == null || seedBox.container == null) {
            return;
        }
        container.setContainer(seedBox.container);
        open = seedBox.open;
    }

    public void fill() {
        final Inventory inventory = player.getInventory();
        final Container inventoryContainer = inventory.getContainer();
        final MutableBoolean bool = new MutableBoolean();
        for (int slot = 0, length = inventoryContainer.getContainerSize(); slot < length; slot++) {
            final Item item = inventoryContainer.get(slot);
            if (item == null || (!FarmingProduct.getSeeds().contains(item.getId()) && !Seedling.containsSeed(item.getId()))) {
                continue;
            }
            container.deposit(null, inventoryContainer, slot, item.getAmount());
            if (inventoryContainer.get(slot) != null) {
                bool.setTrue();
            }
        }
        if (bool.isTrue()) {
            player.sendMessage("You can't store anymore seeds to the box.");
        }
        inventory.refreshAll();
        refresh();
    }

    public void refresh() {
        container.setFullUpdate(true);
        container.refresh(player);
    }

    public void empty() {
        for (int slot = 0; slot < container.getContainerSize(); slot++) {
            remove(slot, Integer.MAX_VALUE);
        }
    }

    public void emptyToBank() {
        for (int slotId = 0; slotId < container.getContainerSize(); slotId++) {
            final Item seed = container.get(slotId);
            if (seed == null) {
                continue;
            }
            player.getBank().getContainer().deposit(player, container, slotId, Integer.MAX_VALUE);
        }
        player.getBank().getContainer().refresh(player);
        refresh();
    }

    public void remove(final int slotId, final int amount) {
        final Item seed = container.get(slotId);
        if (seed == null) {
            return;
        }
        player.getInventory().getContainer().deposit(player, container, slotId, amount);
        player.getInventory().refreshAll();
        refresh();
    }

    public Item get(final int slot) {
        return container.get(slot);
    }

    public Int2ObjectLinkedOpenHashMap<Item> getSeeds() {
        return container.getItems();
    }

    public void clear() {
        container.clear();
    }

    public Container getContainer() {
        return container;
    }

    public boolean isFull(Item item) {
        return container.getSize() == container.getContainerSize() && !container.contains(item);
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

}
