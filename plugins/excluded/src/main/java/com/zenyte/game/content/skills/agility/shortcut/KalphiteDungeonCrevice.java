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
import com.zenyte.game.world.region.GlobalAreaManager;

/**
 * @author Kris | 10/05/2019 21:47
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class KalphiteDungeonCrevice implements Shortcut {
    private static final Animation CRAWL_IN = new Animation(2594);
    private static final Animation CRAWL_OUT = new Animation(2595);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equalsIgnoreCase("Squeeze-through")) {
            if (!DiaryUtil.eligibleFor(DiaryReward.DESERT_AMULET4, player)) {
                player.sendMessage("You need to complete the elite Desert diaries first in order to use this shortcut.");
                return;
            }
            handle(player, object, 0, null);
        } else if (option.equalsIgnoreCase("Listen")) {
            player.sendMessage("You peek through the crack...");
            WorldTasksManager.schedule(() -> {
                final int playerCount = GlobalAreaManager.get("Kalphite Queen Lair").getPlayers().size();
                player.sendMessage("Standard cave: " + (playerCount == 0 ? "No adventurers." : playerCount + (playerCount == 1 ? " adventurer." : " adventurers.")));
            }, 2);
        }
    }

    private static final Animation INVISIBLE = new Animation(2590);
    private static final Location WEST_CREVICE = new Location(3501, 9510, 2);
    private static final Location EAST_CREVICE = new Location(3506, 9506, 2);
    private static final Location WEST_EXIT = new Location(3500, 9510, 2);
    private static final Location EAST_EXIT = new Location(3506, 9505, 2);

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        final boolean west = object.getPositionHash() == WEST_CREVICE.getPositionHash(); // west going east
        WorldTasksManager.schedule(new WorldTask() {
            private int ticks;
            @Override
            public void run() {
                if (ticks == 0) {
                    player.setAnimation(CRAWL_IN);
                    player.setForceMovement(new ForceMovement(west ? WEST_CREVICE : EAST_EXIT, 150, west ? ForceMovement.EAST : ForceMovement.WEST));
                } else if (ticks == 1) player.setLocation(west ? WEST_CREVICE : EAST_CREVICE);
                 else if (ticks == 2) {
                    player.setAnimation(INVISIBLE);
                    player.setForceMovement(new ForceMovement(west ? EAST_CREVICE : WEST_CREVICE, 150, west ? ForceMovement.EAST : ForceMovement.WEST));
                } else if (ticks == 7) {
                    player.setAnimation(CRAWL_OUT);
                    player.setForceMovement(new ForceMovement(west ? EAST_EXIT : WEST_EXIT, 60, west ? ForceMovement.EAST : ForceMovement.WEST));
                } else if (ticks == 8) {
                    player.setLocation(west ? EAST_EXIT : WEST_EXIT);
                    stop();
                }
                ticks++;
            }
        }, 0, 0);
    }

    @Override
    public String getEndMessage(final boolean success) {
        return success ? "You climb your way through the narrow crevice." : null;
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 86;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {16465};
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 9;
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 0;
    }
}
