package com.zenyte.game.content.skills.crafting.actions;

import com.zenyte.game.content.achievementdiary.diaries.FremennikDiary;
import com.zenyte.game.content.achievementdiary.diaries.LumbridgeDiary;
import com.zenyte.game.content.skills.crafting.CraftingDefinitions.JewelleryData;
import com.zenyte.game.content.treasuretrails.clues.SherlockTask;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.dailychallenge.challenge.SkillingChallenge;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Tommeh | 25 aug. 2018 | 20:47:51
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class JewelleryCrafting extends Action {
	private static final Animation ANIMATION = new Animation(3243);
	private JewelleryData data;
	private final int amount;
	private int cycle;
	private int ticks;

	public JewelleryCrafting(final JewelleryData data, final int amount) {
		this.data = data;
		this.amount = amount;
	}

	@Override
	public boolean start() {
		if (data.equals(JewelleryData.SLAYER_RING) && player.getInventory().containsItem(JewelleryData.ETERNAL_SLAYER_RING.getMaterials()[0])) {
			data = JewelleryData.ETERNAL_SLAYER_RING;
		}
		if (player.getSkills().getLevel(SkillConstants.CRAFTING) < data.getLevel()) {
			player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
			player.getDialogueManager().start(new PlainChat(player, "You need at least level " + data.getLevel() + " Crafting to make that."));
			return false;
		}
		for (final Item item : data.getMaterials()) {
			if (!player.getInventory().containsItem(item)) {
				player.getDialogueManager().start(new PlainChat(player, "You don't have the items needed to make this."));
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean process() {
		for (final Item item : data.getMaterials()) {
			if (!player.getInventory().containsItem(item)) return false;
		}
		return cycle < amount;
	}

	public JewelleryCrafting(int amount) {
		this.amount = amount;
	}

	@Override
	public int processWithDelay() {
		if (ticks == 0) {
			player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
			player.setAnimation(ANIMATION);
		} else if (ticks == 2) {
			player.getInventory().deleteItemsIfContains(data.getMaterials(), () -> {
				if (data.equals(JewelleryData.DIAMOND_AMULET)) {
					player.getAchievementDiaries().update(LumbridgeDiary.CRAFT_AMULET_OF_POWER, 1);
				} else if (data.equals(JewelleryData.TIARA)) {
					player.getAchievementDiaries().update(FremennikDiary.CRAFT_A_TIARA, 4);
				} else if (data.equals(JewelleryData.DRAGONSTONE_AMULET)) {
					player.getAchievementDiaries().update(FremennikDiary.CREATE_A_DRAGONSTONE_AMULET);
					player.getDailyChallengeManager().update(SkillingChallenge.CRAFT_DRAGONSTONE_AMULETS);
					SherlockTask.CREATE_UNSTRUNG_DRAGONSTONE_AMULET.progress(player);
				} else if (data.equals(JewelleryData.GOLD_BRACELET)) {
					player.getDailyChallengeManager().update(SkillingChallenge.CRAFT_GOLD_BRACELETS);
				} else if (data.equals(JewelleryData.TOPAZ_AMULET)) {
					player.getDailyChallengeManager().update(SkillingChallenge.CRAFT_TOPAZ_AMULETS);
				} else if (data.equals(JewelleryData.SAPPHIRE_NECKLACE)) {
					player.getDailyChallengeManager().update(SkillingChallenge.CRAFT_SAPPHIRE_NECKLACES);
				} else if (data.equals(JewelleryData.OPAL_BRACELET)) {
					player.getDailyChallengeManager().update(SkillingChallenge.CRAFT_OPAL_BRACELETS);
				} else if (data.equals(JewelleryData.EMERALD_RING)) {
					player.getDailyChallengeManager().update(SkillingChallenge.CRAFT_EMERALD_RINGS);
				} else if (data.equals(JewelleryData.DIAMOND_RING)) {
					player.getDailyChallengeManager().update(SkillingChallenge.CRAFT_DIAMOND_RINGS);
				}
				player.getInventory().addItem(data.getProduct());
				player.getSkills().addXp(SkillConstants.CRAFTING, data.getXp());
				cycle++;
			});
			return ticks = 0;
		}
		ticks++;
		return 0;
	}
}
