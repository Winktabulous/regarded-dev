package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.model.ui.PaneType;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Setting;

import static com.zenyte.game.model.ui.testinterfaces.advancedsettings.SettingsInterface.SETTINGS_SEARCH_LEFT_VARBIT;
import static com.zenyte.game.model.ui.testinterfaces.advancedsettings.SettingsInterface.SETTINGS_SEARCH_RIGHT_VARBIT;

/**
 * @author Tommeh | 28-10-2018 | 16:14
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class WorldMapInterface extends Interface {

    @Override
    protected void attach() {
        put(38, "Close");
        put(4, "Esc Close");
        put(24, "Menu");
    }

    @Override
    public void open(Player player) {
        player.getSettings().refreshSetting(Setting.WORLD_MAP_GUIDE);
        player.getWorldMap().setVisible(true);
        player.getWorldMap().updateLocation();
        player.getPacketDispatcher().sendComponentSettings(getInterface(), 21, 0, 4, AccessMask.CLICK_OP1);
        player.getVarManager().sendBitInstant(SETTINGS_SEARCH_LEFT_VARBIT, 0);
        player.getVarManager().sendBitInstant(SETTINGS_SEARCH_RIGHT_VARBIT, 0);
        if (player.getWorldMap().isFullScreen()) {
            player.getWorldMap().setPreviousPane(player.getInterfaceHandler().getPane());
            player.getInterfaceHandler().sendPane(player.getWorldMap().getPreviousPane(), PaneType.FULL_SCREEN);
            player.getInterfaceHandler().sendInterface(595, 38, PaneType.FULL_SCREEN, true);
            player.getInterfaceHandler().sendInterface(594, 37, PaneType.FULL_SCREEN, false);
        } else {
            player.getInterfaceHandler().sendInterface(getInterface());
        }
    }

    @Override
    protected void build() {
        bind("Close", player -> {
            if (player.isLocked()) {
                return;
            }
            player.getWorldMap().close();
        });
        bind("Esc Close", player -> {
            if (player.isLocked()) {
                return;
            }
            player.getWorldMap().close();
        });
        bind("Menu", player -> player.getSettings().toggleSetting(Setting.WORLD_MAP_GUIDE));
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.WORLD_MAP;
    }
}
