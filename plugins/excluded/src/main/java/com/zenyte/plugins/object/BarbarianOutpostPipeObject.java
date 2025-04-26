package com.zenyte.plugins.object;

import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.pathfinding.events.player.TileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectHandler;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 5 jun. 2018 | 20:41:39
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class BarbarianOutpostPipeObject implements ObjectAction {

    private static final Animation CLIMB_ANIM = new Animation(749);

    private static final Location START_NORTH = new Location(2552, 3561, 0);

    private static final Location START_SOUTH = new Location(2552, 3558, 0);

    @Override
    public void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.setRouteEvent(new TileEvent(player, new TileStrategy(player.getY() >= 3560 ? START_NORTH : START_SOUTH), () -> {
            if (World.getObjectWithId(object, object.getId()) == null || player.getPlane() != object.getPlane()) {
                return;
            }
            player.stopAll();
            player.faceObject(object);
            if (!ObjectHandler.handleOptionClick(player, optionId, object)) {
                return;
            }
            handleObjectAction(player, object, name, optionId, option);
        }, getDelay()));
    }

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (player.getSkills().getLevel(SkillConstants.AGILITY) < 35) {
            player.sendMessage("You need an Agility level of at least 35 to go through the pipe.");
            return;
        }
        final boolean north = player.getY() >= 3560;
        player.lock(3);
        WorldTasksManager.schedule(new WorldTask() {

            int ticks;

            @Override
            public void run() {
                switch(ticks++) {
                    case 1:
                        player.setAnimation(CLIMB_ANIM);
                        player.setForceMovement(new ForceMovement(new Location(object.getX(), north ? object.getY() - 1 : object.getY() + 2, 0), 100, north ? ForceMovement.SOUTH : ForceMovement.NORTH));
                        break;
                    case 2:
                        player.setLocation(north ? START_SOUTH : START_NORTH);
                        break;
                }
            }
        }, 0, 1);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.OBSTACLE_PIPE_20210 };
    }
}
