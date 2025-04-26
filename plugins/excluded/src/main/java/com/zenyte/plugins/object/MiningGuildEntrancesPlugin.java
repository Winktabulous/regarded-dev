package com.zenyte.plugins.object;

import com.zenyte.game.content.achievementdiary.diaries.FaladorDiary;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.Skills;
import com.zenyte.game.world.object.*;

/**
 * @author Kris | 9. juuni 2018 : 09:29:56
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class MiningGuildEntrancesPlugin implements ObjectAction {

    private static final Location OUTSIDE = new Location(3046, 9757, 0);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (object.getId() == ObjectId.LADDER_30367) {
            if (player.getSkills().getLevelForXp(SkillConstants.MINING) < 60) {
                player.sendMessage("You need a Mining level of at least 60 to enter the Mining guild.");
                return;
            }
            Ladder.use(player, option, object, player.getLocation().transform(0, 6400, 0));
            return;
        } else if (object.getId() == ObjectId.DOOR_30364) {
            if (player.getLocation().getPositionHash() == OUTSIDE.getPositionHash()) {
                if (player.getSkills().getLevelForXp(SkillConstants.MINING) < 60) {
                    player.sendMessage("You need a Mining level of at least 60 to enter the Mining guild.");
                    return;
                }
            }
            player.lock(3);
            player.addWalkSteps(object.getX(), object.getY());
            WorldTasksManager.schedule(new WorldTask() {

                private int ticks;

                private WorldObject door;

                @Override
                public void run() {
                    switch(ticks++) {
                        case 0:
                            door = Door.handleGraphicalDoor(object, null);
                            return;
                        case 1:
                            if (player.getLocation().getPositionHash() != OUTSIDE.getPositionHash()) {
                                player.addWalkSteps(door.getX(), door.getY(), 1, false);
                            } else {
                                player.addWalkSteps(object.getX(), object.getY(), 1, false);
                            }
                            return;
                        case 3:
                            if (Skills.ExperienceBoost.PROSPECTOR.hasFull(player) || Skills.ExperienceBoost.GOLDEN_PROSPECTOR.hasFull(player)) {
                                player.getAchievementDiaries().update(FaladorDiary.ENTER_MINING_GUILD);
                            }
                            Door.handleGraphicalDoor(door, object);
                            stop();
                            return;
                    }
                }
            }, 0, 0);
            return;
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.DOOR_30364, ObjectId.LADDER_30367 };
    }
}
