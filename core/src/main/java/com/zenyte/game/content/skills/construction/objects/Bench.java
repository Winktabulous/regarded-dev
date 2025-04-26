package com.zenyte.game.content.skills.construction.objects;

import com.zenyte.game.content.skills.construction.Construction;
import com.zenyte.game.content.skills.construction.ConstructionController;
import com.zenyte.game.content.skills.construction.ObjectInteraction;
import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.pathfinding.events.RouteEvent;
import com.zenyte.game.world.entity.pathfinding.events.player.ObjectEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.ObjectStrategy;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectHandler;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 26. veebr 2018 : 1:12.07
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class Bench implements ObjectInteraction {

    private static final Animation SITTING_DOWN = new Animation(4104);

    private static final Animation STANDING_UP = new Animation(4105);

    private static final Animation[] ANIMS = new Animation[] { new Animation(4089), new Animation(4091), new Animation(4093), new Animation(4095), new Animation(4097), new Animation(4099), new Animation(4101), new Animation(7285), new Animation(7287), new Animation(7289), new Animation(7291), new Animation(7293), new Animation(7295), new Animation(7297), new Animation(7299) };

    @Override
    public void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.setRouteEvent(new ObjectEvent(player, new ObjectStrategy(object, 0, RouteEvent.SOUTH_EXIT), () -> {
            player.stopAll();
            player.faceObject(object);
            if (player.getLocation().getChunkX() != object.getChunkX() || player.getLocation().getChunkY() != object.getChunkY()) {
                player.sendMessage("You can't reach that.");
                return;
            }
            if (!ObjectHandler.handleOptionClick(player, optionId, object)) {
                return;
            }
            if (!(player.getControllerManager().getController() instanceof ConstructionController)) {
                return;
            }
            handleObjectAction(player, player.getConstruction(), player.getConstruction().getReference(object), object, optionId, option);
        }));
    }

    @Override
    public void handleObjectAction(final Player player, final Construction construction, final RoomReference reference, final WorldObject object, final int optionId, final String option) {
        if (object.isLocked()) {
            player.sendMessage("Someone is already sitting in that bench!");
            return;
        }
        object.setLocked(true);
        player.lock(1);
        player.addWalkSteps(object.getX(), object.getY(), 1, false);
        WorldTasksManager.schedule(() -> player.getActionManager().setAction(new BenchSittingAction(object)));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.WOODEN_BENCH, ObjectId.OAK_BENCH, ObjectId.CARVED_OAK_BENCH, ObjectId.TEAK_BENCH, ObjectId.CARVED_TEAK_BENCH, ObjectId.MAHOGANY_BENCH, ObjectId.GILDED_BENCH, ObjectId.TEAK_BENCH_29270, ObjectId.TEAK_BENCH_29271, ObjectId.GNOME_BENCH_29272, ObjectId.GNOME_BENCH_29273, ObjectId.MARBLE_BENCH_29274, ObjectId.MARBLE_BENCH_29275, ObjectId.OBSIDIAN_BENCH_29276, ObjectId.OBSIDIAN_BENCH_29277 };
    }

    private static final class BenchSittingAction extends Action {

        public BenchSittingAction(final WorldObject chair) {
            object = chair;
        }

        private Location face;

        private final WorldObject object;

        private boolean sitting;

        @Override
        public boolean start() {
            face = new Location(player.getLocation());
            if (object.getType() == 10) {
                if (object.getRotation() == 0) {
                    face.moveLocation(0, -1, 0);
                } else if (object.getRotation() == 1) {
                    face.moveLocation(-1, 0, 0);
                } else if (object.getRotation() == 2) {
                    face.moveLocation(0, 1, 0);
                } else if (object.getRotation() == 3) {
                    face.moveLocation(1, 0, 0);
                }
            } else if (object.getType() == 11) {
                if (object.getRotation() == 1) {
                    face.moveLocation(-1, 1, 0);
                } else if (object.getRotation() == 0) {
                    face.moveLocation(-1, -1, 0);
                } else if (object.getRotation() == 2) {
                    face.moveLocation(1, 1, 0);
                } else if (object.getRotation() == 3) {
                    face.moveLocation(1, -1, 0);
                }
            }
            player.setFaceLocation(face);
            player.setAnimation(SITTING_DOWN);
            return true;
        }

        @Override
        public boolean process() {
            return true;
        }

        @Override
        public int processWithDelay() {
            if (!sitting) {
                sitting = true;
                player.setLocation(object);
            } else {
                player.setAnimation(ANIMS[object.getId() - (object.getId() >= 29270 ? 29263 : 13300)]);
            }
            return 0;
        }

        @Override
        public void stop() {
            player.lock(1);
            player.resetWalkSteps();
            player.setAnimation(STANDING_UP);
            WorldTasksManager.schedule(() -> {
                if (player.getLocation().getPositionHash() == object.getPositionHash()) {
                    player.addWalkSteps(face.getX() > player.getX() ? (player.getX() - 1) : face.getX() < player.getX() ? (player.getX() + 1) : player.getX(), face.getY() > player.getY() ? (player.getY() - 1) : face.getY() < player.getY() ? (player.getY() + 1) : player.getY(), 1, false);
                }
                object.setLocked(false);
            });
        }
    }
}
