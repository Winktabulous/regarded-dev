package com.zenyte.game.content.skills.construction.objects;

import com.zenyte.game.content.skills.construction.Construction;
import com.zenyte.game.content.skills.construction.ObjectInteraction;
import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 24. veebr 2018 : 3:29.02
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class Chair implements ObjectInteraction {

    private static final Animation SITTING_DOWN = new Animation(4104);

    private static final Animation STANDING_UP = new Animation(4105);

    private static final Animation[] ANIMS = new Animation[] { new Animation(4073), new Animation(4075), new Animation(4079), new Animation(4081), new Animation(4083), new Animation(4085), new Animation(4087) };

    @Override
    public void handleObjectAction(final Player player, final Construction construction, final RoomReference reference, final WorldObject object, final int optionId, final String option) {
        if (object.isLocked()) {
            player.sendMessage("Someone is already sitting in that chair!");
            return;
        }
        object.setLocked(true);
        player.lock(1);
        player.addWalkSteps(object.getX(), object.getY(), 1, false);
        WorldTasksManager.schedule(() -> player.getActionManager().setAction(new ChairSittingAction(object)));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CHAIR_6752, ObjectId.CHAIR_6753, ObjectId.CHAIR_6754, ObjectId.CHAIR_6755, ObjectId.CHAIR_6756, ObjectId.CHAIR_6757, ObjectId.CHAIR_6758 };
    }

    private static final class ChairSittingAction extends Action {

        public ChairSittingAction(final WorldObject chair) {
            object = chair;
        }

        private Location face;

        private final WorldObject object;

        private boolean sitting;

        @Override
        public boolean start() {
            face = new Location(player.getLocation());
            if (object.getType() == 10) {
                if (object.getRotation() == 0)
                    face.moveLocation(0, -1, 0);
                else if (object.getRotation() == 1)
                    face.moveLocation(-1, 0, 0);
                else if (object.getRotation() == 2)
                    face.moveLocation(0, 1, 0);
                else if (object.getRotation() == 3)
                    face.moveLocation(1, 0, 0);
            } else if (object.getType() == 11) {
                if (object.getRotation() == 1)
                    face.moveLocation(-1, 1, 0);
                else if (object.getRotation() == 0)
                    face.moveLocation(-1, -1, 0);
                else if (object.getRotation() == 2)
                    face.moveLocation(1, 1, 0);
                else if (object.getRotation() == 3)
                    face.moveLocation(1, -1, 0);
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
            } else
                player.setAnimation(ANIMS[object.getId() - 6752]);
            return 0;
        }

        @Override
        public void stop() {
            player.lock(3);
            player.setAnimation(STANDING_UP);
            WorldTasksManager.schedule(() -> {
                if (player.getLocation().getPositionHash() == object.getPositionHash())
                    player.addWalkSteps(face.getX(), face.getY(), 1, false);
                object.setLocked(false);
            });
        }
    }
}
