package com.zenyte.game.content.skills.magic.spells.lunar;

import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.magic.spells.DefaultSpell;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Setting;
import com.zenyte.game.world.entity.player.container.impl.Inventory;

/**
 * @author Kris | 17. veebr 2018 : 2:51.40
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class TanLeather implements DefaultSpell {

	private static final Animation ANIM = new Animation(712);
	private static final Graphics GFX = new Graphics(322, 0, 92);
	private static final SoundEffect sound = new SoundEffect(2879);
	
	private static final int[] HIDES = new int[] { 1753, 1751, 1749, 1747 };
	private static final int[] LEATHER = new int[] { 1745, 2505, 2507, 2509 };

	@Override
	public int getDelay() {
		return 1000;
	}

	@Override
	public boolean spellEffect(final Player player, final int optionId, final String option) {
		if (!DiaryUtil.eligibleFor(DiaryReward.FREMENNIK_SEA_BOOTS3, player)) {
			player.sendMessage("You need to complete the fremennik hard diaries first in order to use this spell.");
			return false;
		}
		if (optionId == 2) {
			player.getSettings().toggleSetting(Setting.HARD_LEATHER_TAN);
			player.sendMessage("You will now tan cowhides into " + (player.getBooleanSetting(Setting.HARD_LEATHER_TAN) ? "hard leather" : "soft leather") + ".");
			return false;
		}
		final Inventory inventory = player.getInventory();
		boolean contains = false;
		if (inventory.containsItem(1739, 1)) {
			contains = true;
		} else {
            for (int hide : HIDES) {
                if (inventory.containsItem(hide, 1)) {
                    contains = true;
                    break;
                }
            }
		}
		if (!contains) {
			player.sendMessage("You do not have any leather that could be tanned.");
			return false;
		}
        player.setAnimation(ANIM);
		player.sendSound(sound);
        player.setGraphics(GFX);
        final boolean hard = player.getBooleanSetting(Setting.HARD_LEATHER_TAN);
        int count = 0;
        loop:
        for (int i = 0; i < 28; i++) {
            final Item item = inventory.getItem(i);
            if (item == null) {
                continue;
            }
            if (item.getId() == 1739) {
                item.setId(hard ? 1743 : 1741);
                if (++count == 5) {
                    break;
                }
            } else {
                for (int index = 0; index < HIDES.length; index++) {
                    if (item.getId() == HIDES[index]) {
                        item.setId(LEATHER[index]);
                        if (++count == 5) {
                            break loop;
                        }
                    }
                }
            }
        }
        player.getInventory().refreshAll();
        this.addXp(player, 81);
        return true;
    }
	
	@Override
	public Spellbook getSpellbook() {
		return Spellbook.LUNAR;
	}

}
