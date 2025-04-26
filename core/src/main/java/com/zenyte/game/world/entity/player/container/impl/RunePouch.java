package com.zenyte.game.world.entity.player.container.impl;

import com.google.gson.annotations.Expose;
import com.zenyte.game.content.boons.impl.ArcaneKnowledge;
import com.zenyte.game.content.skills.magic.Rune;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.region.area.plugins.TempPlayerStatePlugin;

import java.util.Optional;

/**
 * @author Tommeh | 26 mrt. 2018 : 18:08:22
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class RunePouch implements TempPlayerStatePlugin.State {
	public static final Item RUNE_POUCH = new Item(ItemId.RUNE_POUCH);
	public static final Item TOURNAMENT_RUNE_POUCH = new Item(ItemId.TOURNAMENT_RUNE_POUCH);
	public static final Item DIVINE_RUNE_POUCH = new Item(ItemId.DIVINE_RUNE_POUCH);
	public static final Item[] POUCHES = { RUNE_POUCH, DIVINE_RUNE_POUCH, TOURNAMENT_RUNE_POUCH};
	private final transient Player player;
	private final Container container;
	private final TempPlayerStatePlugin.StateType tempType;
	private final boolean tempVariant;

	@Expose
	public int bonusRuneQuantityStored = 0;
	@Expose
	public Rune bonusRuneTypeStored = null;

	public RunePouch(final Player player, TempPlayerStatePlugin.StateType tempType) {
		this(player, tempType, false);
	}

	public RunePouch(final Player player, TempPlayerStatePlugin.StateType tempType, boolean tempVariant) {
		this.player = player;
		this.container = new RunePouchContainer(ContainerPolicy.ALWAYS_STACK, ContainerType.RUNE_POUCH, Optional.ofNullable(player));
		this.tempType = tempType;
		this.tempVariant = tempVariant;
	}

	public int runePouchCapacity() {
		if(player.getInventory().containsItem(TOURNAMENT_RUNE_POUCH))
			return 4;
		return player.getInventory().containsItem(DIVINE_RUNE_POUCH) ? player.getBoonManager().hasBoon(ArcaneKnowledge.class) ? 5 : 4 : 3;
	}

	public void emptyRunePouch() {
		for (int slot = 0; slot < runePouchCapacity(); slot++) {
			final Item rune = container.get(slot);
			if (rune == null || rune.getAmount() <= 0) {
				continue;
			}
			player.getInventory().getContainer().deposit(null, container, slot, rune.getAmount());
		}
		if (!container.isEmpty()) {
			player.sendMessage("Not enough space in your inventory.");
		} else {
			if(player.getRunePouch().bonusRuneQuantityStored > 0 && player.getRunePouch().bonusRuneTypeStored != null) {
				if(!player.getInventory().hasFreeSlots()) {
					player.sendMessage("Please clear one inventory slot to finish emptying your pouch.");
					return;
				}
				player.getInventory().addItem(new Item(player.getRunePouch().bonusRuneTypeStored.getId(), player.getRunePouch().bonusRuneQuantityStored));
				player.getRunePouch().bonusRuneTypeStored = null;
				player.getRunePouch().bonusRuneQuantityStored = 0;
			}
		}

		player.getInventory().refreshAll();
		container.refresh(player);
	}

	public void switchItem(final int fromSlot, final int toSlot) {
		final Item fromItem = container.get(fromSlot);
		final Item toItem = container.get(toSlot);
		container.set(fromSlot, toItem);
		container.set(toSlot, fromItem);
		container.setFullUpdate(true);
		container.refresh(player);
	}

	public final void initialize(final RunePouch pouch) {
		if (pouch == null || pouch.container == null) {
			return;
		}
		this.container.setContainer(pouch.container);
		this.bonusRuneQuantityStored = pouch.bonusRuneQuantityStored;
		this.bonusRuneTypeStored = pouch.bonusRuneTypeStored;
	}

	private int getIdVarbit(final int slot) {
		switch (slot) {
			case 0 -> {
				return 29;
			}
			case 1 -> {
				return 1622;
			}
			case 2 -> {
				return 1623;
			}
			case 3 -> {
				return 14285;
			}
			case 4-> {
				return 19494;
			}
			default -> {
				return -1;
			}
		}
	}

	private int getAmountVarbit(final int slot) {
		switch (slot) {
			case 0 -> {
				return 1624;
			}
			case 1 -> {
				return 1625;
			}
			case 2 -> {
				return 1626;
			}
			case 3 -> {
				return 14286;
			}
			case 4-> {
				return 19495;
			}
			default -> {
				return -1;
			}
		}
	}

	public Item getRune(final int slot) {
		return container.get(slot);
	}

	public int getSlot(final int id) {
		return container.getSlotOf(id);
	}

	public int getAmountOf(final int id) {
		if (!player.getInventory().containsAnyOf(POUCHES)) {
			return 0;
		}
		return container.getAmountOf(id);
	}

	public void clear() {
		container.clear();
		player.getInventory().refreshAll();
		container.refresh(player);
	}

	public Container getContainer() {
		return container;
	}

	public static RunePouch chooseRunePouch(final Player player, final int item) {
		if (item == ItemId.TOURNAMENT_RUNE_POUCH) {
			return player.getSecondaryRunePouch();
		}
		return player.getRunePouch();
	}

	@Override
	public boolean isTempVariant() {
		return tempVariant;
	}

	@Override
	public TempPlayerStatePlugin.StateType tempType() {
		return tempType;
	}

	private final class RunePouchContainer extends Container {
		private RunePouchContainer(final ContainerPolicy policy, final ContainerType type, final Optional<Player> player) {
			super(policy, type, player);
		}

		@Override
		public void set(final int slot, final Item item) {
			super.set(slot, item);
			refresh();
		}

		@Override
		public void refresh(final Player player) {
			super.refresh(player);
			for (int i = 0; i < 4; i++) {
				final Item item = container.getItems().get(i);
				if (item == null) {
					player.getVarManager().sendBit(getIdVarbit(i), 0);
					player.getVarManager().sendBit(getAmountVarbit(i), 0);
					continue;
				}
				final Rune rune = Rune.getRune(item);
				if (rune == null) {
					player.getVarManager().sendBit(getIdVarbit(i), 0);
					player.getVarManager().sendBit(getAmountVarbit(i), 0);
					continue;
				}
				player.getVarManager().sendBit(getIdVarbit(i), rune.ordinal() + 1);
				player.getVarManager().sendBit(getAmountVarbit(i), Math.min(16000, item.getAmount()));
			}
			if(bonusRuneQuantityStored > 0 && bonusRuneTypeStored != null) {
				player.getVarManager().sendBit(getIdVarbit(4), bonusRuneTypeStored.ordinal() + 1);
				player.getVarManager().sendBit(getAmountVarbit(4), Math.min(16000, bonusRuneQuantityStored));
			} else {
				player.getVarManager().sendBit(getIdVarbit(4), 0);
				player.getVarManager().sendBit(getAmountVarbit(4), 0);
			}
		}
	}
}
