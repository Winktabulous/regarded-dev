package com.zenyte.game.content.chambersofxeric.storageunit;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.chambersofxeric.Raid;
import com.zenyte.game.item.Item;
import com.zenyte.game.packet.PacketDispatcher;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;

import java.util.Optional;

/**
 * @author Kris | 21/07/2019 01:56
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SharedStorageInterface extends StorageInterface {
    @Override
    protected void attach() {
        put(4, "Container size");
        put(5, "Switch to private storage");
        put(7, "Interact with item");
    }

    @Override
    public void open(final Player player) {
        final Raid raid = player.getRaid().orElseThrow(RuntimeException::new);
        final SharedStorage storage = raid.constructOrGetSharedStorage();
        storage.getViewingPlayers().add(player);
        final Container container = storage.getContainer();
        final PacketDispatcher dispatcher = player.getPacketDispatcher();
        player.getInterfaceHandler().sendInterface(this);
        dispatcher.sendComponentText(getInterface(), getComponent("Container size"), container.getContainerSize());
        dispatcher.sendComponentSettings(getInterface(), getComponent("Interact with item"), 0, container.getContainerSize(), AccessMask.CLICK_OP1, AccessMask.CLICK_OP2, AccessMask.CLICK_OP3, AccessMask.CLICK_OP4, AccessMask.CLICK_OP5, AccessMask.CLICK_OP10);
        dispatcher.sendUpdateItemContainer(container);
        GameInterface.RAIDS_STORAGE_INVENTORY_INTERFACE.open(player);
    }

    @Override
    public void close(final Player player, final Optional<GameInterface> replacement) {
        final Raid raid = player.getRaid().orElseThrow(RuntimeException::new);
        final SharedStorage storage = raid.constructOrGetSharedStorage();
        storage.getViewingPlayers().remove(player);
    }

    @Override
    protected void build() {
        bind("Switch to private storage", player -> {
            final Raid raid = player.getRaid().orElseThrow(RuntimeException::new);
            final SharedStorage storage = raid.constructOrGetSharedStorage();
            final int size = storage.getContainer().getContainerSize();
            player.getPrivateStorage().open(size == 250 ? 30 : size == 500 ? 60 : 90);
        });
        bind("Interact with item", (player, slotId, itemId, option) -> {
            final Raid raid = player.getRaid().orElseThrow(RuntimeException::new);
            final SharedStorage storage = raid.constructOrGetSharedStorage();
            final Container container = storage.getContainer();
            final int slot = container.getSlotOf(itemId);
            final Item item = container.get(slot);
            if (item == null) {
                return;
            }
            if (!raid.usingFakeScale) {
                if (player.getGameMode().isNonGroupIronman()) {
                    player.sendMessage("Ironmen cannot withdraw items from the shared raid storage");
                    return;
                }
            }
            handleInteraction(player, storage, option, slot, item);
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.RAIDS_SHARED_STORAGE;
    }
}
