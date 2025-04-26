package com.zenyte.game.content.chambersofxeric.plugins.object;

import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

import static com.zenyte.game.content.chambersofxeric.room.DeathlyRoom.keystoneCrystal;

/**
 * @author Kris | 06/07/2019 04:16
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class KeystoneCrystal implements ObjectAction {

    private static final Animation animation = new Animation(832);

    private static final SoundEffect sound = new SoundEffect(2582, 10, 0);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.getRaid().ifPresent(raid -> {
            if (!player.getInventory().hasFreeSlots()) {
                player.sendMessage("You need some more free inventory space to pick it up.");
                return;
            }
            if (player.getInventory().containsItem(keystoneCrystal)) {
                player.sendMessage("You already have a keystone, and see no need to take another.");
                return;
            }
            player.setAnimation(animation);
            World.sendSoundEffect(player.getLocation(), sound);
            player.getInventory().addItem(keystoneCrystal);
        });
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.KEYSTONE_CRYSTAL };
    }
}
