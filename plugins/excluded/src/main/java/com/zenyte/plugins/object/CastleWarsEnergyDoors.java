package com.zenyte.plugins.object;

import com.zenyte.game.content.minigame.castlewars.CastleWars;
import com.zenyte.game.content.minigame.castlewars.CastleWarsArea;
import com.zenyte.game.content.minigame.castlewars.CastleWarsTeam;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class CastleWarsEnergyDoors implements ObjectAction {
    private static final int SARADOMIN_DOOR = 4469;
    private static final int ZAMORAK_DOOR = 4470;

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        final CastleWarsTeam team = CastleWars.getTeam(player);
        if (team.equals(CastleWarsTeam.SARADOMIN) && object.getId() != SARADOMIN_DOOR || team.equals(CastleWarsTeam.ZAMORAK) && object.getId() != ZAMORAK_DOOR) {
            player.sendMessage("You\'re not allowed into the other team\'s starting room.");
            return;
        }
        player.addWalkSteps(object.getX(), object.getY(), 1, true);
        WorldTasksManager.scheduleOrExecute(() -> _handleObjectAction(player, object), player.hasWalkSteps() ? 0 : -1);
    }

    private void _handleObjectAction(final Player player, final WorldObject object) {
        // leave is set to true in this instance as it should be dropped outside of the door.
        if (CastleWarsArea.hasFlag(player)) {
            CastleWarsArea.handleFlagDrop(player, false);
        }
        if (player.getLocation().getPositionHash() != object.getPositionHash()) {
            player.faceObject(object);
            player.addWalkSteps(object.getX(), object.getY(), 1, false);
        } else {
            final Location tile = new Location(getX(object), getY(object), 1);
            player.setFaceLocation(tile);
            player.addWalkSteps(tile.getX(), tile.getY(), 1, false);
        }
    }

    private int getX(final WorldObject object) {
        switch (object.getRotation()) {
        case 0: 
        case 2: 
            return object.getRotation() == 0 ? object.getX() - 1 : object.getX() + 1;
        default: 
            return object.getX();
        }
    }

    private int getY(final WorldObject object) {
        switch (object.getRotation()) {
        case 1: 
        case 3: 
            return object.getRotation() == 1 ? object.getY() + 1 : object.getY() - 1;
        default: 
            return object.getY();
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {SARADOMIN_DOOR, ZAMORAK_DOOR};
    }
}
