package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 26. veebr 2018 : 1:58.03
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class AreaSound implements GamePacketEncoder {
    private final Player player;
    private final Location tile;
    private final SoundEffect sound;

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Id: " + sound.getId() + ", radius: " + sound.getRadius() + ", delay: " + sound.getDelay());
    }

    public AreaSound(Player player, Location tile, SoundEffect sound) {
        this.player = player;
        this.tile = tile;
        this.sound = sound;
    }

    @Override
    public GamePacketOut encode() {
        final GamePacketOut buffer = ServerProt.AREA_SOUND.gamePacketOut();
        buffer.writeShortLE(sound.getId());
        buffer.writeByteC((sound.getRadius() << 4) | (sound.getRepetitions() & 15));
        buffer.writeByte128(sound.getDelay());
        buffer.writeByte128(tile.getLocalHash(player.getLastLoadedMapRegionTile()));
        return buffer;
    }

    @Override
    public LogLevel level() {
        return LogLevel.LOW_PACKET;
    }

}
