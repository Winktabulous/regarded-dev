package com.zenyte.plugins.item;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.skills.cooking.CocktailShaker;
import com.zenyte.game.content.skills.cooking.GnomeCocktail;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.plugins.dialogue.PlainChat;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import mgi.types.config.items.ItemDefinitions;

/**
 * @author Kris | 25. aug 2018 : 22:16:11
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class CocktailShakerItem extends ItemPlugin {
	@Override
	public void handle() {
		bind("Mix-cocktail", (player, item, slotId) -> GameInterface.GNOME_COCKTAIL.open(player));
		bind("Pour", (player, item, slotId) -> {
			final CocktailShaker shaker = CocktailShaker.SHAKERS.get(item.getId());
			if (shaker == null) {
				return;
			}
			if (!player.getInventory().containsItem(2026, 1) || !CocktailShaker.hasGarnish(player, shaker, false)) {
				if (!player.getInventory().containsItem(2026, 1)) {
					player.sendMessage("You need an empty cocktail glass to pour this cocktail.");
				}
				if (shaker != CocktailShaker.DRUNK_DRAGON && shaker != CocktailShaker.CHOCOLATE_SATURDAY) {
					for (final int garnish : shaker.getGarnish()) {
						if (!player.getInventory().containsItem(garnish, 1)) {
							final String name = ItemDefinitions.get(garnish).getName().toLowerCase();
							player.sendMessage("You need " + (name.contains("slice") ? "a slice of " + name.split(" ")[0] : name) + " to finish this cocktail.");
						}
					}
				}
				return;
			}
			if (shaker != CocktailShaker.DRUNK_DRAGON && shaker != CocktailShaker.CHOCOLATE_SATURDAY) {
				for (final int garnish : shaker.getGarnish()) {
					player.getInventory().deleteItem(garnish, 1);
				}
			}
			final String name = shaker.toString().contains("_") ? shaker.toString().replaceAll("_", " ") : shaker.toString();
			player.getInventory().deleteItem(shaker.getId(), 1);
			player.getInventory().deleteItem(2026, 1);
			player.getInventory().addItem(2025, 1);
			player.getInventory().addItem(shaker.getProduct());
			player.sendMessage("You pour the " + name.toLowerCase() + ".");
			if (shaker == CocktailShaker.DRUNK_DRAGON || shaker == CocktailShaker.CHOCOLATE_SATURDAY) {
				String list = "";
				for (final int garnish : shaker.getGarnish()) {
					list += (ItemDefinitions.get(garnish).getName().toLowerCase().contains("pot") ? "cream, " : ItemDefinitions.get(garnish).getName().toLowerCase() + ", ");
				}
				final String message = (shaker == CocktailShaker.CHOCOLATE_SATURDAY) ? "You need to heat then enqueue cream and chocolate dust to finish." : "You need to enqueue " + list + "and then heat to finish this cocktail.";
				player.getDialogueManager().start(new PlainChat(player, message));
			} else {
				player.getSkills().addXp(SkillConstants.COOKING, shaker.getXp() - 30);
			}
		});
		bind("Add-ingreds", (player, item, slotId) -> {
			final CocktailShaker shaker = item.getId() == 9573 ? CocktailShaker.CHOCOLATE_SATURDAY : CocktailShaker.DRUNK_DRAGON;
			if (!CocktailShaker.hasGarnish(player, shaker, true)) {
				for (final int garnish : shaker.getGarnish()) {
					if (!player.getInventory().containsItem(garnish, 1)) {
						final String name = ItemDefinitions.get(garnish).getName().toLowerCase();
						player.sendMessage("You need " + (name.contains("slice") ? "a slice of " + name.split(" ")[0] : name) + " to finish this cocktail.");
					}
				}
				return;
			}
			for (final int garnish : shaker.getGarnish()) {
				player.getInventory().deleteItem(garnish, 1);
			}
			final int product = (shaker == CocktailShaker.CHOCOLATE_SATURDAY) ? 2074 : 9576;
			final String message = (shaker == CocktailShaker.CHOCOLATE_SATURDAY) ? "You enqueue the finishing touches to this cocktail." : "You enqueue some pineapple chunks and cream. You need to warm this cocktail to finish.";
			player.getInventory().deleteItem(item);
			player.getInventory().addItem(product, 1);
			player.getSkills().addXp(SkillConstants.COOKING, shaker.getXp() - 30);
			player.getDialogueManager().start(new PlainChat(player, message));
		});
	}

	@Override
	public int[] getItems() {
		final IntArrayList list = new IntArrayList();
		for (final GnomeCocktail cocktail : GnomeCocktail.VALUES) {
			list.add(cocktail.getShaker());
		}
		list.add(2025);
		list.add(9573);
		list.add(9575);
		return list.toArray(new int[list.size()]);
	}
}
