package com.zenyte.plugins.object;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.itemonobject.RopeOnBattlementAction;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class CastleWarsRope implements ObjectAction {
    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (!player.inArea("Castle Wars")) {
            return;
        }
        if (player.getLocation().getPositionHash() != object.getPositionHash()) {
            player.sendMessage("You can\'t reach that!");
            return;
        }
        final int tileX = (object.getRotation() % 2 == 0) ? ((object.getRotation() == 2) ? object.getX() + 1 : object.getX() - 1) : object.getX();
        final int tileY = (object.getRotation() % 2 == 0) ? object.getY() : ((object.getRotation() == 1) ? object.getY() + 1 : object.getY() - 1);
        final Location tile = new Location(tileX, tileY, 0);
        player.setFaceLocation(tile);
        player.setAnimation(Animation.LADDER_UP);
        WorldTasksManager.schedule(() -> {
            player.setLocation(tile);
        });
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {RopeOnBattlementAction.ROPE_OBJECT_ID};
    }
}
