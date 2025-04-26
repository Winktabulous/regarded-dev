package com.zenyte.game.world.region.area.plugins;

import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import org.jetbrains.annotations.Nullable;

/**
 * @author Tommeh | 29-11-2018 | 22:27
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public interface EntityAttackPlugin {

    default boolean attack(final Player player, final Entity entity, @Nullable PlayerCombat combat) {
        return false;
    }

    default boolean startAttack(final Player player, final Entity entity) {
        return true;
    }
}
