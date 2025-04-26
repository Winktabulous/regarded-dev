package com.zenyte.game.world.entity.pathfinding.events.npc;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.pathfinding.RouteFinder;
import com.zenyte.game.world.entity.pathfinding.RouteResult;
import com.zenyte.game.world.entity.pathfinding.events.RouteEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.EntityStrategy;

/**
 * @author Kris | 13/05/2019 16:55
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class NPCCollidingEvent extends RouteEvent<NPC, EntityStrategy> {
    public NPCCollidingEvent(final NPC entity, final EntityStrategy strategy) {
        super(entity, strategy, null, 0);
    }

    public NPCCollidingEvent(final NPC entity, final EntityStrategy strategy, final Runnable event) {
        super(entity, strategy, event, 0);
    }

    @Override
    protected void resetFlag() {
    }

    @Override
    protected void inform(final String message) {
    }

    /**
     * The process method called every tick, used to execute the event.
     *
     * @return whether to continue processing this event, or to stop it from executing any further.
     */
    public boolean process() {
        if (entity.isTeleported()) {
            return STOP;
        }
        if (!initiated) {
            return initiate();
        }
        if (!entity.hasWalkSteps()) {
            final RouteResult steps = RouteFinder.findConditionalRoute(entity, entity.getSize(), strategy, true);
            if (steps == RouteResult.ILLEGAL) {
                cancel();
                return STOP;
            }
            if (!steps.isAlternative() && steps.getSteps() <= 0) {
                if (delay > 0) {
                    delay--;
                    return CONTINUE;
                }
                resetFlag();
                execute();
                return STOP;
            }
            cancel();
            return STOP;
        }
        return CONTINUE;
    }

    /**
     * The initiation method for this event, attempts to move the {@link #entity} to the destination.
     *
     * @return whether to continue executing this event, or to finish it.
     */
    protected boolean initiate() {
        initiated = true;
        final RouteResult steps = RouteFinder.findConditionalRoute(entity, entity.getSize(), strategy, true);
        if (steps == RouteResult.ILLEGAL) {
            cancel();
            return STOP;
        }
        if (!steps.isAlternative() && steps.getSteps() == 0) {
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
}
