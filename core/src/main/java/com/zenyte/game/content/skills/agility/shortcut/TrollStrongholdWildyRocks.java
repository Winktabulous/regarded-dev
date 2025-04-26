package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.achievementdiary.diaries.WildernessDiary;
import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 26/04/2019 18:39
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class TrollStrongholdWildyRocks implements Shortcut {
    private static final Location WESTERN_TOP = new Location(2918, 3672, 0);
    private static final Location WESTERN_BOTTOM = new Location(2915, 3672, 0);
    private static final Location WESTERN_TOP_OBJ = new Location(2917, 3672, 0);
    private static final Location WESTERN_BOTTOM_OBJ = new Location(2916, 3672, 0);

    private static final Location MIDDLE_TOP = new Location(2921, 3672, 0);
    private static final Location MIDDLE_BOTTOM = new Location(2924, 3673, 0);
    private static final Location MIDDLE_TOP_OBJ = new Location(2922, 3672, 0);
    private static final Location MIDDLE_BOTTOM_OBJ = new Location(2923, 3673, 0);

    private static final Location EASTERN_TOP = new Location(2946, 3678, 0);
    private static final Location EASTERN_BOTTOM = new Location(2949, 3679, 0);
    private static final Location EASTERN_TOP_OBJ = new Location(2947, 3678, 0);
    private static final Location EASTERN_BOTTOM_OBJ = new Location(2948, 3679, 0);

    private static final Location EASTERN_WILDY_BOTTOM = new Location(2949, 3681, 0);
    private static final Location BOTTOM_FAILURE_OBJ = new Location(2949, 3680, 0);

    @Override
    public boolean preconditions(final Player player, final WorldObject object) {
        if (object.matches(BOTTOM_FAILURE_OBJ)) {
            player.sendMessage("You can't go up that way!");
            return false;
        }
        return true;
    }

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        return object.matches(BOTTOM_FAILURE_OBJ) ? new Location(2950, 3681, 0) : object;
    }

    private static final Animation CLIMB = new Animation(740);

    @Override
    public int getLevel(final WorldObject object) {
        return 64;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {
                16545
        };
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return object.matches(EASTERN_TOP_OBJ) ? 4 : 2;
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        Location destination;
        ForceMovement forceMovement;
        if (object.matches(WESTERN_BOTTOM_OBJ) || object.matches(WESTERN_TOP_OBJ)) {
            destination = object.matches(WESTERN_TOP_OBJ) ? WESTERN_BOTTOM : WESTERN_TOP;
            forceMovement = new ForceMovement(destination, 60, ForceMovement.EAST);
        } else if (object.matches(MIDDLE_TOP_OBJ) || object.matches(MIDDLE_BOTTOM_OBJ)) {
            destination = object.matches(MIDDLE_TOP_OBJ) ? MIDDLE_BOTTOM : MIDDLE_TOP;
            forceMovement = new ForceMovement(destination, 60, 0x150);
        } else if (object.matches(EASTERN_TOP_OBJ) || object.matches(EASTERN_BOTTOM_OBJ)) {
            destination = object.matches(EASTERN_TOP_OBJ) ? EASTERN_BOTTOM : EASTERN_TOP;
            forceMovement = new ForceMovement(destination, 60, ForceMovement.WEST);
            player.setAnimation(CLIMB);
            player.setForceMovement(forceMovement);
            WorldTasksManager.schedule(() -> {
                player.setAnimation(Animation.STOP);
                player.setLocation(destination);
                WorldTasksManager.schedule(() -> {
                    player.setAnimation(CLIMB);
                    ForceMovement finalFm = new ForceMovement(EASTERN_WILDY_BOTTOM, 30, ForceMovement.SOUTH);
                    player.setForceMovement(finalFm);
                    WorldTasksManager.schedule(() -> {
                        player.getAchievementDiaries().update(WildernessDiary.TAKE_AGILITY_SHORTCUT);
                        player.setLocation(EASTERN_WILDY_BOTTOM);
                        player.setAnimation(Animation.STOP);
                    });
                });
            }, 1);

            return;
        } else {
            throw new IllegalStateException("Unknown obstacle");
        }
        player.setAnimation(CLIMB);
        player.setForceMovement(forceMovement);
        WorldTasksManager.schedule(() -> {
            player.setAnimation(Animation.STOP);
            player.setLocation(destination);
        }, 1);
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 0;
    }
}
