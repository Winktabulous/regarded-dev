package com.zenyte.game.content.skills.smithing;

import com.zenyte.game.content.achievementdiary.diaries.KandarinDiary;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;

/**
 * @author Tommeh | 31 jul. 2018 | 00:01:00
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class BarbarianSmithing extends Action {

	private final BarbarianWeapon weapon;
	private final int index, amount;
	private int cycle, ticks;

	@Override
	public boolean start() {
		if (player.getSkills().getLevel(SkillConstants.SMITHING) < weapon.getLevel()) {
			player.sendMessage("You need a Smithing level of at least " + weapon.getLevel() + " to smith this weapon.");
			return false;
		}
		if (!player.getInventory().containsItem(weapon.getBar())) {
			return false;
		}
		if (!player.getInventory().containsItem(weapon.getLogs())) {
			player.sendMessage("You don't have the necessary logs for the weapon.");
			return false;
		}
		return true;
	}

	@Override
	public boolean process() {
        if (!player.getInventory().containsItem(weapon.getBar())) {
            player.sendMessage("You don't have the necessary bars for the weapon.");
            return false;
        }
        if (!player.getInventory().containsItem(weapon.getLogs())) {
            player.sendMessage("You don't have the necessary logs for the weapon.");
            return false;
        }
        return cycle < amount;
    }

    public BarbarianSmithing(BarbarianWeapon weapon, int index, int amount) {
        this.weapon = weapon;
        this.index = index;
        this.amount = amount;
    }

    @Override
    public int processWithDelay() {
        if (ticks == 0) {
            player.setAnimation(Smithing.ANIMATION);
            player.sendMessage("You hammer the " + weapon.getBar().getDefinitions().getName().replace("bar", "").toLowerCase() + "and make the " + (index == 0 ? "spear." : "hasta."));
        } else if (ticks == 4) {
            player.getInventory().deleteItemsIfContains(new Item[]{weapon.getBar(), weapon.getLogs()}, () -> {
                if (index == 0 && weapon.getSpear().getId() == 1245) {
                    player.getAchievementDiaries().update(KandarinDiary.SMITH_ADAMANT_SPEAR);
                } else if (index == 1 && weapon.getHasta().getId() == 11377) {
                    player.getAchievementDiaries().update(KandarinDiary.SMITH_A_RUNE_HASTA);
                }
                player.getInventory().addItem(index == 0 ? weapon.getSpear() : weapon.getHasta());
                player.getSkills().addXp(SkillConstants.SMITHING, weapon.getExperience());
                cycle++;
            });
            return ticks = 0;
        }
        ticks++;
        return 0;
    }

}
