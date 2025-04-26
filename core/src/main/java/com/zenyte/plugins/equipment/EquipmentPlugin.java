package com.zenyte.plugins.equipment;

import com.zenyte.game.model.item.pluginextensions.ItemPlugin.BasicOptionHandler;
import com.zenyte.plugins.Plugin;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mgi.types.config.items.ItemDefinitions;

import java.util.Map;

/**
 * @author Kris | 11. nov 2017 : 16:15.02
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public abstract class EquipmentPlugin implements Plugin {
	private final Map<String, BasicOptionHandler> delegatedHandlers = new Object2ObjectOpenHashMap<>(3);

	public abstract void handle();

	public void bind(final String option, final BasicOptionHandler handler) {
		verifyIfOptionExists(option);
		delegatedHandlers.put(option.toLowerCase(), handler);
	}

	public BasicOptionHandler getHandler(final String option) {
		return delegatedHandlers.get(option.toLowerCase());
	}

	public final void setDefaultHandlers() {
		setDefault("Check", (player, item, slotId) -> player.getChargesManager().checkCharges(item));
		setDefault("Remove", (player, item, slotId) -> {
			player.stopAll(false, true, true);
			player.getEquipment().unequipItem(slotId);
		});
	}

	private void setDefault(final String option, final BasicOptionHandler handler) {
		final String lowercase = option.toLowerCase();
		if (delegatedHandlers.containsKey(lowercase)) {
			return;
		}
		delegatedHandlers.put(lowercase, handler);
	}

	private void verifyIfOptionExists(final String option) {
		for (final int id : getItems()) {
			final ItemDefinitions definitions = ItemDefinitions.get(id);
			if (definitions == null) {
				continue;
			}
			final Int2ObjectMap<Object> params = definitions.getParameters();
			if (params.containsValue(option)) {
				return;
			}
		}
		throw new RuntimeException("None of the items enlisted in " + getClass().getSimpleName() + " contains option " + option + ".");
	}

	public abstract int[] getItems();
}
