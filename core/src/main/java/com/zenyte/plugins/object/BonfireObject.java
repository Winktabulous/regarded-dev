package com.zenyte.plugins.object;

import com.zenyte.game.content.skills.firemaking.BonfireAction;
import com.zenyte.game.content.skills.firemaking.Firemaking;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;


/**
 * @author Kris | 10. nov 2017 : 22:20.44
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class BonfireObject implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        Firemaking logs = null;
        for (Firemaking value : Firemaking.VALUES) {
            if(player.getInventory().containsItem(value.getLogs().getId()))
                logs = value;
        }
        if(logs != null)
            player.getActionManager().setAction(new BonfireAction(logs));
        else
            player.sendMessage("You don't have any logs to add to the fire!");
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {29300};
    }
}
