package com.zenyte.game.content.grandexchange;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.packet.PacketDispatcher;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 13/04/2019 21:52
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class GrandExchangeCollectionBox extends Interface {
    @Override
    protected void attach() {
        put(3, "Collect to inventory");
        put(4, "Collect to bank");
        put(5, "Slot 0");
        put(6, "Slot 1");
        put(7, "Slot 2");
        put(8, "Slot 3");
        put(9, "Slot 4");
        put(10, "Slot 5");
        put(11, "Slot 6");
        put(12, "Slot 7");
    }

    @Override
    public void open(final Player player) {
        if (player.isIronman()) {
            player.sendMessage("As an Iron Man, you cannot use the Grand Exchange.");
            return;
        }

        player.getGrandExchange().refreshOffers();
        player.getGrandExchange().resetGEVars();
        player.getInterfaceHandler().sendInterface(this);
        final PacketDispatcher dispatcher = player.getPacketDispatcher();
        for (int i = 0; i < 8; i++) {
            dispatcher.sendComponentSettings(getInterface(), getComponent("Slot " + i), 0, 4, AccessMask.CLICK_OP1, AccessMask.CLICK_OP2, AccessMask.CLICK_OP3, AccessMask.CLICK_OP10);
        }
    }

    @Override
    protected void build() {
        for (int i = 0; i < 8; i++) {
            final int slot = i;
            bind("Slot " + i, (player, slotId, itemId, option) -> {
                final GrandExchange ge = player.getGrandExchange();
                ge.collectFromBox(false, slot, option, slotId - 3);
            });
        }
        bind("Collect to inventory", player -> player.getGrandExchange().collectAll(true, false));
        bind("Collect to bank", player -> player.getGrandExchange().collectAll(false, false));
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.GRAND_EXCHANGE_COLLECTION_BOX;
    }
}
