package com.near_reality.game.content.gauntlet.plugins.resources;

import com.near_reality.game.content.gauntlet.actions.CrystalDepositAction;
import com.near_reality.game.content.gauntlet.objects.CrystalDeposit;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Andys1814.
 * @since 1/25/2022.
 */
public final class GauntletCrystalDeposit implements ObjectAction {

    private static final int CRYSTAL_DEPOSIT = 36064;

    private static final int CRYSTAL_DEPOSIT_CORRUPTED = 35967;

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (!(object instanceof CrystalDeposit)) {
            return;
        }
        final CrystalDeposit deposit = (CrystalDeposit) object;

        boolean corrupted = object.getId() == CRYSTAL_DEPOSIT_CORRUPTED;
        player.getActionManager().setAction(new CrystalDepositAction(deposit, corrupted));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { CRYSTAL_DEPOSIT, CRYSTAL_DEPOSIT_CORRUPTED };
    }

}
