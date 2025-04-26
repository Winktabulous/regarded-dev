/**
 */
package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.SeersTrapdoorD;

/**
 * @author Noele | May 1, 2018 : 3:51:25 AM
 * @see https://noeles.life || noele@zenyte.com
 */
public class SeersVillageAgilityTrapdoor implements ObjectAction {

    private static final Location TOP = new Location(2714, 3472, 3);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (object.getId() == ObjectId.LADDER_26118) {
            player.useStairs(828, TOP, 1, 1);
            player.addAttribute("SeersTrapdoor", 1);
            return;
        }
        if (object.getId() == ObjectId.TRAPDOOR_26119) {
            player.getDialogueManager().start(new SeersTrapdoorD(player));
            return;
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.LADDER_26118, ObjectId.TRAPDOOR_26119 };
    }
}
