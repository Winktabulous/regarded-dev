package com.zenyte.game.content.skills.runecrafting;

import com.zenyte.game.content.achievementdiary.diaries.LumbridgeDiary;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import mgi.types.config.items.ItemDefinitions;
import net.runelite.api.ItemID;

/**
 * @author Kris | 19. dets 2017 : 2:48.32
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class CombinationRunecraftingAction extends Action {
	private static final Animation RUNECRAFTING_ANIM = new Animation(791);
	private static final Graphics RUNECRAFTING_GFX = new Graphics(186, 0, 96);

	public CombinationRunecraftingAction(final CombinationRunecrafting cRune) {
		this.cRune = cRune;
	}

	private final CombinationRunecrafting cRune;

	@Override
	public boolean start() {
		if (player.getSkills().getLevel(SkillConstants.RUNECRAFTING) < cRune.getLevelRequired()) {
			player.sendMessage("You need at least level " + cRune.getLevelRequired() + " Runecrafting to combine these runes.");
			return false;
		}
		if (!player.getInventory().containsItem(7936, 1) && !player.getInventory().containsItem(ItemId.DAEYALT_ESSENCE, 1)) {
			player.sendMessage("You need some pure essence or daeyalt essence in order to combine these runes.");
			return false;
		}
		if (!player.getInventory().containsItem(cRune.getRequiredRuneId(), 1)) {
			player.sendMessage("You need some " + ItemDefinitions.getOrThrow(cRune.getRequiredRuneId()).getName() + "s to combine these runes.");
			return false;
		}
		player.setAnimation(RUNECRAFTING_ANIM);
		player.setGraphics(RUNECRAFTING_GFX);
		player.lock(2);
		return true;
	}

	@Override
	public boolean process() {
		return true;
	}

	@Override
	public int processWithDelay() {
		final int essenceUsed = player.getInventory().containsItem(ItemID.DAEYALT_ESSENCE, 1) ? ItemId.DAEYALT_ESSENCE : 7936;
		final int runes = Math.min(player.getInventory().getAmountOf(essenceUsed), player.getInventory().getAmountOf(cRune.getRequiredRuneId()));
		final int amulet = player.getEquipment().getId(EquipmentSlot.AMULET);
		final boolean isBindingNecklace = amulet == 5521;
		final boolean success = isBindingNecklace || Utils.random(100) > 50;
		player.getInventory().deleteItem(essenceUsed, runes);
		player.getInventory().deleteItem(cRune.getRequiredRuneId(), runes);
		final String name = ItemDefinitions.getOrThrow(cRune.getRuneId()).getName().toLowerCase();
		if (success) {
			if (isBindingNecklace) {
				final int uses = player.getNumericAttribute("binding necklace uses").intValue() + 1;
				player.addAttribute("binding necklace uses", uses % 16);
				if (uses == 16) {
					player.getEquipment().set(EquipmentSlot.AMULET, null);
					player.sendMessage("Your Binding necklace has disintegrated.");
				}
			}
			if (cRune.equals(CombinationRunecrafting.LAVA_RUNE_FIRE)) {
				player.getAchievementDiaries().update(LumbridgeDiary.CRAFT_LAVA_RUNES);
			}
			player.getInventory().addItem(new Item(cRune.getRuneId(), runes * 2));
			player.sendFilteredMessage("You bind the temple's power into " + name + "s.");
			player.sendFilteredMessage("The gods bless your efforts and grant you extra runes.");
		} else {
			player.sendFilteredMessage("You fail to bind the temple's power into " + name + "s.");
		}
		int exp = (int) (cRune.getExperience() * runes);
		if(essenceUsed == ItemId.DAEYALT_ESSENCE)
			exp *= 1.5;

		player.getSkills().addXp(SkillConstants.RUNECRAFTING, exp);
		return -1;
	}
}
