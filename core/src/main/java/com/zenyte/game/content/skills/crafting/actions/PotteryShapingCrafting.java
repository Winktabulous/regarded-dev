package com.zenyte.game.content.skills.crafting.actions;

import com.zenyte.game.content.achievementdiary.diaries.VarrockDiary;
import com.zenyte.game.content.skills.crafting.CraftingDefinitions;
import com.zenyte.game.content.skills.crafting.CraftingDefinitions.PotteryShapingData;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Tommeh | 27 aug. 2018 | 18:54:21
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class PotteryShapingCrafting extends Action {

	private static final Animation ANIMATION = new Animation(883);

	private final PotteryShapingData data;
	private final int amount;
	private int cycle, ticks;

	@Override
	public boolean start() {
		if (player.getSkills().getLevel(SkillConstants.CRAFTING) < data.getLevel()) {
			player.getDialogueManager().start(new PlainChat(player, "You need a Crafting level of " + data.getLevel() + " to make a " + data.getProduct().getDefinitions().getName().toLowerCase() + "."));
			return false;
		}
		if (!player.getInventory().containsItem(CraftingDefinitions.SOFT_CLAY)) {
			player.sendMessage("You need " + CraftingDefinitions.SOFT_CLAY.getDefinitions().getName().toLowerCase() + " to make this item.");
			return false;
		}
		return true;
	}

    public PotteryShapingCrafting(PotteryShapingData data, int amount) {
        this.data = data;
        this.amount = amount;
    }

	@Override
    public boolean process() {
        if (!player.getInventory().containsItem(CraftingDefinitions.SOFT_CLAY)) {
            return false;
        }
        return cycle < amount;
    }

    @Override
    public int processWithDelay() {
        if (ticks == 0) {
            player.setAnimation(ANIMATION);
        } else if (ticks == 4) {
            player.getInventory().deleteItemsIfContains(new Item[]{CraftingDefinitions.SOFT_CLAY}, () -> {
                if (data.equals(PotteryShapingData.BOWL)) {
                    player.getAchievementDiaries().update(VarrockDiary.SPIN_AND_FIRE_A_BOWL, 0x1);
                }
                player.getInventory().addItem(data.getProduct());
                player.getSkills().addXp(SkillConstants.CRAFTING, data.getXp());
                player.sendFilteredMessage("You make the clay into a " + data.getProduct().getDefinitions().getName().replace("Unfired ", "").toLowerCase() + ".");
                cycle++;
            });
            return ticks = 0;
        }
        ticks++;
        return 0;
    }

}
