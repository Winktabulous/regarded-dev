package com.zenyte.plugins.object;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.pathfinding.events.RouteEvent;
import com.zenyte.game.world.entity.pathfinding.events.player.ObjectEvent;
import com.zenyte.game.world.entity.pathfinding.events.player.TileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.ObjectStrategy;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectHandler;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 14/11/2018 21:28
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class WoodcuttingGuildLadder implements ObjectAction {

    private static final Animation climbUp = new Animation(828);

    private static final Animation climbDown = new Animation(827);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.lock(1);
        Location dest;
        if (option.equals("Climb-down")) {
            player.setAnimation(climbDown);
            dest = new Location(object.getX(), object.getY(), object.getPlane() - 1);
            WorldTasksManager.schedule(() -> player.setLocation(dest));
        } else {
            player.setAnimation(climbUp);
            final int rot = object.getRotation();
            dest = new Location(object);
            if ((rot & 1) == 1) {
                dest.moveLocation(rot == 1 ? 1 : -1, 0, 1);
            } else {
                dest.moveLocation(0, rot == 1 ? 1 : -1, 1);
            }
            WorldTasksManager.schedule(() -> player.setLocation(dest));
        }
        player.setFaceLocation(dest);
    }

    @Override
    public int getDelay() {
        return 1;
    }

    @Override
    public void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        Runnable runnable = () -> {
            final WorldObject existingObject = World.getObjectWithId(object, object.getId());
            if (existingObject == null || player.getPlane() != object.getPlane()) {
                return;
            }
            player.stopAll();
            player.faceObject(object);
            if (!ObjectHandler.handleOptionClick(player, optionId, object)) {
                return;
            }
            handleObjectAction(player, object, name, optionId, option);
        };
        RouteEvent event = object.getId() == 28858 ? new ObjectEvent(player, new ObjectStrategy(object), runnable, getDelay()) : new TileEvent(player, new TileStrategy(object), runnable, getDelay());
        player.setRouteEvent(event);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.ROPE_LADDER_28857, ObjectId.ROPE_LADDER_28858 };
    }
}
