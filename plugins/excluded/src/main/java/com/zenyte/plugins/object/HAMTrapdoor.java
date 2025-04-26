package com.zenyte.plugins.object;

import com.zenyte.game.content.skills.thieving.Thieving;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 15/01/2019 01:14
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class HAMTrapdoor implements ObjectAction {

    private static final Animation start = new Animation(537);

    private static final Animation end = new Animation(536);

    private static final Location surface = new Location(3166, 3251, 0);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equals("Open")) {
            player.sendMessage("This trapdoor seems totally locked.");
            return;
        } else if (option.equals("Pick-Lock")) {
            player.getActionManager().setAction(new Action() {

                @Override
                public boolean start() {
                    player.setAnimation(start);
                    player.sendMessage("You attempt to pick the lock on the trap door.");
                    this.delay(3);
                    return true;
                }

                @Override
                public boolean process() {
                    return true;
                }

                @Override
                public int processWithDelay() {
                    if (Thieving.success(player, player.getInventory().containsItem(1523, 1) ? 20 : 50)) {
                        player.setAnimation(end);
                        player.getSkills().addXp(SkillConstants.THIEVING, 4);
                        player.sendMessage("You pick the lock on the trapdoor.");
                        player.getVarManager().sendBit(235, 1);
                        return -1;
                    } else {
                        player.setAnimation(end);
                        player.getSkills().addXp(SkillConstants.THIEVING, 1);
                        final Action action = this;
                        WorldTasksManager.schedule(() -> {
                            if (player.getActionManager().getAction() == action) {
                                player.setAnimation(start);
                                player.sendMessage("You attempt to pick the lock on the trap door.");
                            }
                        });
                        return 4;
                    }
                }
            });
        } else // starts with 537, ends with 536
        // 536, 537 tick after.
        // 1 xp failure, 4 xp success.
        // "You attempt to pick the lock on the trap door.";
        // "You pick the lock on the trapdoor."
        // "You close the trapdoor.";
        // You climb down through the trapdoor...
        // ... and enter a dimly lit cavern area.
        if (option.equals("Close")) {
            player.getVarManager().sendBit(235, 0);
            player.sendMessage("You close the trapdoor.");
        } else if (option.equals("Climb-down")) {
            player.lock(1);
            player.setAnimation(new Animation(828));
            player.sendMessage("You climb down through the trapdoor...");
            WorldTasksManager.schedule(() -> {
                player.getVarManager().sendBit(235, 0);
                player.setLocation(new Location(3149, 9652, 0));
                player.sendMessage("... and enter a dimly lit cavern area.");
            });
        } else if (option.equalsIgnoreCase("Climb-up")) {
            player.useStairs(828, surface, 1, 2);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 5492, ObjectId.LADDER_5493 };
    }
}
