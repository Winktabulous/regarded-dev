package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.GameSetting;
import com.zenyte.game.world.entity.player.Player;

import java.util.Optional;

/**
 * @author Tommeh | 27-4-2019 | 11:24
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class GameSettingsInterface extends Interface {
    @Override
    protected void attach() {
        put(6, "Close");
        put(7, "Handle Setting");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(this);
        final int size = GameSetting.ALL.length;
        for (int index = 0; index < size; index++) {
            final GameSetting setting = GameSetting.ALL[index];
            if (setting == null) {
                continue;
            }
            player.getPacketDispatcher().sendClientScript(10200, index, setting.getName(), setting.getDescription(), setting.getType().ordinal(), player.getNumericAttribute(setting.toString()).intValue());
        }
        player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Handle Setting"), 0, size * 6, AccessMask.CLICK_OP1);
        player.getPacketDispatcher().sendClientScript(10201, size);
    }


    @Override
    protected void build() {
        bind("Handle Setting", (player, slotId, itemId, option) -> {
            final GameSetting setting = GameSetting.get(slotId);
            if (setting == null) {
                return;
            }
            setting.handleSetting(player);
        });
        bind("Close", player -> player.sendMessage("This will be removed in the next cache update coming soon."));
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.GAME_SETTINGS;
    }
}
