package com.zenyte.game.content.kebos.konar.plugins.objects;

import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.DirectionUtil;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.pathfinding.events.player.ObjectEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.ObjectStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 24/10/2019 | 20:55
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class LavaGapObstacle implements ObjectAction {

    private static final Location northDestination = new Location(1271, 10175, 0);

    private static final Location southDestination = new Location(1271, 10170, 0);

    private static final Animation jumpAnim = new Animation(1603);

    @Override
    public void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        final WorldObject obj = new WorldObject(object);
        if (player.getY() > object.getY()) {
            obj.setLocation(object.transform(1, 1, 0));
        } else {
            obj.setLocation(object.transform(1, -1, 0));
        }
        player.setRouteEvent(new ObjectEvent(player, new ObjectStrategy(obj), getRunnable(player, object, name, optionId, option), getDelay()));
    }

    /**
     * TODO: implement proper attach object with correct values so the character doesn't disappear during the animation
     */
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final WorldObject obj = new WorldObject(object);
        if (player.getY() < object.getY()) {
            obj.setLocation(obj.transform(-1, 2, 0));
        }
        // AttachedObject gap = new AttachedObject(obj, 0, 85,  3, 0, 0, 3);
        final Location currentTile = new Location(player.getLocation());
        final Location destination = currentTile.transform(0, player.getY() > object.getY() ? -6 : 6, 0);
        final int direction = DirectionUtil.getFaceDirection(destination.getX() - currentTile.getX(), destination.getY() - currentTile.getY());
        if (player.getY() > object.getY()) {
            player.addWalkSteps(currentTile.getX(), currentTile.getY() - 1, 2, false);
        } else {
            player.addWalkSteps(currentTile.getX(), currentTile.getY() + 1, 2, false);
        }
        player.lock();
        WorldTasksManager.schedule(new WorldTask() {

            int ticks;

            @Override
            public void run() {
                if (!player.hasWalkSteps()) {
                    switch(ticks++) {
                        case 1:
                            // World.sendAttachedObject(player, gap);
                            player.setAnimation(jumpAnim);
                            player.autoForceMovement(destination, 0, 60, direction);
                            break;
                        case 4:
                            player.unlock();
                            stop();
                            break;
                    }
                }
            }
        }, 0, 0);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.LAVA_GAP };
    }
}
