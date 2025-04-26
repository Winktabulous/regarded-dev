package com.zenyte.game.content.partyroom;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.model.ui.SwitchPlugin;
import com.zenyte.game.packet.PacketDispatcher;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import org.jetbrains.annotations.NotNull;

import static com.zenyte.game.content.partyroom.PartyDropChestInterface.getType;

/**
 * @author Kris | 25/12/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PartyDropInventoryInterface extends Interface implements SwitchPlugin {
    @Override
    protected void attach() {
        put(0, "Interact with item");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(this);
        final PacketDispatcher packetDispatcher = player.getPacketDispatcher();
        packetDispatcher.sendComponentSettings(getId(), getComponent("Interact with item"), 0, 27, AccessMask.CLICK_OP1, AccessMask.CLICK_OP2, AccessMask.CLICK_OP3, AccessMask.CLICK_OP4, AccessMask.CLICK_OP5, AccessMask.CLICK_OP10);
        refreshContainer(player);
    }

    @Override
    protected void build() {
        bind("Interact with item", (player, slotId, itemId, option) -> {
            final Inventory inventory = player.getInventory();
            final Item item = inventory.getItem(slotId);
            if (item == null || item.getId() != itemId) {
                return;
            }
            final Container inventoryContainer = inventory.getContainer();
            final Container container = FaladorPartyRoom.getPartyRoom().getPrivateContainer(player);
            final PartyRoomOptionType optionType = getType(player, option);
            if (optionType == PartyRoomOptionType.EXAMINE) {
                player.sendMessage(item.getDefinitions().getExamine());
                return;
            }
            if (optionType == PartyRoomOptionType.X) {
                player.sendInputInt("How many would you like to deposit?", value -> {
                    if (inventory.getItem(slotId) != item) {
                        return;
                    }
                    if (!item.isTradable()) {
                        player.sendMessage("You can't trade that.");
                        return;
                    }
                    container.deposit(player, inventoryContainer, slotId, value);
                    container.refresh(player);
                    refreshContainer(player);
                });
                return;
            }
            if (!item.isTradable()) {
                player.sendMessage("You can't trade that.");
                return;
            }
            final int amount = optionType.getAmount();
            container.deposit(player, inventoryContainer, slotId, amount);
            container.refresh(player);
            refreshContainer(player);
        });
        bind("Interact with item", "Interact with item", (player, fromSlot, toSlot) -> {
            player.getInventory().switchItem(fromSlot, toSlot);
            refreshContainer(player);
        });
    }

    static void refreshContainer(@NotNull final Player player) {
        player.getPacketDispatcher().sendUpdateItemContainer(player.getInventory().getContainer(), ContainerType.FALADOR_PARTY_CHEST_INVENTORY_DEPOSIT);
        player.getInventory().refreshAll();
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.PARTY_DROP_CHEST_INVENTORY;
    }
}
