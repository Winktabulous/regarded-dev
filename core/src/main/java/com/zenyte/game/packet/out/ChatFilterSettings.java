package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Setting;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 28 jul. 2018 | 18:48:30
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class ChatFilterSettings implements GamePacketEncoder {

    private final Player player;

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Trade filter: " + player.getNumericAttribute(Setting.TRADE_FILTER.toString()).intValue() + ", public filter: " + player.getNumericAttribute(Setting.PUBLIC_FILTER.toString()).intValue());
    }

    public ChatFilterSettings(Player player) {
        this.player = player;
    }

    @Override
    public GamePacketOut encode() {
        final GamePacketOut buffer = ServerProt.CHAT_FILTER_SETTINGS.gamePacketOut();
        buffer.writeByte128(player.getNumericAttribute(Setting.TRADE_FILTER.toString()).intValue());
        buffer.writeByteC(player.getNumericAttribute(Setting.PUBLIC_FILTER.toString()).intValue());
        return buffer;
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

}
