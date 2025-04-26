package com.zenyte.game.content.skills.agility;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import org.apache.commons.lang3.ArrayUtils;

public interface IrreversibleObject extends Irreversible {
    default boolean checkForReverse(final Player player, final WorldObject object) {
        return ArrayUtils.contains(getFailObjectIds(), object.getId());
    }

    default void onReverse(final Player player, final WorldObject object){}

    int[] getFailObjectIds();
    boolean failOnReverse();
}
