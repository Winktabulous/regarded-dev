package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.achievementdiary.diaries.LumbridgeDiary;
import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 10/05/2019 18:23
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ZanarisJuttingWall implements Shortcut {
    @Override
    public int getLevel(final WorldObject object) {
        return object.getX() >= 2408 ? 66 : 46;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {17002};
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 4;
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        final boolean eastern = object.getX() >= 2408;
        if (eastern) {
            final int y = 4401;
            player.setAnimation(player.getY() >= y ? new Animation(3276) : new Animation(3277));
            player.autoForceMovement(player.getY() >= y ? object.transform(0, -1, 0) : object.transform(0, 1, 0), 0, 120);
        } else {
            final int y = 4403;
            player.setAnimation(player.getY() >= y ? new Animation(3276) : new Animation(3277));
            player.autoForceMovement(player.getY() >= y ? object.transform(0, -1, 0) : object.transform(0, 1, 0), 0, 120);
        }
        WorldTasksManager.schedule(() -> player.getAchievementDiaries().update(LumbridgeDiary.SQUEEZE_PAST_JUTTING_WALL), 3);
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 0;
    }
}
