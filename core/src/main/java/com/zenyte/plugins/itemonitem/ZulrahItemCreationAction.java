package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.BossDropItem;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.ItemChat;
import com.zenyte.plugins.item.trident.SeaTrident;

import java.util.ArrayList;

import static com.zenyte.game.model.item.BossDropItem.*;

/**
 * @author Tommeh | 22-4-2018 | 00:07
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class ZulrahItemCreationAction implements ItemOnItemAction {
	private static final Animation ANIMATION = new Animation(5244);
	private static final Item CHISEL = new Item(1755);

	@Override
	public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
		final BossDropItem item = BossDropItem.getItemByMaterials(from, to);
		if (item == null) {
			player.sendMessage("Nothing interesting happes.");
			return;
		}
		if (item.equals(TOXIC_BLOWPIPE)) {
			if (player.getSkills().getLevelForXp(SkillConstants.FLETCHING) < 53) {
				player.sendMessage("You need a Fletching level of at least 53 to do this.");
				return;
			}
			player.getInventory().ifDeleteItem(item.getMaterials()[0], () -> {
				player.getInventory().addItem(item.getItem());
				player.setAnimation(ANIMATION);
				player.getSkills().addXp(SkillConstants.FLETCHING, 120);
				player.getDialogueManager().start(new ItemChat(player, item.getItem(), "You carve the fang and turn it into a powerful blowpipe."));
			});
		} else if (item.equals(SERPENTINE_HELMET)) {
			if (player.getSkills().getLevelForXp(SkillConstants.CRAFTING) < 52) {
				player.sendMessage("You need a Crafting level of at least 52 to do this.");
				return;
			}
			player.getInventory().ifDeleteItem(item.getMaterials()[0], () -> {
				player.getInventory().addItem(item.getItem());
				player.setAnimation(ANIMATION);
				player.getSkills().addXp(SkillConstants.CRAFTING, 120);
				player.getDialogueManager().start(new ItemChat(player, item.getItem(), "You adapt the visage to fit on a human head."));
			});
		} else if (item.equals(TRIDENT_OF_THE_SWAMP) || item.equals(TRIDENT_OF_THE_SWAMP_E) || item.equals(TOXIC_STAFF_OF_THE_DEAD)) {
			if (player.getSkills().getLevelForXp(SkillConstants.CRAFTING) < 59) {
				player.sendMessage("You need a Crafting level of at least 59 to do this.");
				return;
			}
			final boolean isTrident = item.equals(TRIDENT_OF_THE_SWAMP) || item.equals(TRIDENT_OF_THE_SWAMP_E);
			if (isTrident) {
				final boolean isCharged = SeaTrident.chargedMap.containsKey(from.getId()) || SeaTrident.chargedMap.containsKey(to.getId());
				if (isCharged || from.getCharges() > 0 || to.getCharges() > 0) {
					player.sendMessage("You can't graft the fang onto a charged trident.");
					return;
				}
				if (!player.getInventory().containsItem(CHISEL)) {
					player.sendMessage("You need a chisel to do this.");
					return;
				}
			}
			final ArrayList<Item> list = new ArrayList<Item>();
			list.add(item.getMaterials()[0]);
			list.add(item.getMaterials()[1]);
			player.getInventory().deleteItemsIfContains(list.toArray(new Item[0]), () -> {
				player.getInventory().addItem(item.getItem());
				player.setAnimation(ANIMATION);
				player.getDialogueManager().start(new ItemChat(player, item.getItem(), "You graft the fang onto the " + (isTrident ? "trident." : "staff of the dead.")));
			});
		} else if (item.equals(TANZANITE_HELMET) || item.equals(MAGMA_HELMET)) {
			final Item helmet = from.getId() == 12931 ? from : to;
			final int slotId = player.getInventory().getItem(fromSlot).getId() == 12931 ? fromSlot : toSlot;
			final Item itemInSlot = player.getInventory().getItem(slotId);
			player.getDialogueManager().start(new Dialogue(player) {
				@Override
				public void buildDialogue() {
					doubleItem(item.getMaterials()[0], item.getMaterials()[1], "The mutagen will change the colour of your helm and<br><col=800000>make it untradeable</col>. You will be able to reverse the<br>change, but will <col=800000>not</col> get the mutagen back.");
					options("Do you wish to proceed?", "Yes", "No").onOptionOne(() -> {
						if (player.getInventory().getItem(slotId) != itemInSlot) {
							return;
						}
						player.getInventory().ifDeleteItem(item.getMaterials()[1], () -> {
							setKey(5);
							player.getInventory().set(slotId, new Item(item.getItem().getId(), 1, helmet.getCharges()));
						});
					});
					item(5, item.getItem(), "The mutagen induces mutation in your helm.");
				}
			});
		} else {
			player.sendMessage("Nothing interesting happens.");
		}
	}

	@Override
	public int[] getItems() {
		return new int[] {12922, 12927, 12932, 1755, 11905, 11907, 11908, 11791, 12931, 13200, 13201, ItemId.UNCHARGED_TRIDENT_E, ItemId.TRIDENT_OF_THE_SEAS_E};
	}
}
