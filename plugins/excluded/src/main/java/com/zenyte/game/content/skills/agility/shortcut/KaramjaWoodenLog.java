package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 10/05/2019 22:00
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class KaramjaWoodenLog implements Shortcut {

    private static final RenderAnimation RENDER = new RenderAnimation(763, 762, 762, 762, 762, 762, 762);

    @Override
    public RenderAnimation getRenderAnimation() {
        return RENDER;
    }

    @Override
    public int getLevel(WorldObject object) {
        return 1;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] { 23644 };
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return 3;
    }

    @Override
    public void startSuccess(Player player, WorldObject object) {
        if (object.getX() == 2907) {
            player.addWalkSteps(2910, 3049, -1, false);
        } else {
            player.addWalkSteps(2906, 3049, -1, false);
        }
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 1;
    }
}
