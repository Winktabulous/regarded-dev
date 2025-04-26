package com.zenyte.game.content.skills.agility;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

public interface Irreversible {
    boolean checkForReverse(final Player player, final WorldObject object);
    boolean failOnReverse();
    default void onReverse(final Player player, final WorldObject object){}
}
