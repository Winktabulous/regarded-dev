package com.zenyte.game.model.item.containers;

import com.zenyte.game.content.skills.prayer.actions.Bones;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import java.util.Optional;


public class BonePouch {



    private final transient Player player;
    private final Container container;

    private boolean open;


    public BonePouch(final Player player) {
        this.player = player;
        this.container = new Container(ContainerPolicy.ALWAYS_STACK, ContainerType.HERB_SACK, Optional.of(player));
    }

    public void initialize(final BonePouch pouch) {
        if (pouch != null && pouch.container != null) {
            container.setContainer(pouch.container);
            open = pouch.open;
        }
    }

    public void fill() {
        if (getSize() == 100) {
            player.sendMessage("You cannot more than 100 bones in your bone pouch.");
            return;
        }
        int added = 0;
        for (int slot = 0; slot < 28; slot++) {
            final Item item = player.getInventory().getItem(slot);
            if (item == null) {
                continue;
            }
            if (Bones.BONES_MAP.containsKey(item.getId())) {
                container.add(item);
                player.getInventory().deleteItem(item);
                added++;
            }
        }
        player.sendMessage(added > 0 ? "You add the bone" + (added == 1 ? "" : "s") + " from your inventory to your pouch." : "You have no bones in your inventory that can be stored in the pouch.");
    }

    public void check() {
        player.sendMessage("You look in your bone pouch and see:");
        if (getSize() == 0) {
            player.sendMessage("The bone pouch is empty.");
            return;
        }
        for (final Int2ObjectMap.Entry<Item> entry : container.getItems().int2ObjectEntrySet()) {
            final Item item = entry.getValue();
            final String name = item.getName();
            final int amount = item.getAmount();
            if (amount > 0) {
                player.sendMessage(amount + " x " + name);
            }
        }
    }

    public void empty(final Container target) {
        if (container.isEmpty()) {
            player.sendMessage("The bone pouch is empty.");
            return;
        }
        player.sendMessage("You rummage around to see if you can extract any bones from your bone pouch.");
        for (final Int2ObjectMap.Entry<Item> entry : container.getItems().int2ObjectEntrySet()) {
            final Item bone = entry.getValue();
            final int slot = entry.getIntKey();
            target.deposit(player, container, slot, bone.getAmount());
        }
        target.refresh(player);
    }

    public int getSize() {
        int size = 0;
        for (final Int2ObjectMap.Entry<Item> entry : container.getItems().int2ObjectEntrySet()) {
            size += entry.getValue().getAmount();
        }
        return size;
    }
    public boolean isFull(Item item) {
        return getSize() >= 100 || container.getSize() == container.getContainerSize() && !container.contains(item) ;
    }
    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public int getAmountOf(final int id) {
        return container.getAmountOf(id);
    }

    public void clear() {
        container.clear();
    }

    public Player getPlayer() {
        return player;
    }

    public Container getContainer() {
        return container;
    }
}
