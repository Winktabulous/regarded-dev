package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 28 jul. 2018 | 19:03:28
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class UpdateRunWeight implements GamePacketEncoder {

    private final Player player;

    @Override
    public void log(@NotNull final Player player) {
        this.log(player, "Weight: " + ((int) (player.getInventory().getWeight() + player.getEquipment().getWeight())));
    }

    public UpdateRunWeight(Player player) {
        this.player = player;
    }

    @Override
    public GamePacketOut encode() {
        final GamePacketOut buffer = ServerProt.UPDATE_RUNWEIGHT.gamePacketOut();
        buffer.writeShort((int) (player.getInventory().getWeight() + player.getEquipment().getWeight()));
        return buffer;
    }

    @Override
    public LogLevel level() {
        return LogLevel.LOW_PACKET;
    }

}
