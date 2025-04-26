package com.zenyte.plugins.object;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 27/06/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class LeafyPalmTree implements ObjectAction {

    private final Animation animation = new Animation(810);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        object.setLocked(true);
        player.setAnimation(animation);
        World.spawnFloorItem(new Item(ItemId.PALM_LEAF), player, object.transform(Direction.values[Utils.random(Direction.values.length - 1)]));
        World.spawnObject(new WorldObject(ObjectId.LEAFY_PALM_TREE_2976, object.getType(), object.getRotation(), object));
        WorldTasksManager.schedule(() -> {
            object.setLocked(false);
            World.spawnObject(object);
        }, 45);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {
                ObjectId.LEAFY_PALM_TREE
        };
    }
}
