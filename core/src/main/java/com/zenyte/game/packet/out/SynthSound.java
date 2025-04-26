package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 26. veebr 2018 : 2:06.19
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class SynthSound implements GamePacketEncoder {
    private final SoundEffect sound;

    @Override
    public void log(@NotNull final Player player) {
        this.log(player, "Sound: " + sound.getId() + ", delay: " + sound.getDelay());
    }

    public SynthSound(SoundEffect sound) {
        this.sound = sound;
    }

    @Override
    public GamePacketOut encode() {
        final GamePacketOut buffer = ServerProt.SYNTH_SOUND.gamePacketOut();
        buffer.writeShort(sound.getId());
        buffer.writeByte(sound.getRepetitions());
        buffer.writeShort(sound.getDelay());
        return buffer;
    }

    @Override
    public LogLevel level() {
        return LogLevel.LOW_PACKET;
    }

}
