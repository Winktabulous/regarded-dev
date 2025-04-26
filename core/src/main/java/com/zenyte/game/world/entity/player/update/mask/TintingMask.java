package com.zenyte.game.world.entity.player.update.mask;

import com.google.common.base.Preconditions;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Tinting;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.masks.UpdateFlags;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.update.UpdateMask;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Kris | 11/06/2022
 */
public class TintingMask extends UpdateMask {
    @Override
    public UpdateFlag getFlag() {
        return UpdateFlag.TINTING;
    }

    @Override
    public void writePlayer(final RSBuffer buffer, final Player player, final Player processedPlayer) {
        final Tinting tinting = processedPlayer.getTinting();
        Preconditions.checkNotNull(tinting);
        buffer.writeShortLE(tinting.getDelay());
        buffer.writeShort128(tinting.getDuration() + tinting.getDelay());
        buffer.writeByteC(tinting.getHue());
        buffer.writeByte128(tinting.getSaturation());
        buffer.write128Byte(tinting.getLuminance());
        buffer.write128Byte(tinting.getOpacity());
    }

    @Override
    public void writeNPC(final RSBuffer buffer, final Player player, final NPC npc) {
        final Tinting tinting = npc.getTinting();
        Preconditions.checkNotNull(tinting);
        buffer.writeShort128(tinting.getDelay());
        buffer.writeShort(tinting.getDuration() + tinting.getDelay());
        buffer.writeByte(tinting.getHue());
        buffer.writeByteC(tinting.getSaturation());
        buffer.writeByte128(tinting.getLuminance());
        buffer.write128Byte(tinting.getOpacity());
    }

    @Override public boolean apply(Player player, Entity entity, UpdateFlags flags, boolean added) {
        if (entity instanceof final Player p && p.getTinting() != null && !p.getTinting().canApply(player)) {
            return false;
        }
        return super.apply(player, entity, flags, added);
    }
}
