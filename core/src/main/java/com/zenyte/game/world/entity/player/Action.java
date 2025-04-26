package com.zenyte.game.world.entity.player;

import com.zenyte.game.world.entity.masks.Animation;

public abstract class Action {

    protected Player player;

    private boolean interrupted;

    public boolean setInterrupted(final boolean interrupted) {
        this.interrupted = interrupted;
        if (interrupted) {
            onInterruption();
        } else {
            return onContinuation();
        }
        return true;
    }

	public abstract boolean start();

	public abstract boolean process();

    public boolean postMovementProcess() {
        return false;
    }

	public abstract int processWithDelay();

    public int processAfterMovement() {
        return 0;
    }

    /**
     * Used for 1-tick karambwan cooking; apparently RS processes the action immediately when the
     * item-on-range plugin is launched, before it even processes the other plugins.
     * Odd behaviour but this is the only way to allow one-ticking, otherwise it'd be
     * barely-functional two-ticking.
     * @return whether or not to run process() immediately on action start.
     */
	public boolean initiateOnPacketReceive() {
	    return false;
    }
	
	protected boolean onContinuation() {
		return start();
	}
	
	protected void onInterruption() {
		player.setAnimation(Animation.STOP);
	}

	public void stop() {
		
	}
	
	public boolean interruptedByCombat() {
		return true;
    }

    public boolean interruptedByDialogue() {
        return true;
    }

    public final void delay(final int delay) {
        player.getActionManager().setActionDelay(delay);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean isInterrupted() {
        return interrupted;
    }


}
