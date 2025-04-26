package com.zenyte.game.model.item.pluginextensions;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemActionHandler;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.plugins.Plugin;
import mgi.types.config.items.ItemDefinitions;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kris | 11. nov 2017 : 16:15.02
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public abstract class ItemPlugin implements Plugin {

	public static final ItemDeathHandler DEFAULT_ITEM_DEATH_HANDLER = (player, item, protectedCount, deepWilderness, pvp) -> {
		ItemDeathPlugin plugin = new ItemDeathPlugin();
		if (item.isTradable()) {
			plugin.lost(() -> List.of(item));
			if (pvp) {
				plugin.status(() -> ItemDeathStatus.DROP_ON_DEATH);
			} else {
				plugin.status(() -> ItemDeathStatus.GO_TO_GRAVESTONE);
			}
		} else {
			if (true || !deepWilderness) {
				plugin.lost(() -> List.of(item));
				plugin.status(() -> ItemDeathStatus.GO_TO_GRAVESTONE);
			} else {
				plugin.status(() -> ItemDeathStatus.DELETE);
			}
		}
		return plugin;
	};

	@NotNull
	public static ItemPlugin getPlugin(final int id) {
		return Utils.getOrDefault(ItemActionHandler.intActions.get(id), ItemActionHandler.defaultAction);
	}

	@NotNull
	public static ItemPlugin getDeathPlugin(final int id) {
		return Utils.getOrDefault(ItemActionHandler.deathIntActions.get(id), ItemActionHandler.defaultAction);
	}

	public Map<String, OptionHandler> getDelegatedInventoryHandlers() {
		return delegatedInventoryHandlers;
	}

	private final Map<String, OptionHandler> delegatedInventoryHandlers = new HashMap<>(3);

	public void setItemDeathHandler(ItemDeathHandler itemDeathHandler) {
		this.itemDeathHandler = itemDeathHandler;
	}

	public ItemDeathHandler getItemDeathHandler() {
		return itemDeathHandler;
	}

	private ItemDeathHandler itemDeathHandler = DEFAULT_ITEM_DEATH_HANDLER;

	public abstract void handle();

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ItemPlugin that = (ItemPlugin) o;

		if (!delegatedInventoryHandlers.equals(that.delegatedInventoryHandlers)) return false;
		return itemDeathHandler.equals(that.itemDeathHandler);
	}

	@Override
	public int hashCode() {
		int result = delegatedInventoryHandlers.hashCode();
		result = 31 * result + itemDeathHandler.hashCode();
		return result;
	}

	public void bind(final String option, final OptionHandler handler) {
		verifyIfOptionExists(option);
		delegatedInventoryHandlers.put(option.toLowerCase(), handler);
	}

	public void bind(final String option, final BasicOptionHandler handler) {
		verifyIfOptionExists(option);
		delegatedInventoryHandlers.put(option.toLowerCase(), handler);
	}

	// Java version, different name to avoid confusion. Should mainly be used from kotlin.
	public void onDeath(final ItemDeathHandler handler) {
		this.itemDeathHandler = handler;
	}

	public OptionHandler getHandler(final String option) {
		return delegatedInventoryHandlers.get(option.toLowerCase());
	}

	public void setDefaultHandlers() {
		setDefault("Drop", (player, item, slotId) -> ItemActionHandler.dropItem(player, "Drop", slotId, 300, 500));
		setDefault("Destroy", (player, item, slotId) -> ItemActionHandler.dropItem(player, "Destroy", slotId, 300, 500));
		setDefault("Check", (player, item, slotId) -> player.getChargesManager().checkCharges(item));
		setDefault("Wear", (player, item, slotId) -> player.getEquipment().wear(slotId));
		setDefault("Wield", (player, item, slotId) -> player.getEquipment().wear(slotId));
		setDefault("Equip", (player, item, slotId) -> player.getEquipment().wear(slotId));
		setDefault("Remove", (player, item, slotId) -> {
			player.stopAll(false, !player.getInterfaceHandler().isVisible(GameInterface.EQUIPMENT_STATS.getId()), slotId == EquipmentSlot.WEAPON.getSlot());
			player.getEquipment().unequipItem(slotId);
		});
	}

	protected void setDefault(final String option, final BasicOptionHandler handler) {
		final String lowercase = option.toLowerCase();
		if (delegatedInventoryHandlers.containsKey(lowercase)) {
			return;
		}
		delegatedInventoryHandlers.put(lowercase, handler);
	}

	private void verifyIfOptionExists(final String option) {
		if (getItems().length == 0) return;
		ItemDefinitions def = null;
		for (final int id : getItems()) {
			final ItemDefinitions definitions = ItemDefinitions.get(id);
			if (definitions == null) {
				continue;
			}
			if (definitions.containsOption(option) || definitions.containsParamByValue(option)) {
				return;
			}
			def = definitions;
		}
		throw new RuntimeException("None of the items enlisted in " + getClass().getSimpleName() + " contains option " + option + " - " + def.getParameters());
	}

	public abstract int[] getItems();


	@FunctionalInterface
	public interface BasicOptionHandler extends OptionHandler {
		void handle(final Player player, final Item item, int slotId);

		@Override
		@Deprecated
		default void handle(final Player player, final Item item, final Container container, int slotId) {
			handle(player, item, slotId);
		}
	}


	@FunctionalInterface
	public interface OptionHandler {
		void handle(final Player player, final Item item, final Container container, int slotId);
	}

	@FunctionalInterface
	public interface ItemDeathHandler {
		ItemDeathPlugin handle(final Player player, final Item item, final int protectedCount, final boolean deepWilderness, final boolean pvp);
	}

	@FunctionalInterface
	public interface ItemSeparationOnDeath {
		List<Item> getSeparated();
	}

	@FunctionalInterface
	public interface ItemStatusOnDeath {
		ItemDeathStatus getStatus();
	}

	public static class ItemDeathPlugin {
		public ItemSeparationOnDeath getLost() {
			return lost;
		}

		public ItemSeparationOnDeath getKept() {
			return kept;
		}

		public ItemStatusOnDeath getStatus() {
			return status;
		}

		private ItemSeparationOnDeath lost = EMPTY;
		private ItemSeparationOnDeath kept = EMPTY;
		private ItemStatusOnDeath status;

		public boolean isAlwaysLostOnDeath() {
			return alwaysLostOnDeath;
		}

		private boolean alwaysLostOnDeath;

		public boolean isAlwaysKeptOnDeath() {
			return alwaysKeptOnDeath;
		}

		private boolean alwaysKeptOnDeath;

		public Runnable getRunAfterDeath() {
			return runAfterDeath;
		}

		private Runnable runAfterDeath;

		public void setAlwaysLostOnDeath() {
			this.alwaysLostOnDeath = true;
		}
		public void setAlwaysKeptOnDeath() {
			this.alwaysKeptOnDeath = true;
		}

		public void lost(ItemSeparationOnDeath block) {
			this.lost = block;
		}

		public void kept(ItemSeparationOnDeath block) {
			this.kept = block;
		}

		public void status(ItemStatusOnDeath block) {
			this.status = block;
		}

		public void afterDeath(Runnable runnable) {
			this.runAfterDeath = runnable;
		}

		private static final ItemSeparationOnDeath EMPTY = List::of;
	}
}
