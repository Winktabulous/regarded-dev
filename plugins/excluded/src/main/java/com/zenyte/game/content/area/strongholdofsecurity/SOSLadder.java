package com.zenyte.game.content.area.strongholdofsecurity;

import com.zenyte.game.content.achievementdiary.diaries.VarrockDiary;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import mgi.utilities.CollectionUtils;

/**
 * @author Kris | 4. sept 2018 : 21:36:01
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 *      profile</a>}
 */
public final class SOSLadder implements ObjectAction {

    private enum Ladder {

        FIRST_FLOOR_LADDER_UP_START(new Location(1859, 5244, 0), new Location(3081, 3421, 0), "You climb up the ladder to the surface."),
        FIRST_FLOOR_LADDER_UP_CENTER(new Location(1913, 5226, 0), new Location(3081, 3421, 0), "You climb up the ladder which seems to twist and wind in all directions."),
        FIRST_FLOOR_LADDER_DOWN(new Location(1902, 5222, 0), new Location(2042, 5245, 0), "You climb down the ladder to the next level."),
        FIRST_FLOOR_SPIKY_CHAIN(new Location(1881, 5232, 0), new Location(1859, 5243, 0), "You climb up the chain very very carefully, squeeze through a passage then climb a ladder."),
        SECOND_FLOOR_LADDER_UP_START(new Location(2042, 5246, 0), new Location(1859, 5243, 0), "You climb up the ladder to the level above."),
        SECOND_FLOOR_LADDER_UP_CENTER(new Location(2017, 5210, 0), new Location(2042, 5245, 0), "You shin up the rope, squeeze through a passage then climb a ladder."),
        SECOND_FLOOR_LADDER_DOWN(new Location(2026, 5218, 0), new Location(2123, 5252, 0), "You climb down the ladder to the next level."),
        SECOND_FLOOR_ROPE_WEST(new Location(2011, 5192, 0), new Location(2042, 5245, 0), "You shin up the rope, squeeze through a passage then climb a ladder."),
        SECOND_FLOOR_ROPE_SOUTH(new Location(2031, 5189, 0), new Location(2042, 5245, 0), "You shin up the rope, squeeze through a passage then climb a ladder."),
        SECOND_FLOOR_ROPE_EAST(new Location(2040, 5208, 0), new Location(2042, 5245, 0), "You shin up the rope, squeeze through a passage then climb a ladder."),
        THIRD_FLOOR_LADDER_UP_START(new Location(2123, 5251, 0), new Location(2042, 5245, 0), "You climb up the ladder to the level above."),
        THIRD_FLOOR_LADDER_UP_CENTER(new Location(2150, 5278, 0), new Location(2123, 5252, 0), "You shin up the rope, squeeze through a passage then climb a ladder."),
        THIRD_FLOOR_LADDER_DOWN(new Location(2148, 5284, 0), new Location(2358, 5215, 0), "You climb down the ladder to the next level."),
        FOURTH_FLOOR_LADDER_UP_START(new Location(2358, 5216, 0), new Location(2123, 5252, 0), "You climb up the ladder to the level above."),
        FOURTH_FLOOR_LADDER_UP_CENTER(new Location(2350, 5215, 0), new Location(3081, 3421, 0), "You shin up the rope, squeeze through a passage then climb a ladder."),
        FOURTH_FLOOR_BONE_CHAIN(new Location(2309, 5240, 0), new Location(3081, 3421, 0), "You shin up the rope, squeeze through a passage then climb a ladder.");

        private final Location objectTile;

        private final Location destination;

        private static final Animation UP = new Animation(828);

        private static final Animation DOWN = new Animation(827);

        private static final Ladder[] VALUES = values();

        private final String message;

        Ladder(Location objectTile, Location destination, String message) {
            this.objectTile = objectTile;
            this.destination = destination;
            this.message = message;
        }

        public Animation getAnimation(final String option) {
            return option.endsWith("up") ? UP : DOWN;
        }
    }

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        final SOSLadder.Ladder ladder = CollectionUtils.findMatching(Ladder.VALUES, l -> l.objectTile.matches(object));
        if (ladder == null) {
            throw new RuntimeException("Unable to find matching ladder for " + object + ".");
        }
        player.setAnimation(ladder.getAnimation(option));
        player.sendMessage(ladder.message);
        player.lock();
        WorldTasksManager.schedule(() -> {
            if (ladder.equals(Ladder.FIRST_FLOOR_SPIKY_CHAIN) || ladder.message.equals("You shin up the rope, squeeze through a passage then climb a ladder.")) {
                player.sendMessage("You climb up the ladder which seems to twist and wind in all directions.");
            }
            if (ladder.equals(Ladder.FIRST_FLOOR_LADDER_DOWN)) {
                player.getAchievementDiaries().update(VarrockDiary.ENTER_SECOND_LEVEL_SOS);
            }
            player.setLocation(ladder.destination);
            player.unlock();
        });
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.LADDER_20784, ObjectId.LADDER_20785, ObjectId.LADDER_19003, ObjectId.ROPE_19001, ObjectId.LADDER_19004, ObjectId.DRIPPING_VINE, ObjectId.GOO_COVERED_VINE, ObjectId.DRIPPING_VINE_23706, ObjectId.BONEY_LADDER, ObjectId.BONE_CHAIN, ObjectId.SPIKEY_CHAIN_20782 };
    }

    @Override
    public int getDelay() {
        return 1;
    }
}
