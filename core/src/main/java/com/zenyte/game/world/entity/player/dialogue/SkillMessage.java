package com.zenyte.game.world.entity.player.dialogue;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.packet.PacketDispatcher;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.var.VarCollection;
import com.zenyte.plugins.dialogue.MakeType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kris | 22. okt 2017 : 13:29.45
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public class SkillMessage implements Message {
	public static final int INTERFACE_ID = 270;

	public SkillMessage(final int maxQuantity, final boolean maxTen, final String message, final Item... items) {
		final StringBuilder builder = new StringBuilder(message);
		for (final Item item : items) {
			builder.append("|");
			if (item.getAmount() == 1) {
				builder.append(item.getDefinitions().getName());
				continue;
			}
			final String name = item.getDefinitions().getName().toLowerCase();
			builder.append(item.getAmount()).append(" ").append(name);
			builder.append(name.endsWith("s") ? "'" : "s");
		}
		arguments.add(builder.toString());
		arguments.add(maxTen ? 1 : 0);
		this.message = message;
		arguments.add(maxQuantity);
		for (int i = 0; i < 10; i++) {
			arguments.add(i < items.length ? items[i].getId() : -1);
		}
	}

	public SkillMessage(final int maxQuantity, final MakeType type, final String message, final Item... items) {
		final StringBuilder builder = new StringBuilder(message);
		for (final Item item : items) {
			builder.append("|");
			if (item.getAmount() == 1) {
				builder.append(item.getDefinitions().getName());
				continue;
			}
			final String name = item.getDefinitions().getName().toLowerCase();
			builder.append(item.getAmount()).append(" ").append(name);
			builder.append(name.endsWith("s") ? "'" : "s");
		}
		arguments.add(builder.toString());
		arguments.add(type.getId());
		this.message = message;
		arguments.add(maxQuantity);
		for (int i = 0; i < 10; i++) {
			arguments.add(i < items.length ? items[i].getId() : -1);
		}
	}

	private final List<Object> arguments = new ArrayList<Object>(14);
	private final String message;

	@Override
	public void display(final Player player) {
		final Object lastAmount = player.getTemporaryAttributes().get("lastSkillDialogueAmount");
		final int amount = lastAmount instanceof Integer ? (int) lastAmount : 28;
		arguments.add(amount);
		final PacketDispatcher dispatcher = player.getPacketDispatcher();
		player.getInterfaceHandler().sendInterface(InterfacePosition.DIALOGUE, INTERFACE_ID);
		player.getPacketDispatcher().sendClientScript(2046, arguments.toArray(new Object[0]));
		// player.getPacketDispatcher().sendComponentText(INTERFACE_ID, 5, message);
		VarCollection.DIALOGUE_FULL_SIZE.updateSingle(player);
		for (int i = 0; i < amount; i++) {
			dispatcher.sendComponentSettings(INTERFACE_ID, 14 + i, 1, 7, AccessMask.CONTINUE);
		}
	}
}
