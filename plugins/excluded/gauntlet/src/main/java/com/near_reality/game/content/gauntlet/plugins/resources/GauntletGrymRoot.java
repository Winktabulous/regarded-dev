package com.near_reality.game.content.gauntlet.plugins.resources;

import com.near_reality.game.content.gauntlet.actions.GrymRootAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

public final class GauntletGrymRoot implements ObjectAction {

    private static final int GRYM_ROOT = 36070;

    private static final int GRYM_ROOT_CORRUPTED = 35973;

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.getActionManager().setAction(new GrymRootAction(object, object.getId() == GRYM_ROOT_CORRUPTED));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { GRYM_ROOT, GRYM_ROOT_CORRUPTED };
    }

}
