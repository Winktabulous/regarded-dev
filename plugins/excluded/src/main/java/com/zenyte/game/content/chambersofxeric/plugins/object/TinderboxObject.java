package com.zenyte.game.content.chambersofxeric.plugins.object;

import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 06/07/2019 03:57
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class TinderboxObject implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.getRaid().ifPresent(raid -> {
            if (!player.getInventory().hasFreeSlots()) {
                player.sendMessage("You need some free inventory space to take a tinderbox.");
                return;
            }
            player.setAnimation(new Animation(827));
            player.sendSound(new SoundEffect(2582));
            player.getInventory().addItem(590, 1);
        });
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.TINDERBOX };
    }
}
