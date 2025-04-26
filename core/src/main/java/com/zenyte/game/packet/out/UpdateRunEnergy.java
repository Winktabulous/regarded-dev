package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 28 jul. 2018 | 19:02:06
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class UpdateRunEnergy implements GamePacketEncoder {

    private final int energy;

    @Override
    public void log(@NotNull final Player player) {
        this.log(player, "Energy: " + energy);
    }

    public UpdateRunEnergy(int energy) {
        this.energy = energy;
    }

    @Override
    public GamePacketOut encode() {
        final GamePacketOut buffer = ServerProt.UPDATE_RUNENERGY.gamePacketOut();
        buffer.writeShort(Math.min(65535, energy * 100));
        return buffer;
    }

    @Override
    public LogLevel level() {
        return LogLevel.LOW_PACKET;
    }

}
