package com.zenyte.game.world.region.area.plugins;

import com.zenyte.game.world.entity.Entity;

/**
 * @author Kris | 30. juuni 2018 : 20:53:34
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 * TODO: Rewrite
 */
public interface ProjectileClipPlugin {

	boolean isProjectileClipped(final Entity entity, final int fromX, final int fromY, final int z, final int x, final int y, final boolean checkClose, final int size, final boolean recursive);

}
