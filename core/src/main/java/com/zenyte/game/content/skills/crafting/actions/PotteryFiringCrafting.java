package com.zenyte.game.content.skills.crafting.actions;

import com.zenyte.game.content.achievementdiary.diaries.VarrockDiary;
import com.zenyte.game.content.skills.crafting.CraftingDefinitions.PotteryFiringData;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Tommeh | 27 aug. 2018 | 18:45:07
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class PotteryFiringCrafting extends Action {

	private static final Animation ANIMATION = new Animation(1317);

	private final PotteryFiringData data;
	private final int amount;
	private int cycle, ticks;

	@Override
	public boolean start() {
		if (player.getSkills().getLevel(SkillConstants.CRAFTING) < data.getLevel()) {
			player.getDialogueManager().start(new PlainChat(player, "You need a Crafting level of at least " + data.getLevel() + " to make a " + data.getProduct().getDefinitions().getName().toLowerCase() + "."));
			return false;
		}
		if (!player.getInventory().containsItem(data.getMaterial())) {
			player.sendMessage("You need an " + data.getMaterial().getDefinitions().getName().toLowerCase() + " to make this item.");
			return false;
		}
		return true;
	}

    public PotteryFiringCrafting(PotteryFiringData data, int amount) {
        this.data = data;
        this.amount = amount;
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
            player.sendFilteredMessage("You put a" + data.getMaterial().getDefinitions().getName().replace("Unfired", "").toLowerCase() + " in the oven.");
            player.setAnimation(ANIMATION);
        } else if (ticks == 5) {
            player.getInventory().deleteItemsIfContains(new Item[]{data.getMaterial()}, () -> {
                if (data.equals(PotteryFiringData.BOWL)) {
                    player.getAchievementDiaries().update(VarrockDiary.SPIN_AND_FIRE_A_BOWL, 0x2);
                }
                player.getSkills().addXp(SkillConstants.CRAFTING, data.getXp());
                player.getInventory().addItem(data.getProduct());
                player.sendFilteredMessage("You remove the" + data.getMaterial().getDefinitions().getName().replace("Unfired", "").toLowerCase() + " from the oven.");
                cycle++;
            });
            return ticks = 0;
        }
        ticks++;
        return 0;
    }

}
