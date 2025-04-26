package com.zenyte.game.content.skills.construction;

import com.google.gson.annotations.Expose;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.ContainerResult;

/**
 * @author Kris | 24. veebr 2018 : 4:05.18
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class TipJar {
	private final transient Player player;
	@Expose
	private long amount;
	@Expose
	private boolean displayingNotifications;
	@Expose
	private boolean bankCoinsOnLogout;

	public TipJar(final Player player) {
		this.player = player;
		displayingNotifications = true;
	}

	public void set(final TipJar jar) {
		amount = jar.amount;
		displayingNotifications = jar.displayingNotifications;
		bankCoinsOnLogout = jar.bankCoinsOnLogout;
	}

	public void onLogout() {
		if (!bankCoinsOnLogout) {
			return;
		}
		if (amount == 0) {
			return;
		}
		final ContainerResult req = player.getBank().add(new Item(995, (int) Math.min(Integer.MAX_VALUE, amount)));
		final int succeededAmount = req.getSucceededAmount();
		amount -= succeededAmount;
	}

	public void addMoney(final String name, final int amount) {
		player.sendMessage(name + " has left you a tip: Coins x " + amount);
		this.amount += amount;
	}

	public void withdraw() {
		if (amount == 0) {
			player.sendMessage("You have no coins in the jar right now!");
			return;
		}
		player.sendInputInt("How many coins would you like to withdraw?", value -> {
			if (!player.getInventory().hasFreeSlots() && !player.getInventory().containsItem(995, 1)) {
				player.sendMessage("You need some more free space to withdraw the cash.");
				return;
			}
			if (value > amount) {
				value = (int) amount;
			}
			final int inInv = player.getInventory().getAmountOf(995);
			if (value > (Integer.MAX_VALUE - inInv)) {
				value = (Integer.MAX_VALUE - inInv);
			}
			if (value == 0) {
				player.sendMessage("You haven't got enough room to withdraw that much!");
				return;
			}
			player.getInventory().addItem(995, value);
			amount -= value;
		});
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public boolean isDisplayingNotifications() {
		return displayingNotifications;
	}

	public void setDisplayingNotifications(boolean displayingNotifications) {
		this.displayingNotifications = displayingNotifications;
	}

	public boolean isBankCoinsOnLogout() {
		return bankCoinsOnLogout;
	}

	public void setBankCoinsOnLogout(boolean bankCoinsOnLogout) {
		this.bankCoinsOnLogout = bankCoinsOnLogout;
	}
}
