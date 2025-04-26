package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 10/05/2019 22:15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class AgilityPyramidClimbingRocks implements Shortcut {

    @Override
    public int getLevel(final WorldObject object) {
        if (object.getId() == ObjectId.CLIMBING_ROCKS_10852) {
            return 1;
        }
        return 30;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] { 11948, 11949 };
    }

    private static final Animation CLIMB = new Animation(740);

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 4;
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        final Direction direction = player.getX() <= object.getX() ? Direction.EAST : Direction.WEST;
        Location destination = player.getLocation().transform(direction, 4);
        ForceMovement forceMovement = new ForceMovement(destination, 120, ForceMovement.WEST);
        player.setAnimation(CLIMB);
        player.setForceMovement(forceMovement);
        player.sendSound(new SoundEffect(2454, 1, 0, 5));
        WorldTasksManager.schedule(() -> {
            player.getSkills().addXp(SkillConstants.AGILITY, direction == Direction.EAST ? 5 : 1);
            player.setAnimation(Animation.STOP);
            player.setLocation(destination);
        }, 3);
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 0;
    }
}
