package com.zenyte.game.content.skills.agility.barbariancourse;

import com.zenyte.game.content.skills.agility.AbstractAgilityCourse;
import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

import java.util.function.Consumer;

/**
 * @author Kris | 9. veebr 2018 : 4:57.02
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class BarbarianOutpostCourse extends AbstractAgilityCourse {

    @Override
    public double getAdditionalCompletionXP() {
        return 46.2;
    }

    @Override
    public void runCourseObstacle(Player player, WorldObject object, AgilityCourseObstacle obstacle) {
        obstacle.updateStage(player);

        final Consumer<Player> onComplete;
        final double additionalXP;
        int index = obstacle.getIndex();
        if (index == getObstacleCount() && (object.getX() == 2542 && object.getId() == 1948)) {
            additionalXP = getAdditionalCompletionXP();
            onComplete = onComplete();
        } else {
            additionalXP = 0;
            onComplete = null;
        }

        obstacle.handle(player, object, additionalXP, onComplete);
    }

}
