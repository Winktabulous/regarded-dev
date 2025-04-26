package com.zenyte.game.world.entity.player.update.mask;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Entity.EntityType;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.masks.UpdateFlags;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.update.UpdateMask;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Kris | 7. mai 2018 : 17:14:26
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class FaceLocationMask extends UpdateMask {
    @Override
    public UpdateFlag getFlag() {
        return UpdateFlag.FACE_COORDINATE;
    }

    @Override
    public boolean apply(Player player, final Entity entity, final UpdateFlags flags, final boolean added) {
        return flags.get(getFlag()) || entity.getEntityType() == EntityType.PLAYER && added;
    }

    @Override
    public void writePlayer(final RSBuffer buffer, final Player player, final Player processedPlayer) {
        buffer.writeShort(processedPlayer.getDirection());
    }

    @Override
    public void writeNPC(final RSBuffer buffer, final Player player, final NPC npc) {
        final Location tile = Utils.getOrDefault(npc.getFaceLocation(), npc.getLocation());
        buffer.writeShortLE128((tile.getX() << 1) + 1);
        buffer.writeShort128((tile.getY() << 1) + 1);
        buffer.writeByte(0);
    }

}
