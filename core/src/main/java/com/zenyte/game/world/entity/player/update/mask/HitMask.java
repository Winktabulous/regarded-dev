package com.zenyte.game.world.entity.player.update.mask;

import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.HitBar;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.update.UpdateMask;
import com.zenyte.net.io.RSBuffer;

import java.util.List;

/**
 * @author Kris | 7. mai 2018 : 17:05:32
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 * profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 * profile</a>}
 */
public final class HitMask extends UpdateMask {
    @Override
    public UpdateFlag getFlag() {
        return UpdateFlag.HIT;
    }

    @Override
    public void writePlayer(final RSBuffer buffer, final Player player, final Player processedPlayer) {
        final boolean myPlayer = player.equals(processedPlayer);
        final List<Hit> hits = processedPlayer.getNextHits();

        int length = Math.min(255, hits.size());
        buffer.writeByte128(length);
        Hit hit;
        for (int i = 0; i < length; i++) {
            hit = hits.get(i);
            Entity source = hit.getSource();
            hit.getAppliedSplat().writeMask(buffer, !myPlayer && !player.equals(source));
            buffer.writeSmart(hit.getDamage());
            buffer.writeSmart(hit.getDelay());
        }
        final List<HitBar> bars = processedPlayer.getHitBars();
        length = Math.min(255, bars.size());
        buffer.write128Byte(length);
        HitBar bar;
        for (int i = 0; i < length; i++) {
            bar = bars.get(i);
            buffer.writeSmart(bar.getType());
            buffer.writeSmart(bar.interpolateTime());
            if (bar.interpolateTime() != 32767) {
                buffer.writeSmart(bar.getDelay());
                buffer.writeByte128(bar.getPercentage());
                if (bar.interpolateTime() > 0) {
                    buffer.writeByte128(bar.interpolatePercentage());
                }
            }
        }
    }

    @Override
    public void writeNPC(final RSBuffer buffer, final Player player, final NPC npc) {
        final List<Hit> hits = npc.getNextHits();

        int length = Math.min(255, hits.size());
        buffer.writeByte(length);
        Hit hit;
        for (int i = 0; i < length; i++) {
            hit = hits.get(i);
            Entity source = hit.getSource();
            hit.getAppliedSplat().writeMask(buffer, !player.equals(source));
            buffer.writeSmart(hit.getDamage());
            buffer.writeSmart(hit.getDelay());
        }
        final List<HitBar> bars = npc.getHitBars();
        length = Math.min(255, bars.size());
        buffer.write128Byte(length);
        HitBar bar;
        for (int i = 0; i < length; i++) {
            bar = bars.get(i);
            buffer.writeSmart(bar.getType());
            buffer.writeSmart(bar.interpolateTime());
            if (bar.interpolateTime() != 32767) {
                buffer.writeSmart(bar.getDelay());
                buffer.writeByteC(bar.getPercentage());
                if (bar.interpolateTime() > 0) {
                    buffer.writeByteC(bar.interpolatePercentage());
                }
            }
        }
    }

}
