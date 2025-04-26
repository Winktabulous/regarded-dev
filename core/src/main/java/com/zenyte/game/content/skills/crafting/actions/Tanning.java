package com.zenyte.game.content.skills.crafting.actions;

import com.zenyte.game.content.achievementdiary.diaries.MorytaniaDiary;
import com.zenyte.game.content.skills.crafting.CraftingDefinitions.Leather;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.player.Action;

/**
 * @author Tommeh | 15 mei 2018 | 19:51:50
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class Tanning extends Action {
	private final Leather leather;
	private final int amount;

	private boolean check(final boolean message) {
		if (!player.getInventory().containsItem(leather.getBase())) {
			if (message) {
				player.sendMessage("You don't have any " + leather.getBase().getName().toLowerCase() + "s to tan.");
			}
			return false;
		}
		if (!player.getInventory().containsItem(new Item(995, leather.getCost()))) {
			if (message) {
				player.sendMessage("You haven't got enough coins to pay for " + leather.getName() + ".");
			}
			return false;
		}
		return true;
	}

	public Tanning(Leather leather, int amount) {
		this.leather = leather;
		this.amount = amount;
	}

	@Override
	public boolean start() {
		return check(true);
	}

	@Override
	public boolean process() {
		return true;
	}

	@Override
	public int processWithDelay() {
		player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
		if (amount > 0) {
			player.getAchievementDiaries().update(MorytaniaDiary.HAVE_SBOTT_TAN_ITEMS);
		}
		int i;
		for (i = 0; i < amount; i++) {
			if (!check(false)) {
				break;
			}
			final Item price = new Item(995, leather.getCost());
			player.getInventory().deleteItem(price);
			player.getInventory().deleteItem(leather.getBase());
			player.getInventory().addItem(leather.getProduct());
		}
		if (i > 0) player.sendFilteredMessage("The tanner tans your " + leather.getBase().getName().toLowerCase() + ".");
		return -1;
	}
}
