package com.zenyte.plugins.object;

import com.zenyte.game.content.skills.mining.actions.DenseRunestoneMining;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 26/04/2019 17:22
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DenseRunestoneObject implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equals("Chip")) {
            player.getActionManager().setAction(new DenseRunestoneMining(object));
        } else if (option.equals("Check")) {
            player.sendMessage("The runestone has depleted.");
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 8981, 10796 };
    }
}
