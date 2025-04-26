package com.zenyte.plugins.itemonnpc;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CATierType;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Savions.
 */
public class SlayerHelmGhommalAction implements ItemOnNPCAction {

	@Override public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
		if (player.getCombatAchievements().hasTierCompleted(CATierType.ELITE)) {
			player.getDialogueManager().start(new Dialogue(player, NpcId.GHOMMAL) {
				@Override public void buildDialogue() {
					if (!player.getCombatAchievements().hasTierCompleted(CATierType.MASTER)) {
						options("Upgrade (cosmetic) your slayer helm to the TzTok slayer helmet?", new DialogueOption("Yes"), new DialogueOption("No")).onOptionTwo(() -> {
							upgrade(item, player, item.getId() == ItemId.SLAYER_HELMET ? ItemId.TZTOK_SLAYER_HELMET : ItemId.TZTOK_SLAYER_HELMET_I);
							setKey(10);
						});
					} else if (!player.getCombatAchievements().hasTierCompleted(CATierType.GRANDMASTER)) {
						options("Choose your cosmetic upgrade", new DialogueOption("TzTok slayer helmet"), new DialogueOption("Vampyric slayer helmet")).onOptionOne(() -> {
							upgrade(item, player, item.getId() == ItemId.SLAYER_HELMET ? ItemId.TZTOK_SLAYER_HELMET : ItemId.TZTOK_SLAYER_HELMET_I);
							setKey(10);
						}).onOptionTwo(() -> {
							upgrade(item, player, item.getId() == ItemId.SLAYER_HELMET ? ItemId.VAMPYRIC_SLAYER_HELMET : ItemId.VAMPYRIC_SLAYER_HELMET_I);
							setKey(10);
						});
					} else {
						options("Choose your cosmetic upgrade", new DialogueOption("TzTok slayer helmet"), new DialogueOption("Vampyric slayer helmet"), new DialogueOption("TzKal slayer helmet")).onOptionOne(() -> {
							upgrade(item, player, item.getId() == ItemId.SLAYER_HELMET ? ItemId.TZTOK_SLAYER_HELMET : ItemId.TZTOK_SLAYER_HELMET_I);
							setKey(10);
						}).onOptionTwo(() -> {
							upgrade(item, player, item.getId() == ItemId.SLAYER_HELMET ? ItemId.VAMPYRIC_SLAYER_HELMET : ItemId.VAMPYRIC_SLAYER_HELMET_I);
							setKey(10);
						}).onOptionThree(() -> {
							upgrade(item, player, item.getId() == ItemId.SLAYER_HELMET ? ItemId.TZKAL_SLAYER_HELMET : ItemId.TZKAL_SLAYER_HELMET_I);
							setKey(10);
						});
					}
					npc(10, "Enjoy your upgrade!");
				}
			});
		}
	}

	private static void upgrade(final Item item, final Player player, final int upgradeId) {
		player.getInventory().ifDeleteItem(item, () -> {
			player.getInventory().addItem(new Item(upgradeId));
		});
	}


	@Override public Object[] getItems() {
		return new Object[] {ItemId.SLAYER_HELMET, ItemId.SLAYER_HELMET_I};
	}

	@Override public Object[] getObjects() {
		return new Object[] {NpcId.GHOMMAL};
	}
}
