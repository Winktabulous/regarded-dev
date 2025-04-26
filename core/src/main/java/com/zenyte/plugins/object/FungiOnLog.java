package com.zenyte.plugins.object;

import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 14/06/2019 11:27
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class FungiOnLog implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equalsIgnoreCase("Pick")) {
            player.setAnimation(new Animation(827));
            final boolean doubleLoot = DiaryUtil.eligibleFor(DiaryReward.MORYTANIA_LEGS3, player);
            player.getInventory().addOrDrop(new Item(2970, doubleLoot ? 2 : 1));
            World.spawnObject(new WorldObject(3508, object.getType(), object.getRotation(), object));
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.FUNGI_ON_LOG };
    }
}
