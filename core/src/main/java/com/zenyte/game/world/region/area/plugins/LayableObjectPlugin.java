package com.zenyte.game.world.region.area.plugins;

import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 21/09/2019 16:17
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public interface LayableObjectPlugin {

    enum LayableObjectType {
        BIRD_SNARE,
        BOX_TRAP,
        FIRE,
        MITHRIL_SEED
    }

    boolean canLay(@NotNull final Player player, @NotNull final LayableObjectType type);

}
