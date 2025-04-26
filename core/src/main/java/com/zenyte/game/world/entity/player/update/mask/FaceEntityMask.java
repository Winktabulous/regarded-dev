package com.zenyte.game.world.entity.player.update.mask;

import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.masks.UpdateFlags;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.update.UpdateMask;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Kris | 7. mai 2018 : 17:02:41
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class FaceEntityMask extends UpdateMask {

    @Override
    public UpdateFlag getFlag() {
        return UpdateFlag.FACE_ENTITY;
    }

    @Override
    public boolean apply(Player player, final Entity entity, final UpdateFlags flags, final boolean added) {
        return flags.get(UpdateFlag.FACE_ENTITY) || entity.getFaceEntity() != -1 || added;
    }

    @Override
    public void writePlayer(final RSBuffer buffer, final Player player, final Player processedPlayer) {
        buffer.writeShortLE(processedPlayer.getFaceEntity());
    }

    @Override
    public void writeNPC(final RSBuffer buffer, final Player player, final NPC npc) {
        int index = npc.getFaceEntity();
        if (index == 65535) {
            index = 16777215;
        }

        buffer.writeShortLE128(index & 0xffff);
        buffer.write128Byte(index >>> 16);
    }

}
