package com.zenyte.game.model.item.containers;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.plugins.dialogue.ItemChat;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.apache.commons.lang3.ArrayUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

/**
 * @author Tommeh | 29-3-2019 | 18:54
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class GemBag {
    private final transient Player player;
    private final Container container;

    private boolean open;

    private static final Map<Integer, String> GEMS = new HashMap<Integer, String>() {
        {
            put(1623, "Sapphires");
            put(1621, "Emeralds");
            put(1619, "Rubies");
            put(1617, "Diamonds");
            put(1631, "Dragonstones");
        }
    };
    public static final Integer[] IDS = GEMS.keySet().toArray(new Integer[0]);
    public static final Item GEM_BAG = new Item(12020);
    public static final Item GEM_BAG_OPEN = new Item(ItemId.OPEN_GEM_BAG);

    public GemBag(final Player player) {
        this.player = player;
        this.container = new Container(ContainerPolicy.ALWAYS_STACK, ContainerType.GEM_BAG, Optional.of(player));
    }

    public void initialize(final GemBag bag) {
        if (bag != null && bag.container != null) {
            container.setContainer(bag.container);
            open = bag.open;
        }
    }

    public boolean isFull(Item item) {
        return getSize() >= 300 || container.getSize() == container.getContainerSize() && !container.contains(item) ;
    }

    public void fill() {
        if (getSize() == 300) {
            player.sendMessage("You cannot more than 300 gems in your gem bag.");
            return;
        }
        final HashSet<Integer> checked = new HashSet<Integer>(28);
        for (int slot = 0; slot < 28; slot++) {
            final Item item = player.getInventory().getItem(slot);
            if (item == null) {
                continue;
            }
            if (ArrayUtils.contains(IDS, item.getId())) {
                if (getAmountOf(item.getId()) == 60) {
                    if (!checked.contains(item.getId())) {
                        player.sendMessage("You cannot store anymore " + GEMS.get(item.getId()).toLowerCase() + ".");
                        checked.add(item.getId());
                    }
                    continue;
                }
                container.add(item);
                player.getInventory().deleteItem(item);
            }
        }
    }

    public void check() {
        final StringBuilder builder = new StringBuilder();
        GEMS.forEach((id, name) -> {
            final int amount = container.getAmountOf(id);
            builder.append(name).append(": ").append(amount).append(id == IDS[2] ? "<br>" : " / ");
        });
        player.getDialogueManager().start(new ItemChat(player, GEM_BAG, builder.substring(0, builder.toString().length() - 2)));
    }

    public void empty(final Container target) {
        if (container.isEmpty()) {
            player.sendMessage("The gem bag is empty.");
            return;
        }
        for (final Int2ObjectMap.Entry<Item> entry : container.getItems().int2ObjectEntrySet()) {
            final Item gem = entry.getValue();
            final int slot = entry.getIntKey();
            target.deposit(player, container, slot, gem.getAmount());
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

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public int getAmountOf(final int id) {
        return container.getAmountOf(id);
    }

    public Int2ObjectLinkedOpenHashMap<Item> getGems() {
        return container.getItems();
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
