package com.zenyte.game.content.chambersofxeric.skills;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;

/**
 * @author Kris | 17. nov 2017 : 7:05.15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class GourdPicking extends Action {

    /**
     * The player animation that is played when the player picks a gourd fruit.
     */
    private static final Animation pickingAnimation = new Animation(2280);

    /**
     * The sound effect that is played when the player picks a gourd fruit.
     */
    private static final SoundEffect sound = new SoundEffect(2437);
    /**
     * Whether or not the player is picking the gourd fruits one by one, or entire inventory at once.
     */
    private final boolean singular;

    public GourdPicking(final boolean singular) {
        this.singular = singular;
    }

    @Override
    public boolean start() {
        if (!player.getInventory().hasFreeSlots()) {
            player.sendMessage("You need some more free inventory space to pick the gourd tree.");
            return false;
        }
        player.setAnimation(pickingAnimation);
        delay(2);
        return true;
    }

    @Override
    public boolean process() {
        return player.getInventory().hasFreeSlots();
    }

    @Override
    public int processWithDelay() {
        player.getInventory().addItem(new Item(20800, singular ? 1 : Integer.MAX_VALUE));
        player.sendMessage("You pick " + (singular ? "a" : "some") + " gourd fruit from the tree, tearing the " + (singular ? "top" : "tops") + " off in the process.");
        player.sendSound(sound);
        if (singular) {
            player.setAnimation(pickingAnimation);
            return 2;
        }
        return -1;
    }

    @Override
    public void stop() {
    }

}
