package com.zenyte.plugins.object;

import com.zenyte.game.content.skills.woodcutting.actions.Woodcutting;
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
 * @since 3/20/2020
 */
public class ThickVine implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final Woodcutting.AxeResult axe = Woodcutting.getAxe(player).get();
        if (axe == null) {
            player.sendMessage("You do not have an axe which you have the woodcutting level to use.");
            return;
        }
        player.lock(2);
        player.setAnimation(axe.getDefinition().getTreeCutAnimation());
        WorldTasksManager.schedule(() -> {
            World.removeObject(object);
            player.setAnimation(Animation.STOP);
            player.getSkills().addXp(SkillConstants.WOODCUTTING, 2);
        }, 1);
        WorldTasksManager.schedule(() -> World.spawnObject(object), 99);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {ObjectId.THICK_VINE, ObjectId.THICK_VINES};
    }
}
