package com.zenyte.game.world.entity.pathfinding.events.player;

import com.zenyte.game.world.entity.pathfinding.RouteFinder;
import com.zenyte.game.world.entity.pathfinding.RouteResult;
import com.zenyte.game.world.entity.pathfinding.strategy.EntityStrategy;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 06/01/2019 00:05
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CombatEntityEvent extends EntityEvent {
    public CombatEntityEvent(final Player entity, final EntityStrategy strategy) {
        super(entity, strategy, null, false);
    }

    @Override
    public boolean process() {
        final RouteResult steps = RouteFinder.findRoute(entity, entity.getSize(), strategy, true);
        if (steps == RouteResult.ILLEGAL) {
            cancel();
            return STOP;
        }
        if (!steps.isAlternative() && steps.getSteps() == 0) {
            if (delay > 0) {
                delay--;
                return CONTINUE;
            }
            resetFlag();
            execute();
            return STOP;
        }
        final int[] bufferX = steps.getXBuffer();
        final int[] bufferY = steps.getYBuffer();
        entity.resetWalkSteps();
        if (entity.isFrozen() || entity.isStunned()) {
            return CONTINUE;
        }
        for (int step = steps.getSteps() - 1; step >= 0; step--) {
            if (!entity.addWalkSteps(bufferX[step], bufferY[step], 25, true)) {
                cancel();
                return STOP;
            }
        }
        return CONTINUE;
    }

    @Override
    public boolean initiate() {
        throw new RuntimeException("Combat events are not initiated.");
    }

    @Override
    protected void inform(final String message) {
    }
}
