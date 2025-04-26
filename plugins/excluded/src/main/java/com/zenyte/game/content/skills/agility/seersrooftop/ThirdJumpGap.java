/**
 *
 */
package com.zenyte.game.content.skills.agility.seersrooftop;

import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.content.skills.agility.MarkOfGrace;
import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.ItemChat;

/**
 * @author Noele | May 1, 2018 : 4:18:15 AM
 * @see https://noeles.life || noele@zenyte.com
 */
public final class ThirdJumpGap extends AgilityCourseObstacle {

    private static final Item IMAGE = new Item(6517);
    private static final Location FINISH = new Location(2701, 3465, 2);

    public ThirdJumpGap() {
        super(SeersRooftopCourse.class, 5);
    }

    public boolean preconditions(final Player player, final WorldObject object) {
        if (player.getBooleanAttribute("SeersTrapdoor")) {
            player.getDialogueManager().start(new ItemChat(player, IMAGE, "This course begins at the bank."));
            return false;
        }
        return true;
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        player.setFaceLocation(new Location(player.getX(), player.getY() - 1, 0));
        WorldTasksManager.schedule(new WorldTask() {
            private int ticks;

            @Override
            public void run() {
                if (ticks == 0)
                    player.setAnimation(Animation.LEAP);
                else if (ticks == 1) {
                    player.setAnimation(Animation.LAND);
                    player.setLocation(FINISH);
                    MarkOfGrace.spawn(player, SeersRooftopCourse.MARK_LOCATIONS, 60, 20);
                    stop();
                }
                ticks++;
            }
        }, 0, 0);
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 60;
    }

    @Override
    public int[] getObjectIds() {
        return new int[]{ObjectId.GAP_14930};
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 2;
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 25;
    }
}
