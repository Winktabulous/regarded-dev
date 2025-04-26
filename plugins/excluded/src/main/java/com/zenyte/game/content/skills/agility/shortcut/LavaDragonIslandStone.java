package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

public class LavaDragonIslandStone implements Shortcut {
    private static final Location SOUTH = new Location(3201, 3807, 0);
    private static final Location NORTH = new Location(3201, 3810, 0);

    @Override
    public void startSuccess(Player player, WorldObject object) {
        if (!DiaryUtil.eligibleFor(DiaryReward.WILDERNESS_SWORD3, player)) {
            player.sendMessage("You need to complete the wilderness hard diary to use this shortcut.");
            return;
        }
        final boolean north = player.getY() > object.getY();
        WorldTasksManager.schedule(new WorldTask() {
            private int ticks;
            @Override
            public void run() {
                if (ticks == 0) {
                    player.setAnimation(Animation.JUMP);
                    player.setForceMovement(new ForceMovement(player.getLocation(), 15, object, 35, north ? ForceMovement.SOUTH : ForceMovement.NORTH));
                } else if (ticks == 1) player.setLocation(object);
                 else if (ticks == 2) {
                    player.setAnimation(Animation.JUMP);
                    player.setForceMovement(new ForceMovement(player.getLocation(), 15, north ? SOUTH : NORTH, 35, north ? ForceMovement.SOUTH : ForceMovement.NORTH));
                } else if (ticks == 3) {
                    player.setLocation(north ? SOUTH : NORTH);
                    stop();
                }
                ticks++;
            }
        }, 0, 0);
    }

    @Override
    public int getLevel(WorldObject object) {
        return 74;
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return 3;
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 0;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {14918};
    }
}
