package com.zenyte.game.content.skills.magic.spells.lunar;

import com.google.common.collect.ImmutableMap;
import com.zenyte.game.content.achievementdiary.diaries.VarrockDiary;
import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.magic.spells.ItemSpell;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.GameTab;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 17. veebr 2018 : 17:51.56
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class PlankMake implements ItemSpell {

	private static final Animation ANIM = new Animation(6298);
	private static final Graphics GFX = new Graphics(1063, 0, 60);
	private static final SoundEffect sound = new SoundEffect(3617);
	
	@Override
	public int getDelay() {
		return 1800;
	}

	@Override
	public boolean spellEffect(final Player player, final Item item, final int slot) {
		final PlankData data = PlankData.DATA.get(item.getId());
		if (data == null) {
		    player.getInterfaceHandler().openGameTab(GameTab.SPELLBOOK_TAB);
			player.sendMessage("You can only cast plank make on logs, oak logs, teak logs or mahogany logs.");
			return false;
		} else if (!player.getInventory().containsItem(995, data.getCost())) {
			player.sendMessage("You need at least " + data.getCost() + " coins to convert " + data.toString().toLowerCase().replaceAll("_", " ") + ".");
			return false;
		}
		if (data.equals(PlankData.MAHOGANY_LOGS)) {
			player.getAchievementDiaries().update(VarrockDiary.MAKE_20_MAHOGANY_PLANKS);
		}
		item.setId(data.getPlank());
		player.getInventory().refresh(slot);
		player.getInventory().deleteItem(995, data.getCost());
		player.setAnimation(ANIM);
		player.setGraphics(GFX);
		player.sendSound(sound);
		this.addXp(player, 90);
		return true;
	}
	
	private enum PlankData {
        LOGS(960, 70),
        OAK_LOGS(8778, 175),
        TEAK_LOGS(8780, 350),
        MAHOGANY_LOGS(8782, 1050);

        public static final ImmutableMap<Integer, PlankData> DATA = ImmutableMap.of(1511, LOGS, 1521, OAK_LOGS,
                6333, TEAK_LOGS, 6332, MAHOGANY_LOGS);
        private final int plank, cost;

        PlankData(final int plank, final int cost) {
            this.plank = plank;
            this.cost = cost;
        }

        public int getPlank() {
            return plank;
        }

        public int getCost() {
            return cost;
        }
    }
	
	@Override
	public Spellbook getSpellbook() {
		return Spellbook.LUNAR;
	}

}
