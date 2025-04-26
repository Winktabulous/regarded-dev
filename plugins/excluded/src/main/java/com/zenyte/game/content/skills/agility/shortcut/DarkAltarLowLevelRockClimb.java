package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 26/04/2019 17:03
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DarkAltarLowLevelRockClimb implements Shortcut {

    private static final Animation CRAWL = new Animation(1148);

    private static final RenderAnimation RENDER = new RenderAnimation(738, 737, 737, 737, 737, 737, -1);

    private static final Location CRAWL_FACE = new Location(1775, 3849, 0);

    private static final Location TOP_ROCK = new Location(1773, 3849, 0);

    private static final Location TOP = new Location(1774, 3849, 0);

    private static final Location BOTTOM = new Location(1769, 3849, 0);

    private static final ForceMovement CRAWL_DOWN = new ForceMovement(BOTTOM, 120, ForceMovement.EAST);

    @Override
    public void startSuccess(Player player, WorldObject object) {
        if (object.getId() != ObjectId.ROCKS_27988) {
            player.setFaceLocation(CRAWL_FACE);
        }
        WorldTasksManager.schedule(new WorldTask() {

            private int ticks;

            @Override
            public void run() {
                if (object.getId() == ObjectId.ROCKS_27988) {
                    if (ticks == 0) {
                        player.getAppearance().setRenderAnimation(RENDER);
                        player.addWalkSteps(object.getX(), object.getY(), -1, false);
                    } else if (ticks == 1)
                        player.addWalkSteps(TOP_ROCK.getX(), TOP_ROCK.getY(), -1, false);
                    else if (ticks == 2)
                        player.addWalkSteps(TOP.getX(), TOP.getY(), -1, false);
                    else if (ticks == 3) {
                        player.getAppearance().resetRenderAnimation();
                        stop();
                    }
                } else {
                    if (ticks == 1) {
                        player.setAnimation(CRAWL);
                        player.setForceMovement(CRAWL_DOWN);
                    } else if (ticks == 5) {
                        player.setLocation(BOTTOM);
                        stop();
                    }
                }
                ticks++;
            }
        }, 0, 0);
    }

    @Override
    public int getLevel(WorldObject object) {
        return 52;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] { 27988, 27987 };
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return object.getId() == 27988 ? 4 : 6;
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 0;
    }
}
