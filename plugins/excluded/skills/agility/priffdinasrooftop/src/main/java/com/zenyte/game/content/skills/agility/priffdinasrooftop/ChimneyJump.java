package com.zenyte.game.content.skills.agility.priffdinasrooftop;

import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.content.skills.agility.Failable;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.WorldObject;
import org.jetbrains.annotations.NotNull;

/**
 * @author R-Y-M-R
 * @date 2/11/2022
 * @see <a href="https://www.rune-server.ee/members/necrotic/">RuneServer</a>
 */
public final class ChimneyJump extends AgilityCourseObstacle implements Failable {

    private static final Location START_LOC = new Location(3273, 6105, 2);
    private static final Location CLIMB_LOC = new Location(3273, 6106, 2);
    private static final Location END_LOC = new Location(3269, 6112, 2);
    private static final Location JUMP_OFF_LOC = new Location(3273, 6107, 2);
    private static final Location FAIL_LOCATION = new Location(3272, 6108, 2);
    private static final Animation CLAMBER = new Animation(2585);
    private static final Animation JUMP = new Animation(1603);
    private static final Animation FAIL_FREE_FALL = new Animation(768);
    private static final Animation FAIL_JUMP = new Animation(769);
    private static final Animation FAIL_HIT_GROUND_NEW_IMPACT = new Animation(4367);
    private static final SoundEffect CLAMBER_SOUND = new SoundEffect(2478, 20);
    private static final SoundEffect JUMPING_YEET_SOUND = new SoundEffect(1936);
    private static final SoundEffect FAILURE_SOUND = new SoundEffect(2493);
    private static final SoundEffect HIT_SOUND = new SoundEffect(519, 20);

    public ChimneyJump() {
        super(PriffdinasRooftopCourse.class, 3);
    }

    @Override
    public int distance(@NotNull final WorldObject object) {
        return 0;
    }

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        return START_LOC;
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        player.getPacketDispatcher().sendSoundEffect(CLAMBER_SOUND);
        WorldTasksManager.scheduleOrExecute(new WorldTask() {
            int ticks;
            @Override
            public void run() {
                switch (ticks++) {
                    case 0: {
                        player.faceDirection(Direction.NORTH);
                        player.forceAnimation(CLAMBER);
                        player.teleport(CLIMB_LOC);
                        break;
                    }
                    case 1: {
                        player.setLocation(new Location(JUMP_OFF_LOC));
                        player.setFaceLocation(END_LOC);
                        break;
                    }
                    case 2: {
                        player.getPacketDispatcher().sendSoundEffect(JUMPING_YEET_SOUND);
                        player.setAnimation(JUMP);
                        player.autoForceMovement(END_LOC, 0, 60, Direction.WEST.getDirection());
                        stop();
                        break;
                    }
                }
            }
        }, 0, 0);
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 75;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] { 36227 };
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 28.1;
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        if (!success) return 3;
        return 5;
    }

    int permanentSuccessLevel() {
        return 91;
    }

    @Override
    public void startFail(Player player, WorldObject object) {
        WorldTasksManager.scheduleOrExecute(new WorldTask() {
            int ticks;
            @Override
            public void run() {
                switch(ticks++) {
                    case 0:
                        player.faceDirection(Direction.NORTH);
                        player.forceAnimation(CLAMBER);
                        player.teleport(CLIMB_LOC);
                        break;
                    case 1:
                        player.setLocation(new Location(JUMP_OFF_LOC));
                        player.setFaceLocation(END_LOC);
                        break;
                    case 2:
                        player.setAnimation(FAIL_JUMP);
                        player.autoForceMovement(FAIL_LOCATION, 0, 60);
                        break;
                    default:
                        if (player.getLocation().equals(FAIL_LOCATION)) {
                            player.getPacketDispatcher().sendSoundEffect(FAILURE_SOUND);
                            player.resetWalkSteps();
                            stop();
                        }
                        break;
                }
            }
        }, 0, 0);
    }

    @Override
    public void endFail(Player player, WorldObject object) {
        WorldTasksManager.scheduleOrExecute(new WorldTask() {
            int ticks;
            @Override
            public void run() {
                switch (ticks++) {
                    case 0:
                        player.teleport(new Location(FAIL_LOCATION.getX(), FAIL_LOCATION.getY(), 1));
                        player.forceAnimation(FAIL_FREE_FALL);
                        break;
                    case 1:
                        player.teleport(new Location(FAIL_LOCATION.getX(), FAIL_LOCATION.getY(), 0));
                        player.forceAnimation(FAIL_HIT_GROUND_NEW_IMPACT);
                        break;
                    case 3:
                        player.getPacketDispatcher().sendSoundEffect(HIT_SOUND);
                        player.applyHit(new Hit(Utils.random(7, 9), HitType.REGULAR));
                        break;
                    case 4:
                        player.setForceTalk("Ow!!");
                        stop();
                        break;
                }
            }
        }, 0, 0);
    }

    @Override
    public boolean successful(@NotNull final Player player, @NotNull final WorldObject object) {
        final int level = player.getSkills().getLevel(SkillConstants.AGILITY);
        final int neverFail = permanentSuccessLevel();
        final int baseChance = 75;
        final int adjustmentPercentage = 100 - baseChance;
        final float successPerLevel = (float) adjustmentPercentage / ((float) neverFail - baseChance);
        final float successChance = baseChance + Math.max(0, (level - getLevel(object))) * successPerLevel;
        return Utils.random(100) < successChance;
    }

    @Override
    public double getFailXp(WorldObject object) {
        return 0;
    }
}