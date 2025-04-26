package com.zenyte.plugins.itemonobject;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 26/03/2019 14:20
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BucketOnSand implements ItemOnObjectAction {

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        player.getActionManager().setAction(new BucketSandFilling(object));
    }

    @Override
    public Object[] getItems() {
        return new Object[] { 1925 };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.SANDPIT, ObjectId.SAND_PIT, 4373, ObjectId.SANDPIT_22726 };
    }

    private static final class BucketSandFilling extends Action {

        private final WorldObject object;

        private static final Animation animation = new Animation(895);

        private static final SoundEffect synth = new SoundEffect(2584);

        @Override
        public boolean start() {
            player.setAnimation(animation);
            player.sendSound(synth);
            delay(1);
            return true;
        }

        @Override
        public boolean process() {
            return player.getInventory().containsItem(1925, 1);
        }

        @Override
        public int processWithDelay() {
            player.setAnimation(animation);
            player.sendSound(synth);
            player.faceObject(object);
            player.sendFilteredMessage("You fill the bucket with sand.");
            player.getInventory().ifDeleteItem(new Item(1925, 1), () -> player.getInventory().addOrDrop(new Item(1783, 1)));
            return 2;
        }

        public BucketSandFilling(WorldObject object) {
            this.object = object;
        }
    }
}
