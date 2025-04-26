package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.grandexchange.ExchangeHistory;
import com.zenyte.game.content.grandexchange.ExchangeType;
import com.zenyte.game.content.grandexchange.GrandExchange;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.util.ItemUtil;
import com.zenyte.game.world.entity.player.Player;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author Tommeh | 28-10-2018 | 18:42
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class GrandExchangeHistoryInterface extends Interface {
    @Override
    protected void attach() {
        put(2, "Open Offers");
        put(3, "Click history entry");
    }

    @Override
    public void open(Player player) {
        if (player.isIronman()) {
            player.sendMessage("As an Iron Man, you cannot use the Grand Exchange.");
            return;
        }
        final LinkedList<ExchangeHistory> history = player.getGrandExchange().getHistory();
        player.getInterfaceHandler().sendInterface(getInterface());
        GameInterface.INVENTORY_TAB.open(player);
        player.getPacketDispatcher().sendClientScript(1644);
        final Iterator<ExchangeHistory> lit = history.descendingIterator();
        int index = 0;
        while (lit.hasNext()) {
            final ExchangeHistory record = lit.next();
            player.getPacketDispatcher().sendClientScript(1645, index++, record.getId(), record.getType().equals(ExchangeType.BUYING) ? 0 : 1, record.getQuantity(), record.getPrice());
        }
        player.getPacketDispatcher().sendClientScript(1646);
        player.getPacketDispatcher().sendComponentSettings(getInterface(), 3, 0, history.size() * 6, AccessMask.CLICK_OP1, AccessMask.CLICK_OP10);
    }

    @Override
    protected void build() {
        bind("Open Offers", player -> player.getGrandExchange().openOffersInterface());
        bind("Click history entry", (player, slotId, itemId, option) -> {
            final GrandExchange exchange = player.getGrandExchange();
            final ExchangeHistory record = exchange.getHistory().get((exchange.getHistory().size() - (slotId / 6)) - 1);
            if (record == null) {
                return;
            }
            if (option == 1) {
                exchange.openOffersInterface();
                exchange.buy(exchange.getFreeSlot(), true);
                exchange.setItem(record.getId());
                exchange.setQuantity(record.getQuantity());
                exchange.setPrice(new Item(record.getId()).getSellPrice());
                player.getInterfaceHandler().closeInput();
            } else if (option == 10) {
                ItemUtil.sendItemExamine(player, record.getId());
            }
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.GRAND_EXCHANGE_HISTORY;
    }
}
