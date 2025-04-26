package com.zenyte.plugins.object;

import com.zenyte.game.content.skills.magic.spells.teleports.structures.LeverTeleport;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 21/06/2019 00:04
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MageArenaLever implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equalsIgnoreCase("Pull")) {
            final boolean outside = player.getLocation().matches(new Location(3105, 3956, 0));
            new LeverTeleport(outside ? new Location(3105, 3951, 0) : new Location(3105, 3956, 0), null, "... and teleport " + (outside ? "into the mage arena" : "out of the mage arena") + ".", null).teleport(player);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.LEVER_9706, ObjectId.LEVER_9707 };
    }
}
