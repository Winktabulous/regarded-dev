package com.zenyte.game.packet.in.event;

import com.zenyte.game.model.ui.testinterfaces.GameNoticeboardInterface;
import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Setting;
import com.zenyte.game.world.entity.player.Settings;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 25-1-2019 | 19:18
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class ChatSetModeEvent implements ClientProtEvent {

    private final int publicFilter, privateFilter, tradeFilter;

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Public: " + publicFilter + ", private: " + privateFilter + ", trade: " + tradeFilter);
    }

    @Override
    public void handle(final Player player) {
        final Settings settings = player.getSettings();
        settings.setSetting(Setting.PUBLIC_FILTER, publicFilter);
        settings.setSetting(Setting.TRADE_FILTER, tradeFilter);
        settings.setSetting(Setting.PRIVATE_FILTER, privateFilter);
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

    public ChatSetModeEvent(int publicFilter, int privateFilter, int tradeFilter) {
        this.publicFilter = publicFilter;
        this.privateFilter = privateFilter;
        this.tradeFilter = tradeFilter;
    }
}
