package com.zenyte.game.content.chambersofxeric.plugins.object;

import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 06/07/2019 03:56
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BronzeAxeObject implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.getRaid().ifPresent(raid -> {
            if (!player.getInventory().hasFreeSlots()) {
                player.sendMessage("You need some free inventory space to take a bronze axe.");
                return;
            }
            if (player.getInventory().containsItem(1351, 1)) {
                player.sendMessage("You already have one of those.");
                return;
            }
            player.setAnimation(new Animation(827));
            player.sendSound(new SoundEffect(2582));
            player.getInventory().addItem(1351, 1);
        });
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.BRONZE_AXE_31862 };
    }
}
