package com.zenyte.game.content.skills.agility;

import com.zenyte.game.util.Direction;
import com.zenyte.game.util.DirectionUtil;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

public interface IrreversibleDirection extends Irreversible {

    default Direction getReverseDirection(final Player player, final WorldObject object) {
        return object.getFaceDirection();
    }

    default boolean checkForReverse(final Player player, final WorldObject object) {
        return Math.abs(getReverseDirection(player, object).getDirection() -
                DirectionUtil.getFaceDirection(object.getPosition(), player.getLocation())) > 257;
    }

    default void onReverse(final Player player, final WorldObject object){}

    boolean failOnReverse();
}
