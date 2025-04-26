package com.zenyte.plugins.object;

import com.zenyte.game.content.skills.magic.spells.teleports.TeleportCollection;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 28/04/2019 19:20
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PuroPuroPortal implements ObjectAction {
    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        TeleportCollection.ZANARIS_CENTER_OF_CROP_CIRCLE.teleport(player);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {
                25014
        };
    }
}
