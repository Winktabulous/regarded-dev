package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import mgi.utilities.StringFormatUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Tommeh | 28 jul. 2018 | 18:56:47
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class UpdateIgnoreList implements GamePacketEncoder {
    private final List<IgnoreEntry> list;

    @Override
    public void log(@NotNull final Player player) {
        log(player, "");
    }

    public UpdateIgnoreList(List<IgnoreEntry> list) {
        this.list = list;
    }

    @Override
    public GamePacketOut encode() {
        final GamePacketOut buffer = ServerProt.UPDATE_IGNORELIST.gamePacketOut();
        for (final UpdateIgnoreList.IgnoreEntry entry : list) {
            buffer.writeByte(entry.added ? 1 : 0);
            buffer.writeString(StringFormatUtil.formatString(entry.username));
            buffer.writeString("");
            buffer.writeString("");
        }
        return buffer;
    }

    @Override
    public LogLevel level() {
        return LogLevel.LOW_PACKET;
    }

    public static final class IgnoreEntry {
        private final String username;
        private final boolean added;

        public IgnoreEntry(String username, boolean added) {
            this.username = username;
            this.added = added;
        }
    }

}
