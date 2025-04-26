package com.zenyte.plugins.dialogue;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.OptionMessage;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Kris | 4. jaan 2018 : 1:56.31
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class OptionDialogue extends Dialogue {

	public OptionDialogue(Player player, final String title, final String[] options, final Runnable[] actions) {
		super(player);
		this.title = title;
		this.options = options;
		this.actions = actions;
	}
	
	private final String title;
	private final String[] options;
	private final Runnable[] actions;

	@Override
	public void buildDialogue() {
		final String[] nullFreeArray = Arrays.stream(options).filter(Objects::nonNull).toArray(String[]::new);
		final OptionMessage message = new OptionMessage(title, nullFreeArray);
		if (actions.length >= 1)
			message.onOptionOne(() -> {
				if (actions[0] != null)
					actions[0].run();
			});
		if (actions.length >= 2)
			message.onOptionTwo(() -> {
				if (actions[1] != null)
					actions[1].run();
			});
		if (actions.length >= 3)
			message.onOptionThree(() -> {
				if (actions[2] != null)
					actions[2].run();
			});
		if (actions.length >= 4)
			message.onOptionFour(() -> {
				if (actions[3] != null)
					actions[3].run();
			});
		if (actions.length >= 5)
			message.onOptionFive(() -> {
				if (actions[4] != null)
					actions[4].run();
			});
    	dialogue.put(getKey(), message);
	}

}
