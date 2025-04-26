package com.near_reality.game.content.gauntlet.plugins.resources;

import com.near_reality.game.content.gauntlet.actions.LinumTirinumAction;
import com.near_reality.game.content.gauntlet.objects.LinumTirinum;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Andys1814
 */
public final class GauntletLinumTirinum implements ObjectAction {

    private static final int LINUM_TIRINUM = 36072;

    private static final int LINUM_TIRINUM_CORRUPTED = 35975;

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (!(object instanceof LinumTirinum)) {
            return;
        }
        final LinumTirinum linum = (LinumTirinum) object;

        boolean corrupted = object.getId() == LINUM_TIRINUM_CORRUPTED;
        player.getActionManager().setAction(new LinumTirinumAction(linum, corrupted));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { LINUM_TIRINUM, LINUM_TIRINUM_CORRUPTED };
    }

}
