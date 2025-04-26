package com.zenyte.game.world.entity.player.dialogue;

import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Arham 4 on 2/15/2016.
 * <p>
 * Represents a singular option dialogue with onClick handling each option.
 */

/**
 * I was thinking about maybe even making a SubDialogue class for the option dialogue. Though, I think that's
 * complicating things.
 */
public class DualItemOptionMessage implements Message {
	private static final Logger log = LoggerFactory.getLogger(DualItemOptionMessage.class);
	private static final int OPTION_INTERFACE = 140;
	private final Map<Integer, Runnable> optionActions = new HashMap<Integer, Runnable>();
	private final String text;
	private final int item1;
	private final int item2;
	private final String option1;
	private final String option2;

	public DualItemOptionMessage(final String text, final int item1, final int item2, final String option1, final String option2) {
		this.text = text;
		this.item1 = item1;
		this.item2 = item2;
		this.option1 = option1;
		this.option2 = option2;
	}

	public DualItemOptionMessage setOptions(final Runnable... runnables) {
		int index = 1;
		for (final Runnable run : runnables) {
			optionActions.put(index++, run);
		}
		return this;
	}

	public DualItemOptionMessage onOptionOne(final Runnable runnable) {
		optionActions.put(1, runnable);
		return this;
	}

	public DualItemOptionMessage onOptionTwo(final Runnable runnable) {
		optionActions.put(2, runnable);
		return this;
	}

	void onClick(final int option) {
		final Runnable action = optionActions.get(option);
		if (action == null) {
			return;
		}
		try {
			action.run();
		} catch (Exception e) {
			log.error("", e);
		}
	}

	@Override
	public final void display(final Player player) {
		player.getPacketDispatcher().sendClientScript(2379);
		player.getVarManager().sendBitInstant(5983, 0);
		player.getInterfaceHandler().sendInterface(InterfacePosition.DIALOGUE, OPTION_INTERFACE);
		player.getPacketDispatcher().sendComponentText(OPTION_INTERFACE, 2, option1);
		player.getPacketDispatcher().sendComponentText(OPTION_INTERFACE, 3, option2);
		player.getPacketDispatcher().sendComponentText(OPTION_INTERFACE, 4, text);
		player.getPacketDispatcher().sendComponentItem(OPTION_INTERFACE, 5, item1, 0);
		player.getPacketDispatcher().sendComponentItem(OPTION_INTERFACE, 6, item2, 0);
	}
}
