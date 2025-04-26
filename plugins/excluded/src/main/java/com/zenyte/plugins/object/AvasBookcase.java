package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 14/05/2019 23:43
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class AvasBookcase implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (object.getId() != 160 && player.getX() < 3097) {
            player.sendMessage("You can't reach that.");
            return;
        }
        if (object.getId() == ObjectId.LEVER_160) {
            player.sendMessage("You pull the lever and the secret bookcase doorway opens up.");
        } else {
            player.sendMessage("You search the bookcase...");
        }
        new FadeScreen(player, () -> {
            if (object.getId() != ObjectId.LEVER_160) {
                player.sendMessage("...and find a secret passage through the wall behind it.");
            }
            player.setLocation(player.getX() < 3097 ? new Location(3098, 3358, 0) : new Location(3096, 3358, 0));
        }).fade(3);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.BOOKCASE, ObjectId.BOOKCASE_156, ObjectId.LEVER_160 };
    }
}
