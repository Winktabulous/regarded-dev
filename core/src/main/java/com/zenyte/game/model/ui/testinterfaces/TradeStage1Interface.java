package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.flowerpoker.FlowerPokerManager;
import com.zenyte.game.content.flowerpoker.FlowerPokerStatus;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.ItemUtil;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Trade;
import com.zenyte.game.world.entity.player.container.impl.TradeStatus;

import java.util.Optional;

/**
 * @author Tommeh | 10-3-2019 | 18:59
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class TradeStage1Interface extends Interface {
    @Override
    protected void attach() {
        put(10, "Accept");
        put(25, "Remove Item");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(getInterface());
    }

    @Override
    public void close(final Player player, final Optional<GameInterface> replacement) {
        if (!replacement.isPresent() || !replacement.get().equals(GameInterface.TRADE_STAGE2)) {
            if(FlowerPokerManager.get(player).inInterface)
                FlowerPokerManager.get(player).closeStake(FlowerPokerStatus.CANCEL);
            else
                player.getTrade().closeTrade(TradeStatus.CANCEL);

            player.getInterfaceHandler().closeInterfaces();
        }
    }

    @Override
    protected void build() {
        bind("Accept", player -> {
            if(FlowerPokerManager.get(player).inInterface)
                FlowerPokerManager.get(player).accept(1);
            else
                player.getTrade().accept(1);
        });
        bind("Remove Item", (player, slotId, itemId, option) -> {
            if (option == 10) {
                ItemUtil.sendItemExamine(player, itemId);
                return;
            }
            if(FlowerPokerManager.get(player).inInterface) {
                FlowerPokerManager flowerPokerManager = FlowerPokerManager.get(player);

                final Item item = flowerPokerManager.staked_items.get(slotId);
                if (item == null) {
                    return;
                }
                if (option == 5) {
                    player.sendInputInt("Enter amount:", amount -> flowerPokerManager.removeItem(slotId, amount));
                } else {
                    final int amount = option == 1 ? 1 : option == 2 ? 5 : option == 3 ? 10 : flowerPokerManager.staked_items.getAmountOf(item.getId());
                    flowerPokerManager.removeItem(slotId, amount);
                }
            } else {
                final Trade trade = player.getTrade();
                final Item item = trade.getContainer().get(slotId);
                if (item == null) {
                    return;
                }
                if (option == 5) {
                    player.sendInputInt("Enter amount:", amount -> trade.removeItem(slotId, amount));
                } else {
                    final int amount = option == 1 ? 1 : option == 2 ? 5 : option == 3 ? 10 : trade.getContainer().getAmountOf(item.getId());
                    trade.removeItem(slotId, amount);
                }
            }
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.TRADE_STAGE1;
    }
}
