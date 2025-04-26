package com.zenyte.game.content.skills.crafting.actions;

import com.zenyte.game.content.achievementdiary.diaries.FaladorDiary;
import com.zenyte.game.content.skills.crafting.CraftingDefinitions.WeavingData;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Tommeh | 25 sep. 2018 | 20:58:08
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>
 */
public class Weaving extends Action {
	private static final Animation ANIMATION = new Animation(2270);
	private final WeavingData data;
	private final int amount;
	private int cycle;
	private int ticks;

	@Override
	public boolean start() {
		if (!player.getInventory().containsItem(data.getMaterial())) {
			player.sendMessage("You need " + data.getMaterial().getAmount() + " " + data.getMaterialsName() + " to weave this item.");
			return false;
		}
		if (player.getSkills().getLevel(SkillConstants.CRAFTING) < data.getLevel()) {
			player.getDialogueManager().start(new PlainChat(player, "You need a Crafting level of " + data.getLevel() + " to make a " + data.getProduct().getDefinitions().getName().toLowerCase() + "."));
			return false;
		}
		return true;
	}

	@Override
	public boolean process() {
		if (!player.getInventory().containsItem(data.getMaterial())) {
			return false;
		}
        return cycle < amount;
    }

	@Override
	public int processWithDelay() {
		if (ticks == 0) {
			player.sendFilteredMessage("You weave the " + data.getMaterialsName() + " into a " + data.getProduct().getDefinitions().getName().toLowerCase() + ".");
			player.setAnimation(ANIMATION);
		} else if (ticks == 2) {
			if (data.equals(WeavingData.BASKET)) {
				player.getAchievementDiaries().update(FaladorDiary.CRAFT_FRUIT_BASKET);
			}
			player.getSkills().addXp(SkillConstants.CRAFTING, data.getXp());
			player.getInventory().deleteItem(data.getMaterial());
			player.getInventory().addItem(data.getProduct());
			cycle++;
			return ticks = 0;
		}
		ticks++;
		return 0;
	}

	public Weaving(final WeavingData data, final int amount) {
		this.data = data;
		this.amount = amount;
	}
}
