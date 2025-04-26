package com.zenyte.game.world.entity.player;

import com.zenyte.game.model.ui.InterfacePosition;

/**
 * @author Kris | 07/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DelayedActionManager {
    private final Player player;
    private Action action;
    private int actionDelay;
    private transient long lastAction = System.currentTimeMillis();

    public DelayedActionManager(final Player player) {
        this.player = player;
    }

    public void forceStop() {
        if (action == null) {
            return;
        }
        action.stop();
        action = null;
    }

    public Action getAction() {
        return action;
    }

    public void setActionDelay(final int skillDelay) {
        actionDelay = skillDelay;
    }

    public void interrupt(final boolean interrupt) {
        if (action == null) {
            return;
        }
        final boolean interruption = action.setInterrupted(interrupt);
        if (!interruption) {
            forceStop();
        }
    }

    public void process() {
        if (action != null) {
            if (player.isDead()) {
                forceStop();
            }
        }
        if (action != null && action.interruptedByDialogue() && player.getInterfaceHandler().containsInterface(InterfacePosition.DIALOGUE)) {
            if (actionDelay > 0) {
                actionDelay--;
            }
            return;
        } else if (action != null && action.isInterrupted()) {
            interrupt(false);
        }
        if (action != null) {
            if (!action.process()) {
                forceStop();
            }
        }
        if (actionDelay > 0) {
            actionDelay--;
            return;
        }
        if (action == null) {
            return;
        }
        try {
            final int delay = action.processWithDelay();
            if (delay == -1) {
                forceStop();
                return;
            }
            actionDelay += delay;
        } catch (Exception e) {
            e.printStackTrace();
            forceStop();
        }
    }

    public boolean setAction(final Action action) {
        forceStop();
        action.setPlayer(player);
        if (!action.start()) {
            action.stop();
            return false;
        }
        this.lastAction = System.currentTimeMillis();
        this.action = action;
        if (action.initiateOnPacketReceive()) process();
        return true;
    }

    public long getLastAction() {
        return lastAction;
    }
}
