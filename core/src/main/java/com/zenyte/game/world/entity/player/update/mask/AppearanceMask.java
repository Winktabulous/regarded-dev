package com.zenyte.game.world.entity.player.update.mask;

import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.masks.UpdateFlags;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.update.UpdateMask;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Kris | 7. mai 2018 : 17:16:20
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class AppearanceMask extends UpdateMask {

    @Override
    public UpdateFlag getFlag() {
        return UpdateFlag.APPEARANCE;
    }

    public boolean apply(Player player, final Entity entity, final UpdateFlags flags, final boolean added) {
        return added || flags.get(getFlag());
    }

    @Override
    public void writePlayer(final RSBuffer buffer, final Player player, final Player processedPlayer) {
        processedPlayer.getAppearance().writeAppearanceData(buffer);
    }

}
