package com.zenyte.plugins.object;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Setting;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 1 jan. 2018 : 18:07:47
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class GodwarsEntranceObject implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (!player.getBooleanAttribute(Setting.GODWARS_ENTRANCE_ROPE.toString())) {
            if (player.getInventory().containsItem(new Item(954))) {
                player.getSettings().toggleSetting(Setting.GODWARS_ENTRANCE_ROPE);
                player.getInventory().deleteItem(new Item(954));
            } else {
                player.sendMessage("You aren't carrying a rope with you.");
            }
        } else {
            player.useStairs(828, new Location(2882, 5311, 2), 1, 0, "You climb down the rope.");
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 26419 };
    }
}
