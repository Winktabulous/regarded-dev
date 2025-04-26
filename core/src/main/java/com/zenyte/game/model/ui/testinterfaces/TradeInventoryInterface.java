package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.flowerpoker.FlowerPokerManager;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.ItemUtil;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Trade;

/**
 * @author Tommeh | 10-3-2019 | 19:08
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class TradeInventoryInterface extends Interface {
    @Override
    protected void attach() {
        put(0, "Add Item");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(getInterface());
    }

    @Override
    protected void build() {
        bind("Add Item", (player, slotId, itemId, option) -> {
            if (option == 10) {
                ItemUtil.sendItemExamine(player, itemId);
                return;
            }

            if (FlowerPokerManager.get(player).inInterface) {
                FlowerPokerManager flowerPokerManager = FlowerPokerManager.get(player);
                final Item item = player.getInventory().getItem(slotId);
                if (item == null) {
                    return;
                }
                if (option == 5) {
                    player.sendInputInt("Enter amount:", amount -> flowerPokerManager.addItem(slotId, amount));
                } else {
                    final int amount = option == 1 ? 1 : option == 2 ? 5 : option == 3 ? 10 : player.getInventory().getAmountOf(item.getId());
                    flowerPokerManager.addItem(slotId, amount);
                }
            } else {
                final Trade trade = player.getTrade();
                final Item item = player.getInventory().getItem(slotId);
                if (item == null) {
                    return;
                }
                if (option == 5) {
                    player.sendInputInt("Enter amount:", amount -> trade.addItem(slotId, amount));
                } else {
                    final int amount = option == 1 ? 1 : option == 2 ? 5 : option == 3 ? 10 : player.getInventory().getAmountOf(item.getId());
                    trade.addItem(slotId, amount);
                }
            }
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.TRADE_INVENTORY;
    }
}
