package com.zenyte.game.world.entity.player.update.mask;

import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.update.UpdateMask;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Kris | 7. mai 2018 : 16:56:56
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class ForceMovementMask extends UpdateMask {

    @Override
    public UpdateFlag getFlag() {
        return UpdateFlag.FORCE_MOVEMENT;
    }

    @Override
    public void writePlayer(final RSBuffer buffer, final Player player, final Player processedPlayer) {
        writeEntity(buffer, processedPlayer);
    }

    @Override
    public void writeNPC(RSBuffer buffer, Player player, NPC npc) {
        ForceMovement forceMovement = npc.getForceMovement();
        if (forceMovement == null) {
            forceMovement = new ForceMovement(npc.getLocation(), 1, 0);
        }
        final Location first = forceMovement.getToFirstTile();
        final Location second = forceMovement.getToSecondTile();
        final int x = npc.getX();
        final int y = npc.getY();

        buffer.write128Byte(first == null ? 0 : first.getX() - x);
        buffer.writeByteC(first == null ? 0 : first.getY() - y);

        buffer.writeByte128(second == null ? 0 : second.getX() - x);
        buffer.write128Byte(second == null ? 0 : second.getY() - y);
        buffer.writeShort(forceMovement.getFirstTileTicketDelay());
        buffer.writeShort(forceMovement.getSecondTileTicketDelay());
        buffer.writeShort128(forceMovement.getDirection());
    }

    private void writeEntity(RSBuffer buffer, Entity entity) {
        ForceMovement forceMovement = entity.getForceMovement();
        if (forceMovement == null) {
            forceMovement = new ForceMovement(entity.getLocation(), 1, 0);
        }
        final Location first = forceMovement.getToFirstTile();
        final Location second = forceMovement.getToSecondTile();
        final int x = entity.getX();
        final int y = entity.getY();

        buffer.writeByte(first == null ? 0 : first.getX() - x);
        buffer.write128Byte(first == null ? 0 : first.getY() - y);

        buffer.writeByteC(second == null ? 0 : second.getX() - x);
        buffer.writeByte128(second == null ? 0 : second.getY() - y);
        buffer.writeShortLE(forceMovement.getFirstTileTicketDelay());
        buffer.writeShortLE128(forceMovement.getSecondTileTicketDelay());
        buffer.writeShortLE128(forceMovement.getDirection());
    }

}
