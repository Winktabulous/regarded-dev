package com.zenyte.game.content.chambersofxeric.storageunit;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.GameInterface;
import com.zenyte.game.content.chambersofxeric.Raid;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.plugins.events.InitializationEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Kris | 3. mai 2018 : 14:33:42
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class PrivateStorage implements Storage {
    /**
     * The player who owns this private storage.
     */
    private final transient Player player;
    /**
     * The container holding the items in this private storage.
     */
    private final Container container;

    /**
     * The constructor for the private storage, passing in the owner of the storage itself.
     *
     * @param player the player who owns the private storage.
     */
    public PrivateStorage(@NotNull final Player player) {
        this.player = player;
        container = new Container(ContainerPolicy.NORMAL, ContainerType.PRIVATE_STORAGE, Optional.of(player));
    }

    @Subscribe
    public static final void onInitialization(final InitializationEvent event) {
        final Player player = event.getPlayer();
        final Player saved = event.getSavedPlayer();
        final PrivateStorage storage = saved.getPrivateStorage();
        if (storage == null) {
            return;
        }
        player.getPrivateStorage().container.setContainer(storage.container);
    }

    /**
     * Opens the private storage interface with the size specified. If the size is -1, the private storage is being opened outside of a raid.
     *
     * @param size the size of the container, or -1 if outside of raid(technical upper limit is still 90)
     */
    public void open(final int size) {
        player.getTemporaryAttributes().put("private storage size", size);
        GameInterface.RAIDS_PRIVATE_STORAGE.open(player);
    }

    /**
     * Switches the items in the slots passed on as the parameters.
     *
     * @param fromSlot the slot of the item which is being dragged.
     * @param toSlot   the slot of the item to which the item is being dragged.
     */
    public void switchItem(final int fromSlot, final int toSlot) {
        final Item fromItem = container.get(fromSlot);
        final Item toItem = container.get(toSlot);
        container.set(fromSlot, toItem);
        container.set(toSlot, fromItem);
        refresh();
    }

    /**
     * Resets inaccessible items in the private storage by removing them.
     *
     * @return this storage for chaining effect.
     */
    public PrivateStorage resetInaccessibleItems() {
        for (int slot = 0, length = container.getContainerSize(); slot < length; slot++) {
            final Item item = container.get(slot);
            if (item == null) {
                continue;
            }
            if (!Raid.isInternalItem(item.getId())) {
                continue;
            }
            container.set(slot, null);
        }
        return this;
    }

    @Override
    public void deposit(final Player player, final int slotId, final int amount) {
        container.deposit(player, player.getInventory().getContainer(), slotId, amount);
        player.getInventory().refresh();
        refresh();
    }

    @Override
    public void withdraw(final Player player, final int slot, final int amount, final boolean sendMessages) {
        container.withdraw(sendMessages ? player : null, player.getInventory().getContainer(), slot, amount);
        player.getInventory().refresh();
        refresh();
    }

    @Override
    public void refresh() {
        container.refresh(player);
    }

    public Container getContainer() {
        return container;
    }
}
