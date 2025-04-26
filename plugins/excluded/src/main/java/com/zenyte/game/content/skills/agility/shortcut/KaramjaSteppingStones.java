package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 10/05/2019 22:03
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class KaramjaSteppingStones implements Shortcut {
    @Override
    public int getLevel(final WorldObject object) {
        return 30;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {23645, 23646, 23647};
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 1;
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        final boolean direction = player.getY() < object.getY();
        player.setFaceLocation(object);
        player.setAnimation(Animation.JUMP);
        player.setForceMovement(new ForceMovement(player.getLocation(), 15, object, 35, direction ? ForceMovement.NORTH : ForceMovement.SOUTH));
        WorldTasksManager.schedule(() -> player.setLocation(object));
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 0;
    }
}
