package com.zenyte.game.content.minigame.pestcontrol.plugins;

import com.zenyte.game.content.minigame.pestcontrol.area.AbstractLanderArea;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.RegionArea;

/**
 * @author Kris | 26. juuni 2018 : 18:13:46
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class GangplankLadderPlugin implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equals("Climb")) {
            final RegionArea area = player.getArea();
            if (area instanceof AbstractLanderArea) {
                player.setLocation(((AbstractLanderArea) area).getType().getExitPoint());
            }
            player.sendMessage("You leave the lander.");
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.LADDER_14314, ObjectId.LADDER_25629, ObjectId.LADDER_25630 };
    }
}
