package com.zenyte.game.world.entity.player.update.mask;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.update.UpdateMask;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Kris | 7. mai 2018 : 17:12:05
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class AnimationMask extends UpdateMask {
    @Override
    public UpdateFlag getFlag() {
        return UpdateFlag.ANIMATION;
    }

    @Override
    public void writePlayer(final RSBuffer buffer, final Player player, final Player processedPlayer) {
        final Animation animation = Utils.getOrDefault(processedPlayer.getAnimation(), Animation.STOP);
        buffer.writeShortLE128(animation.getId());
        buffer.write128Byte(animation.getDelay());
    }

    @Override
    public void writeNPC(final RSBuffer buffer, final Player player, final NPC npc) {
        final Animation animation = Utils.getOrDefault(npc.getAnimation(), Animation.STOP);
        buffer.writeShortLE128(animation.getId());
        buffer.writeByte128(animation.getDelay());
    }

}
