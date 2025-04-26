package com.zenyte.game.content.chambersofxeric.storageunit;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.chambersofxeric.Raid;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.SwitchPlugin;
import com.zenyte.game.packet.PacketDispatcher;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.container.impl.bank.Bank;
import com.zenyte.game.world.entity.player.privilege.GameMode;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author Kris | 21/07/2019 02:41
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PrivateStorageInterface extends StorageInterface implements SwitchPlugin {
    @Override
    protected void attach() {
        put(4, "Size");
        put(5, "Shared storage button");
        put(6, "Interact with item");
        put(9, "Withdraw all");
        put(10, "Deposit/Bank all");
    }

    @Override
    public void open(final Player player) {
        final PrivateStorage storage = player.getPrivateStorage();
        final Container container = storage.getContainer();
        final Object attribute = player.getTemporaryAttributes().remove("private storage size");
        if (!(attribute instanceof Number)) {
            throw new IllegalStateException("Size undefined");
        }
        final int size = ((Number) attribute).intValue();
        container.setContainerSize(size == -1 ? 90 : size);
        if (size == -1) {
            storage.resetInaccessibleItems();
        }
        final PacketDispatcher dispatcher = player.getPacketDispatcher();
        dispatcher.sendComponentText(getInterface(), getComponent("Size"), size == -1 ? "N/A" : size);
        dispatcher.sendComponentVisibility(getInterface(), getComponent("Shared storage button"), size == -1);
        dispatcher.sendComponentSettings(getInterface(), getComponent("Interact with item"), 0, container.getContainerSize(), AccessMask.CLICK_OP1, AccessMask.CLICK_OP2, AccessMask.CLICK_OP3, AccessMask.CLICK_OP4, AccessMask.CLICK_OP5, AccessMask.CLICK_OP10, AccessMask.DRAG_DEPTH1, AccessMask.DRAG_TARGETABLE);
        container.shift();
        container.setFullUpdate(true);
        storage.refresh();
        player.getInterfaceHandler().sendInterface(this);
        player.getVarManager().sendBit(3459, 1);
        GameInterface.RAIDS_STORAGE_INVENTORY_INTERFACE.open(player);
    }

    @Override
    protected void build() {
        bind("Shared storage button", player -> player.getRaid().ifPresent(raid -> GameInterface.RAIDS_SHARED_STORAGE.open(player)));
        bind("Interact with item", (player, slotId, itemId, option) -> {
            final PrivateStorage storage = player.getPrivateStorage();
            final Container container = storage.getContainer();
            final int slot = container.getSlotOf(itemId);
            final Item item = container.get(slot);
            if (item == null) {
                return;
            }
            if (player.getGameMode().equals(GameMode.ULTIMATE_IRON_MAN) && player.getRaid().isPresent()) {
                player.sendMessage("You cannot use the storage units as an ultimate ironman.");
                return;
            }
            handleInteraction(player, storage, option, slot, item);
        });
        bind("Withdraw all", player -> {
            if (player.getGameMode().equals(GameMode.ULTIMATE_IRON_MAN) && player.getRaid().isPresent()) {
                player.sendMessage("You cannot use the storage units as an ultimate ironman.");
                return;
            }
            final PrivateStorage storage = player.getPrivateStorage();
            final Container container = storage.getContainer();
            if (container.getSize() == 0) {
                player.sendMessage("There's nothing to withdraw.");
                return;
            }
            for (int slot = 0; slot < container.getContainerSize(); slot++) {
                final Item item = container.get(slot);
                if (item == null) {
                    continue;
                }
                player.getInventory().getContainer().deposit(null, container, slot, item.getAmount());
            }
            player.getInventory().refreshAll();
            container.refresh(player);
            if (container.getSize() != 0) {
                player.sendMessage("Not enough space in your inventory.");
            }
        });
        bind("Deposit/Bank all", player -> {
            if (player.getGameMode().equals(GameMode.ULTIMATE_IRON_MAN) && player.getRaid().isPresent()) {
                player.sendMessage("You cannot use the storage units as an ultimate ironman.");
                return;
            }
            final Optional<Raid> raid = player.getRaid();
            final PrivateStorage storage = player.getPrivateStorage();
            if (raid.isPresent()) {
                final Inventory inventory = player.getInventory();
                final Container container = inventory.getContainer();
                if (container.getSize() == 0) {
                    player.sendMessage("There's nothing to deposit..");
                    return;
                }
                for (int slot = 0; slot < container.getContainerSize(); slot++) {
                    final Item item = container.get(slot);
                    if (item == null || Bank.unbankableItems.contains(item.getId())) {
                        continue;
                    }
                    storage.getContainer().deposit(null, container, slot, item.getAmount());
                }
                storage.refresh();
                container.refresh(player);
                if (container.getSize() != 0) {
                    player.sendMessage("Not enough space in your private storage.");
                }
                return;
            }
            final Container container = storage.getContainer();
            if (container.getSize() == 0) {
                player.sendMessage("There's nothing to bank.");
                return;
            }
            for (int slot = 0; slot < container.getContainerSize(); slot++) {
                final Item item = container.get(slot);
                if (item == null) {
                    continue;
                }
                player.getBank().getContainer().deposit(null, container, slot, item.getAmount());
            }
            player.getBank().refreshContainer();
            container.refresh(player);
            if (container.getSize() != 0) {
                player.sendMessage("Not enough space in your bank.");
            }
        });
        bind("Interact with item", "Interact with item", (player, fromSlot, toSlot) -> player.getPrivateStorage().switchItem(fromSlot, toSlot));
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.RAIDS_PRIVATE_STORAGE;
    }
}
