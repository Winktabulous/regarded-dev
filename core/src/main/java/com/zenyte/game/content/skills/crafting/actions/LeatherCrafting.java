package com.zenyte.game.content.skills.crafting.actions;

import com.zenyte.game.content.achievementdiary.diaries.LumbridgeDiary;
import com.zenyte.game.content.achievementdiary.diaries.MorytaniaDiary;
import com.zenyte.game.content.skills.crafting.CraftingDefinitions;
import com.zenyte.game.content.treasuretrails.clues.CharlieTask;
import com.zenyte.game.content.treasuretrails.clues.SherlockTask;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.dailychallenge.challenge.SkillingChallenge;
import com.zenyte.plugins.dialogue.PlainChat;
import com.zenyte.plugins.dialogue.PlayerChat;

import static com.zenyte.game.content.skills.crafting.CraftingDefinitions.*;

/**
 * @author Tommeh | 26 aug. 2018 | 13:34:34
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class LeatherCrafting extends Action {
	private static final Animation ANIMATION = new Animation(1249);
	private final int category;
	private final int slotId;
	private final int amount;
	private int cycle;
	private int ticks;

	public static Item[] getCategory(Player player, Item from, Item to) {
		Item[] items = null;
		int category = -1;
		if (from.getId() == 1741 || to.getId() == 1741) {
			items = PRODUCTS[0];
			category = 0;
		} else if (from.getId() == 1743 || to.getId() == 1743) {
			items = PRODUCTS[1];
			category = 1;
		} else if (from.getId() == 2370 || to.getId() == 2370 && (from.getId() == 1095 || from.getId() == 1129) && (to.getId() == 1095 || to.getId() == 1129)) {
			items = PRODUCTS[2];
			category = 2;
		} else if (from.getId() == 10113 || to.getId() == 10113 && (from.getId() == 1063 || from.getId() == 1065 || from.getId() == 2487 || from.getId() == 2489 || from.getId() == 2491 || to.getId() == 1063 || to.getId() == 1065 || to.getId() == 2487 || to.getId() == 2489 || to.getId() == 2491)) {
			items = PRODUCTS[3];
			category = 3;
		} else if (from.getId() == 1745 || to.getId() == 1745) {
			items = PRODUCTS[4];
			category = 4;
		} else if (from.getId() == 2505 || to.getId() == 2505) {
			items = PRODUCTS[5];
			category = 5;
		} else if (from.getId() == 2507 || to.getId() == 2507) {
			items = PRODUCTS[6];
			category = 6;
		} else if (from.getId() == 2509 || to.getId() == 2509) {
			items = PRODUCTS[7];
			category = 7;
		} else if (from.getId() == 6289 || to.getId() == 6289) {
			items = PRODUCTS[8];
			category = 8;
		} else if (from.getId() == 10820 || to.getId() == 10820) {
			items = PRODUCTS[9];
			category = 9;
		} else if (from.getId() == 7536 || from.getId() == 7538 || to.getId() == 7536 || to.getId() == 7538) {
			items = PRODUCTS[10];
			category = 10;
		} else if (from.getId() == 13383 || to.getId() == 13383) {
			items = PRODUCTS[11];
			category = 11;
		}
		player.getTemporaryAttributes().put("LeatherCategory", category);
		return items;
	}

	public static boolean isMaterial(Player player, Item from, Item to) {
		for (int i = 0; i < 2; i++) if ((from.getId() == MATERIALS[2][i].getId() || to.getId() == MATERIALS[2][i].getId()) && (from.getId() == 2370 || to.getId() == 2370)) return true;
		for (int i = 0; i < 2; i++) if ((from.getId() == MATERIALS[3][i].getId() || to.getId() == MATERIALS[3][i].getId()) && (from.getId() == 10113 || to.getId() == 10113)) return true;
		for (int i = 0; i < 2; i++) if ((from.getId() == MATERIALS[10][i].getId() || to.getId() == MATERIALS[10][i].getId()) && (from.getId() == CraftingDefinitions.CHISEL.getId() || to.getId() == CraftingDefinitions.CHISEL.getId())) return true;
		if ((from.getId() == CraftingDefinitions.THREAD.getId() || to.getId() == CraftingDefinitions.THREAD.getId()) && (from.getId() == CraftingDefinitions.NEEDLE.getId() || to.getId() == CraftingDefinitions.NEEDLE.getId())) player.getDialogueManager().start(new PlayerChat(player, "Perhaps I should use the needle with a piece of leather instead."));
		for (int i = 0; i < MATERIALS.length; i++) {
			for (int x = 0; x < MATERIALS[i].length; x++) if ((from.getId() == MATERIALS[i][x].getId() || to.getId() == MATERIALS[i][x].getId()) && (from.getId() == CraftingDefinitions.NEEDLE.getId() || to.getId() == CraftingDefinitions.NEEDLE.getId()) && from.getId() != CraftingDefinitions.THREAD.getId() && to.getId() != CraftingDefinitions.THREAD.getId()) return true;
		}
		return false;
	}

	@Override
	public boolean start() {
		if (player.getSkills().getLevel(SkillConstants.CRAFTING) < LEVELS[category][slotId]) {
			player.getDialogueManager().start(new PlainChat(player, "You need a Crafting level of " + LEVELS[category][slotId] + " to make a " + PRODUCTS[category][slotId].getDefinitions().getName().toLowerCase() + "."));
			return false;
		}
		if (!player.getInventory().containsItem(MATERIALS[category][slotId])) {
			final String message = (MATERIALS[category][slotId].getAmount() > 1 ? "You need " + MATERIALS[category][slotId].getAmount() + " " + (MATERIALS[category][slotId].getDefinitions().getName().endsWith("y") ? MATERIALS[category][slotId].getDefinitions().getName().toLowerCase().replace("y", "ies") : MATERIALS[category][slotId].getDefinitions().getName().endsWith("s") ? MATERIALS[category][slotId].getDefinitions().getName().toLowerCase() : MATERIALS[category][slotId].getDefinitions().getName().toLowerCase() + "s") : "You need " + MATERIALS[category][slotId].getAmount() + " " + MATERIALS[category][slotId].getDefinitions().getName().toLowerCase()) + " to make a " + PRODUCTS[category][slotId].getDefinitions().getName().toLowerCase() + ".";
			player.sendMessage(message);
			return false;
		}
		if (category == 2 && !player.getInventory().containsItem(2370, 1)) {
			return false;
		}
        return category != 3 || player.getInventory().containsItem(10113, 1);
    }

	@Override
	public boolean process() {
		if (!player.getInventory().containsItem(MATERIALS[category][slotId])) {
			return false;
		}
		if (!player.getInventory().containsItem(CraftingDefinitions.THREAD)) {
			player.sendMessage("You've ran out of thread.");
			return false;
		}
		if (!player.getInventory().containsItem(CraftingDefinitions.NEEDLE)) {
			player.sendMessage("You haven't got anymore needles.");
			return false;
		}
		if (category == 2 && !player.getInventory().containsItem(2370, 1)) {
			return false;
		}
		if (category == 3 && !player.getInventory().containsItem(10113, 1)) {
			return false;
		}
        return cycle < amount;
    }

	public LeatherCrafting(int category, int slotId, int amount) {
		this.category = category;
		this.slotId = slotId;
		this.amount = amount;
	}

	@Override
	public int processWithDelay() {
		final Item product = PRODUCTS[category][slotId];
		if (ticks == 0) {
			if (category != 10) {
				player.setAnimation(ANIMATION);
			}
		} else if (ticks == 3) {
			if (product.getId() == PRODUCTS[0][6].getId()) {
				player.getAchievementDiaries().update(LumbridgeDiary.CRAFT_A_COIF);
			} else if (product.getId() == PRODUCTS[7][2].getId()) {
				player.getDailyChallengeManager().update(SkillingChallenge.CRAFT_BLACK_DRAGONHIDE_BODIES);
				player.getAchievementDiaries().update(MorytaniaDiary.CRAFT_BLACK_DRAGONHIDE_BODY);
			}
			if (product.getId() == ItemId.LEATHER_BODY) {
				CharlieTask.CRAFT_A_LEATHER_BODY.progress(player);
			} else if (product.getId() == ItemId.LEATHER_CHAPS) {
				CharlieTask.CRAFT_LEATHER_CHAPS.progress(player);
			}
			if (Utils.random(5) == 0) {
				player.getInventory().deleteItem(CraftingDefinitions.THREAD);
			}
			if (category == 3) {
				player.getInventory().deleteItem(new Item(10113));
			} else if (category == 2) {
				player.getInventory().deleteItem(new Item(2370));
			}
			if (product.getId() == 1135) {
				SherlockTask.CRAFT_GREEN_DHIDE_BODY.progress(player);
			}
			player.getInventory().deleteItem(MATERIALS[category][slotId]);
			player.getInventory().addItem(product);
			player.getSkills().addXp(SkillConstants.CRAFTING, EXPERIENCE[category][slotId]);
			player.sendFilteredMessage("You make a " + product.getDefinitions().getName().toLowerCase() + ".");
			cycle++;
			return ticks = 0;
		}
		ticks++;
		return 0;
	}
}
