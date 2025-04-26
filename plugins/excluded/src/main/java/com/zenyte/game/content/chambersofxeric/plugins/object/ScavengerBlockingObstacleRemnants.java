package com.zenyte.game.content.chambersofxeric.plugins.object;

import com.zenyte.game.content.chambersofxeric.room.LargeScavengerRoom;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 05/09/2019 19:42
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ScavengerBlockingObstacleRemnants implements ObjectAction {
    private static final Animation crossRubble = new Animation(839);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        cross(player, object);
    }

    @Override
    public int getDelay() {
        return 1;
    }

    public static final void cross(@NotNull final Player player, @NotNull final WorldObject object) {
        player.lock(3);
        final int offsetX = object.getX() - player.getX();
        final int offsetY = object.getY() - player.getY();
        final Location destination = new Location(object.getX() + offsetX, object.getY() + offsetY, object.getPlane());
        player.setAnimation(crossRubble);
        player.autoForceMovement(destination, 90);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {LargeScavengerRoom.RUBBLE, LargeScavengerRoom.STUB};
    }
}
