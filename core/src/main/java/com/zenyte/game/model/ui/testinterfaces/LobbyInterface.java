package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.model.ui.InterfaceHandler;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.model.ui.PaneType;
import com.zenyte.game.packet.PacketDispatcher;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;
import mgi.types.config.enums.EnumDefinitions;

import static com.zenyte.game.GameInterface.EMOTE_TAB;

/**
 * @author Tommeh | 28-10-2018 | 18:56
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class LobbyInterface extends Interface {
    @Override
    protected void attach() {
        put(6, "Message of the week");
        put(75, "Bank pin");
        put(81, "Click here to play");
    }

    @Override
    public void open(Player player) {
        final PacketDispatcher dispatcher = player.getPacketDispatcher();
        final InterfaceHandler handler = player.getInterfaceHandler();
        dispatcher.sendComponentText(378, getComponent("Bank pin"), "You do not have a Bank PIN.<br>Please visit a bank if you would like one.");
        dispatcher.sendComponentText(378, getComponent("Message of the week"), "<col=1f1fff>Cromperty</col> got a <col=003600>right-click option</col> to claim <col=ffffff>essence</col>,<br>and if you <col=ffff00>enable the ESC key</col> for closing menus,<br>it will work on the <col=3f007f>World Map</col> and <col=6f0000>this screen</col>.");
        dispatcher.sendClientScript(233, 24772660, 34024, 0, 0, 225, 1296, 0, 348, -1);
        dispatcher.sendClientScript(233, 24772661, 23446, 0, 50, 165, 1717, 0, 1116, -1);
        dispatcher.sendPane(PaneType.FULL_SCREEN);
        dispatcher.sendClientScript(2494, 1);
        handler.sendInterface(InterfacePosition.CHATBOX, 162);
        handler.sendInterface(InterfacePosition.PRIVATE_CHAT, 163);
        handler.sendInterface(378, 27, PaneType.FULL_SCREEN, false);
        dispatcher.sendClientScript(1080, "");
        handler.sendInterface(InterfacePosition.SKILLS_TAB, 320);
        dispatcher.sendComponentSettings(399, 7, 0, EnumDefinitions.get(1374).getSize(), AccessMask.CLICK_OP1);
        dispatcher.sendComponentSettings(399, 8, 0, EnumDefinitions.get(1375).getSize(), AccessMask.CLICK_OP1);
        dispatcher.sendComponentSettings(399, 9, 0, EnumDefinitions.get(1376).getSize(), AccessMask.CLICK_OP1);
        handler.sendInterface(InterfacePosition.INVENTORY_TAB, 149);
        handler.sendInterface(InterfacePosition.EQUIPMENT_TAB, 387);
        handler.sendInterface(InterfacePosition.PRAYER_TAB, 541);
        handler.sendInterface(InterfacePosition.SPELLBOOK_TAB, 218);
        handler.sendInterface(InterfacePosition.FRIENDS_TAB, 432);
        handler.sendInterface(InterfacePosition.ACCOUNT_MANAGEMENT, 429);
        handler.sendInterface(InterfacePosition.LOGOUT_TAB, 182);
        GameInterface.SETTINGS.open(player);
        EMOTE_TAB.open(player);
        dispatcher.sendComponentSettings(216, 1, 0, 47, AccessMask.CLICK_OP1);
        GameInterface.MUSIC_TAB.open(player);
        //handler.sendInterface(InterfacePosition.CLAN_TAB, 7);
        GameInterface.COMBAT_TAB.open(player);
        dispatcher.sendClientScript(2014, 0, 0, 0, 0, 0, 0);
        dispatcher.sendClientScript(2015, 0);
    }

    /**
     * Second arg is the model id
     */
    @Override
    protected void build() {
        bind("Click here to play", player -> {
            player.getInterfaceHandler().sendGameFrame();
            player.getInterfaceHandler().closeInterface(378);
            player.onLobbyClose();
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.LOBBY;
    }
}
