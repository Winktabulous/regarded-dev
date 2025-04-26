package com.zenyte.plugins.object;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Christopher
 * @since 3/21/2020
 */
public class Vines implements ObjectAction {
    private static final Animation rakingAnim = new Animation(2273);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (!player.carryingItem(ItemId.RAKE)) {
            player.sendMessage("You need a rake to do this.");
            return;
        }
        player.lock(2);
        player.setAnimation(rakingAnim);
        WorldTasksManager.schedule(() -> {
            World.removeObject(object);
            player.setAnimation(Animation.STOP);
            player.getSkills().addXp(SkillConstants.FARMING, 2);
        });
        WorldTasksManager.schedule(() -> World.spawnObject(object), 99);
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{ObjectId.VINES_30644};
    }
}
