package com.near_reality.game.content.gauntlet.plugins.resources;

import com.near_reality.game.content.gauntlet.actions.PhrenRootsAction;
import com.near_reality.game.content.gauntlet.objects.PhrenRoots;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

public final class GauntletPhrenRoots implements ObjectAction {

    private static final int PHREN_ROOTS = 36066;

    private static final int PHREN_ROOTS_CORRUPTED = 35969;

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (!(object instanceof PhrenRoots)) {
            return;
        }
        final PhrenRoots roots = (PhrenRoots) object;

        boolean corrupted = object.getId() == PHREN_ROOTS_CORRUPTED;
        player.getActionManager().setAction(new PhrenRootsAction(roots, corrupted));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { PHREN_ROOTS, PHREN_ROOTS_CORRUPTED };
    }

}
