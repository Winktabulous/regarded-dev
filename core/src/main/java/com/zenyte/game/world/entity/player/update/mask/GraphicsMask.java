package com.zenyte.game.world.entity.player.update.mask;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.masks.UpdateFlags;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.update.UpdateMask;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Kris | 7. mai 2018 : 16:40:32
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class GraphicsMask extends UpdateMask {
    @Override
    public UpdateFlag getFlag() {
        return UpdateFlag.GRAPHICS;
    }

    @Override
    public void writePlayer(final RSBuffer buffer, final Player player, final Player processedPlayer) {
        final Graphics graphics = Utils.getOrDefault(processedPlayer.getGraphics(), Graphics.RESET);
        buffer.writeShortLE128(graphics.getId());
        buffer.writeInt(graphics.getDelay() | graphics.getHeight() << 16);
    }

    @Override
    public void writeNPC(final RSBuffer buffer, final Player player, final NPC npc) {
        final Graphics graphics = Utils.getOrDefault(npc.getGraphics(), Graphics.RESET);
        buffer.writeShort128(graphics.getId());
        buffer.writeInt(graphics.getDelay() | graphics.getHeight() << 16);
    }

    @Override public boolean apply(Player player, Entity entity, UpdateFlags flags, boolean added) {
        if (entity instanceof final Player p && p.getGraphics() != null && !p.getGraphics().canApply(player)) {
            return false;
        }
        return super.apply(player, entity, flags, added);
    }

}
