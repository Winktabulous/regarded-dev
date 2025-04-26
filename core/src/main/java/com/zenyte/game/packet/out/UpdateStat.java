package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 28 jul. 2018 | 19:04:32
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class UpdateStat implements GamePacketEncoder {

    private final int stat;
    private final int experience;
    private final int currentStat;

    public UpdateStat(final int stat, final double experience, final int currentStat) {
        this.stat = stat;
        this.experience = (int) experience;
        this.currentStat = Math.min(255, currentStat);
    }

    @Override
    public void log(@NotNull final Player player) {
        this.log(player, "Stat: " + stat + ", experience: " + player.getSkills().getExperience(stat) + ", current " +
                "level: " + player.getSkills().getLevel(stat));
    }

    public UpdateStat(int stat, int experience, int currentStat) {
        this.stat = stat;
        this.experience = experience;
        this.currentStat = currentStat;
    }

    @Override
    public GamePacketOut encode() {
        final GamePacketOut buffer = ServerProt.UPDATE_STAT.gamePacketOut();
        buffer.write128Byte(currentStat);
        buffer.write128Byte(stat);
        buffer.writeIntIME(experience);
        return buffer;
    }

    @Override
    public LogLevel level() {
        return LogLevel.LOW_PACKET;
    }

}
