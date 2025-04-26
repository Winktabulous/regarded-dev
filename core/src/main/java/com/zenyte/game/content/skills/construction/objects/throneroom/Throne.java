package com.zenyte.game.content.skills.construction.objects.throneroom;

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
 * @author Kris | 26. veebr 2018 : 18:49.11
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class Throne implements ObjectInteraction {

    private static final Animation SITTING_DOWN = new Animation(4104);

    private static final Animation STANDING_UP = new Animation(4105);

    private static final Animation[] ANIMS = new Animation[] { new Animation(4111), new Animation(4112), new Animation(4113), new Animation(4114), new Animation(4115), new Animation(4116), new Animation(4117) };

    @Override
    public void handleObjectAction(final Player player, final Construction construction, final RoomReference reference, final WorldObject object, final int optionId, final String option) {
        if (object.isLocked()) {
            player.sendMessage("Someone is already sitting on that throne!");
            return;
        }
        object.setLocked(true);
        player.lock(1);
        player.addWalkSteps(object.getX(), object.getY(), 1, false);
        final Location face = new Location(player.getLocation());
        if (object.getType() == 10) {
            if (object.getRotation() == 0)
                face.moveLocation(0, -1, 0);
            else if (object.getRotation() == 1)
                face.moveLocation(-1, 0, 0);
            else if (object.getRotation() == 2)
                face.moveLocation(0, 1, 0);
            else if (object.getRotation() == 3)
                face.moveLocation(1, 0, 0);
        }
        player.setFaceLocation(face);
        WorldTasksManager.schedule(() -> {
            player.setFaceLocation(face);
            player.getActionManager().setAction(new ThroneSittingAction(object, face));
        });
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.THRONE_13665, ObjectId.THRONE_13666, ObjectId.THRONE_13667, ObjectId.THRONE_13668, ObjectId.THRONE_13669, ObjectId.THRONE_13670, ObjectId.THRONE_13671 };
    }

    private static final class ThroneSittingAction extends Action {

        public ThroneSittingAction(final WorldObject chair, final Location face) {
            object = chair;
            this.face = face;
        }

        private final Location face;

        private final WorldObject object;

        private boolean sitting;

        @Override
        public boolean start() {
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
                player.setAnimation(ANIMS[object.getId() - 13665]);
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
