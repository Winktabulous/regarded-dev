package com.zenyte.game.world.entity.player.variables;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

/**
 * @author Kris | 5. juuni 2018 : 02:00:40
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class Variable {
	public Variable(final int ticks) {
		this.ticks = ticks;
		messages = null;
	}

	public Variable(final int ticks, final VariableTask task) {
		this.ticks = ticks;
		messages = null;
		this.task = task;
	}

	public Variable(final int ticks, final VariableMessage... messages) {
		this(ticks, null, messages);
	}

	public Variable(final int ticks, final VariableTask task, final VariableMessage... messages) {
		this.ticks = ticks;
		this.messages = new Int2ObjectOpenHashMap<String>(messages.length);
		this.task = task;
		for (int i = messages.length - 1; i >= 0; i--) {
			final VariableMessage message = messages[i];
			this.messages.put(message.onTick, message.message);
		}
	}

	transient Int2ObjectOpenHashMap<String> messages;
	transient VariableTask task;
	int ticks;
}
