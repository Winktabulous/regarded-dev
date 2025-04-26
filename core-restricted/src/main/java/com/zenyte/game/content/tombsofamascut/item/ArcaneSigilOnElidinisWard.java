package com.zenyte.game.content.tombsofamascut.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Skills;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

import static com.zenyte.game.content.skills.runecrafting.BasicRunecraftingAction.RUNECRAFTING_ANIM;
import static com.zenyte.game.content.skills.runecrafting.BasicRunecraftingAction.RUNECRAFTING_GFX;

@SuppressWarnings("unused")
public class ArcaneSigilOnElidinisWard implements PairedItemOnItemPlugin {

	private static final Item SOUL_RUNES = new Item(ItemId.SOUL_RUNE, 10_000);
	private static final Item ELIDINIS_WARD_F = new Item(ItemId.ELIDINIS_WARD_F);

	@Override
	public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
		if (Math.max(player.getSkills().getLevel(Skills.PRAYER), player.getSkills().getLevelForXp(Skills.PRAYER)) < 90) {
			player.sendMessage("You need a Prayer level of at least 90 to fortify Elidinis' ward.");
			return;
		}

		if (player.getSkills().getLevel(Skills.SMITHING) < 90) {
			player.sendMessage("You need a Smithing level of at least 90 to fortify Elidinis' ward.");
			return;
		}

		if (!player.getInventory().containsItem(SOUL_RUNES)) {
			player.sendMessage("You need at least 10,000 Soul runes to fortify Elidinis' ward.");
			return;
		}

		player.getDialogueManager().start(new Dialogue(player) {
			@Override
			public void buildDialogue() {
				options("Consume 10,000 soul runes to fortify Elidinis' ward?", new DialogueOption("Yes.", () -> {
					if (!player.getInventory().containsItem(SOUL_RUNES) || !player.getInventory().containsItem(from) || !player.getInventory().containsItem(to)) {
						return;
					}

					player.lock();
					player.setAnimation(RUNECRAFTING_ANIM);
					player.setGraphics(RUNECRAFTING_GFX);
					player.delay(2, () -> {
						player.unlock();
						player.getInventory().deleteItem(from);
						player.getInventory().deleteItem(to);
						player.getInventory().deleteItem(SOUL_RUNES);
						player.getInventory().addItem(ELIDINIS_WARD_F);
						player.getSkills().addXp(Skills.PRAYER, 260);
						player.getSkills().addXp(Skills.SMITHING, 260);
						player.getDialogueManager().start(new Dialogue(player) {
							@Override
							public void buildDialogue() {
								item(ELIDINIS_WARD_F, "You fuse your Arcane sigil to Elidinis' ward, returning it to its former glory.");
							}
						});
					});
				}), new DialogueOption("No."));
			}
		});
	}

	@Override
	public ItemPair[] getMatchingPairs() {
		return new ItemPair[] {new ItemPair(ItemId.ARCANE_SIGIL, ItemId.ELIDINIS_WARD)};
	}

}
