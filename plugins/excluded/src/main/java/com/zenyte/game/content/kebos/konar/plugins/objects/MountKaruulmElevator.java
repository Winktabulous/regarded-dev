package com.zenyte.game.content.kebos.konar.plugins.objects;

import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.pathfinding.events.player.TileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 13/10/2019 | 22:40
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class MountKaruulmElevator implements ObjectAction {

    private static final Animation activationAnim = new Animation(798);

    private static final Location dungeonEntranceLoc = new Location(1311, 10188, 0);

    private static final Location elevatorPosition = new Location(1310, 3807, 0);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equals("Activate")) {
            player.lock();
            WorldTasksManager.schedule(new WorldTask() {

                int ticks;

                @Override
                public void run() {
                    if (!player.hasWalkSteps()) {
                        switch(ticks++) {
                            case 0:
                                player.faceObject(World.getObjectWithId(elevatorPosition, 34513));
                                break;
                            case 1:
                                player.setAnimation(activationAnim);
                                break;
                            case 2:
                                new FadeScreen(player, () -> player.setLocation(dungeonEntranceLoc)).fade(3);
                                stop();
                                break;
                        }
                    }
                }
            }, 0, 0);
        }
    }

    @Override
    public void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.setRouteEvent(new TileEvent(player, new TileStrategy(object), getRunnable(player, object, name, optionId, option), getDelay()));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.ELEVATOR, ObjectId.ELEVATOR_CONTROLS_34513 };
    }
}
