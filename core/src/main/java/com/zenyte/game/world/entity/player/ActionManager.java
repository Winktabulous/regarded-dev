package com.zenyte.game.world.entity.player;

import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ActionManager {
	private static final Logger log = LoggerFactory.getLogger(ActionManager.class);
	private final Player player;
	private Action action;
	private int actionDelay;
	private Class<?> lastActionClass;
	private long lastActionCancelTick;
	private transient long lastAction = System.currentTimeMillis();
	private transient boolean processed = false;
	private transient boolean preventDelaySet;


	public ActionManager(final Player player) {
		this.player = player;
	}

	public void addActionDelay(final int skillDelay) {
		actionDelay += skillDelay;
	}

	public void forceStop() {
		if (action == null) {
			return;
		}
		this.lastActionClass = action.getClass();
		this.lastActionCancelTick = WorldThread.WORLD_CYCLE;
		action.stop();
		action = null;
	}

	public boolean wasInCombatThisTick() {
		if (lastActionClass == null) {
			return false;
		}
		return lastActionCancelTick == WorldThread.WORLD_CYCLE && PlayerCombat.class.isAssignableFrom(lastActionClass);
	}

	public Action getAction() {
		return action;
	}

	public int getActionDelay() {
		return actionDelay;
	}

	public void setActionDelay(final int skillDelay) {
		actionDelay = skillDelay;
	}

	public boolean hasSkillWorking() {
		return action != null;
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
		processed = false;
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
			processed = true;
			if (preventDelaySet) {
				preventDelaySet = false;
			} else {
				actionDelay += delay;
			}
		} catch (Exception e) {
			e.printStackTrace();
			forceStop();
		}
	}

	public void forceProcessAfterMovement() {
		this.processed = true;
	}

	public void processAfterMovement() {
		if (action != null) {
			if (player.isDead()) {
				forceStop();
			}
		}
		if (action != null && action.interruptedByDialogue() && player.getInterfaceHandler().containsInterface(InterfacePosition.DIALOGUE)) {
			return;
		}
		if (action != null) {
			if (action.postMovementProcess()) {
				return;
			}
		}
		if (action == null || !processed) {
			return;
		}
		try {
			final int delay = action.processAfterMovement();
			if (delay == -1) {
				forceStop();
				return;
			}
			if (preventDelaySet) {
				preventDelaySet = false;
			} else {
				actionDelay += delay;
			}
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

	public Class<?> getLastActionClass() {
		return lastActionClass;
	}

	public long getLastActionCancelTick() {
		return lastActionCancelTick;
	}

	public long getLastAction() {
		return lastAction;
	}

	public void preventDelaySet() { preventDelaySet = true; }

}
