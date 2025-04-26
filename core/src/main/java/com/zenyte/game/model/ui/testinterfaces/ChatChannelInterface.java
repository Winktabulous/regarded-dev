package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.model.ui.PaneType;
import com.zenyte.game.world.entity.player.Player;

import static com.near_reality.game.model.ui.chat_channel.ChatChannelPlayerExtKt.getChatChannelInterfaceType;

/**
 * @author Tommeh | 27-10-2018 | 19:10
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ChatChannelInterface extends Interface {

    @Override
    protected void attach() {
        put(20, "Open clan chat set-up");
    }

    @Override
    public void open(Player player) {
        getChatChannelInterfaceType(player).sendTabInterface(player, getInterface());
    }

    @Override
    protected void build() {
        bind("Open clan chat set-up", player -> {
            if (player.getInterfaceHandler().containsInterface(InterfacePosition.CENTRAL)) {
                player.sendMessage("Please close the interface you have open before using 'Clan Setup'.");
                return;
            }
            if (player.isUnderCombat()) {
                player.sendMessage("You can't do this while in combat.");
                return;
            }
            GameInterface.CLAN_CHAT_SETUP.open(player);
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.CHAT_CHANNEL_TAB;
    }
}
