package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.packet.PacketDispatcher;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.util.ItemUtil;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.region.area.plugins.TempPlayerStatePlugin;
import com.zenyte.utils.TextUtils;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.Optional;

/**
 * @author Kris | 11/05/2019 20:19
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PriceCheckerInterface extends Interface {
    @Override
    protected void attach() {
        put(10, "Add all");
        put(5, "Search for item");
        put(2, "Interact with item");
        put(8, "Item sprite in search");
        put(12, "Item name in search");
    }

    @Override
    public void close(final Player player, final Optional<GameInterface> replacement) {
        player.getPriceChecker().returnItems();
        player.getInterfaceHandler().closeInterface(GameInterface.PRICE_CHECKER_INVENTORY);
    }

    @Override
    public void open(final Player player) {
        if (TempPlayerStatePlugin.enableTempState(player, TempPlayerStatePlugin.StateType.INVENTORY)) {
            player.sendMessage("Cannot open price checker right now.");
            return;
        }
        final PacketDispatcher packetDispatcher = player.getPacketDispatcher();
        player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, getId());
        packetDispatcher.sendClientScript(600, 1, 1, 15, (getId() << 16) | (getComponent("Item name in search")));
        packetDispatcher.sendComponentSettings(getId(), getComponent("Interact with item"), 0, 27, AccessMask.CLICK_OP1, AccessMask.CLICK_OP2, AccessMask.CLICK_OP3, AccessMask.CLICK_OP4, AccessMask.CLICK_OP5, AccessMask.CLICK_OP10);
        packetDispatcher.sendComponentText(getId(), getComponent("Item name in search"), "Total guide price:<br><col=ffffff>0</col>");
        final Container container = player.getPriceChecker().getContainer();
        container.setFullUpdate(true);
        container.refresh(player);
        GameInterface.PRICE_CHECKER_INVENTORY.open(player);
    }

    @Override
    protected void build() {
        bind("Interact with item", (player, slotId, itemId, option) -> {
            final Item item = player.getPriceChecker().get(slotId);
            if (item == null) {
                return;
            }
            if (item.getId() != itemId) {
                return;
            }
            if (option == 10) {
                ItemUtil.sendItemExamine(player, item);
                return;
            }
            if (option == 5) {
                player.sendInputInt("Enter amount:", amount -> player.getPriceChecker().withdraw(player, player.getInventory().getContainer(), slotId, amount));
                return;
            }
            final int amount = option == 1 ? 1 : option == 2 ? 5 : option == 3 ? 10 : Integer.MAX_VALUE;
            player.getPriceChecker().withdraw(player, player.getInventory().getContainer(), slotId, amount);
        });
        bind("Search for item", (player, slotId, itemId, option) -> player.sendInputItem("Select an item to ask about its price:", item -> {
            player.getPacketDispatcher().sendComponentItem(getId(), getComponent("Item sprite in search"), item.getId(), 1);
            player.getPacketDispatcher().sendClientScript(600, 0, 1, 15, (getId() << 16) | (getComponent("Item name in search")));
            player.getPacketDispatcher().sendComponentText(getId(), getComponent("Item name in search"), item.getName() + ":<br><col=ffffff>" + TextUtils.formatCurrency(item.getSellPrice()) + " coins</col>");
        }));
        bind("Add all", (player, slotId, itemId, option) -> {
            final Container container = player.getInventory().getContainer();
            if (container.getSize() == 0) {
                player.sendMessage("Your inventory is already empty.");
                return;
            }
            final MutableInt untradablesCount = new MutableInt();
            for (int slot = 0; slot < container.getContainerSize(); slot++) {
                final Item item = container.get(slot);
                if (item == null) {
                    continue;
                }
                if (!item.isTradable()) {
                    untradablesCount.increment();
                    continue;
                }
                player.getPriceChecker().deposit(player, container, slot, item.getAmount());
            }
            if (untradablesCount.intValue() > 0) {
                player.sendMessage("You have items that cannot be traded.");
            }
            if (container.getSize() > untradablesCount.intValue()) {
                player.sendMessage("Not enough space in your price checker.");
            }
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.PRICE_CHECKER;
    }
}
