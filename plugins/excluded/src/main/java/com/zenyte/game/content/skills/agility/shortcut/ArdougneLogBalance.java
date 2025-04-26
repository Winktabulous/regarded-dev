package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Failable;
import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.*;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 14. apr 2018 : 22:33.48
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 *      profile</a>}
 */
public final class ArdougneLogBalance implements Shortcut, Failable {

    private static final RenderAnimation RENDER = new RenderAnimation(763, 762, 762, 762, 762, 762, 762);

    private static final RenderAnimation SWIM_RENDER = new RenderAnimation(773, 773, 772, 772, 772, 772, 772);

    private static final Animation BALANCING = new Animation(763);

    private static final Animation FALLING_LEFT = new Animation(2581);

    private static final Animation FALLING_RIGHT = new Animation(2582);

    private static final Location FAIL_DESTINATION_A = new Location(2603, 3330, 0);

    private static final Location FAIL_DESTINATION_B = new Location(2598, 3331, 0);

    private static final Location FAIL_MIDDLE = new Location(2600, 3331, 0);

    private static final Location SPLASH_TILE = new Location(2600, 3334, 0);

    private static final Location MIDDLE = new Location(2600, 3336, 0);

    private static final Graphics SPLASH = new Graphics(68);

    @Override
    public int getLevel(final WorldObject object) {
        return 33;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] { 16546, 16548 };
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 4;
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        if (!success)
            return -1;
        return 3;
    }

    @Override
    public RenderAnimation getRenderAnimation() {
        return RENDER;
    }

    @Override
    public String getStartMessage(final boolean success) {
        return "You attempt to walk across the slippery log.";
    }

    @Override
    public String getEndMessage(final boolean success) {
        if (!success)
            return null;
        return "You make it across the log without any problems.";
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        if (object.getId() == ObjectId.LOG_BALANCE_16548)
            player.addWalkSteps(2598, 3336, -1, false);
        else
            player.addWalkSteps(2602, 3336, -1, false);
    }

    @Override
    public void startFail(final Player player, final WorldObject object) {
        player.addWalkSteps(MIDDLE.getX(), MIDDLE.getY(), -1, false);
        WorldTasksManager.schedule(new WorldTask() {

            private int ticks;

            private final boolean bool = Utils.randomDouble() > 0.5F;

            @Override
            public void run() {
                if (ticks == 0) {
                    player.setAnimation(Animation.STOP);
                } else if (ticks == 1) {
                    player.stopAll();
                    player.getAppearance().resetRenderAnimation();
                    player.setAnimation(BALANCING);
                } else if (ticks == 2) {
                    player.sendMessage("You lose your footing and fall into the river.");
                    player.setAnimation(object.getId() == 16548 ? FALLING_LEFT : FALLING_RIGHT);
                } else if (ticks == 3) {
                    player.getAppearance().setRenderAnimation(SWIM_RENDER);
                    World.sendGraphics(SPLASH, SPLASH_TILE);
                    player.setLocation(SPLASH_TILE);
                } else if (ticks == 4) {
                    player.sendMessage("You feel like you're drowning.");
                    player.addWalkSteps(FAIL_MIDDLE.getX(), FAIL_MIDDLE.getY(), -1, false);
                } else if (ticks == 7) {
                    final Location tile = bool ? FAIL_DESTINATION_A : FAIL_DESTINATION_B;
                    player.addWalkSteps(tile.getX(), tile.getY(), -1, false);
                } else if (ticks == (bool ? 10 : 9)) {
                    player.sendMessage("You finally come to the shore.");
                    player.applyHit(new Hit(Utils.random(1, 4), HitType.REGULAR));
                    finish(player, object, 0, false, null);
                    player.unlock();
                    stop();
                }
                ticks++;
            }
        }, 0, 0);
    }

    @Override
    public double getFailXp(final WorldObject object) {
        return 2;
    }
}
