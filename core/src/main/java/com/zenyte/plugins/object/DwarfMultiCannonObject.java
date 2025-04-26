package com.zenyte.plugins.object;

import com.zenyte.game.content.multicannon.DwarfMultiCannonType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 10. nov 2017 : 22:17.17
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
@SuppressWarnings("unused")
public final class DwarfMultiCannonObject implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (!player.getDwarfMulticannon().handleCannon(object, optionId)) {
            player.sendMessage("This is not your cannon.");
        }
    }

    @Override
    public Object[] getObjects() {
        DwarfMultiCannonType type1 = DwarfMultiCannonType.REGULAR;
        DwarfMultiCannonType type2 = DwarfMultiCannonType.ORNAMENT;
        return new Object[] {
                type1.getBaseLoc(), type1.getStandLoc(), type1.getBarrelsLoc(), type1.getCannonLoc(), type1.getBrokenCannonLoc(),
                type2.getBaseLoc(), type2.getStandLoc(), type2.getBarrelsLoc(), type2.getCannonLoc(), type2.getBrokenCannonLoc(),
        };
    }
}
