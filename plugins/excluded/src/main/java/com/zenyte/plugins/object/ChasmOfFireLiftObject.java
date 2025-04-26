package com.zenyte.plugins.object;

import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Tommeh | 24-3-2019 | 22:05
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ChasmOfFireLiftObject implements ObjectAction {

    private enum ChasmOfFireLift {

        UPPER_LEVEL_NORTH(new Location(1437, 10094, 3), new Location(1437, 10093, 2)),
        UPPER_LEVEL_SOUTH(new Location(1452, 10068, 3), new Location(1457, 10095, 2)),
        MIDDLE_LEVEL_FIRST(new Location(1437, 10094, 2), new Location(1438, 10093, 3)),
        MIDDLE_LEVEL_SECOND(new Location(1458, 10095, 2), new Location(1457, 10095, 1)),
        MIDDLE_LEVEL_THIRD(new Location(1458, 10075, 2), new Location(1457, 10075, 1)),
        MIDDLE_LEVEL_FOURTH(new Location(1452, 10068, 2), new Location(1451, 10069, 3)),
        UPPER_LEVEL_FIRST(new Location(1458, 10095, 1), new Location(1457, 10095, 2)),
        UPPER_LEVEL_SECOND(new Location(1458, 10075, 1), new Location(1451, 10069, 2));

        private final Location location;

        private final Location destination;

        private static final Set<ChasmOfFireLift> ALL = EnumSet.allOf(ChasmOfFireLift.class);

        private static final Map<Integer, ChasmOfFireLift> LIFTS = new HashMap<>();

        public static ChasmOfFireLift get(final Location location) {
            return LIFTS.get(location.getPositionHash());
        }

        static {
            for (final ChasmOfFireLiftObject.ChasmOfFireLift lift : ALL) {
                LIFTS.put(lift.getLocation().getPositionHash(), lift);
            }
        }

        ChasmOfFireLift(Location location, Location destination) {
            this.location = location;
            this.destination = destination;
        }

        public Location getLocation() {
            return location;
        }

        public Location getDestination() {
            return destination;
        }
    }

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final ChasmOfFireLiftObject.ChasmOfFireLift lift = ChasmOfFireLift.get(object);
        if (lift == null) {
            return;
        }
        final boolean descending = object.getPlane() > lift.getDestination().getPlane();
        player.lock();
        player.getDialogueManager().start(new PlainChat(player, "You step inside the gibbet.<br><br>The chain creaks as you " + (descending ? "descend" : "ascend") + " further into the Chasm.", false));
        new FadeScreen(player, () -> {
            player.unlock();
            player.setLocation(lift.getDestination());
            player.getInterfaceHandler().closeInterface(InterfacePosition.DIALOGUE);
        }).fade(2);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.LIFT_30258, ObjectId.LIFT_30259 };
    }
}
