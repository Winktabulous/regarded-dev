package com.zenyte.game.content.skills.agility.priffdinasrooftop;

import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.content.skills.agility.Failable;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.WorldObject;
import org.jetbrains.annotations.NotNull;

/**
 * @author R-Y-M-R
 * @date 2/11/2022
 * @see <a href="https://www.rune-server.ee/members/necrotic/">RuneServer</a>
 */
public final class SecondTightRope extends AgilityCourseObstacle implements Failable {

    private static final RenderAnimation RENDER = new RenderAnimation(RenderAnimation.STAND, 762, RenderAnimation.WALK);
    private static final Animation FALL_ROPE = new Animation(770);
    private static final Animation FALLING = new Animation(4367);
    private static final Location START_LOC = new Location(3278, 6142, 2);
    private static final Location FAIL_TILE = new Location(3274, 6146, 2);
    private static final Location END_LOC = new Location(3271, 6149, 2);
    private static final int WALK_TIME = 6;
    private static final SoundEffect TIGHT_ROPE_SOUND = new SoundEffect(2495);
    private static final SoundEffect FAILURE_SOUND = new SoundEffect(2493);
    private static final SoundEffect HIT_SOUND = new SoundEffect(519, 20);

    public SecondTightRope() {
        super(PriffdinasRooftopCourse.class, 8);
    }

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        return START_LOC;
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        if (player.getNumericTemporaryAttribute("prif_portal").intValue() == 4) {
            player.getVarManager().sendBit(9298, 4);
        }
        player.getPacketDispatcher().sendSoundEffect(TIGHT_ROPE_SOUND);
        player.addWalkSteps(END_LOC.getX(), END_LOC.getY(), -1, false);
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 75;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] { 36234 };
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 30.7;
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        if (success)
            return WALK_TIME;
        return 4;
    }

    @Override
    public RenderAnimation getRenderAnimation() {
        return RENDER;
    }

    int permanentSuccessLevel() {
        return 91;
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
    public void startFail(Player player, WorldObject object) {
        player.addWalkSteps(END_LOC.getX(), END_LOC.getY(), -1, false);
        WorldTasksManager.scheduleOrExecute(new WorldTask() {
            int ticks;
            @Override
            public void run() {
                if (player.getLocation().equals(FAIL_TILE)) {
                    player.getPacketDispatcher().sendSoundEffect(FAILURE_SOUND);
                    player.resetWalkSteps();
                    player.forceAnimation(FALL_ROPE);
                    stop();
                }
                ticks++;
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
                    case 0: {
                        player.teleport(new Location(FAIL_TILE.getX(), FAIL_TILE.getY() + 1, 0));
                        player.forceAnimation(FALLING);
                        break;
                    }
                    case 2: {
                        player.applyHit(new Hit(Utils.random(7, 9), HitType.REGULAR));
                        player.getPacketDispatcher().sendSoundEffect(HIT_SOUND);
                        break;
                    }
                    case 3: {
                        player.setForceTalk("Gah!!!");
                        stop();
                        break;
                    }
                }
            }
        }, 0, 0);
    }

    @Override
    public double getFailXp(WorldObject object) {
        return 0;
    }
}