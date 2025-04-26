package com.zenyte.game.content.skills.construction.objects.achievementgallery;

import com.zenyte.game.content.skills.construction.Construction;
import com.zenyte.game.content.skills.construction.ObjectInteraction;
import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 26. veebr 2018 : 19:42.44
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class MountedDisplayCase implements ObjectInteraction {

    private static final Animation ANIM = new Animation(645);

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.MOUNTED_EMBLEM, ObjectId.MOUNTED_COINS };
    }

    @Override
    public void handleObjectAction(Player player, Construction construction, RoomReference reference, WorldObject object, int optionId, String option) {
        if (option.equals("admire")) {
            player.lock(5);
            player.setAnimation(ANIM);
            player.sendMessage("You admire the " + object.getName().toLowerCase() + ".");
        }
    }
}
