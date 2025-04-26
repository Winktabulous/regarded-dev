package com.zenyte.game.world.entity.player.update.mask;

import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.update.UpdateMask;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Kris | 7. mai 2018 : 17:10:46
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class TemporaryMovementMask extends UpdateMask {

    @Override
    public UpdateFlag getFlag() {
        return UpdateFlag.TEMPORARY_MOVEMENT_TYPE;
    }

    @Override
    public void writePlayer(final RSBuffer buffer, final Player player, final Player processedPlayer) {
        buffer.write128Byte(processedPlayer.getUpdateFlags().get(UpdateFlag.FORCE_MOVEMENT) ? 1 : processedPlayer.isTeleported() ? 127 : (processedPlayer.getRunDirection() != -1 ? 2 : 1));
    }

}
