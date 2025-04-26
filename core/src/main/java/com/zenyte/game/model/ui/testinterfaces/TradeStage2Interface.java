package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.flowerpoker.FlowerPokerManager;
import com.zenyte.game.content.flowerpoker.FlowerPokerStatus;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.TradeStatus;

import java.util.Optional;

/**
 * @author Tommeh | 10-3-2019 | 19:07
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class TradeStage2Interface extends Interface {

    @Override
    protected void attach() {
        put(13, "Accept");
        put(14, "Decline");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(getInterface());
    }

    @Override
    public void close(final Player player, final Optional<GameInterface> replacement) {
        if(FlowerPokerManager.get(player).inInterface)
            FlowerPokerManager.get(player).closeStake(FlowerPokerStatus.CANCEL);
        else
            player.getTrade().closeTrade(TradeStatus.CANCEL);
        player.getInterfaceHandler().closeInterfaces();
    }

    @Override
    protected void build() {
        bind("Accept", player ->  {
            if(FlowerPokerManager.get(player).inInterface)
                FlowerPokerManager.get(player).accept(2);
            else
                player.getTrade().accept(2);
        });
        bind("Decline", player -> player.getInterfaceHandler().closeInterfaces());
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.TRADE_STAGE2;
    }
}
