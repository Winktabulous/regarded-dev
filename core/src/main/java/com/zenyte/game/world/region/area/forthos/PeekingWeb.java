package com.zenyte.game.world.region.area.forthos;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.plugins.object.SpiderWebObjectAction;

/**
 * @author Andys1814
 */
public final class PeekingWeb implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        switch (optionId) {
            case 1:
//                player.getCombat
                SpiderWebObjectAction.slash(player, object, player.getWeapon());
                return;
            case 2:
                int count = GlobalAreaManager.getArea(SarachnisArea.class).getPlayers().size();
                String label = count == 1 ? "adventurer" : "adventurers";
                player.sendMessage("You listen and hear " + Utils.convertNumberToRelative(count) + " " + label + " inside the crypt.");
                return;
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 34898 };
    }

}
