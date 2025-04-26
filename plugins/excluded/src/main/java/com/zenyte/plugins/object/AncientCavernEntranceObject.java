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
import com.zenyte.game.world.entity.player.cutscene.actions.FadeScreenAction;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectHandler;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 5 jun. 2018 | 18:06:56
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class AncientCavernEntranceObject implements ObjectAction {

    private static final Animation JUMP_ANIM = new Animation(6723);

    private static final Location ENTRANCE_LOCATION = new Location(1763, 5365, 1);

    @Override
    public void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.setRouteEvent(new TileEvent(player, new TileStrategy(new Location(player.getX(), 3515, 0)), () -> {
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
        player.lock();
        player.faceObject(object);
        WorldTasksManager.schedule(new WorldTask() {

            int ticks;

            @Override
            public void run() {
                switch(ticks++) {
                    case 1:
                        player.setAnimation(JUMP_ANIM);
                        player.setForceMovement(new ForceMovement(new Location(player.getX(), 3509, 0), 190, ForceMovement.SOUTH));
                        break;
                    case 3:
                        new FadeScreenAction(player, 3).run();
                        break;
                    case 5:
                        player.setAnimation(Animation.STOP);
                        player.setLocation(ENTRANCE_LOCATION);
                        player.lock(1);
                        player.sendMessage("You dive into the swirling maelstrom of the whirlpool.");
                        player.sendMessage("You are swirled beneath the water, the darkness and pressure are overwhelming.");
                        player.sendMessage("Mystical forces guide you into a cavern below the whirlpool.");
                        stop();
                        break;
                }
            }
        }, 0, 1);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.WHIRLPOOL_25274 };
    }
}
