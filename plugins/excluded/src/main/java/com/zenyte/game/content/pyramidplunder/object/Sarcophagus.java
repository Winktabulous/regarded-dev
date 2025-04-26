package com.zenyte.game.content.pyramidplunder.object;

import com.zenyte.game.content.pyramidplunder.PlunderRoom;
import com.zenyte.game.content.pyramidplunder.PyramidPlunderConstants;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import org.jetbrains.annotations.NotNull;

/**
 * @author Christopher
 * @since 4/4/2020
 */
public class Sarcophagus implements ObjectAction {
    public static void reset(final Player player) {
        player.getVarManager().sendBit(SarcophagusOpenAction.SARCOPHAGUS_VARBIT, SarcophagusOpenAction.UNLOOTED);
    }

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final int rot = object.getRotation();
        final Location location = new Location(object);
        //A bit of terrible code to ensure the object is accessed from the correct position.
        if (rot == 1) {
            location.moveLocation(-1, 0, 0);
        } else if (rot == 0) {
            location.moveLocation(1, -1, 0);
        } else if (rot == 3) {
            location.moveLocation(2, 1, 0);
        } else if (rot == 2) {
            location.moveLocation(0, 2, 0);
        }
        //Special case cus OSRS does this so why not.
        if (!player.getLocation().matches(location)) {
            player.addWalkSteps(location.getX(), location.getY(), 1, true);
            player.lock(2);
            WorldTasksManager.schedule(() -> execute(player, object), 1);
            return;
        }
        execute(player, object);
    }

    private final void execute(@NotNull final Player player, @NotNull final WorldObject object) {
        player.faceObject(object);
        final PlunderRoom currentRoom = PlunderRoom.get(player.getVarManager().getBitValue(PlunderDoor.ROOM_VARBIT));
        player.getActionManager().setAction(new SarcophagusOpenAction(object, currentRoom));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {PyramidPlunderConstants.SARCOPHAGUS};
    }
}
