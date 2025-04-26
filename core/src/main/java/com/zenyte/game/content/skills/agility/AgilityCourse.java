package com.zenyte.game.content.skills.agility;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * @author Kris | 21. dets 2017 : 18:42.25
 * @author Jire
 */
public interface AgilityCourse {

    int getObstacleCount();

    double getAdditionalCompletionXP();

    default Consumer<Player> onComplete() {
        return null;
    }

    default void runCourseObstacle(final Player player,
                                   final WorldObject object,
                                   final AgilityCourseObstacle obstacle) {
        final int stage = obstacle.updateStage(player);
        final int index = obstacle.getIndex();
        final Consumer<Player> onComplete;
        final double additionalXP;
        if (index == getObstacleCount() && stage == index - 1) {
            additionalXP = getAdditionalCompletionXP();
            onComplete = onComplete();
        } else {
            additionalXP = 0;
            onComplete = null;
        }
        obstacle.handle(player, object, additionalXP, onComplete);
    }

    void initObstacle(@NotNull final AgilityCourseObstacle obstacle);

    @Nullable
    AgilityCourseObstacle getObstacle(final int objectID);

}
