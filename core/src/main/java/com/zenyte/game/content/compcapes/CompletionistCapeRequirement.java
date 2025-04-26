package com.zenyte.game.content.compcapes;

import java.util.function.Function;
import java.util.function.Predicate;
import com.zenyte.game.world.entity.player.Player;

public class CompletionistCapeRequirement {

	private final String task;
	private final Function<Player, Integer> function;
	private final int requirement;

	CompletionistCapeRequirement(String task, Function<Player, Integer> function, int requirement) {
		this.task = task;
		this.function = function;
		this.requirement = requirement;
	}

	public String getTask() {
		return task;
	}

	public Function<Player, Integer> getFunction() {
		return function;
	}

	public boolean test(Player player) {
		return function.apply(player) >= requirement;
	}

	public int getRequirement() {
		return requirement;
	}

}
