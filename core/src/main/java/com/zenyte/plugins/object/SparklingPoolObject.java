package com.zenyte.plugins.object;

import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.pathfinding.events.player.TileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.*;

/**
 * @author Tommeh | 29 mei 2018 | 01:48:21
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class SparklingPoolObject implements ObjectAction {

    private static final Location UPPER_START = new Location(2542, 4718, 0);

    private static final Location LOWER_START = new Location(2509, 4689, 0);

    private static final AttachedObject UPPER = new AttachedObject(new WorldObject(2878, 10, 0, 2541, 4719, 0), 0, 120, -2, 2, -2, 2);

    private static final AttachedObject LOWER = new AttachedObject(new WorldObject(2879, 10, 0, 2508, 4686, 0), 0, 120, -2, 2, -2, 2);

    private static final Location MIDDLE_UPPER = new Location(2542, 4720, 0);

    private static final Location MIDDLE_LOWER = new Location(2509, 4687, 0);

    private static final Location DESTINATION_UPPER = new Location(2509, 4689, 0);

    private static final Location DESTINATION_LOWER = new Location(2542, 4718, 0);

    private static final Animation JUMP_ANIM = new Animation(741, 20);

    private static final Animation FIRST_ANIM = new Animation(804);

    private static final Graphics SPLASHES_GFX = new Graphics(68);

    @Override
    public void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.setRouteEvent(new TileEvent(player, new TileStrategy(object.getId() == 2878 ? UPPER_START : LOWER_START), () -> {
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
    public int getDelay() {
        return 1;
    }

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        final Location middle = object.getId() == 2878 ? MIDDLE_UPPER : MIDDLE_LOWER;
        final Location destination = object.getId() == 2878 ? DESTINATION_UPPER : DESTINATION_LOWER;
        player.getDialogueManager().start(new Dialogue(player) {

            @Override
            public void buildDialogue() {
                plain("You step into the pool of sparkling water. You feel an energy rush<br><br>through your veins.").executeAction(() -> {
                    World.sendAttachedObject(player, object.getId() == 2878 ? UPPER : LOWER);
                    player.setAnimation(JUMP_ANIM);
                    player.setForceMovement(new ForceMovement(player.getLocation(), 15, middle, 60, object.getId() == 2878 ? ForceMovement.NORTH : ForceMovement.SOUTH));
                    WorldTasksManager.schedule(new WorldTask() {

                        int ticks;

                        @Override
                        public void run() {
                            switch(ticks++) {
                                case 0:
                                    player.setLocation(middle);
                                    break;
                                case 1:
                                    player.setAnimation(FIRST_ANIM);
                                    player.setGraphics(SPLASHES_GFX);
                                    break;
                                case 2:
                                    player.setAnimation(Animation.STOP);
                                    player.setLocation(destination);
                                    stop();
                                    break;
                            }
                        }
                    }, 0, 1);
                });
            }
        });
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.SPARKLING_POOL, ObjectId.SPARKLING_POOL_2879 };
    }
}
